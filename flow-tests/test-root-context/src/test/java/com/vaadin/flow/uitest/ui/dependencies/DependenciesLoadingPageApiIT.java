/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui.dependencies;

import org.junit.Ignore;

/**
 * See {@link DependenciesLoadingAnnotationsIT} for more details about the test.
 * Test runs and performs the same checks as
 * {@link DependenciesLoadingAnnotationsIT}, but this test opens a different
 * page to test, that's why the class exists and needed.
 *
 * @author Vaadin Ltd
 * @since 1.0.
 * @see DependenciesLoadingAnnotationsIT
 */
@Ignore("Doesn't work ccdm, see https://github.com/vaadin/flow/issues/7328")
public class DependenciesLoadingPageApiIT
        extends DependenciesLoadingAnnotationsIT {

    @Override
    protected String getCssSuffix() {
        return "WebRes";
    }
}
