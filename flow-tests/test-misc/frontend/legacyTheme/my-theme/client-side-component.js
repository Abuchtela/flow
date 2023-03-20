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

class ClientSideComponent extends PolymerElement {
  static get template() {
    return html`
       <div id="themed">Themed Client Side Component</div>
`;
  }
  
  static get is() {
      return 'client-side-component'
  }
  
}
  
customElements.define(ClientSideComponent.is, ClientSideComponent);
