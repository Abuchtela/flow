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


class ExistingElement extends PolymerElement {
  static get template() {
    return html`
       <div>
            <input id="input" on-change="valueChange">
            <div>
                <label id="label"></label>
            </div>
        </div>
        <button on-click="clear" id="button">Clear</button>
    `;
  }
  static get is() {
    return 'existing-element'
  }
    
}
  
customElements.define(ExistingElement.is, ExistingElement);
