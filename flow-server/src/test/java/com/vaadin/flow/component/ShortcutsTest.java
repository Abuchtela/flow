/*
 * Copyright 2000-2019 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.flow.component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

public class ShortcutsTest {

    @Test
    public void hasOnlyStaticMethods() {
        Method[] methods = Shortcuts.class.getDeclaredMethods();

        for (Method method : methods) {
            if (!Modifier.isStatic(method.getModifiers())) {
                Assert.fail(String.format("Method %s(%s) should be static",
                        method.getName(),
                        Stream.of(method.getParameterTypes())
                                .map(Class::getSimpleName)
                                .collect(Collectors.joining(", "))
                ));
            }
        }
    }
}
