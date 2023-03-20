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
import org.openqa.selenium.WebElement;

import com.vaadin.flow.testutil.ChromeBrowserTest;

public class NoRouterIT extends ChromeBrowserTest {

    @Test
    public void applicationShouldStart() {
        open();

        WebElement button = findElement(By.tagName("button"));
        button.click();

        Assert.assertEquals(1, findElements(By.className("response")).size());
    }

}
