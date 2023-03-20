/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.fusion.rest;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.vaadin.fusion.FusionController;
import com.vaadin.fusion.FusionControllerMockBuilder;
import com.vaadin.flow.server.startup.ApplicationConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import({ FusionEndpoints.class, MyRestController.class })
public class EndpointWithRestControllerTest {

    private MockMvc mockMvcForEndpoint;

    @Autowired
    private MockMvc mockMvcForRest;

    @Autowired
    private ApplicationContext applicationContext;

    private ApplicationConfiguration appConfig;

    @Before
    public void setUp() {
        appConfig = Mockito.mock(ApplicationConfiguration.class);

        FusionControllerMockBuilder controllerMockBuilder = new FusionControllerMockBuilder();
        FusionController controller = controllerMockBuilder
                .withApplicationContext(applicationContext).build();
        mockMvcForEndpoint = MockMvcBuilders.standaloneSetup(controller)
                .build();
        Assert.assertNotEquals(null, applicationContext);
    }

    @Test
    // https://github.com/vaadin/flow/issues/8010
    public void shouldNotExposePrivateAndProtectedFields_when_CallingFromRestAPIs()
            throws Exception {
        String result = mockMvcForRest
                .perform(
                        get("/api/get").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        assertEquals("{\"name\":\"Bond\"}", result);
    }

    @Test
    // https://github.com/vaadin/flow/issues/8034
    public void should_BeAbleToSerializePrivateFieldsOfABean_when_CallingFromConnectEndPoint() {
        try {
            String result = callEndpointMethod("getBeanWithPrivateFields");
            assertEquals(
                    "{\"codeNumber\":\"007\",\"name\":\"Bond\",\"firstName\":\"James\"}",
                    result);
        } catch (Exception e) {
            fail("failed to serialize a bean with private fields");
        }
    }

    @Test
    // https://github.com/vaadin/flow/issues/8034
    public void should_BeAbleToSerializeABeanWithZonedDateTimeField() {
        try {
            String result = callEndpointMethod("getBeanWithZonedDateTimeField");
            assertNotNull(result);
            assertNotEquals("", result);
            assertNotEquals(
                    "{\"message\":\"Failed to serialize endpoint 'VaadinConnectTypeConversionEndpoints' method 'getBeanWithZonedDateTimeField' response. Double check method's return type or specify a custom mapper bean with qualifier 'vaadinEndpointMapper'\"}",
                    result);
        } catch (Exception e) {
            fail("failed to serialize a bean with ZonedDateTime field");
        }
    }

    @Test
    // https://github.com/vaadin/flow/issues/8067
    public void should_RepsectJacksonAnnotation_when_serializeBean()
            throws Exception {
        String result = callEndpointMethod("getBeanWithJacksonAnnotation");
        assertEquals("{\"name\":null,\"rating\":2,\"bookId\":null}", result);
    }

    @Test
    /**
     * this requires jackson-datatype-jsr310, which is added as a test scope
     * dependency. jackson-datatype-jsr310 is provided in
     * spring-boot-starter-web, which is part of vaadin-spring-boot-starter
     */
    public void should_serializeLocalTimeInExpectedFormat_when_UsingSpringBoot()
            throws Exception {
        String result = callEndpointMethod("getLocalTime");
        assertEquals("\"08:00:00\"", result);
    }

    private String callEndpointMethod(String methodName) throws Exception {
        String endpointName = FusionEndpoints.class.getSimpleName();
        String requestUrl = String.format("/%s/%s", endpointName, methodName);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(requestUrl)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        return mockMvcForEndpoint.perform(requestBuilder).andReturn()
                .getResponse().getContentAsString();
    }

    private ServletContext mockServletContext() {
        ServletContext context = Mockito.mock(ServletContext.class);
        Mockito.when(
                context.getAttribute(ApplicationConfiguration.class.getName()))
                .thenReturn(appConfig);
        return context;
    }
}
