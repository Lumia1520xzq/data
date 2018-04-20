Ext.define('WF.store.editTestChannelStore', {
    extend: 'Ext.window.Window',
    alias: 'editTestChannelStore',
    width: 466,
    height: 400,
    modal: true,
    resizable: true,
    autoScroll:true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var testChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getChildChannels.do',
            fields: ['id','name'],
            listeners: {
                load: function(testChannelStore, records, successful) {
                    if (successful) {
                        var model = me.down('grid').getSelectionModel();
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
            border: false,
            store: testChannelStore,
            xtype: 'searchpanel',
            title: '查询',
            collapsible: true,
            collapsed: false,
            columns: 3,
            buildField: "Manual",
            forceFit: true,
            items: [{
                name: 'name',
                fieldLabel: '测试渠道',
            }]
        });
        me.add({
            xtype: 'grid',
            store: testChannelStore,
            buildField: "Manual",
            forceFit: true,
            selType: 'checkboxmodel',
            selModel: {
                mode: 'MULTI',
                checkOnly: true
            },

            columns: [{
                text: '渠道ID',
                dataIndex: 'id',
                width: 100,
                menuDisabled: true,
                sortable : false
            },{
                text: '渠道名称',
                dataIndex: 'name',
                width: 200,
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
            var ids = [];
            for (var i = 0; i < records.length; i++) {
                ids.push(records[i].data.name);
            }
            console.info(  Ext.ComponentQuery.query('#editGrowTestWindow'))
            var currentParentWindow = Ext.ComponentQuery.query('#editGrowTestWindow')[0];
            currentParentWindow.down("[name='testChannel']").setValue(ids);
            currentWindow.close();

        }
    }]
});