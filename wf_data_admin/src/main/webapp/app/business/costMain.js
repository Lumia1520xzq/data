Ext.define('WF.business.costMain', {
    extend: 'Ext.panel.Panel',
    title: '用户成本查询',
    xtype: 'costMain',
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
            url: 'data/business/cost/list.do',
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
            },{
                name: 'channelId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: false,
                queryMode: "local",
                store: 'childChannelStore',
                listeners : {
                    select : function(combo, records, objs) {
                        store.load({
                            params:{
                                xxx : 'xxx',
                                xxx :1
                            }
                        });
                    }

                }
            }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            showPaging:false,
            flex: 1,
            forceFit: true,
            buildField: "Manual",
            height: 600,
            autoScroll: true,
            tbar: [{
                text: '刷新',
                iconCls: "icon-reload",
                handler: function () {
                    store.reload();
                }
            }],
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