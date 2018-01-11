Ext.define('WF.view.platform.thirdBettingMain', {
    extend: 'Ext.panel.Panel',
    title: '第三方投注数据概览',
    xtype: 'thirdBettingMain',
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
            url: 'data/admin/platform/record/getList.do',
            fields: ['businessDate','channelName','gameType','userCount', 'bettingCount', 'bettingAmount','bettingArpu','bettingAsp', 'resultAmount','resultRate']
        });

        var gameChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'game_channel'
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



        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '查询',
            collapsible: true,
            collapsed: false,
            columns: 3,
            buildField: "Manual",
            forceFit: true,
            items: [{
                name: 'channelId',
                fieldLabel: '渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: true,
                queryMode: "local",
                store: gameChannelStore
            },{
                name: 'startTime',
                fieldLabel: '开始时间',
                xtype: 'datefield',
                format: 'Y-m-d',
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d")
            },{
                name: 'endTime',
                fieldLabel: '结束时间',
                xtype: 'datefield',
                format: 'Y-m-d',
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d")
            }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [ {
                text: '日期',
                width: 100,
                dataIndex: 'businessDate',
                menuDisabled: true,
                sortable: false
            }, {
                text: '渠道',
                dataIndex: 'channelName',
                width: 100,
                menuDisabled: true,
                sortable: false
            },{
                text: '游戏名称',
                dataIndex: 'gameType',
                width: 100,
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var index = gameTypeStore.find('value', value);
                    if (index != -1) {
                        var record = gameTypeStore.getAt(index);
                        return record.data.label;
                    }
                    return '--';
                }
            }, {
                text: '投注人数',
                dataIndex: 'userCount',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注笔数',
                dataIndex: 'bettingCount',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注流水',
                dataIndex: 'bettingAmount',
                width: 100,
                menuDisabled: true,
                sortable: false
            },{
                text: '投注ARPU',
                dataIndex: 'bettingArpu',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注ASP',
                dataIndex: 'bettingAsp',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '返奖金额',
                dataIndex: 'resultAmount',
                width: 100,
                menuDisabled: true,
                sortable: false
            },  {
                text: '返奖率',
                dataIndex: 'resultRate',
                renderer:function(value){
                    return value+"%"
                },
                width: 100,
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});