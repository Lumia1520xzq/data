Ext.define('WF.view.sys.config.sysConfigMain', {
    extend: 'Ext.panel.Panel',
    title: '配置项管理',
    xtype: 'sysConfigMain',
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
            url: 'data/admin/sysconfig/list.do',
            fields: ['id', 'name', 'value', 'remark', 'channelId']
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
                name: 'name',
                fieldLabel: '配置项名称'
            }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '名称',
                dataIndex: 'name',
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
                text: '备注',
                width: 120,
                dataIndex: 'remark',
                menuDisabled: true,
                sortable: false
            }, {
                text: '渠道',
                dataIndex: 'channelId',
                width: 80,
                menuDisabled: true,
                sortable: false
            }
            ]
        });
    }
});