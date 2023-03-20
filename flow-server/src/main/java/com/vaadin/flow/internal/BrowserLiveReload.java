/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.internal;

import org.atmosphere.cpr.AtmosphereResource;

/**
 * Provides a way to reload browser tabs via web socket connection passed as a
 * {@link AtmosphereResource}.
 * <p>
 * For internal use only. May be renamed or removed in a future release.
 *
 * @author Vaadin Ltd
 *
 */
public interface BrowserLiveReload {

    /**
     * Live reload enabling technology detected.
     */
    enum Backend {
        HOTSWAP_AGENT, JREBEL, SPRING_BOOT_DEVTOOLS;
    }

    /**
     * Detects and return enabling live reload backend technology.
     * 
     * @return enabling technology, or <code>null</code> if none
     */
    Backend getBackend();

    /**
     * Sets the live reload backend technology explicitly.
     *
     * @param backend
     *            enabling technology, not <code>null</code>.
     */
    void setBackend(Backend backend);

    /**
     * Sets the web socket connection resource when it's established.
     *
     * @param resource
     *            a web socket connection resource, not <code>null</code>.
     */
    void onConnect(AtmosphereResource resource);

    /**
     * Removes the web socket connection resource, not <code>null</code>.
     *
     * @param resource
     *            a web socket connection resource
     */
    void onDisconnect(AtmosphereResource resource);

    /**
     * Returns whether the passed connection is a browser live-reload
     * connection.
     * 
     * @param resource
     *            a web socket connection resource, not <code>null</code>.
     * @return whether the web socket connection is for live reload
     */
    boolean isLiveReload(AtmosphereResource resource);

    /**
     * Requests reload via the resource provided via
     * {@link #onConnect(AtmosphereResource)} call.
     */
    void reload();

    /**
     * Called when any message is received through the connection.
     */
    void onMessage(String msg);

}
