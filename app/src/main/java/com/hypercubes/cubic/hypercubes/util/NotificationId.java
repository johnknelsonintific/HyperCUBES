package com.hypercubes.cubic.hypercubes.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Helper class to generate unique notification IDs per application instance.
 */
public class NotificationId {
    // TODO: this should get the initial value from a persisted storage as application may restart.
    private static final AtomicInteger sCounter = new AtomicInteger(1);

    /**
     * Get a new notification ID.
     * @return New notification ID.
     */
    public static int getId() {
        return sCounter.incrementAndGet();
    }
}