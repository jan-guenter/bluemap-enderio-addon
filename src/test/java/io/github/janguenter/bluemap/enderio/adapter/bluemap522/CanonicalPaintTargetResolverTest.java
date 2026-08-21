/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap522;

import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector4f;
import de.bluecolored.bluemap.core.resources.ResourcePath;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.BlockState;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.VariantSet;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variants;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Element;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Face;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Model;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.Rotation;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.model.TextureVariable;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.util.Direction;
import de.bluecolored.bluemap.core.util.Key;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CanonicalPaintTargetResolverTest {

    private static final ResourcePath<Model> MODEL_PATH =
            new ResourcePath<>("minecraft:block/stone");
    private static final ResourcePath<Texture> TEXTURE_PATH =
            new ResourcePath<>("minecraft:block/stone");

    @Test
    void admitsOnlyOneUnconditionalVariant() {
        Variant variant = new Variant(MODEL_PATH);
        assertSame(variant, CanonicalPaintTargetResolver.propertylessVariant(
                new BlockState(new Variants(new VariantSet[0], new VariantSet(variant)))
        ));
        assertNull(CanonicalPaintTargetResolver.propertylessVariant(null));
        assertNull(CanonicalPaintTargetResolver.propertylessVariant(
                new BlockState(new Variants(
                        new VariantSet[] {new VariantSet(variant)}, new VariantSet(variant)
                ))
        ));
        assertNull(CanonicalPaintTargetResolver.propertylessVariant(
                new BlockState(new Variants(
                        new VariantSet[0], new VariantSet(variant, variant)
                ))
        ));
    }

    @Test
    void provesCanonicalFullCubeAndRejectsRotatedOrIncompleteModels() {
        Variant variant = new Variant(MODEL_PATH);
        assertTrue(CanonicalPaintTargetResolver.canonicalVariant(
                variant, canonicalModel(Rotation.ZERO, true, 0, true)
        ));
        assertFalse(CanonicalPaintTargetResolver.canonicalVariant(
                new Variant(MODEL_PATH, 0F, 90F, 0F),
                canonicalModel(Rotation.ZERO, true, 0, true)
        ));
        assertFalse(CanonicalPaintTargetResolver.canonicalVariant(
                variant, canonicalModel(Rotation.ZERO, true, 1, true)
        ));
        assertFalse(CanonicalPaintTargetResolver.canonicalVariant(
                variant, canonicalModel(Rotation.ZERO, true, 0, false)
        ));
    }

    @Test
    void provesOnlyStaticFullyOpaqueTextures() throws IOException {
        assertTrue(CanonicalPaintTargetResolver.canonicalOpaque(texture(255)));
        assertFalse(CanonicalPaintTargetResolver.canonicalOpaque(texture(0)));
        assertFalse(CanonicalPaintTargetResolver.canonicalOpaque(texture(127)));
        assertFalse(CanonicalPaintTargetResolver.canonicalOpaque(null));
    }

    private static Model canonicalModel(
            Rotation rotation,
            boolean shade,
            int lightEmission,
            boolean includeAllFaces
    ) {
        EnumMap<Direction, Face> faces = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            if (!includeAllFaces && direction == Direction.NORTH) {
                continue;
            }
            faces.put(direction, new Face(
                    new Vector4f(0F, 0F, 16F, 16F),
                    new TextureVariable(TEXTURE_PATH),
                    direction
            ));
        }
        Element element = new Element(
                Vector3f.ZERO, new Vector3f(16F, 16F, 16F),
                rotation, shade, lightEmission, faces
        );
        return new Model(Map.of(), new Element[] {element}, true);
    }

    private static Texture texture(int alpha) throws IOException {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        int argb = alpha << 24 | 0x007f5f3f;
        image.setRGB(0, 0, argb);
        image.setRGB(1, 0, argb);
        image.setRGB(0, 1, argb);
        image.setRGB(1, 1, argb);
        return Texture.from(Key.parse("test:paint"), image);
    }
}
