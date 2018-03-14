Ext.define('WF.view.business.userProfitMain', {
    extend: 'Ext.panel.Panel',
    title: '用户净盈利信息',
    xtype: 'userProfitMain',
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
            url: 'data/admin/business/profit/list.do',
            fields: [
                'userName', 'userId', 'profitAmount',
                'totalRechargeAmount', 'totalWarsProfit', 'costAmount',
                'totalWarsBetting', 'noUseGoldAmount'
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
            items: [
                {
                    colspan: 1,
                    name: 'userId',
                    xtype: 'searchfield',
                    displayField: 'nickname',
                    valueField: "id",
                    queryMode: "local",
                    store: 'userStore',
                    editable: false,
                    fieldLabel: '用户'
                }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [
                {
                    text: '用户昵称',
                    width: 50,
                    dataIndex: 'userName',
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: '游戏用户ID',
                    dataIndex: 'userId',
                    width: 50,
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: 'P',//盈利
                    width: 50,
                    dataIndex: 'profitAmount',
                    menuDisabled: true,
                    sortable: false,
                    renderer: function (value) {
                        if (value == null) {
                            return '0.00';
                        } else {
                            return Ext.util.Format.number(value, "0,000.00");
                        }
                    }
                },
                {
                    text: 'B1',//充值
                    width: 50,
                    dataIndex: 'totalRechargeAmount',
                    menuDisabled: true,
                    sortable: false,
                    renderer: function (value) {
                        if (value == null) {
                            return '0.00';
                        } else {
                            return Ext.util.Format.number(value, "0,000.00");
                        }
                    }
                },
                {
                    text: 'B2',//向其它用户获取金叶子
                    width: 50,
                    dataIndex: 'totalWarsProfit',
                    menuDisabled: true,
                    sortable: false,
                    renderer: function (value) {
                        if (value == null) {
                            return '0.00';
                        } else {
                            return Ext.util.Format.number(value, "0,000.00");
                        }
                    }
                },
                {
                    text: 'S1',//实物出口
                    width: 50,
                    dataIndex: 'costAmount',
                    menuDisabled: true,
                    sortable: false,
                    renderer: function (value) {
                        if (value == null) {
                            return '0.00';
                        } else {
                            return Ext.util.Format.number(value, "0,000.00");
                        }
                    }
                },
                {
                    text: 'S2',//向其它用户输出金叶子
                    width: 50,
                    dataIndex: 'totalWarsBetting',
                    menuDisabled: true,
                    sortable: false,
                    renderer: function (value) {
                        if (value == null) {
                            return '0.00';
                        } else {
                            return Ext.util.Format.number(value, "0,000.00");
                        }
                    }
                },
                {
                    text: 'S3',//剩余金叶子
                    width: 30,
                    dataIndex: 'noUseGoldAmount',
                    menuDisabled: true,
                    sortable: false,
                    renderer: function (value) {
                        if (value == null) {
                            return '0.00';
                        } else {
                            return Ext.util.Format.number(value/1000, "0,000.00");
                        }
                    }
                }
            ]
        });
    }
});