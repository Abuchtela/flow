/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.multiwar.war2;

import com.vaadin.flow.component.html.Div;

public class HelloComponent extends Div {
    public HelloComponent() {
        setId("hello");
        setText("Hello from " + getClass().getName());
        addClickListener(e -> {
            setText("Hello " + getText());
        });
    }
}
