/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.component.page;

import java.io.Serializable;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.AppShellSettings;

/**
 * An interface to configure application features and the host page where the
 * Vaadin application is running. It automatically configures the index.html
 * page. Configuration can be done using a class implementing this interface
 * with following annotations that affect the generated index.html page (also
 * known as 'application shell'):
 *
 * <ul>
 * <li>{@link Meta}: appends an HTML {@code <meta>} tag to the bottom of the
 * {@code <head>} element</li>
 * <li>{@link Inline}: inlines a static content in any point of the
 * document</li>
 * <li>{@link Viewport}: defines the viewport tag of the page</li>
 * <li>{@link BodySize}: configures the size of the body</li>
 * <li>{@link PageTitle}: establishes the page title</li>
 * <li>{@link Push}: configures automatic server push</li>
 * <li>{@link PWA}: defines application PWA properties</li>
 * </ul>
 *
 * <p>
 * There is a single application shell for the entire Vaadin application, and
 * there can only be one class implementing {@link AppShellConfigurator} per
 * Application.
 * </p>
 *
 * <p>
 * NOTE: the application shell class is the only valid target for the page
 * configuration annotations listed above. The application would fail to start
 * if any of these annotations is wrongly placed on a class other than the
 * application shell class.
 * <p>
 *
 * <code>
 * &#64;Meta(name = "Author", content = "Donald Duck")
 * &#64;PWA(name = "My Fun Application", shortName = "fun-app")
 * &#64;Inline("my-custom-javascript.js")
 * &#64;Viewport("width=device-width, initial-scale=1")
 * &#64;BodySize(height = "100vh", width = "100vw")
 * &#64;PageTitle("my-title")
 * &#67;Push(value = PushMode.AUTOMATIC, transport = Transport.WEBSOCKET_XHR)
 * public class AppShell implements AppShellConfigurator {
 * }
 * </code>
 *
 * @since 3.0
 */
public interface AppShellConfigurator extends Serializable {

    /**
     * Configure the initial application shell settings when called.
     *
     * @param settings
     *            initial application shell settings
     */
    default void configurePage(AppShellSettings settings) {
    }
}
