Ext.define('WF.userManual.userManual', {
    extend: 'Ext.window.Window',
    alias: 'userManual',
    title: '用户手册',
    modal: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        this.add(Ext.create("WF.userManual.manualCenter"));
    }
});