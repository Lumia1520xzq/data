Ext.define('WF.sys.sysconfigMain', {
    extend: 'Ext.panel.Panel',
    title: '配置项管理',
    xtype: 'sysconfigMain',
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
            tbar: [{
                text: '新增',
                iconCls: "icon-add",
                handler: function () {
                    var doRefresh = me.down('datagrid').store;
                    Ext.create('WF.sys.addSysconfig', {doRefresh: doRefresh}).show();
                }
            }],
            columns: [{
                menuDisabled: true,
                sortable: false,
                xtype: 'linkColumn',
                header: '操作',
                width: 60,
                callback: function (link, record) {
                    return link;
                },
                links: [{
                    icon: 'edit',
                    linkText: '编辑',
                    handler: function (grid, rowIndex, colIndex, record) {
                        var doRefresh = me.down('datagrid').store;
                        var win = Ext.create("WF.sys.editSysconfig", {
                            doRefresh: doRefresh
                        });
                        win.down('dataform').setValues(record.data);
                        win.show();
                    }
                }]
            }, {
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