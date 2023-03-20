/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.uitest.servlet.ViewTestLayout;
import com.vaadin.flow.router.Route;

@Route(value = "com.vaadin.flow.uitest.ui.FragmentLinkView2", layout = ViewTestLayout.class)
public class FragmentLinkView2 extends FragmentLinkView {

    public FragmentLinkView2() {
        getElement().insertChild(0, new Element("div").setText("VIEW 2")
                .setAttribute("id", "view2"));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // do not call super onAttach since it adds a hashchangelistener
    }
}
