/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.flow.component.html.testbench.H2Element;
import com.vaadin.flow.component.html.testbench.NativeButtonElement;
import com.vaadin.flow.testutil.ChromeBrowserTest;
import com.vaadin.flow.testutil.DevModeGizmoElement;
import com.vaadin.testbench.TestBenchElement;

public class FeatureIT extends ChromeBrowserTest {

    @Test
    public void enableAndDisableFeature() {
        open();
        Assert.assertEquals("Feature file missing",
                $(H2Element.class).id("value").getText());
        DevModeGizmoElement gizmo = $(DevModeGizmoElement.class).waitForFirst();

        gizmo.expand();

        gizmo.$(NativeButtonElement.class).id("features").click();
        gizmo.$(TestBenchElement.class)
                .id("feature-toggle-viteForFrontendBuild").click();

        try {
            $(NativeButtonElement.class).id("check").click();

            Assert.assertEquals(
                    "Feature file exists with properties: com.vaadin.experimental.viteForFrontendBuild",
                    $(H2Element.class).id("value").getText());
        } finally {
            $(NativeButtonElement.class).id("remove").click();

            Assert.assertEquals("Feature file missing",
                    $(H2Element.class).id("value").getText());
        }
    }

}
