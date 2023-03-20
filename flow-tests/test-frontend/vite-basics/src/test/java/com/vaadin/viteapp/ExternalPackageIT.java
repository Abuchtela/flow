/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.viteapp;

import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.testbench.NativeButtonElement;
import com.vaadin.flow.component.html.testbench.ParagraphElement;
import com.vaadin.viteapp.views.empty.MainView;

import org.junit.Assert;
import org.junit.Test;

public class ExternalPackageIT extends ViteDevModeIT {

    @Test
    public void packageOutsideNpmWorks() {
        $(NativeButtonElement.class).id(MainView.OUTSIDE).click();
        Assert.assertEquals("It works - It works", $(ParagraphElement.class)
                .id(MainView.OUTSIDE_RESULT).getText());
    }
}
