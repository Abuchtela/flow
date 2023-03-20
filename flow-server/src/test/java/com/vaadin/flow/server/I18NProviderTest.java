/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.vaadin.flow.di.DefaultInstantiator;
import com.vaadin.flow.di.Instantiator;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.internal.CurrentInstance;
import com.vaadin.flow.shared.ApplicationConstants;
import com.vaadin.tests.util.MockDeploymentConfiguration;

public class I18NProviderTest {

    private VaadinServletService service;

    private MockDeploymentConfiguration config = new MockDeploymentConfiguration();

    @Test
    public void no_property_defined_should_leave_with_default_locale()
            throws ServletException, ServiceException {
        initServletAndService(config);

        Assert.assertEquals("Locale was not the expected default locale",
                Locale.getDefault(), VaadinSession.getCurrent().getLocale());
    }

    @Test
    public void property_defined_should_init_registy_with_provider()
            throws ServletException, ServiceException {
        config.setApplicationOrSystemProperty(InitParameters.I18N_PROVIDER,
                TestProvider.class.getName());

        initServletAndService(config);

        Assert.assertEquals("Found wrong registry", TestProvider.class,
                VaadinService.getCurrent().getInstantiator().getI18NProvider()
                        .getClass());
    }

    @Test
    public void with_defined_provider_locale_should_be_the_available_one()
            throws ServletException, ServiceException {
        config.setApplicationOrSystemProperty(InitParameters.I18N_PROVIDER,
                TestProvider.class.getName());

        initServletAndService(config);

        I18NProvider i18NProvider = VaadinService.getCurrent().getInstantiator()
                .getI18NProvider();
        Assert.assertNotNull("No provider for ", i18NProvider);

        Assert.assertEquals("Locale was not the defined locale",
                i18NProvider.getProvidedLocales().get(0),
                VaadinSession.getCurrent().getLocale());

    }

    @After
    public void clearCurrentInstances() {
        CurrentInstance.clearAll();
    }

    private void initServletAndService(DeploymentConfiguration config)
            throws ServletException, ServiceException {
        service = new MockVaadinServletService(config) {
            @Override
            public Instantiator getInstantiator() {
                return new DefaultInstantiator(service);
            }
        };

        HttpServletRequest httpServletRequest = Mockito
                .mock(HttpServletRequest.class);
        HttpSession mockHttpSession = Mockito.mock(HttpSession.class);
        WrappedSession mockWrappedSession = new WrappedHttpSession(
                mockHttpSession) {
            final ReentrantLock lock = new ReentrantLock();
            {
                lock.lock();
            }

            @Override
            public Object getAttribute(String name) {
                Object res;
                String lockAttribute = service.getServiceName() + ".lock";
                if (lockAttribute.equals(name)) {
                    res = lock;
                } else {
                    res = super.getAttribute(name);
                }
                return res;
            }
        };

        VaadinRequest request = new VaadinServletRequest(httpServletRequest,
                service) {
            @Override
            public String getParameter(String name) {
                if (ApplicationConstants.REQUEST_TYPE_PARAMETER.equals(name)) {
                    return null;
                }
                return "1";
            }

            @Override
            public WrappedSession getWrappedSession(
                    boolean allowSessionCreation) {
                return mockWrappedSession;
            }
        };

        try {
            service.findVaadinSession(request);
        } catch (SessionExpiredException e) {
            throw new RuntimeException(e);
        }

        VaadinService.setCurrent(service);
    }
}
