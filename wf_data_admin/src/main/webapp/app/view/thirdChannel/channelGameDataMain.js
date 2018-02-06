Ext.define('WF.view.thirdChannel.channelGameDataMain', {
    extend: 'Ext.panel.Panel',
    title: '游戏数据统计',
    xtype: 'channelGameDataMain' + this.parameters + '',
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
            url: 'data/admin/channel/gamedata/list.do',
            fields: [
                "gameType",
                "searchDate", "dau", "bettingUserCount",
                "bettingCount", "bettingAmount", "amountGap",
                "bettingRate", "returnRate", "arpu", "asp"
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

        var childChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getChildChannels.do',
            fields: ['id', 'name'],
            baseParams: {
                parentId: me.parameters
            }
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
                xtype: 'hiddenfield',
                name: 'parentId',
                value: me.parameters
            }, {
                name: 'parentId',
                fieldLabel: '渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: childChannelStore
            }, {
                name: 'gameType',
                fieldLabel: '游戏',
                xtype: 'combobox',
                store: gameTypeStore,
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false
            }, {
                name: 'BeginDate',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -6), "Y-m-d"),
                format: 'Y-m-d'

            }, {
                name: 'endDate',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                value: Ext.util.Format.date(Ext.Date.add(new Date()), "Y-m-d"),
                format: 'Y-m-d'

            }]
        });

        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '日期',
                dataIndex: 'searchDate',
                width: 50,
                menuDisabled: true,
                sortable: false
            }, {
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
            }, {
                text: 'DAU',
                width: 50,
                dataIndex: 'dau',
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
                text: '投注金额',
                width: 50,
                dataIndex: 'bettingAmount',
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注转化率',
                width: 50,
                dataIndex: 'bettingRate',
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注ARPU',
                width: 50,
                dataIndex: 'arpu',
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注ASP',
                width: 50,
                dataIndex: 'asp',
                menuDisabled: true,
                sortable: false
            }, {
                text: '流水差',
                width: 50,
                dataIndex: 'amountGap',
                menuDisabled: true,
                sortable: false
            }, {
                text: '返奖率',
                width: 50,
                dataIndex: 'returnRate',
                menuDisabled: true,
                sortable: false
            }]
        });

    }
});