/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.viteapp;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.vaadin.flow.testutil.ChromeBrowserTest;

abstract public class ViteDevModeIT extends ChromeBrowserTest {
    @BeforeClass
    public static void driver() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void openView() {
        getDriver().get(getRootURL());
        waitForDevServer();
        getCommandExecutor().waitForVaadin();
        waitUntil(ExpectedConditions
                .presenceOfElementLocated(By.id("loadAndShowJson")), 300);
    }
}
