package com.vaadin.tests.components.orderedlayout;

import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class LayoutRenderTimeTest extends UI {

    @Override
    public void init(VaadinRequest r) {
        VerticalLayout root = new VerticalLayout();
        root.setWidth("100%");
        setContent(root);

        for (int i = 1; i <= 100; i++) {
            root.addComponent(getRow(i));
        }
    }

    private HorizontalLayout getRow(int i) {
        HorizontalLayout row = new HorizontalLayout();
        // row.setWidth("100%");
        // row.setSpacing(true);

        Embedded icon = new Embedded(null, new ThemeResource(
                "../runo/icons/32/document.png"));
        // row.addComponent(icon);
        // row.setComponentAlignment(icon, Alignment.MIDDLE_LEFT);

        Label text = new Label(
                "Row content #"
                        + i
                        + ". In pellentesque faucibus vestibulum. Nulla at nulla justo, eget luctus tortor. Nulla facilisi. Duis aliquet.");
        // row.addComponent(text);
        // row.setExpandRatio(text, 1);

        Button button = new Button("Edit");
        button.addStyleName(Reindeer.BUTTON_SMALL);
        row.addComponent(button);
        // row.setComponentAlignment(button, Alignment.MIDDLE_LEFT);

        button = new Button("Delete");
        button.addStyleName(Reindeer.BUTTON_SMALL);
        row.addComponent(button);
        // row.setComponentAlignment(button, Alignment.MIDDLE_LEFT);

        return row;
    }
}
