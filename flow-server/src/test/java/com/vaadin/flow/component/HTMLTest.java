/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.flow.dom.Element;

public class HTMLTest {

    @Test
    public void attachedToElement() {
        // This will throw an assertion error if the element is not attached to
        // the component
        new Html("<b>Hello</b>").getParent();
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullHtml() {
        new Html((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullStream() {
        new Html((InputStream) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyHtml() {
        new Html("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void twoRoots() {
        new Html("<b></b><div></div>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void text() {
        new Html("hello");
    }

    @Test
    public void simpleHtml() {
        Html html = new Html("<span>hello</span>");
        Assert.assertEquals(Tag.SPAN, html.getElement().getTag());
        Assert.assertEquals("hello", html.getInnerHtml());
    }

    @Test
    public void rootAttributes() {
        Html html = new Html("<span foo='bar'>hello</span>");
        Assert.assertEquals(Tag.SPAN, html.getElement().getTag());
        Assert.assertEquals(1, html.getElement().getAttributeNames().count());
        Assert.assertEquals("bar", html.getElement().getAttribute("foo"));
        Assert.assertEquals("hello", html.getInnerHtml());
    }

    @Test
    public void rootSpecialAttributes() {
        Html html = new Html(
                "<span class='foo' style='color: red'>hello</span>");
        Element element = html.getElement();
        Assert.assertEquals(Tag.SPAN, element.getTag());

        Assert.assertEquals(2, element.getAttributeNames().count());
        Assert.assertEquals("foo", element.getAttribute("class"));
        Assert.assertEquals("color:red", element.getAttribute("style"));
        Assert.assertEquals("hello", html.getInnerHtml());
    }

    @Test
    public void fromStream() {
        new Html(new ByteArrayInputStream(
                "<div><span>contents</span></div>".getBytes()));
    }

    @Test
    public void brokenHtml() {
        Html html = new Html("<b></div>");
        Assert.assertEquals("b", html.getElement().getTag());
        Assert.assertEquals("", html.getInnerHtml());
    }

    @Test
    public void extraWhitespace() {
        String input = "   <span>    " //
                + "    <div>" //
                + "       <b>Hello!</b>" //
                + "    </div>" //
                + "</span>" + "  " //
                + "" //
                + "";
        Html html = new Html(input);
        Assert.assertEquals(Tag.SPAN, html.getElement().getTag());
        String expectedInnerHtml = input.replaceAll("^[ ]*<span>", "")
                .replaceAll("</span>[ ]*$", "");
        Assert.assertEquals(expectedInnerHtml, html.getInnerHtml());
    }

    @Test
    public void emptyAttribute_elementIsCreatedAndHasAttribute() {
        Html html = new Html("<audio controls></audio>");

        Assert.assertEquals("", html.getElement().getAttribute("controls"));

        Assert.assertEquals("<audio controls></audio>",
                html.getElement().getOuterHTML());
    }

    @Test
    public void styleElementAsString_elementIsUsed() {
        Html html = new Html("<style></style>");
        Assert.assertEquals("style", html.getElement().getTag());
    }

    @Test

    public void styleElementAsStream_elementIsUsed() {
        Html html = new Html(new ByteArrayInputStream(
                "<style></style>".getBytes(StandardCharsets.UTF_8)));
        Assert.assertEquals("style", html.getElement().getTag());
    }

}
