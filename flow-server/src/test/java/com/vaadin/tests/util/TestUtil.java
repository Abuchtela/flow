/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.tests.util;

import java.lang.ref.WeakReference;
import java.util.Iterator;

import org.junit.Assert;

public class TestUtil {
    public static void assertArrays(Object[] actualObjects,
            Object[] expectedObjects) {
        Assert.assertEquals(
                "Actual contains a different number of values than was expected",
                expectedObjects.length, actualObjects.length);

        for (int i = 0; i < actualObjects.length; i++) {
            Object actual = actualObjects[i];
            Object expected = expectedObjects[i];

            Assert.assertEquals("Item[" + i + "] does not match", expected,
                    actual);
        }

    }

    public static void assertIterableEquals(Iterable<?> iterable1,
            Iterable<?> iterable2) {
        Iterator<?> i1 = iterable1.iterator();
        Iterator<?> i2 = iterable2.iterator();

        while (i1.hasNext()) {
            Object o1 = i1.next();
            if (!i2.hasNext()) {
                Assert.fail(
                        "The second iterable contains fewer items than the first. The object "
                                + o1 + " has no match in the second iterable.");
            }
            Object o2 = i2.next();
            Assert.assertEquals(o1, o2);
        }
        if (i2.hasNext()) {
            Assert.fail(
                    "The second iterable contains more items than the first. The object "
                            + i2.next()
                            + " has no match in the first iterable.");
        }
    }

    /**
     * Checks whether a weak reference is garbage collected. This methods also
     * tries to force collection of the reference by doing a few iterations of
     * {@link System#gc()}.
     *
     * @param ref
     *            the weak reference to check
     * @return <code>true</code> if the reference has been collected,
     *         <code>false</code> if the reference is still reachable
     * @throws InterruptedException
     *             if interrupted while waiting for garbage collection to finish
     */
    public static boolean isGarbageCollected(WeakReference<?> ref)
            throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            System.gc();
            if (ref.get() == null) {
                return true;
            }
        }
        return false;
    }
}
