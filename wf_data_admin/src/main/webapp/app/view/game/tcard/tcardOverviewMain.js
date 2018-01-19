Ext.define('WF.view.game.tcard.tcardOverviewMain', {
    extend: 'Ext.panel.Panel',
    title: '乐赢三张-数据概览',
    xtype: 'tcardOverviewMain',
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
            url: 'data/admin/tcard/getList.do',
            fields: ['searchDate','dauCount', 'userCount', 'bettingAmount', 'resultAmount','amountDiff','tableAmount','conversionRate','returnRate1','returnRate2','bettingArpu','bettingAsp']
        });

        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });

        var allChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getAllChannels.do',
            fields: ['id', 'name']
        });

        var childChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getChildChannels.do',
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
                name: 'parentId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: parentChannelStore,
                listeners :{
                    change:function(obj,val){
                        childChannelStore.load({
                            params: {
                                parentId : val
                            }
                        });
                        // me.down('#channelId').setValue('')
                    }
                }
            },{
                name: 'channelId',
                fieldLabel: '子渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: childChannelStore
            },{
                name: 'startTime',
                fieldLabel: '开始时间',
                xtype: 'datefield',
                format: 'Y-m-d'
            },{
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
            tbar: [{
                text: '指标说明',
                iconCls: "icon-add",
                handler: function () {
                    var doRefresh = me.down('datagrid').store;
                    Ext.create('WF.view.game.tcard.showTips', {doRefresh: doRefresh}).show();
                }
            }],
            columns: [ {
                text: '日期',
                width: 100,
                dataIndex: 'searchDate',
                menuDisabled: true,
                sortable: false
            }, {
                text: 'DAU',
                dataIndex: 'dauCount',
                width: 100,
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
                text: '投注人数',
                dataIndex: 'userCount',
                width: 100,
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
                        return Ext.util.Format.number(value, "0,000");
                    }else {
                        return 0.00;
                    }
                }
            }, {
                text: '返奖流水',
                dataIndex: 'resultAmount',
                width: 100,
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
                text: '流水差',
                dataIndex: 'amountDiff',
                width: 100,
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
                text: '桌费金额',
                dataIndex: 'tableAmount',
                width: 100,
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
                text: '投注转化率',
                dataIndex: 'conversionRate',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '返奖率1',
                dataIndex: 'returnRate1',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '返奖率2',
                dataIndex: 'returnRate2',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注arpu',
                dataIndex: 'bettingArpu',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注asp',
                dataIndex: 'bettingAsp',
                width: 100,
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});