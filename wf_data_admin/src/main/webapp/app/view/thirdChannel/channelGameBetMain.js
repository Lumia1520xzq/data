Ext.define('WF.view.thirdChannel.channelGameBetMain', {
    extend: 'Ext.panel.Panel',
    title: '渠道游戏投注数据',
    xtype: 'channelGameBetMain' + this.parameters + '',
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
            url: 'data/admin/report/getList.do',
            fields: [
                "searchDate", "dau", "importRate", "bettingUserCount",
                "bettingCount", "bettingAmount", "amountGap",
                "bettingRate", "returnRate", "arpu", "asp", "avgBettingCount",
                "newUserCount", "newUserRate", "newUserRemain", "gameType"
            ],
            baseParams: {
                parentId: me.parameters
            }
        });

        var gameTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'game_type'
            }
        });

        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
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
                name: 'gameType',
                fieldLabel: '游戏',
                xtype: 'combobox',
                store: gameTypeStore,
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false
            }, {
                name: 'startTime',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'endTime',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [
                {
                    text: '游戏',
                    width: 50,
                    dataIndex: 'gameType',
                    menuDisabled: true,
                    sortable: false,
                    renderer: function (value) {
                        var record = gameTypeStore.findRecord('value', value, 0, false, false, true);
                        if (record == null) {
                            return '全部';
                        } else {
                            return record.data.label;
                        }
                    }
                },
                {
                    text: '日期',
                    dataIndex: 'searchDate',
                    width: 50,
                    menuDisabled: true,
                    sortable: false
                }, {
                    text: 'DAU',
                    width: 50,
                    dataIndex: 'dau',
                    menuDisabled: true,
                    sortable: false
                }, {
                    text: '导入率',
                    width: 50,
                    dataIndex: 'importRate',
                    menuDisabled: true,
                    sortable: false
                }, {
                    text: '投注人数',
                    width: 50,
                    dataIndex: 'bettingUserCount',
                    menuDisabled: true,
                    sortable: false
                }, {
                    text: '投注笔数',
                    width: 50,
                    dataIndex: 'bettingCount',
                    menuDisabled: true,
                    sortable: false
                }, {
                    text: '投注流水',
                    width: 50,
                    dataIndex: 'bettingAmount',
                    menuDisabled: true,
                    sortable: false
                }, {
                    text: '流水差',
                    width: 50,
                    dataIndex: 'amountGap',
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: '投注转化率',
                    width: 50,
                    dataIndex: 'bettingRate',
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: '返奖率',
                    width: 50,
                    dataIndex: 'returnRate',
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: '投注ARPU',
                    width: 50,
                    dataIndex: 'arpu',
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: '投注ASP',
                    width: 50,
                    dataIndex: 'asp',
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: '人均频次',
                    width: 50,
                    dataIndex: 'avgBettingCount',
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: '新增用户',
                    width: 50,
                    dataIndex: 'newUserCount',
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: '新增投注转化率',
                    width: 50,
                    dataIndex: 'newUserRate',
                    menuDisabled: true,
                    sortable: false
                },
                {
                    text: '新增次留',
                    width: 50,
                    dataIndex: 'newUserRemain',
                    menuDisabled: true,
                    sortable: false
                }
            ]
        });

    }
});