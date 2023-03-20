/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.uitest.servlet.ViewTestLayout;

/**
 * View for testing DOM changes inside attach listeners, based on
 * https://github.com/vaadin/flow/issues/4209
 *
 * @since 1.0
 */
@Route(value = "com.vaadin.flow.uitest.ui.AttachListenerView", layout = ViewTestLayout.class)
public class AttachListenerView extends AbstractDivView {

    private Map<String, Input> radioButtons = new HashMap<>();

    public AttachListenerView() {
        Div group = new Div();

        createRadioButton(group, "firstAsHost", "host", "Add to first");
        createRadioButton(group, "middleAsHost", "host", "Add to middle");
        createRadioButton(group, "lastAsHost", "host", "Add to last");
        add(group);

        group = new Div();
        createRadioButton(group, "firstAsChild", "child", "First as child");
        createRadioButton(group, "middleAsChild", "child", "Middle as child");
        createRadioButton(group, "lastAsChild", "child", "Last as child");
        add(group);

        group = new Div();
        createRadioButton(group, "attachListenerToFirst", "listener",
                "Attach listener to first");
        createRadioButton(group, "attachListenerToMiddle", "listener",
                "Attach listener to middle");
        createRadioButton(group, "attachListenerToLast", "listener",
                "Attach listener to last");
        add(group);

        /*
         * The tests consists in creating permutations of components being added
         * to themselves, and some components listening to the attach events.
         * Initially all components are added to the result Div, and the attach
         * event generated by that is treated according to the selected options.
         *
         * The structure of the result Div is then changed by the attach
         * listener, and evaluated by the integration tests.
         */
        add(createButton("Submit", "submit", click -> {
            Span first = new Span("First");
            Span middle = new Span("Middle");
            Span last = new Span("Last");

            Span host = getHostComponent(first, middle, last);
            Span child = getChildComponent(first, middle, last);
            Span listener = getListenerComponent(first, middle, last);

            configureAttachPermutation(host, child, listener);

            Div result = new Div(first, middle, last);
            result.setId("result");
            add(result);
        }));
    }

    private Span getHostComponent(Span first, Span middle, Span last) {
        Span host;
        if (isChecked("firstAsHost")) {
            host = first;
        } else if (isChecked("middleAsHost")) {
            host = middle;
        } else {
            host = last;
        }
        return host;
    }

    private Span getChildComponent(Span first, Span middle, Span last) {
        Span child;
        if (isChecked("firstAsChild")) {
            child = first;
        } else if (isChecked("middleAsChild")) {
            child = middle;
        } else {
            child = last;
        }
        return child;
    }

    private Span getListenerComponent(Span first, Span middle, Span last) {
        Span listener;
        if (isChecked("attachListenerToFirst")) {
            listener = first;
        } else if (isChecked("attachListenerToMiddle")) {
            listener = middle;
        } else {
            listener = last;
        }
        return listener;
    }

    private void configureAttachPermutation(Span host, Span child,
            Span listener) {
        listener.addAttachListener(event -> {
            /*
             * The isInitialAttach check is needed to prevent stackoverflow
             * errors when the child listens to attach events from itself.
             */
            if (event.isInitialAttach()) {
                host.add(child);
            }
        });
    }

    private Input createRadioButton(HasComponents parent, String id,
            String group, String text) {

        Input input = new Input();
        input.getElement().setAttribute("type", "radio")
                .setAttribute("name", group).setAttribute("value", text)
                .addPropertyChangeListener("checked", "change", event -> {
                });
        input.setId(id);
        radioButtons.put(id, input);

        Label label = new Label(text);
        label.setFor(id);

        parent.add(input, label);

        return input;
    }

    private boolean isChecked(String inputId) {
        return radioButtons.get(inputId).getElement()
                .getProperty("checked") != null;
    }
}
