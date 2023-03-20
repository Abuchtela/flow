/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.fusion.auth;

import javax.servlet.http.Cookie;

import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.communication.IndexHtmlResponse;
import com.vaadin.flow.shared.ApplicationConstants;

public class CsrfIndexHtmlRequestListenerTest {
    static private final String TEST_CONTEXT_PATH = "/test-context";
    private CsrfIndexHtmlRequestListener csrfIndexHtmlRequestListener;
    private IndexHtmlResponse indexHtmlResponse;
    private VaadinRequest vaadinRequest;

    @Before
    public void setup() {
        csrfIndexHtmlRequestListener = Mockito
                .spy(new CsrfIndexHtmlRequestListener());
        vaadinRequest = Mockito.mock(VaadinRequest.class);
        Mockito.doReturn(TEST_CONTEXT_PATH).when(vaadinRequest)
                .getContextPath();
        Mockito.doReturn(true).when(vaadinRequest).isSecure();
        VaadinResponse vaadinResponse = Mockito.mock(VaadinResponse.class);
        Document document = Mockito.mock(Document.class);
        indexHtmlResponse = new IndexHtmlResponse(vaadinRequest, vaadinResponse,
                document);
    }

    @Test
    public void should_setCsrfCookie_when_null_cookies_and_SpringCsrfTokenNotPresent() {
        Mockito.doReturn(false).when(csrfIndexHtmlRequestListener)
                .isSpringCsrfTokenPresent(vaadinRequest);

        useRequestCookies(null);

        csrfIndexHtmlRequestListener.modifyIndexHtmlResponse(indexHtmlResponse);

        verifyCsrfCookieAdded();
    }

    @Test
    public void should_setCsrfCookie_when_absent_and_SpringCsrfTokenNotPresent() {
        Mockito.doReturn(false).when(csrfIndexHtmlRequestListener)
                .isSpringCsrfTokenPresent(vaadinRequest);

        useRequestCookies(new Cookie[0]);

        csrfIndexHtmlRequestListener.modifyIndexHtmlResponse(indexHtmlResponse);

        verifyCsrfCookieAdded();
    }

    @Test
    public void should_notSetCsrfCookie_when_SpringCsrfPresents() {
        Mockito.doReturn(true).when(csrfIndexHtmlRequestListener)
                .isSpringCsrfTokenPresent(vaadinRequest);

        csrfIndexHtmlRequestListener.modifyIndexHtmlResponse(indexHtmlResponse);

        Mockito.verify(indexHtmlResponse.getVaadinResponse(), Mockito.never())
                .addCookie(ArgumentMatchers.any());
    }

    @Test
    public void should_notSetCsrfCookie_when_present() {
        Mockito.doReturn(false).when(csrfIndexHtmlRequestListener)
                .isSpringCsrfTokenPresent(vaadinRequest);

        Cookie csrfRequestCookie = new Cookie(ApplicationConstants.CSRF_TOKEN,
                "foo");
        useRequestCookies(new Cookie[] { csrfRequestCookie });

        csrfIndexHtmlRequestListener.modifyIndexHtmlResponse(indexHtmlResponse);

        Mockito.verify(indexHtmlResponse.getVaadinResponse(), Mockito.never())
                .addCookie(ArgumentMatchers.any());
    }

    private void useRequestCookies(Cookie[] cookies) {
        Mockito.doReturn(cookies).when(indexHtmlResponse.getVaadinRequest())
                .getCookies();
    }

    private void verifyCsrfCookieAdded() {
        ArgumentCaptor<Cookie> cookieArgumentCaptor = ArgumentCaptor
                .forClass(Cookie.class);
        Mockito.verify(indexHtmlResponse.getVaadinResponse())
                .addCookie(cookieArgumentCaptor.capture());
        Cookie csrfCookie = cookieArgumentCaptor.getValue();

        Assert.assertEquals(ApplicationConstants.CSRF_TOKEN,
                csrfCookie.getName());
        Assert.assertFalse("Unexpected empty string value",
                csrfCookie.getValue().isEmpty());
        Assert.assertFalse(csrfCookie.isHttpOnly());
        Assert.assertTrue(csrfCookie.getSecure());
        Assert.assertEquals(TEST_CONTEXT_PATH, csrfCookie.getPath());
    }
}