/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.server.frontend.scanner.samples;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.router.Route;

@Route("")
@JsModule("foo.js")
@JavaScript("bar.js")
public class RouteComponent extends Component {

}
