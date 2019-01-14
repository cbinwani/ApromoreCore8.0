zk.$package('org.apromore');

org.apromore.CustomWidget = zk.$extends(zk.Widget, {

    _value: 'widget uninitialized',

    getValue: function() {
        return this._value;
    },

    setValue: function(newValue) {
        if (this._value != newValue) {
            this._value = newValue;
        }
    },

    redraw: function(out) {
        globalCustomWidget = this;
        out.push('<div', this.domAttrs_(), '></div>');
        out.push('<script>',
                     "var _viewer = new BpmnJS({ container: '#", this.uuid, "' }); ",
                     "_viewer.importXML(globalCustomWidget.getValue(), function(err) { ",
                         "if (err) { ",
                             "alert('Unable to redraw BPMN content'); ",
                         "} else { ",
                             "var canvas = _viewer.get('canvas'); ",
                             "canvas.zoom('fit-viewport'); ",
                         "} ",
                     "}); ",
                 '</script>');
    }
});
