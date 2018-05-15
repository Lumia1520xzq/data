Ext.define('WF.view.sys.bigdata.sysBigDataDictMain', {
    extend: 'Ext.panel.Panel',
    title: '字典管理',
    xtype: 'sysBigDataDictMain',
    closable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var store = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/bigdata/dict/list.do',
            fields: ['id', 'label', 'value', 'type', 'description', 'sort']
        });
        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '查询',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            items: [{
                name: 'type',
                fieldLabel: '字典Key'
            }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '展示值',
                dataIndex: 'label',
                width: 80,
                menuDisabled: true,
                sortable: false
            }, {
                text: '值',
                width: 100,
                dataIndex: 'value',
                menuDisabled: true,
                sortable: false
            }, {
                text: 'Key',
                width: 120,
                dataIndex: 'type',
                menuDisabled: true,
                sortable: false
            }, {
                text: '描述',
                dataIndex: 'description',
                width: 80,
                menuDisabled: true,
                sortable: false
            }, {
                text: '排序',
                dataIndex: 'sort',
                width: 80,
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});