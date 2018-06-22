Ext.define('WF.view.game.landlords.landlordsAnalysisMain', {
    extend: 'Ext.panel.Panel',
    title: '斗地主-场次分析',
    xtype: 'landlordsAnalysisMain',
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
            url: 'data/admin/landlords/getList.do',
            fields: ['searchDate', 'deskTypeName', 'dauCount', 'userCount',
                'conversionRate', 'bettingAmount', 'resultAmount', 'tableAmount',
                'returnRate', 'bettingArpu', 'amountDiffArpu', 'gameTimes', 'avgGameTimes', 'bettingAsp']
        });

        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });

        var deskTypeStore = Ext.create('DCIS.Store', {
            fields: ['id', 'name'],
            data: [
                {name: '全部', id: 0},
                {name: '新手场', id: 1},
                {name: '初级场', id: 2},
                {name: '中级场', id: 3},
                {name: '高级场', id: 4}
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
            forceFit: true,
            items: [{
                name: 'parentId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: parentChannelStore
            }, {
                name: 'deskType',
                fieldLabel: '场次',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                multiSelect: true,
                editable: true,
                queryMode: "local",
                store: deskTypeStore,
                value: 0
            }, {
                name: 'startTime',
                fieldLabel: '开始时间',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'endTime',
                fieldLabel: '结束时间',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            showPaging: false,
            columns: [
                {
                    text: '日期',
                    width: 100,
                    dataIndex: 'searchDate',
                    menuDisabled: true,
                    sortable: false,
                    // locked: true,
                },
                {
                    text: '场次',
                    width: 100,
                    dataIndex: 'deskTypeName',
                    menuDisabled: true,
                    sortable: false,
                    // locked: true,
                    renderer: function (value) {
                        if (value != null) {
                            return value;
                        } else {
                            return "全部";
                        }
                    }
                },
                {
                    text: 'DAU',
                    dataIndex: 'dauCount',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000");
                        } else {
                            return 0.00;
                        }
                    }
                },
                {
                    text: '投注人数',
                    dataIndex: 'userCount',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000");
                        } else {
                            return 0.00;
                        }
                    }
                },
                {
                    text: '投注转化率',
                    dataIndex: 'conversionRate',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return value;
                        } else {
                            return 0.00 + "%";
                        }
                    }
                },
                {
                    text: '投注流水',
                    dataIndex: 'bettingAmount',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000");
                        } else {
                            return 0.00;
                        }
                    }
                },
                {
                    text: '返奖流水',
                    dataIndex: 'resultAmount',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000");
                        } else {
                            return 0.00;
                        }
                    }
                },
                {
                    text: '桌费',
                    dataIndex: 'tableAmount',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000");
                        } else {
                            return 0.00;
                        }
                    }
                },
                {
                    text: '返奖率',
                    dataIndex: 'returnRate',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return value;
                        } else {
                            return 0.00 + "%";
                        }
                    }
                },
                {
                    text: '投注ARPU',
                    dataIndex: 'bettingArpu',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000.00");
                        } else {
                            return 0.00;
                        }
                    }
                },
                {
                    text: '流水差ARPU',
                    dataIndex: 'amountDiffArpu',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000.00");
                        } else {
                            return 0.00;
                        }
                    }
                },
                {
                    text: '局数',
                    dataIndex: 'gameTimes',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000");
                        } else {
                            return 0;
                        }
                    }
                },
                {
                    text: '人均局数',
                    dataIndex: 'avgGameTimes',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000");
                        } else {
                            return 0;
                        }
                    }
                },
                {
                    text: '投注ASP',
                    dataIndex: 'bettingAsp',
                    menuDisabled: true,
                    sortable: false,
                    flex: 1,
                    renderer: function (value) {
                        if (value != null) {
                            return Ext.util.Format.number(value, "0,000.00");
                        } else {
                            return 0.00;
                        }
                    }
                }

            ]
        });
    }
});