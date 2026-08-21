/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap522;

import de.bluecolored.bluemap.core.util.Key;

import java.util.Optional;
import java.util.regex.Pattern;

/** Strict decoder for the exact top-level persisted Paint string. */
final class PaintSnapshotDecoder {

    private static final int MAX_TEXT = 128;
    private static final Pattern IDENTIFIER = Pattern.compile(
            "[a-z0-9_.-]+:[a-z0-9_./-]+"
    );

    Optional<Key> decode(EnderIoBlockEntityData data) {
        return data == null ? Optional.empty() : decode(data.paint());
    }

    Optional<Key> decode(String paint) {
        if (paint == null || paint.isEmpty() || paint.length() > MAX_TEXT
                || !IDENTIFIER.matcher(paint).matches()) {
            return Optional.empty();
        }
        Key key = Key.parse(paint);
        if ("enderio".equals(key.getNamespace())
                || "minecraft:air".equals(key.getFormatted())) {
            return Optional.empty();
        }
        return Optional.of(key);
    }
}
