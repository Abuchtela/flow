/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.component.webcomponent;

import java.io.Serializable;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.WebComponentExporter;
import com.vaadin.flow.di.Instantiator;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.webcomponent.PropertyData;
import com.vaadin.flow.server.webcomponent.WebComponentBinding;

import elemental.json.JsonObject;

/**
 * Result of defining an embeddable web component using
 * {@link WebComponentExporter}. Provides all the necessary information to
 * generate the web component resources and constructs new
 * {@link WebComponentBinding} instances with
 * {@link #createWebComponentBinding(com.vaadin.flow.di.Instantiator, com.vaadin.flow.dom.Element, elemental.json.JsonObject)};
 *
 * @param <C>
 *            type of the component being exported
 * @author Vaadin Ltd.
 * @since 2.0
 *
 * @see com.vaadin.flow.component.WebComponentExporter.WebComponentConfigurationFactory
 *      for constructing new instances
 */
public interface WebComponentConfiguration<C extends Component>
        extends Serializable {

    /**
     * Check if the configuration has a property identified by the {@code
     * propertyName}.
     *
     * @param propertyName
     *            name of the property, not null
     * @return has property
     */
    boolean hasProperty(String propertyName);

    /**
     * Retrieve the type of a property's value. If the property is not known,
     * returns {@code null}
     *
     * @param propertyName
     *            name of the property, not null
     * @return property type or null
     */
    Class<? extends Serializable> getPropertyType(String propertyName);

    /**
     * Retrieve the type of the component.
     *
     * @return component type
     */
    Class<C> getComponentClass();

    /**
     * Set of all the {@link PropertyData} objects defining the web component's
     * properties.
     *
     * @return set of {@code PropertyData}
     */
    Set<PropertyData<? extends Serializable>> getPropertyDataSet();

    /**
     * Creates a new {@link WebComponentBinding} instance.
     *
     * @param instantiator
     *            {@link com.vaadin.flow.di.Instantiator} used to construct
     *            instances
     * @param element
     *            element which acts as the root element for the exported
     *            {@code component} instance
     * @param newAttributeDefaults
     *            {@link JsonObject} containing default overrides set by the
     *            user defining the component on a web page. These defaults are
     *            set using the web component's attributes.
     * @return web component binding which can be used by the web component host
     *         to communicate with the component it is hosting
     */
    WebComponentBinding<C> createWebComponentBinding(Instantiator instantiator,
            Element element, JsonObject newAttributeDefaults);

    /**
     * Retrieves the tag name configured by the web component exporter.
     *
     * @return tag name, not {@code null}
     */
    String getTag();

    /**
     * Retrieves the type of the {@link WebComponentExporter} from which this
     * configuration has been generated.
     *
     * @return web component exporter class
     */
    Class<? extends WebComponentExporter<C>> getExporterClass();
}
