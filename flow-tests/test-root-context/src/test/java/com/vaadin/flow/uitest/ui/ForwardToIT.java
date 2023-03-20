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
import org.openqa.selenium.By;

import com.vaadin.flow.testutil.ChromeBrowserTest;

public class ForwardToIT extends ChromeBrowserTest {

    @Test
    public void testForwardingToView() {
        String initUrl = getDriver().getCurrentUrl();
        open();

        Assert.assertTrue("should forward to specified view",
                findElement(By.id("root")).isDisplayed());
        Assert.assertTrue("should update update the URL",
                getDriver().getCurrentUrl().endsWith(
                        "com.vaadin.flow.uitest.ui.BasicComponentView"));

        getDriver().navigate().back();
        Assert.assertEquals("should replace history state",
                getDriver().getCurrentUrl(), initUrl);
    }
}
