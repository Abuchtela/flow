/*
 * Copyright (C) 2000-2023 Vaadin Ltd
 *
 * This program is available under Vaadin Commercial License and Service Terms.
 *
 * See <https://vaadin.com/commercial-license-and-service-terms> for the full
 * license.
 */
import {PolymerElement} from '@polymer/polymer/polymer-element.js';
import {html} from '@polymer/polymer/lib/utils/html-tag.js';

class TemplateScalabilityView extends PolymerElement {
  static get is() {
    return 'template-scalability-view';
  }

  static get template() {
    return html`
                  <style include="shared-styles">
            :host {
                display: block;
            }
        </style>

        <div id="content">

        </div>
    `;
  }
}

customElements.define(TemplateScalabilityView.is, TemplateScalabilityView);
