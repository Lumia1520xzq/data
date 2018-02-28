Ext.define('WF.view.thirdChannel.channelRechargeMain', {
    extend: 'Ext.panel.Panel',
    title: '充值数据',
    xtype: 'channelRechargeMain' + this.parameters + '',
    closable: true,
    autoScroll: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;

        var store = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/channel/userdata/recharge/list.do',
            fields: ['businessDate', 'dau', 'recharUserCount', 'rechargeCount', 'rechargeAmount', 'rechargeArpu', 'rechargeArppu'],
            baseParams: {
                parentId: me.parameters
            }
        });

        var childChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getChildChannels.do',
            fields: ['id', 'name'],
            baseParams: {
                parentId: me.parameters
            }
        });

        var userTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            fields: ['value', 'lable'],
            data: [
                {"value": 0, "lable": "全部用户"},
                {"value": 1, "lable": "新增用户"}
            ]
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
            forceFit: false,
            items: [{
                xtype: 'hiddenfield',
                name: 'parentId',
                value: me.parameters
            }, {
                name: 'channelId',
                fieldLabel: '子渠道',
                xtype: 'combo',
                columns: 2,
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: childChannelStore
            }, {
                name: 'userType',
                fieldLabel: '用户类型',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'lable',
                valueField: "value",
                editable: true,
                queryMode: "local",
                value: 0,
                store: userTypeStore
            }, {
                xtype: 'datefield',
                name: 'beginDate',
                format: 'Y-m-d',
                fieldLabel: '开始时间',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -6), "Y-m-d")
            }, {
                xtype: 'datefield',
                name: 'endDate',
                format: 'Y-m-d',
                fieldLabel: '结束时间',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY), "Y-m-d")
            }]
        });


        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '日期',
                dataIndex: 'businessDate',
                width: 30,
                menuDisabled: true,
                sortable: false
            }, {
                text: 'DAU',
                width: 30,
                dataIndex: 'dau',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000");
                    } else {
                        return 0;
                    }
                }
            }, {
                text: '充值人数',
                width: 30,
                dataIndex: 'recharUserCount',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000");
                    } else {
                        return 0.00;
                    }
                }
            }, {
                text: '充值笔数',
                width: 30,
                dataIndex: 'rechargeCount',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000");
                    } else {
                        return 0.00;
                    }
                }
            }, {
                text: '充值金额',
                width: 30,
                dataIndex: 'rechargeAmount',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000");
                    } else {
                        return 0.00;
                    }
                }
            },{
                text: 'ARPU',
                width: 30,
                dataIndex: 'rechargeArpu',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000.0");
                    } else {
                        return 0.00;
                    }
                }
            }, {
                text: 'ARPPU',
                width: 30,
                dataIndex: 'rechargeArppu',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    if (value != null) {
                        return Ext.util.Format.number(value, "0,000.0");
                    } else {
                        return 0.00;
                    }
                }
            }]
        });
    }
});