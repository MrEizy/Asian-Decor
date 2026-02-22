package net.thejadeproject.asiandecor.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.thejadeproject.asiandecor.blocks.ModBlocks;
import net.thejadeproject.asiandecor.screen.custom.ShapeMakerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ShapeMakerBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {

    private static final String PREVIEW_BLOCKS_TAG = "PreviewBlocks";

    public enum ShapeType {
        SQUARE("Square"),
        CIRCLE("Circle"),
        SPHERE("Sphere"),
        DOME("Dome"),
        TUBE("Tube"),
        PYRAMID("Pyramid"),
        HEXAGON("Hexagon"),
        OCTAGON("Octagon"),
        TRIANGLE("Triangle"),
        PENTAGON("Pentagon"),
        STAR("Star"),
        HEART("Heart"),
        CONE("Cone"),
        TORUS("Torus"),
        ELLIPSE("Ellipse"),
        ARCH("Arch"),
        SPIRAL("Spiral");

        private final String displayName;
        ShapeType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum RedstoneMode {
        OFF("Off"),
        PULSE("Pulse"),
        CONSTANT("Constant");

        private final String displayName;
        RedstoneMode(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    private ShapeType selectedShape = ShapeType.SQUARE;
    private int xOffset = 0;
    private int yOffset = 1;
    private int zOffset = 0;
    private int radius = 5;
    private int thickness = 1;
    private boolean previewEnabled = false;
    private RedstoneMode redstoneMode = RedstoneMode.OFF;

    private final ItemStack[] inventory = new ItemStack[9];
    private final Set<BlockPos> previewBlocks = new HashSet<>();
    private boolean wasPowered = false;

    public ShapeMakerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHAPE_MAKER.get(), pos, state);
        for (int i = 0; i < 9; i++) {
            inventory[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Shape Maker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ShapeMakerMenu(containerId, playerInventory, this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ShapeMakerBlockEntity be) {
        if (!level.isClientSide && be.redstoneMode == RedstoneMode.CONSTANT) {
            boolean isPowered = level.hasNeighborSignal(pos);
            if (isPowered && be.hasBuildingBlocks()) {
                be.buildShape();
            }
        }
    }

    public void onRedstonePulse(boolean isPowered) {
        if (redstoneMode == RedstoneMode.PULSE && isPowered && !wasPowered) {
            if (hasBuildingBlocks()) {
                buildShape();
            }
        }
        wasPowered = isPowered;
    }

    public boolean hasBuildingBlocks() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty() && stack.getItem() instanceof net.minecraft.world.item.BlockItem) {
                return true;
            }
        }
        return false;
    }

    private void drawLine(BlockPos center, Set<BlockPos> positions,
                          int x0, int z0, int x1, int z1, int y) {

        int dx = Math.abs(x1 - x0);
        int dz = Math.abs(z1 - z0);
        int sx = x0 < x1 ? 1 : -1;
        int sz = z0 < z1 ? 1 : -1;
        int err = dx - dz;

        while (true) {
            positions.add(center.offset(x0, y, z0));

            if (x0 == x1 && z0 == z1) break;

            int e2 = 2 * err;
            if (e2 > -dz) { err -= dz; x0 += sx; }
            if (e2 < dx)  { err += dx; z0 += sz; }
        }
    }

    private boolean insideRegularPolygon(int x, int z, int sides, double radius) {

        double apothem = radius * Math.cos(Math.PI / sides);

        for (int i = 0; i < sides; i++) {
            double angle = (2 * Math.PI * i) / sides;

            double nx = Math.cos(angle);
            double nz = Math.sin(angle);

            double projection = x * nx + z * nz;

            if (projection > apothem)
                return false;
        }

        return true;
    }

    public void buildShape() {
        if (level == null || level.isClientSide) return;

        BlockPos center = worldPosition.offset(xOffset, yOffset, zOffset);
        Set<BlockPos> positions = calculateShape(center);

        for (BlockPos pos : positions) {
            // Can replace preview blocks or empty blocks
            boolean canPlace = level.isEmptyBlock(pos) ||
                    level.getBlockState(pos).is(ModBlocks.PREVIEW_BLOCK.get());

            if (canPlace) {
                // Find a valid block from inventory
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = inventory[i];
                    if (!stack.isEmpty() && stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem) {
                        level.setBlock(pos, blockItem.getBlock().defaultBlockState(), Block.UPDATE_ALL);
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            inventory[i] = ItemStack.EMPTY;
                        }
                        setChanged();
                        break;
                    }
                }
            }
        }
    }

    public void updatePreview() {
        if (level == null || level.isClientSide) return;

        clearPreview();

        BlockPos center = worldPosition.offset(xOffset, yOffset, zOffset);
        Set<BlockPos> newPositions = calculateShape(center);

        for (BlockPos pos : newPositions) {
            if (level.isEmptyBlock(pos)) {
                boolean success = level.setBlock(pos, ModBlocks.PREVIEW_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL);
                if (success) {
                    previewBlocks.add(pos);
                }
            }
        }
        setChanged();
    }

    public void clearPreview() {
        if (level == null || level.isClientSide) return;

        for (BlockPos pos : new HashSet<>(previewBlocks)) { // Copy to avoid concurrent modification
            if (level.getBlockState(pos).is(ModBlocks.PREVIEW_BLOCK.get())) {
                level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
        previewBlocks.clear();
        setChanged();
    }

    public void dropInventory() {
        if (level == null || level.isClientSide) return;
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                net.minecraft.world.entity.item.ItemEntity item = new net.minecraft.world.entity.item.ItemEntity(
                        level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, stack
                );
                level.addFreshEntity(item);
            }
        }
    }

    // TRUE 3D SHAPES - Polygons have polygonal cross-section at every Y level
    private Set<BlockPos> calculateShape(BlockPos center) {
        Set<BlockPos> positions = new HashSet<>();
        switch (selectedShape) {
            case SQUARE -> calculateSquare(center, positions);
            case CIRCLE -> calculateCircle(center, positions);
            case SPHERE -> calculateSphere(center, positions);
            case DOME -> calculateDome(center, positions);
            case TUBE -> calculateTube(center, positions);
            case PYRAMID -> calculatePyramid(center, positions);
            case HEXAGON -> calculateHexagon(center, positions);
            case OCTAGON -> calculateOctagon(center, positions);
            case TRIANGLE -> calculateTriangle(center, positions);
            case PENTAGON -> calculatePentagon(center, positions);
            case STAR -> calculateStar(center, positions);
            case HEART -> calculateHeart(center, positions);
            case CONE -> calculateCone(center, positions);
            case TORUS -> calculateTorus(center, positions);
            case ELLIPSE -> calculateEllipse(center, positions);
            case ARCH -> calculateArch(center, positions);
            case SPIRAL -> calculateSpiral(center, positions);
        }
        return positions;
    }

    // TRUE 3D SQUARE - Box with walls, floor, roof
    private void calculateSquare(BlockPos center, Set<BlockPos> positions) {
        for (int y = 0; y < thickness; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    boolean isFloor = y == 0;
                    boolean isRoof = y == thickness - 1;
                    boolean isWall = Math.abs(x) >= radius - 1 || Math.abs(z) >= radius - 1;

                    if (isFloor || isRoof || isWall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    // TRUE 3D CIRCLE - Cylinder with circular floor/roof
    private void calculateCircle(BlockPos center, Set<BlockPos> positions) {
        for (int y = 0; y < thickness; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distSq = x * x + z * z;
                    boolean inCircle = distSq <= radius * radius;
                    boolean inInnerCircle = distSq <= (radius - 2) * (radius - 2);

                    boolean isFloorOrRoof = (y == 0 || y == thickness - 1) && inCircle;
                    boolean isWall = inCircle && !inInnerCircle && y > 0 && y < thickness - 1;

                    if (isFloorOrRoof || isWall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    // SPHERE - Hollow shell
    private void calculateSphere(BlockPos center, Set<BlockPos> positions) {
        int innerRadius = Math.max(0, radius - 2);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distSq = x * x + y * y + z * z;
                    if (distSq <= radius * radius && distSq >= innerRadius * innerRadius) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    // DOME - Half sphere with floor
    private void calculateDome(BlockPos center, Set<BlockPos> positions) {
        int innerRadius = Math.max(0, radius - 2);
        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distSq = x * x + y * y + z * z;
                    boolean inSphere = distSq <= radius * radius;
                    boolean inInnerSphere = distSq <= innerRadius * innerRadius;

                    boolean isFloor = y == 0 && (x * x + z * z <= radius * radius);
                    boolean isShell = inSphere && !inInnerSphere && y > 0;

                    if (isFloor || isShell) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    // TUBE - Vertical pipe with caps
    private void calculateTube(BlockPos center, Set<BlockPos> positions) {
        int length = thickness * 3;
        int innerRadius = Math.max(0, radius - 2);

        for (int y = 0; y < length; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distSq = x * x + z * z;
                    boolean inTube = distSq <= radius * radius;
                    boolean inInnerTube = distSq <= innerRadius * innerRadius;

                    boolean isCap = (y == 0 || y == length - 1) && inTube;
                    boolean isWall = inTube && !inInnerTube && y > 0 && y < length - 1;

                    if (isCap || isWall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    // PYRAMID - True 3D with square cross-section
    private void calculatePyramid(BlockPos center, Set<BlockPos> positions) {
        for (int y = 0; y < radius; y++) {
            int currentRadius = radius - y;
            int innerRadius = Math.max(0, currentRadius - 2);

            for (int x = -currentRadius; x <= currentRadius; x++) {
                for (int z = -currentRadius; z <= currentRadius; z++) {
                    int maxDist = Math.max(Math.abs(x), Math.abs(z));
                    boolean isFloor = y == 0 && maxDist <= currentRadius;
                    boolean isWall = maxDist >= innerRadius && maxDist <= currentRadius && y > 0;

                    if (isFloor || isWall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    private void calculateHexagon(BlockPos center, Set<BlockPos> positions) {

        int innerRadius = Math.max(0, radius - thickness);
        int height = thickness * 2;

        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {

                    boolean outer = insideRegularPolygon(x, z, 6, radius);
                    boolean inner = insideRegularPolygon(x, z, 6, innerRadius);

                    boolean floorOrRoof =
                            (y == 0 || y == height - 1) && outer;

                    boolean wall =
                            outer && !inner &&
                                    y > 0 && y < height - 1;

                    if (floorOrRoof || wall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    private void calculateOctagon(BlockPos center, Set<BlockPos> positions) {

        int innerRadius = Math.max(0, radius - thickness);
        int height = thickness * 2;

        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {

                    boolean outer = insideRegularPolygon(x, z, 8, radius);
                    boolean inner = insideRegularPolygon(x, z, 8, innerRadius);

                    boolean floorOrRoof =
                            (y == 0 || y == height - 1) && outer;

                    boolean wall =
                            outer && !inner &&
                                    y > 0 && y < height - 1;

                    if (floorOrRoof || wall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    private void calculatePentagon(BlockPos center, Set<BlockPos> positions) {

        int innerRadius = Math.max(0, radius - thickness);
        int height = thickness * 2;

        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {

                    boolean outer = insideRegularPolygon(x, z, 5, radius);
                    boolean inner = insideRegularPolygon(x, z, 5, innerRadius);

                    boolean floorOrRoof =
                            (y == 0 || y == height - 1) && outer;

                    boolean wall =
                            outer && !inner &&
                                    y > 0 && y < height - 1;

                    if (floorOrRoof || wall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    // TRIANGLE - Triangular prism
    private void calculateTriangle(BlockPos center, Set<BlockPos> positions) {
        for (int y = 0; y < thickness; y++) {
            for (int z = 0; z < radius; z++) {
                int widthAtZ = (radius - z) * 2;
                int innerWidth = Math.max(0, widthAtZ - 4);

                for (int x = -widthAtZ / 2; x <= widthAtZ / 2; x++) {
                    boolean inTri = Math.abs(x) <= widthAtZ / 2;
                    boolean inInnerTri = Math.abs(x) <= innerWidth / 2 && z < radius - 2;

                    boolean isFloorOrRoof = (y == 0 || y == thickness - 1) && inTri;
                    boolean isWall = inTri && !inInnerTri && y > 0 && y < thickness - 1;

                    if (isFloorOrRoof || isWall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    // STAR - Star prism
    private void calculateStar(BlockPos center, Set<BlockPos> positions) {
        for (int y = 0; y < thickness; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double angle = Math.atan2(z, x);
                    double dist = Math.sqrt(x * x + z * z);
                    double starRadius = radius * (0.5 + 0.5 * Math.cos(angle * 5));

                    boolean inStar = dist <= starRadius;
                    boolean inInnerStar = dist <= starRadius - 2;

                    boolean isFloorOrRoof = (y == 0 || y == thickness - 1) && inStar;
                    boolean isWall = inStar && !inInnerStar && y > 0 && y < thickness - 1;

                    if (isFloorOrRoof || isWall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    // HEART - Heart prism
    private void calculateHeart(BlockPos center, Set<BlockPos> positions) {
        for (int y = 0; y < thickness; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double xNorm = (double) x / radius * 1.5;
                    double zNorm = (double) z / radius * 1.5;
                    double formula = Math.pow(xNorm * xNorm + zNorm * zNorm - 1, 3) -
                            xNorm * xNorm * zNorm * zNorm * zNorm;

                    if (formula <= 0) {
                        // Check if near edge for walls
                        boolean isEdge = false;
                        for (int dx = -1; dx <= 1 && !isEdge; dx++) {
                            for (int dz = -1; dz <= 1 && !isEdge; dz++) {
                                if (dx == 0 && dz == 0) continue;
                                double nx = (double) (x + dx) / radius * 1.5;
                                double nz = (double) (z + dz) / radius * 1.5;
                                double nFormula = Math.pow(nx * nx + nz * nz - 1, 3) - nx * nx * nz * nz * nz;
                                if (nFormula > 0) isEdge = true;
                            }
                        }

                        boolean isFloorOrRoof = (y == 0 || y == thickness - 1);

                        if (isFloorOrRoof || isEdge) {
                            positions.add(center.offset(x, y, z));
                        }
                    }
                }
            }
        }
    }

    // CONE - Tapered with base
    private void calculateCone(BlockPos center, Set<BlockPos> positions) {
        int height = radius * 2;
        for (int y = 0; y < height; y++) {
            double currentRadius = radius * (1.0 - (double) y / height);
            double innerRadius = Math.max(0, currentRadius - 2);

            for (int x = -(int)currentRadius; x <= currentRadius; x++) {
                for (int z = -(int)currentRadius; z <= currentRadius; z++) {
                    double distSq = x * x + z * z;
                    boolean inCone = distSq <= currentRadius * currentRadius;
                    boolean inInnerCone = distSq <= innerRadius * innerRadius;

                    boolean isBase = y == 0 && inCone;
                    boolean isWall = inCone && !inInnerCone && y > 0;

                    if (isBase || isWall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    // TORUS - Ring (no floor/roof needed as it's a ring)
    private void calculateTorus(BlockPos center, Set<BlockPos> positions) {
        int tubeRadius = Math.max(2, radius / 3);
        int majorRadius = radius - tubeRadius;
        int innerTubeRadius = Math.max(0, tubeRadius - 2);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -tubeRadius; y <= tubeRadius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distFromCenter = Math.sqrt(x * x + z * z);
                    double distFromTubeCenter = Math.sqrt(Math.pow(distFromCenter - majorRadius, 2) + y * y);

                    boolean inTube = distFromTubeCenter <= tubeRadius;
                    boolean inInnerTube = distFromTubeCenter <= innerTubeRadius;

                    if (inTube && !inInnerTube) {
                        positions.add(center.offset(x, y + radius/2, z));
                    }
                }
            }
        }
    }

    // ELLIPSE - Elliptical prism
    private void calculateEllipse(BlockPos center, Set<BlockPos> positions) {
        for (int y = 0; y < thickness; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distOuter = (x * x) / (double)(radius * radius) +
                            (z * z) / (double)((radius/2) * (radius/2));
                    double innerA = Math.max(1, radius - 2);
                    double innerB = Math.max(1, radius/2 - 2);
                    double distInner = (x * x) / (double)(innerA * innerA) +
                            (z * z) / (double)(innerB * innerB);

                    boolean inEllipse = distOuter <= 1;
                    boolean inInnerEllipse = distInner <= 1;

                    boolean isFloorOrRoof = (y == 0 || y == thickness - 1) && inEllipse;
                    boolean isWall = inEllipse && !inInnerEllipse && y > 0 && y < thickness - 1;

                    if (isFloorOrRoof || isWall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    // ARCH - Curved with walls and floor
    private void calculateArch(BlockPos center, Set<BlockPos> positions) {
        int width = radius * 2;
        int height = radius;
        int depth = thickness;

        for (int x = -width/2; x <= width/2; x++) {
            for (int y = 0; y < height; y++) {
                double archHeight = height * Math.sin(Math.PI * (x + width/2.0) / width);
                double innerArchHeight = Math.max(0, archHeight - 2);

                for (int z = 0; z < depth; z++) {
                    boolean inArch = y <= archHeight;
                    boolean inInnerArch = y <= innerArchHeight && y > 0;

                    boolean isFloor = y == 0 && inArch;
                    boolean isWall = inArch && !inInnerArch;

                    if (isFloor || isWall) {
                        positions.add(center.offset(x, y, z));
                    }
                }
            }
        }
    }

    private void calculateSpiral(BlockPos center, Set<BlockPos> positions) {
        int height = thickness * 3;
        double rotations = 2.0;

        int prevX = 0;
        int prevZ = 0;
        boolean first = true;

        for (int y = 0; y < height; y++) {

            double angle = (y / (double) height) * rotations * 2 * Math.PI;

            int x = (int)Math.round(Math.cos(angle) * radius);
            int z = (int)Math.round(Math.sin(angle) * radius);

            if (!first) {
                drawLine(center, positions, prevX, prevZ, x, z, y);
            }

            prevX = x;
            prevZ = z;
            first = false;
        }
    }

    // Getters and Setters
    public ShapeType getSelectedShape() { return selectedShape; }
    public void setSelectedShape(ShapeType shape) {
        this.selectedShape = shape;
        if (previewEnabled) updatePreview();
        setChanged();
    }

    public int getXOffset() { return xOffset; }
    public void setXOffset(int x) {
        this.xOffset = x;
        if (previewEnabled) updatePreview();
        setChanged();
    }

    public int getYOffset() { return yOffset; }
    public void setYOffset(int y) {
        this.yOffset = y;
        if (previewEnabled) updatePreview();
        setChanged();
    }

    public int getZOffset() { return zOffset; }
    public void setZOffset(int z) {
        this.zOffset = z;
        if (previewEnabled) updatePreview();
        setChanged();
    }

    public int getRadius() { return radius; }
    public void setRadius(int r) {
        this.radius = Math.max(1, r);
        if (previewEnabled) updatePreview();
        setChanged();
    }

    public int getThickness() { return thickness; }
    public void setThickness(int t) {
        this.thickness = Math.max(1, t);
        if (previewEnabled) updatePreview();
        setChanged();
    }

    public boolean isPreviewEnabled() { return previewEnabled; }
    public void setPreviewEnabled(boolean enabled) {
        this.previewEnabled = enabled;
        if (!enabled) clearPreview();
        else updatePreview();
        setChanged();
    }

    public RedstoneMode getRedstoneMode() { return redstoneMode; }
    public void setRedstoneMode(RedstoneMode mode) {
        this.redstoneMode = mode;
        setChanged();
    }

    // Container methods
    @Override
    public int getContainerSize() { return 9; }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory[slot];
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = inventory[slot];
        if (stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack result = stack.split(amount);
        if (stack.isEmpty()) inventory[slot] = ItemStack.EMPTY;
        setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory[slot];
        inventory[slot] = ItemStack.EMPTY;
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory[slot] = stack;
        if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.getBlockPos().distToCenterSqr(player.position()) < 64;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < 9; i++) {
            inventory[i] = ItemStack.EMPTY;
        }
        setChanged();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return !stack.isEmpty() && stack.getItem() instanceof net.minecraft.world.item.BlockItem;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof net.minecraft.world.item.BlockItem;
    }

    // NBT
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("Shape", selectedShape.name());
        tag.putInt("XOffset", xOffset);
        tag.putInt("YOffset", yOffset);
        tag.putInt("ZOffset", zOffset);
        tag.putInt("Radius", radius);
        tag.putInt("Thickness", thickness);
        tag.putBoolean("Preview", previewEnabled);
        tag.putString("RedstoneMode", redstoneMode.name());

        // Save inventory
        for (int i = 0; i < 9; i++) {
            if (!inventory[i].isEmpty()) {
                tag.put("Slot" + i, inventory[i].save(registries));
            }
        }

        // SAVE PREVIEW BLOCK POSITIONS
        if (!previewBlocks.isEmpty()) {
            long[] packedPositions = new long[previewBlocks.size()];
            int i = 0;
            for (BlockPos pos : previewBlocks) {
                packedPositions[i++] = pos.asLong();
            }
            tag.putLongArray(PREVIEW_BLOCKS_TAG, packedPositions);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        try {
            selectedShape = ShapeType.valueOf(tag.getString("Shape"));
        } catch (IllegalArgumentException e) {
            selectedShape = ShapeType.SQUARE;
        }
        xOffset = tag.getInt("XOffset");
        yOffset = tag.getInt("YOffset");
        zOffset = tag.getInt("ZOffset");
        radius = tag.getInt("Radius");
        thickness = tag.getInt("Thickness");
        previewEnabled = tag.getBoolean("Preview");
        try {
            redstoneMode = RedstoneMode.valueOf(tag.getString("RedstoneMode"));
        } catch (IllegalArgumentException e) {
            redstoneMode = RedstoneMode.OFF;
        }

        // Load inventory
        for (int i = 0; i < 9; i++) {
            if (tag.contains("Slot" + i)) {
                inventory[i] = ItemStack.parse(registries, tag.getCompound("Slot" + i)).orElse(ItemStack.EMPTY);
            } else {
                inventory[i] = ItemStack.EMPTY;
            }
        }

        // LOAD PREVIEW BLOCK POSITIONS
        previewBlocks.clear();
        if (tag.contains(PREVIEW_BLOCKS_TAG)) {
            long[] packedPositions = tag.getLongArray(PREVIEW_BLOCKS_TAG);
            for (long packed : packedPositions) {
                previewBlocks.add(BlockPos.of(packed));
            }
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}