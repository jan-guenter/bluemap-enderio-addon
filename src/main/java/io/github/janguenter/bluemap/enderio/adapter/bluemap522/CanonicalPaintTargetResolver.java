/*
 * SPDX-License-Identifier: MIT
 *
 * The conservative model proof adapts owner-controlled MIT code from the
 * Functional Storage add-on. No Ender IO source or asset is used.
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap522;

import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector4f;
import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.ResourcePath;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.VariantSet;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variants;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Element;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Face;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Model;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.util.Direction;
import de.bluecolored.bluemap.core.util.Key;

import java.util.Optional;

/** Proves one ordinary, opaque, propertyless canonical full-cube target. */
final class CanonicalPaintTargetResolver {

    private static final Vector3f FULL_MIN = Vector3f.ZERO;
    private static final Vector3f FULL_MAX = new Vector3f(16F, 16F, 16F);
    private static final Vector4f FULL_UV = new Vector4f(0F, 0F, 16F, 16F);
    private static final Vector4f MIRRORED_FULL_UV = new Vector4f(16F, 0F, 0F, 16F);
    private static final int MAX_VARIANTS = 16;

    private final ResourcePack resourcePack;

    CanonicalPaintTargetResolver(ResourcePack resourcePack) {
        this.resourcePack = resourcePack;
    }

    Optional<VariantSet> resolve(Key target) {
        de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState raw =
                resourcePack.getBlockStates().get(target);
        VariantSet variants = propertylessVariants(raw);
        if (variants == null) {
            return Optional.empty();
        }
        for (Variant variant : variants.getVariants()) {
            Model model = variant.getModel().getResource(resourcePack.getModels()::get);
            if (!canonicalVariant(variant, model) || !allTexturesOpaque(model)) {
                return Optional.empty();
            }
        }
        return Optional.of(variants);
    }

    static VariantSet propertylessVariants(
            de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState raw
    ) {
        if (raw == null || raw.getMultipart() != null) {
            return null;
        }
        Variants variants = raw.getVariants();
        if (variants == null || variants.getVariants().length != 0
                || variants.getDefaultVariant() == null) {
            return null;
        }
        VariantSet defaults = variants.getDefaultVariant();
        int count = defaults.getVariants().length;
        return count >= 1 && count <= MAX_VARIANTS ? defaults : null;
    }

    static boolean canonicalVariant(Variant variant, Model model) {
        if (variant == null
                || variant.getRenderer() != BlockRendererType.DEFAULT
                || ResourcePack.MISSING_BLOCK_MODEL.equals(variant.getModel())
                || variant.isUvlock()
                || variant.getX() != 0F
                || variant.getZ() != 0F
                || !safeCubeYRotation(variant.getY())
                || Double.compare(variant.getWeight(), 1D) != 0
                || model == null
                || !model.isAmbientocclusion()
                || model.getElements() == null
                || model.getElements().length != 1
                || model.getElements()[0] == null) {
            return false;
        }
        Element element = model.getElements()[0];
        if (!FULL_MIN.equals(element.getFrom())
                || !FULL_MAX.equals(element.getTo())
                || !element.isShade()
                || element.getLightEmission() != 0
                || element.getRotation().getX() != 0F
                || element.getRotation().getY() != 0F
                || element.getRotation().getZ() != 0F
                || element.getFaces().size() != Direction.values().length) {
            return false;
        }
        for (Direction direction : Direction.values()) {
            Face face = element.getFaces().get(direction);
            if (face == null || face.getCullface() != direction
                    || face.getRotation() != 0 || face.getTintindex() != -1
                    || !fullRangeUv(face.getUv())) {
                return false;
            }
        }
        return true;
    }

    private static boolean safeCubeYRotation(float rotation) {
        return rotation == 0F || rotation == 180F;
    }

    private static boolean fullRangeUv(Vector4f uv) {
        return FULL_UV.equals(uv) || MIRRORED_FULL_UV.equals(uv);
    }

    private boolean allTexturesOpaque(Model model) {
        Element element = model.getElements()[0];
        for (Direction direction : Direction.values()) {
            Face face = element.getFaces().get(direction);
            ResourcePath<Texture> path = face.getTexture()
                    .getTexturePath(model.getTextures()::get);
            Texture texture = path == null ? null : resourcePack.getTextures().get(path);
            if (!canonicalOpaque(texture)) {
                return false;
            }
        }
        return true;
    }

    static boolean canonicalOpaque(Texture texture) {
        return texture != null && texture.getAnimation() == null
                && !texture.isHalfTransparent()
                && texture.getColorStraight().a >= 1F;
    }
}
