/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.base.devserver.startup;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.annotation.HandlesTypes;

import com.vaadin.base.devserver.startup.DevModeInitializer.DevModeClassFinder;
import com.vaadin.flow.component.WebComponentExporter;
import com.vaadin.flow.component.WebComponentExporterFactory;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.SessionInitListener;
import com.vaadin.flow.server.UIInitListener;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.theme.NoTheme;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.ThemeDefinition;

import org.junit.Assert;
import org.junit.Test;

public class DevModeClassFinderTest {

    private DevModeClassFinder classFinder = new DevModeClassFinder(
            Collections.emptySet());

    @Test
    public void applicableClasses_knownClasses() {
        Collection<Class<?>> classes = getApplicableClasses();

        List<Class<?>> knownClasses = Arrays.asList(Route.class,
                UIInitListener.class, VaadinServiceInitListener.class,
                WebComponentExporter.class, WebComponentExporterFactory.class,
                NpmPackage.class, NpmPackage.Container.class, JsModule.class,
                JsModule.Container.class, JavaScript.class,
                JavaScript.Container.class, CssImport.class,
                CssImport.Container.class, Theme.class, NoTheme.class,
                HasErrorParameter.class, PWA.class, AppShellConfigurator.class);

        for (Class<?> clz : classes) {
            assertTrue("should be a known class " + clz.getName(),
                    knownClasses.contains(clz));
        }
        Assert.assertEquals(knownClasses.size(), classes.size());
    }

    @Test
    public void callGetSubTypesOfByClass_expectedType_doesNotThrow() {
        for (Class<?> clazz : getApplicableClasses()) {
            classFinder.getSubTypesOf(clazz);
        }
    }

    @Test
    public void callGetSubTypesOfByName_expectedType_doesNotThrow()
            throws ClassNotFoundException {
        for (Class<?> clazz : getApplicableClasses()) {
            classFinder.getSubTypesOf(clazz.getName());
        }
    }

    @Test
    public void callGetgetAnnotatedClassesByName_expectedType_doesNotThrow()
            throws ClassNotFoundException {
        for (Class<?> clazz : getApplicableClasses()) {
            classFinder.getAnnotatedClasses(clazz.getName());
        }
    }

    @Test
    public void callGetgetAnnotatedClassesByClass_expectedType_doesNotThrow()
            throws ClassNotFoundException {
        for (Class<?> clazz : getApplicableClasses()) {
            if (clazz.isAnnotation()) {
                classFinder.getAnnotatedClasses((Class) clazz);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void callGetgetAnnotatedClassesByClass_unexpectedType_throw() {
        classFinder.getAnnotatedClasses(Test.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void callGetgetAnnotatedClassesByName_unexpectedType_throw()
            throws ClassNotFoundException {
        classFinder.getAnnotatedClasses(ThemeDefinition.class.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void callGetSubTypesOfByClass_unexpectedType_throw() {
        DevModeClassFinder classFinder = new DevModeClassFinder(
                Collections.emptySet());
        classFinder.getSubTypesOf(Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void callGetSubTypesOfByName_unexpectedType_throw()
            throws ClassNotFoundException {
        classFinder.getSubTypesOf(SessionInitListener.class.getName());
    }

    private Collection<Class<?>> getApplicableClasses() {
        HandlesTypes handlesTypes = DevModeStartupListener.class
                .getAnnotation(HandlesTypes.class);
        return Stream.of(handlesTypes.value()).collect(Collectors.toList());
    }
}
