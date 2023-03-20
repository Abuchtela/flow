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

class ChildTemplate extends PolymerElement {
  static get is() { return 'child-template' }

  static get template() {
    return html`
       <div on-click="handleClick">Child Template</div>
    <div id="text">[[text]]</div>
    `;
  }
}
customElements.define(ChildTemplate.is, ChildTemplate);
