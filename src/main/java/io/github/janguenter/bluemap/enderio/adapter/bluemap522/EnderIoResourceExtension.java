/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap522;

import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.VariantSet;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variants;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.BlockProperties;
import de.bluecolored.bluemap.core.world.BlockState;
import io.github.janguenter.bluemap.enderio.profile.EnderIo8211Profile;
import io.github.janguenter.bluemap.enderio.profile.ExactEnderIoArtifactDetector;

import java.nio.file.Path;

/** Exact-artifact activation and one-host synthetic routing. */
final class EnderIoResourceExtension implements ResourcePackExtension {

    static final Key SYNTHETIC = Key.parse("bluemap_enderio:painted_redstone");

    private final ResourcePack resourcePack;
    private final EnderIoRuntime runtime;

    EnderIoResourceExtension(ResourcePack resourcePack, EnderIoRuntime runtime) {
        this.resourcePack = resourcePack;
        this.runtime = runtime;
    }

    @Override
    public void loadResources(Iterable<Path> roots) {
        if (Boolean.getBoolean("bluemap.enderio.disabled")) {
            runtime.inactive("operator-disabled");
            return;
        }
        if (!ExactEnderIoArtifactDetector.matches(
                roots, EnderIo8211Profile.JAR_SHA256, EnderIo8211Profile.JAR_SIZE)) {
            runtime.inactive("exact-artifact-missing-or-duplicate");
            return;
        }
        if (!validDispatch(resourcePack.getBlockStates().get(SYNTHETIC))) {
            runtime.inactive("synthetic-dispatch-invalid");
            return;
        }
        runtime.activate();
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

    private static boolean validDispatch(
            de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState state
    ) {
        if (state == null || state.getMultipart() != null) {
            return false;
        }
        Variants variants = state.getVariants();
        if (variants == null || variants.getVariants().length != 0
                || variants.getDefaultVariant() == null) {
            return false;
        }
        VariantSet defaults = variants.getDefaultVariant();
        return defaults.getVariants().length == 1
                && BlueMap522Adapter.isExpectedDispatch(defaults.getVariants()[0]);
    }
}
