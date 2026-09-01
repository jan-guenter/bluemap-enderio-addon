/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap523;

import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.BlockProperties;
import de.bluecolored.bluemap.core.world.BlockState;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.SyntheticDispatch;
import io.github.janguenter.bluemap.enderio.profile.EnderIo8211Profile;
import io.github.janguenter.bluemap.enderio.profile.ExactEnderIoArtifactDetector;

import java.nio.file.Path;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/** Exact-artifact activation and one-host synthetic routing. */
final class EnderIoResourceExtension implements ResourcePackExtension {

    static final Key SYNTHETIC = Key.parse("bluemap_enderio:painted_redstone");

    private final ResourcePack resourcePack;
    private final EnderIoRuntime runtime;
    private volatile Map<Variant, BlockRendererType> originalRenderers = Map.of();
    private boolean rendererSnapshotCaptured;

    EnderIoResourceExtension(ResourcePack resourcePack, EnderIoRuntime runtime) {
        this.resourcePack = resourcePack;
        this.runtime = runtime;
    }

    @Override
    public void loadResources(Iterable<Path> roots) {
        captureOriginalRenderers();
        if (Boolean.getBoolean("bluemap.enderio.disabled")) {
            runtime.inactive("operator-disabled");
            return;
        }
        if (!ExactEnderIoArtifactDetector.matches(
                roots, EnderIo8211Profile.JAR_SHA256, EnderIo8211Profile.JAR_SIZE)) {
            runtime.inactive("exact-artifact-missing-or-duplicate");
            return;
        }
        if (!SyntheticDispatch.matches(
                resourcePack.getBlockStates().get(SYNTHETIC),
                BlueMap523Adapter.renderer()
        )) {
            runtime.inactive("synthetic-dispatch-invalid");
            return;
        }
        if (!BlueNbtHotAddSupport.retainsPersistedPaint()) {
            runtime.inactive("bluenbt-retention-probe-failed");
            return;
        }
        runtime.activate();
    }

    boolean originallyRenderedBy(Variant variant, BlockRendererType renderer) {
        return originalRenderers.get(variant) == renderer;
    }

    void captureOriginalRenderers() {
        if (rendererSnapshotCaptured) {
            return;
        }
        IdentityHashMap<Variant, BlockRendererType> captured = new IdentityHashMap<>();
        resourcePack.getBlockStates().values().forEach(state -> state.forEach(variant -> {
            if (variant.getRenderer() == BlockRendererType.DEFAULT) {
                captured.put(variant, BlockRendererType.DEFAULT);
            }
        }));
        originalRenderers = Collections.unmodifiableMap(captured);
        rendererSnapshotCaptured = true;
    }

    @Override
    public void bake() {
        if (runtime.active()) {
            System.out.println("BlueMap Ender IO add-on active: routed 1 painted host.");
        }
    }

    @Override
    public Key getBlockStateKey(Key key) {
        return runtime.active() && EnderIo8211Profile.HOST_ID.equals(key.getFormatted())
                ? SYNTHETIC : key;
    }

    @Override
    public void getBlockProperties(BlockState state, BlockProperties.Builder builder) {
        if (runtime.active()
                && EnderIo8211Profile.HOST_ID.equals(state.getId().getFormatted())) {
            builder.culling(false).occluding(false).cullingIdentical(false);
        }
    }

}
