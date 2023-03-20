/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.viteapp.views.template;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;

@Tag(ReflectivelyReferencedComponent.TAG)
@JsModule("./templates/ReflectivelyReferencedComponent.ts")
public class ReflectivelyReferencedComponent extends LitTemplate {

    public static final String TAG = "reflectively-referenced-component";
}
