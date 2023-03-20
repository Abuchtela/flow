/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.viteapp;

import com.vaadin.flow.component.html.testbench.ParagraphElement;
import com.vaadin.flow.testutil.ChromeBrowserTest;
import com.vaadin.flow.testutil.DevModeGizmoElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.viteapp.views.empty.MainView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BasicsIT extends ViteDevModeIT {

    @Test
    public void applicationStarts() {
        TestBenchElement header = $("h2").first();
        Assert.assertEquals("This place intentionally left empty",
                header.getText());
    }

    @Test
    public void imageFromThemeShown() {
        getDriver().get(getRootURL());
        waitForDevServer();
        TestBenchElement img = $("img").id(MainView.PLANT);
        waitUntil(driver -> {
            String heightString = (String) executeScript(
                    "return getComputedStyle(arguments[0]).height.replace('px','')",
                    img);
            float height = Float.parseFloat(heightString);
            return (height > 150);
        });
    }

    @Test
    public void applicationUsesVite() {
        getDriver().get(getRootURL());
        waitForDevServer();
        TestBenchElement viteStatus = $(ParagraphElement.class).id("status");
        Assert.assertEquals("Vite feature is true", viteStatus.getText());
    }

    @Test
    public void debugWindowShown() {
        DevModeGizmoElement gizmo = $(DevModeGizmoElement.class).waitForFirst();
        gizmo.expand();
        Assert.assertNotNull(gizmo.$("div").attributeContains("class", "window")
                .attributeContains("class", "visible").waitForFirst());
    }

    @Test
    public void canImportJson() {
        $("button").id(MainView.LOAD_AND_SHOW_JSON).click();
        Assert.assertEquals("{\"hello\":\"World\"}",
                $("*").id(MainView.JSON_CONTAINER).getText());
    }

    @Test
    public void importFromDirectoryWorks() {
        String importResult = $("div").id("directoryImportResult").getText();
        Assert.assertEquals("Directory import ok", importResult);
    }
}
