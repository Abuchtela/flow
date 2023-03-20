/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui.push;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.vaadin.flow.testcategory.PushTests;
import com.vaadin.flow.testutil.ChromeBrowserTest;

@Category(PushTests.class)
public class PushWithRequireJSIT extends ChromeBrowserTest {

    @Test
    public void pushWithRequireJS() {
        open();

        checkLogsForErrors(
                msg -> msg.contains("sockjs-node") || msg.contains("[WDS]"));
    }

}
