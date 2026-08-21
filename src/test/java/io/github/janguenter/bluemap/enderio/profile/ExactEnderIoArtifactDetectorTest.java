/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.profile;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExactEnderIoArtifactDetectorTest {

    @Test
    void acceptsOnlyOneExternallySuppliedExactJar() {
        String configured = System.getProperty("enderIoJar");
        assertTrue(configured != null && !configured.isBlank(),
                "test JVM needs -PenderIoJar=<exact JAR>");
        Path exact = Path.of(configured);
        assertTrue(ExactEnderIoArtifactDetector.matches(
                List.of(exact), EnderIo8211Profile.JAR_SHA256, EnderIo8211Profile.JAR_SIZE
        ));
        assertTrue(ExactEnderIoArtifactDetector.matches(
                List.of(exact, exact),
                EnderIo8211Profile.JAR_SHA256,
                EnderIo8211Profile.JAR_SIZE
        ));
        assertFalse(ExactEnderIoArtifactDetector.matches(
                List.of(Path.of("src/main/resources/bluemap.addon.json")),
                EnderIo8211Profile.JAR_SHA256,
                EnderIo8211Profile.JAR_SIZE
        ));
    }

    @Test
    void rejectsUnpinnedIdentityArguments() {
        assertThrows(IllegalArgumentException.class, () ->
                ExactEnderIoArtifactDetector.matches(List.of(), "0".repeat(64), 1L)
        );
    }
}
