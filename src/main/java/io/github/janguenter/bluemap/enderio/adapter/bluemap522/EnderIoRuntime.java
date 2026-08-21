/*
 * SPDX-License-Identifier: MIT
 */

package io.github.janguenter.bluemap.enderio.adapter.bluemap522;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/** Shared activation state and bounded diagnostics for the one-host route. */
final class EnderIoRuntime {

    static final EnderIoRuntime INSTANCE = new EnderIoRuntime();
    private static final int MAX_DIAGNOSTICS = 8;

    private final AtomicBoolean active = new AtomicBoolean();
    private final AtomicInteger diagnostics = new AtomicInteger();

    private EnderIoRuntime() {
    }

    boolean active() {
        return active.get();
    }

    void activate() {
        active.set(true);
    }

    void inactive(String reason) {
        active.set(false);
        report("inactive-" + reason);
    }

    void report(String reason) {
        if (diagnostics.incrementAndGet() <= MAX_DIAGNOSTICS) {
            System.err.println("BlueMap Ender IO add-on: " + reason + ".");
        }
    }
}
