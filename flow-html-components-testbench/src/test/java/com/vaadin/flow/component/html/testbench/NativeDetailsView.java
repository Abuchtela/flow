/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.component.html.testbench;

import java.util.concurrent.atomic.AtomicInteger;

import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeDetails;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

@Route("Details")
public class NativeDetailsView extends Div implements AfterNavigationObserver {

    private final NativeDetails details;

    public NativeDetailsView() {
        AtomicInteger eventCounter = new AtomicInteger(0);

        Div log = new Div();
        log.setId("log");

        details = new NativeDetails("summary", new Paragraph("content"));
        details.setId("details");
        details.addToggleListener(e -> {
            log.setText("Toggle event number '" + eventCounter.incrementAndGet()
                    + "' is '" + e.isOpened() + "'");
        });

        NativeButton button = new NativeButton("open or close summary");
        button.setId("btn");
        button.addClickListener(e -> {
            // reverts the current details' open state
            details.setOpen(!details.isOpen());
        });
        add(log, button, details);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        details.setOpen(event.getLocation().getPath().endsWith("open"));
    }
}
