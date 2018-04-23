Ext.define('WF.view.data.growTest.viewResult', {
    extend: 'Ext.window.Window',
    alias: 'viewResult',
    title: '查看结果',
    modal: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        me.add({
            xtype: 'dataform',
            baseCls: 'x-plain',
            border: true,
            columns: 2,

            items: [{
                name: 'result',
                xtype: 'textarea',
                width: 800,
                height: 400,
                afterLabelTextTpl: required,
                allowBlank: false,
                colspan: 2,
                fieldLabel: '结果'
            }]
        });
    }
});