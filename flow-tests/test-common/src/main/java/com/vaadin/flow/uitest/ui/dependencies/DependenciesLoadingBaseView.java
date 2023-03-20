/**
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
package com.vaadin.flow.uitest.ui.dependencies;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.uitest.ui.AbstractDivView;

/**
 * This class provides test base for IT test that check dependencies being
 * loaded correctly.
 *
 * @author Vaadin Ltd
 * @since 1.0.
 */
public class DependenciesLoadingBaseView extends AbstractDivView {
    static final String PRELOADED_DIV_ID = "preloadedDiv";
    static final String INLINE_CSS_TEST_DIV_ID = "inlineCssTestDiv";
    static final String DOM_CHANGE_TEXT = "I appear after inline and eager dependencies and before lazy";

    protected DependenciesLoadingBaseView(String cssSuffix) {
        add(createDiv(PRELOADED_DIV_ID + cssSuffix, "Preloaded div"),
                createDiv(INLINE_CSS_TEST_DIV_ID + cssSuffix,
                        "A div for testing inline css"));
    }

    private Div createDiv(String id, String text) {
        Div div = new Div();
        div.setId(id);
        div.setText(text);
        return div;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // See eager.js for attachTestDiv code
        getPage().executeJs("window.attachTestDiv($0)", DOM_CHANGE_TEXT);
    }

}
