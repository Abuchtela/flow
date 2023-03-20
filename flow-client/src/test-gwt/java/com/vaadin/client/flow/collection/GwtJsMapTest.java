/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.client.flow.collection;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.client.ClientEngineTestBase;
import com.vaadin.client.WidgetUtil;

public class GwtJsMapTest extends ClientEngineTestBase {

    public void testMap() {
        JsMap<String, Integer> map = JsCollections.map();

        assertEquals(0, map.size());

        map.set("One", 1).set("Two", 2);

        assertEquals(2, map.size());

        assertTrue(map.has("One"));
        assertTrue(map.has("Two"));
        assertFalse(map.has("Three"));

        assertEquals(1, (int) map.get("One"));
        assertEquals(2, (int) map.get("Two"));
        assertNull(map.get("Threee"));

        assertTrue(map.delete("One"));
        assertFalse(map.delete("Three"));
        assertFalse(map.has("One"));

        map.clear();
        assertEquals(0, map.size());
        assertFalse(map.has("Two"));
    }

    public void testMapForEach() {
        Map<String, Integer> seenValues = new HashMap<>();

        JsMap<String, Integer> map = JsCollections.map();

        map.set("One", 1).set("Two", 2);

        map.forEach((value, key) -> seenValues.put(key, value));

        Map<String, Integer> expectedValues = new HashMap<>();
        expectedValues.put("One", 1);
        expectedValues.put("Two", 2);

        assertEquals(expectedValues, seenValues);
    }

    public void testMapValues() {
        JsMap<String, Integer> map = JsCollections.map();

        map.set("One", 1).set("Two", 2);

        JsArray<Integer> values = map.mapValues();

        assertEquals(2, values.length());

        assertEquals(1, values.get(0).intValue());
        assertEquals(2, values.get(1).intValue());

        map.delete("One");

        values = map.mapValues();
        assertEquals(1, values.length());
        assertEquals(2, values.get(0).intValue());

        map.clear();
        values = map.mapValues();
        assertEquals(0, values.length());
    }

    /**
     * Tests that it's possible to cast an instance to its own type.
     *
     * Most of the JS produced by GWT does not make any assertions about types,
     * but explicit casts and some use of generics leads to code that might do a
     * JavaScript instanceof check for @JsType classes, thus failing if the type
     * defined in the annotation doesn't match the runtime type.
     */
    public void testCanCast() {
        // Ok if this doesn't throw ClassCastException
        JsMap<Object, Object> map = WidgetUtil.crazyJsCast(JsCollections.map());
        assertNotNull(map);
    }

}
