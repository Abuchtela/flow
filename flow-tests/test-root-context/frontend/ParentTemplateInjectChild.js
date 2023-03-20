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

class ParentInjectChild extends PolymerElement {
  static get is() {
    return 'parent-inject-child'
  }

  static get template() {
    return html`
       <injected-child id='child'></injected-child>
       <button on-click="updateChild" id="button">Update Child</button>
    `;
  }
}
customElements.define(ParentInjectChild.is, ParentInjectChild);
