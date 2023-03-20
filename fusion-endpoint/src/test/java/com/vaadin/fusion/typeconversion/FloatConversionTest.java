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

public class FloatConversionTest extends BaseTypeConversionTest {
    @Test
    public void should_ConvertToFloat_When_ReceiveANumber() {
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "1", "2.0");
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "-1", "0.0");
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "0", "1.0");

        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "1",
                "2.0");
        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "-1",
                "0.0");
        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "0",
                "1.0");
    }

    @Test
    public void should_ConvertToFloat_When_ReceiveANumberAsString() {
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "\"1\"",
                "2.0");
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "\"-1\"",
                "0.0");
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "\"0\"",
                "1.0");

        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "\"1\"",
                "2.0");
        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "\"-1\"",
                "0.0");
        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "\"0\"",
                "1.0");
    }

    @Test
    public void should_ConvertToFloat_When_ReceiveDecimalAsNumber() {
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "1.1", "2.1");
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "1.9", "2.9");
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "-1.9",
                "-0.9");

        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "1.1",
                "2.1");
        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "1.9",
                "2.9");
        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "-1.9",
                "-0.9");
    }

    @Test
    public void should_ConvertToFloat_When_ReceiveDecimalAsString() {
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "\"1.1\"",
                "2.1");
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "\"1.9\"",
                "2.9");
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "\"-1.9\"",
                "-0.9");

        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "\"1.1\"",
                "2.1");
        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "\"1.9\"",
                "2.9");
        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed",
                "\"-1.9\"", "-0.9");
    }

    @Test
    public void should_HandleOverflowFloat_When_ReceiveANumberOverflowOrUnderflow() {
        String overflowFloat = "3.4028236E38";
        String overflowFloatString = "\"3.4028236E38\"";
        String underflowFloat = "-3.4028235E39";
        String underflowFloatString = "\"-3.4028235E39\"";
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", overflowFloat,
                "\"" + Float.POSITIVE_INFINITY + "\"");
        assertEqualExpectedValueWhenCallingMethod("addOneFloat",
                overflowFloatString, "\"" + Float.POSITIVE_INFINITY + "\"");

        assertEqualExpectedValueWhenCallingMethod("addOneFloat", underflowFloat,
                "\"" + Float.NEGATIVE_INFINITY + "\"");
        assertEqualExpectedValueWhenCallingMethod("addOneFloat",
                underflowFloatString, "\"" + Float.NEGATIVE_INFINITY + "\"");

        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed",
                overflowFloat, "\"" + Float.POSITIVE_INFINITY + "\"");
        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed",
                overflowFloatString, "\"" + Float.POSITIVE_INFINITY + "\"");

        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed",
                underflowFloat, "\"" + Float.NEGATIVE_INFINITY + "\"");
        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed",
                underflowFloatString, "\"" + Float.NEGATIVE_INFINITY + "\"");
    }

    @Test
    public void should_HandleSpecialCaseForFloat_When_ReceiveNull() {
        assertEqualExpectedValueWhenCallingMethod("addOneFloat", "null", "1.0");

        assertEqualExpectedValueWhenCallingMethod("addOneFloatBoxed", "null",
                "null");
    }

    @Test
    public void should_HandleSpecialCaseForFloat_When_ReceiveSpecialInput() {
        assert400ResponseWhenCallingMethod("addOneFloat", "NaN");
        assert400ResponseWhenCallingMethod("addOneFloat", "Infinity");
        assert400ResponseWhenCallingMethod("addOneFloat", "-Infinity");

        assert400ResponseWhenCallingMethod("addOneFloatBoxed", "NaN");
        assert400ResponseWhenCallingMethod("addOneFloatBoxed", "Infinity");
        assert400ResponseWhenCallingMethod("addOneFloatBoxed", "-Infinity");
    }
}
