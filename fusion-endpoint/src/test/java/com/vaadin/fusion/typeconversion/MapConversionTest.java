/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.fusion.typeconversion;

import org.junit.Test;

public class MapConversionTest extends BaseTypeConversionTest {

    @Test
    public void should_ConvertToMapOfString_When_ReceiveMapOfString() {
        String inputValue = "{\"key\": \"value\", \"second_key\":\"2\"}";
        String expectedValue = "{\"key\":\"value-foo\",\"second_key\":\"2-foo\"}";
        assertEqualExpectedValueWhenCallingMethod("getFooMapStringString",
                inputValue, expectedValue);
    }

    @Test
    public void should_ConvertToMapOfString_When_ReceiveMapWithNonStringValue() {
        String inputValue = "{\"key\": 1, \"second_key\": 2.0}";
        String expectedValue = "{\"key\":\"1-foo\",\"second_key\":\"2.0-foo\"}";
        assertEqualExpectedValueWhenCallingMethod("getFooMapStringString",
                inputValue, expectedValue);
    }

    @Test
    public void should_ConvertToMapOfInteger_When_ReceiveMapOfInteger() {
        String inputValue = "{\"key\":2,\"second_key\":3}";
        String expectedValue = "{\"key\":3,\"second_key\":4}";
        assertEqualExpectedValueWhenCallingMethod("getAddOneMapStringInteger",
                inputValue, expectedValue);
    }

    @Test
    public void should_ConvertToMapOfInteger_When_ReceiveMapOfNonInteger() {
        String inputValue = "{\"key\":\"2\"}";
        String expectedValue = "{\"key\":3}";
        assertEqualExpectedValueWhenCallingMethod("getAddOneMapStringInteger",
                inputValue, expectedValue);
    }

    @Test
    public void should_ConvertToMapOfInteger_When_ReceiveMapOfDecimal() {
        String inputValue = "{\"key\": 2.0}";
        String expectedValue = "{\"key\":3}";
        assertEqualExpectedValueWhenCallingMethod("getAddOneMapStringInteger",
                inputValue, expectedValue);

        inputValue = "{\"key\":2.9}";
        assertEqualExpectedValueWhenCallingMethod("getAddOneMapStringInteger",
                inputValue, expectedValue);
    }

    @Test
    public void should_ConvertToMapOfDouble_When_ReceiveMapOfDecimal() {
        String inputValue = "{\"key\": 2.0}";
        String expectedValue = "{\"key\":3.0}";
        assertEqualExpectedValueWhenCallingMethod("getAddOneMapStringDouble",
                inputValue, expectedValue);
    }

    @Test
    public void should_FailToConvertToMapOfDouble_When_ReceiveMapOfInteger() {
        String inputValue = "{\"key\": 2}";
        String expectedValue = "{\"key\":3.0}";
        assertEqualExpectedValueWhenCallingMethod("getAddOneMapStringDouble",
                inputValue, expectedValue);
    }

    @Test
    public void should_FailToConvertToMapOfStringEnum_When_ReceiveMapOfStringEnum() {
        String inputValue = "{\"first_key\": \"FIRST\", \"second_key\": \"SECOND\"}";
        String expectedValue = "{\"first_key\":\"SECOND\",\"second_key\":\"THIRD\"}";
        assertEqualExpectedValueWhenCallingMethod("getNextEnumMapStringEnum",
                inputValue, expectedValue);
    }

    @Test
    public void should_ConvertToMapOfEnumInteger_When_ReceiveMapOfEnumInteger() {
        String inputValue = "{\"FIRST\": \"1\"}";
        String expectedValue = "{\"FIRST\":2}";
        assertEqualExpectedValueWhenCallingMethod("getAddOneMapEnumInteger",
                inputValue, expectedValue);
    }
}
