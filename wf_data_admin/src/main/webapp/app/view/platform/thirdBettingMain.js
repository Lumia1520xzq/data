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
            fields: ['businessDate', 'channelName', 'gameType','dau', 'userCount', 'bettingCount',
                'bettingAmount', 'bettingArpu', 'bettingAsp', 'resultAmount', 'resultRate',
                'newUserSecondRetention','allSecondRetention','newUserThreeDayRetention','threeDayRetention']
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
            sumData: function () {
            callapi("data/admin/platform/record/sumData.do", me.down('dataform').form.getValues(), function (response) {
                me.down("[name='bettingData']").setValue(Ext.util.Format.number(response.bettingData, "0,000.00"));
                me.down("[name='returnData']").setValue(Ext.util.Format.number(response.returnData, "0,000.00"));
            });
        },
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
            }, {
                name: 'gameType',
                fieldLabel: '游戏类型',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: true,
                queryMode: "local",
                store: gameTypeStore
            }, {
                name: 'startTime',
                fieldLabel: '开始时间',
                xtype: 'datefield',
                format: 'Y-m-d',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -1), "Y-m-d")
            }, {
                name: 'endTime',
                fieldLabel: '结束时间',
                xtype: 'datefield',
                format: 'Y-m-d',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -1), "Y-m-d")
            }]
        });

        me.add({
            items: [{
                xtype: 'displayfield',
                name: 'bettingData',
                value: 0,
                fieldLabel: '<span style="font-size:14px;font-weight:bold">投注金额合计：</span>',
                padding: 5
            },{
                xtype: 'displayfield',
                name: 'returnData',
                value: 0,
                fieldLabel: '<span style="font-size:14px;font-weight:bold">返奖金额合计：</span>',
                padding: 5
            }],
            layout: 'hbox'
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
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
            }, {
                text: '游戏名称',
                dataIndex: 'gameType',
                width: 100,
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var record = gameTypeStore.findRecord('value', value,0,false,false,true);
                    if(record == null){
                        return '--';
                    }else {
                        return record.data.label;
                    }
                }
            }, {
                text: '投注人数',
                dataIndex: 'userCount',
                width: 80,
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                       return Ext.util.Format.number(value, "0,000");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: 'dau',
                dataIndex: 'dau',
                width: 80,
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '投注笔数',
                dataIndex: 'bettingCount',
                width: 80,
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '投注流水',
                dataIndex: 'bettingAmount',
                width: 100,
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000.00");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '投注ARPU',
                dataIndex: 'bettingArpu',
                width: 80,
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000.00");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '投注ASP',
                dataIndex: 'bettingAsp',
                width: 80,
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000.00");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '返奖金额',
                dataIndex: 'resultAmount',
                width: 100,
                menuDisabled: true,
                sortable: false,
                renderer:function (value) {
                    if(value != null){
                        return Ext.util.Format.number(value, "0,000.00");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '返奖率',
                dataIndex: 'resultRate',
                renderer: function (value) {
                    return value + "%"
                },
                width: 80,
                menuDisabled: true,
                sortable: false
            }, {
                text: '新增次留',
                dataIndex: 'newUserSecondRetention',
                renderer: function (value) {
                    return value + "%"
                },
                width: 80,
                menuDisabled: true,
                sortable: false
            }, {
                text: '全量次留',
                dataIndex: 'allSecondRetention',
                renderer: function (value) {
                    return value + "%"
                },
                width: 80,
                menuDisabled: true,
                sortable: false
            }, {
                text: '新增3日留存',
                dataIndex: 'newUserThreeDayRetention',
                renderer: function (value) {
                    return value + "%"
                },
                width: 80,
                menuDisabled: true,
                sortable: false
            }, {
                text: '3日留存',
                dataIndex: 'threeDayRetention',
                renderer: function (value) {
                    return value + "%"
                },
                width: 80,
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});