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
package com.vaadin.flow.webcomponent.devbuild;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeButton;

public class ExportedComponentTwo extends Div {

    public static final String EXPORTED_ID_TWO = "exported-inner-two";

    public ExportedComponentTwo() {
        setId(EXPORTED_ID_TWO);

        NativeButton testButton = new NativeButton("Test Button");
        add(testButton);
    }
}
