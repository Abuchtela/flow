/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.component;

import java.io.Serializable;

import com.vaadin.flow.shared.Registration;

/**
 * Mixin interface for components that support adding detach listeners.
 *
 * @author Vaadin Ltd
 * @since 1.0
 */
public interface DetachNotifier extends Serializable {

    /**
     * Adds a detach listener to this component.
     *
     * @param listener
     *            the listener to add, not <code>null</code>
     * @return a handle that can be used for removing the listener
     */
    default Registration addDetachListener(
            ComponentEventListener<DetachEvent> listener) {
        if (this instanceof Component) {
            return ComponentUtil.addListener((Component) this,
                    DetachEvent.class, listener);
        } else {
            throw new IllegalStateException(String.format(
                    "The class '%s' doesn't extend '%s'. "
                            + "Make your implementation for the method '%s'.",
                    getClass().getName(), Component.class.getSimpleName(),
                    "addDetachListener"));
        }
    }
}
