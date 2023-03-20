/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.component.html;

import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasAriaLabel;
import com.vaadin.flow.component.HasOrderedComponents;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;

/**
 * Component representing a <code>&lt;nav&gt;</code> element.
 *
 * @author Vaadin Ltd
 * @since 1.0
 */
@Tag(Tag.NAV)
public class Nav extends HtmlContainer
        implements ClickNotifier<Nav>, HasOrderedComponents, HasAriaLabel {

    /**
     * Creates a new empty nav.
     */
    public Nav() {
        super();
    }

    /**
     * Creates a new nav with the given child components.
     *
     * @param components
     *            the child components
     */
    public Nav(Component... components) {
        super(components);
    }
}
