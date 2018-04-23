Ext.define('WF.store.userIdStore', {
    extend: 'Ext.window.Window',
    alias: 'userIdStore',
    width: 466,
    height: 400,
    modal: true,
    resizable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;

        var userIdStore = new Ext.data.SimpleStore({
            autoLoad: true,
            fields: ["userId"],
            data: [[0],[1],[2],[3],[4],[5],[6],[7],[8],[9]],
            listeners: {
                load: function(userIdStore, records, successful) {
                    if (successful) {
                        for (var i = 0; i < records.length; i++) {
                            if (records[i].data.checked) {
                                model.select(records[i], true, false);
                            }
                        }
                    }
                }
            }
        });
        me.add({
            xtype: 'grid',
            store: userIdStore,
            buildField: "Manual",
            forceFit: true,
            selType: 'checkboxmodel',
            selModel: {
                mode: 'MULTI',
                checkOnly: true
            },
            columns: [{
                text: '用户ID',
                dataIndex: 'userId',
                width: 100,
                menuDisabled: true,
                sortable : false
            }]
        });
    }
    ,
    buttons: [{
        text: '保存信息',
        iconCls: "icon-ok",
        handler: function () {
            var currentWindow = this.up('window');
            var records = currentWindow.down('grid').getSelectionModel().getSelection();
            console.info(records.length);
            var ids = [];
            for (var i = 0; i < records.length; i++) {
                ids.push(records[i].data.userId);
            }
            console.info(  Ext.ComponentQuery.query('#addGrowTestWindow'))
            var currentParentWindow = Ext.ComponentQuery.query('#addGrowTestWindow')[0];
            currentParentWindow.down("[name='userIds']").setValue(ids);
            currentWindow.close();

        }
    }]
});