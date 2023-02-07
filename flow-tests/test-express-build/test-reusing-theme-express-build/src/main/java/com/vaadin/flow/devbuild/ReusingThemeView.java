/*
 * Copyright 2000-2023 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.flow.devbuild;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.uitest.servlet.ViewTestLayout;

@Route(value = "com.vaadin.flow.devbuild.ReusingThemeView", layout = ViewTestLayout.class)
public class ReusingThemeView extends Div {

    public ReusingThemeView() {
        Image snowFlake = new Image(
                "themes/reusable-theme/fortawesome/icons/snowflake.svg",
                "snowflake");
        snowFlake.setHeight("5em");
        snowFlake.setId("snowflake");
        add(snowFlake);
    }
}
