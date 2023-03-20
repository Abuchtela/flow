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

class ConvertToBean extends PolymerElement {
  static get is() {
    return 'convert-to-bean'
  }

  static get template() {
    return html`
       <style>
      label {
        display: block;
      }
   </style>
   <div>[[bean.day]]</div>
    <div style="width: 200px;">
        <label for="day">Enter your birthday:</label><input id="day" value="{{date.day::input}}"></input>
        <label for="month">Enter the month of your birthday:</label><input id="month" value="{{date.month::input}}"></input>
        <label for="year">Enter the year of your birthday:</label><input id="year" value="{{date.year::input}}"></input>
        <button on-click="submit" id="click">Submit</button>
        <label id="msg">[[message]]</label>
    </div>
    `;
  }
}
customElements.define(ConvertToBean.is, ConvertToBean);
