/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui.theme;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;

@Route("com.vaadin.flow.uitest.ui.theme.ThemeView")
public class ThemeView extends Div {

    public static final String MY_COMPONENT_ID = "field";
    public static final String TEST_TEXT_ID = "test-text";
    public static final String SNOWFLAKE_ID = "fortawesome";
    public static final String BUTTERFLY_ID = "butterfly";
    public static final String OCTOPUSS_ID = "octopuss";
    public static final String FONTAWESOME_ID = "font-awesome";
    public static final String SUB_COMPONENT_ID = "sub-component";
    public static final String DICE_ID = "dice";
    public static final String CSS_SNOWFLAKE = "icon-snowflake";

    public ThemeView() {
        final Span textSpan = new Span("This is the theme test view");
        textSpan.setId(TEST_TEXT_ID);

        Span subCss = new Span();
        subCss.setId(SUB_COMPONENT_ID);

        Span butterfly = new Span();
        butterfly.setId(BUTTERFLY_ID);

        Span octopuss = new Span();
        octopuss.setId(OCTOPUSS_ID);

        Span cssSnowflake = new Span();
        cssSnowflake.setId(CSS_SNOWFLAKE);

        Span faText = new Span("This test is FontAwesome.");
        faText.setClassName("fas fa-coffee");
        faText.setId(FONTAWESOME_ID);

        Image snowFlake = new Image(
                "themes/app-theme/fortawesome/icons/snowflake.svg",
                "snowflake");
        snowFlake.setHeight("1em");
        snowFlake.setId(SNOWFLAKE_ID);

        Span diceImageSpan = new Span();
        diceImageSpan.getStyle().set("background-image",
                "url('themes/app-theme/img/dice.jpg')");
        diceImageSpan.setId(DICE_ID);

        add(textSpan, snowFlake, subCss, butterfly, octopuss, cssSnowflake,
                faText, diceImageSpan);

        add(new Div());
        add(new MyComponent().withId(MY_COMPONENT_ID));
    }
}
