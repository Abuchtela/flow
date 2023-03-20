/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.component.internal;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.vaadin.flow.server.VaadinSession;

/**
 * A completable future that will throw from blocking operations if the current
 * thread holds the session lock.
 * <p>
 * This is used for pending JS results because a response providing the value
 * cannot be processed while the session is locked.
 * <p>
 * Throwing is unfortunately only practical for this immediate instance, but
 * there isn't any sensible way of also intercepting for instances derived using
 * e.g. <code>thenAccept</code>.
 * 
 * <p>
 * For internal use only. May be renamed or removed in a future release.
 *
 * @since 2.1.4
 */
public class DeadlockDetectingCompletableFuture<T>
        extends CompletableFuture<T> {
    private final VaadinSession session;

    /**
     * Creates a new deadlock detecting completable future tied to the given
     * session.
     *
     * @param session
     *            the session to use, or <code>null</code> to not do any
     *            deadlock checking
     */
    public DeadlockDetectingCompletableFuture(VaadinSession session) {
        this.session = session;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        throwIfDeadlock();
        return super.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        throwIfDeadlock();
        return super.get(timeout, unit);
    }

    @Override
    public T join() {
        throwIfDeadlock();
        return super.join();
    }

    private void throwIfDeadlock() {
        if (isDone()) {
            // Won't block if we're done
            return;
        }
        if (session != null && session.hasLock()) {
            /*
             * Disallow blocking if the current thread holds the lock for the
             * session that would need to be locked by a request thread to
             * complete the result
             */
            throw new IllegalStateException(
                    "Cannot block on the value from the thread that has locked the session. "
                            + "This is because the request that delivers the value cannot be processed while this thread holds the session lock.");
        }
    }
}
