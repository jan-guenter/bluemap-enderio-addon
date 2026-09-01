/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap523;

import de.bluecolored.bluemap.core.map.TextureGallery;
import de.bluecolored.bluemap.core.map.hires.MaxCapacityReachedException;
import de.bluecolored.bluemap.core.map.hires.RenderSettings;
import de.bluecolored.bluemap.core.map.hires.TileModelView;
import de.bluecolored.bluemap.core.map.hires.block.BlockRenderer;
import de.bluecolored.bluemap.core.map.hires.block.ResourceModelRenderer;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.VariantSet;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.util.math.Color;
import de.bluecolored.bluemap.core.world.block.BlockNeighborhood;
import io.github.janguenter.bluemap.enderio.profile.EnderIo8211Profile;

import java.util.Optional;

/** Replaces the one exact host with a strictly proven persisted paint model. */
final class EnderIoPaintedBlockRenderer implements BlockRenderer {

    private final ResourcePack resourcePack;
    private final EnderIoRuntime runtime;
    private final ResourceModelRenderer resources;
    private final CanonicalPaintTargetResolver targets;
    private final PaintSnapshotDecoder decoder = new PaintSnapshotDecoder();

    EnderIoPaintedBlockRenderer(
            ResourcePack resourcePack,
            TextureGallery textures,
            RenderSettings settings,
            EnderIoRuntime runtime
    ) {
        this.resourcePack = resourcePack;
        this.runtime = runtime;
        this.resources = new ResourceModelRenderer(resourcePack, textures, settings);
        this.targets = new CanonicalPaintTargetResolver(
                resourcePack, BlueMap523Adapter.extension(resourcePack)
        );
    }

    @Override
    public void render(
            BlockNeighborhood block,
            Variant ignored,
            TileModelView target,
            Color mapColor
    ) {
        int start = target.getStart();
        Color initialMapColor = new Color().set(mapColor);
        if (!runtime.active()) {
            renderStock(block, target, mapColor);
            return;
        }
        try {
            if (!renderExact(block, target, mapColor)) {
                resetAndRenderStock(block, target, start, mapColor, initialMapColor);
            }
        } catch (MaxCapacityReachedException exception) {
            resetPartial(target, start, mapColor, initialMapColor);
            throw exception;
        } catch (RuntimeException | LinkageError exception) {
            runtime.report("render-failed-" + exception.getClass().getSimpleName());
            resetAndRenderStock(block, target, start, mapColor, initialMapColor);
        }
    }

    private boolean renderExact(
            BlockNeighborhood block,
            TileModelView target,
            Color mapColor
    ) {
        if (!EnderIo8211Profile.HOST_ID.equals(
                block.getBlockState().getId().getFormatted())
                || !block.getBlockState().getProperties().isEmpty()) {
            runtime.report("fallback-host-state");
            return false;
        }
        EnderIoBlockEntityData data = block.getBlockEntity()
                instanceof EnderIoBlockEntityData found ? found : null;
        if (data == null) {
            runtime.report("fallback-block-entity-projection");
            return false;
        }
        String blockEntityId = data.getId() == null
                ? null : data.getId().getFormatted();
        if (!EnderIo8211Profile.matches(
                block.getBlockState().getId().getFormatted(), blockEntityId)) {
            runtime.report("fallback-block-entity-id");
            return false;
        }
        Optional<Key> paint = decoder.decode(data);
        if (paint.isEmpty()) {
            runtime.report("fallback-paint-snapshot");
            return false;
        }
        Optional<VariantSet> targetVariants = targets.resolve(paint.orElseThrow());
        if (targetVariants.isEmpty()) {
            runtime.report("fallback-target-model");
            return false;
        }
        targetVariants.orElseThrow().forEach(
                block.getX(), block.getY(), block.getZ(),
                variant -> resources.render(block, variant, target, mapColor)
        );
        return true;
    }

    private void resetAndRenderStock(
            BlockNeighborhood block,
            TileModelView target,
            int start,
            Color mapColor,
            Color initialMapColor
    ) {
        resetPartial(target, start, mapColor, initialMapColor);
        try {
            renderStock(block, target, mapColor);
        } catch (MaxCapacityReachedException exception) {
            resetPartial(target, start, mapColor, initialMapColor);
            throw exception;
        }
    }

    static void resetPartial(
            TileModelView target,
            int start,
            Color mapColor,
            Color initialMapColor
    ) {
        target.getTileModel().reset(start);
        target.initialize(start);
        mapColor.set(initialMapColor);
    }

    private void renderStock(
            BlockNeighborhood block,
            TileModelView target,
            Color mapColor
    ) {
        de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState raw =
                resourcePack.getBlockStates().get(block.getBlockState().getId());
        if (raw == null) {
            return;
        }
        raw.forEach(
                block.getBlockState(), block.getX(), block.getY(), block.getZ(),
                variant -> resources.render(block, variant, target, mapColor)
        );
    }
}
