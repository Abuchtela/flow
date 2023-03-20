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

class ServerModelNullList extends PolymerElement {
  static get is() { return 'server-model-null-list'; }

  static get template() {
    return html`
         This custom element has a corresponding server model with List property that is never changed.
    `;
  }
}

window.customElements.define(ServerModelNullList.is, ServerModelNullList);
