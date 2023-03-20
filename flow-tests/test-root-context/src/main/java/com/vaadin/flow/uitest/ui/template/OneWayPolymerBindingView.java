/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui.template;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.AllowClientUpdates;
import com.vaadin.flow.templatemodel.ClientUpdateMode;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.uitest.servlet.ViewTestLayout;

@Route(value = "com.vaadin.flow.uitest.ui.template.OneWayPolymerBindingView", layout = ViewTestLayout.class)
@Tag("my-one-way-template")
@JsModule("OneWayPolymerBinding.js")
public class OneWayPolymerBindingView
        extends PolymerTemplate<OneWayPolymerBindingView.MessageModel> {
    static final String MESSAGE = "testMessage";
    static final String NEW_MESSAGE = "newMessage";

    @EventHandler
    private void changeModelValue() {
        getModel().setMessage(NEW_MESSAGE);
    }

    public interface MessageModel extends TemplateModel {
        void setMessage(String message);

        void setTitle(String title);

        @AllowClientUpdates(ClientUpdateMode.ALLOW)
        String getMessage();

        @AllowClientUpdates(ClientUpdateMode.ALLOW)
        String getTitle();
    }

    public OneWayPolymerBindingView() {
        setId("template");
        getModel().setMessage(MESSAGE);
    }
}
