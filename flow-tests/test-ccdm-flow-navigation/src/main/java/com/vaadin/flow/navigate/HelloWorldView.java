/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.navigate;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "hello")
@PageTitle("Hello World")
public class HelloWorldView extends Span {
    public static final String NAVIGATE_ABOUT = "navigate-about";
    public static final String IS_CONNECTED_ON_INIT = "is-connected-on-init";
    public static final String IS_CONNECTED_ON_ATTACH = "is-connected-on-attach";

    private final Span isConnectedOnInit = new Span("");
    private final Span isConnectedOnAttach = new Span("");

    public HelloWorldView() {
        setId("hello-world-view");
        NativeButton toAbout = new NativeButton("Say hello",
                e -> getUI().get().navigate("about"));
        toAbout.setId(NAVIGATE_ABOUT);
        add(toAbout);

        isConnectedOnInit.setId(IS_CONNECTED_ON_INIT);
        updateIsConnected(isConnectedOnInit);
        add(new Paragraph(new Text("Connected on init: "), isConnectedOnInit));

        isConnectedOnAttach.setId(IS_CONNECTED_ON_ATTACH);
        isConnectedOnAttach.addAttachListener(
                event -> updateIsConnected(isConnectedOnAttach));
        add(new Paragraph(new Text("Connected on attach: "),
                isConnectedOnAttach));
    }

    private void updateIsConnected(Span output) {
        output.getElement()
                .executeJs("this.textContent=String(this.isConnected)");
    }
}
