/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui.push;

import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.internal.nodefeature.PushConfigurationMap;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.shared.ui.Transport;

/*
 * Note that @Push is generally not supported in this location, but instead
 * explicitly picked up by logic in the BasicPushUI constructor.
 */
@Push(transport = Transport.WEBSOCKET)
public class BasicPushWebsocketUI extends BasicPushUI {

    @Override
    public void init(VaadinRequest request) {
        super.init(request);
        // Don't use fallback so we can easier detect failures
        getPushConfiguration().setParameter(
                PushConfigurationMap.FALLBACK_TRANSPORT_KEY, "none");
    }

}
