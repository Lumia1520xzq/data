Ext.define('WF.view.game.tcard.tcardAnalysisMain', {
    extend: 'Ext.panel.Panel',
    title: '乐赢三张-场次分析',
    xtype: 'tcardAnalysisMain',
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
            url: 'data/admin/tcard/getAnalysisList.do',
            fields: ['searchDate',
                'lowBettingUser','midBettingUser', 'highBettingUser',
                'lowTableFee', 'midTableFee', 'highTableFee',
                'lowTables','midTables', 'highTables',
                'lowAvgRounds','midAvgRounds','highAvgRounds']
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
            columns: 4,
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
                        me.down('#channelId').setValue('')
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
            columns: [ {
                text: '日期',
                width: 100,
                dataIndex: 'searchDate',
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注人数(初级)',
                dataIndex: 'lowBettingUser',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注人数(中级)',
                dataIndex: 'midBettingUser',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注人数(高级)',
                dataIndex: 'highBettingUser',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '桌费(初级)',
                dataIndex: 'lowTableFee',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '桌费(中级)',
                dataIndex: 'midTableFee',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '桌费(高级)',
                dataIndex: 'highTableFee',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '桌数(初级)',
                dataIndex: 'lowTables',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '桌数(中级)',
                dataIndex: 'midTables',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '桌数(高级)',
                dataIndex: 'highTables',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '人均局数(初级)',
                dataIndex: 'lowAvgRounds',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '人均局数(中级)',
                dataIndex: 'midAvgRounds',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '人均局数(高级)',
                dataIndex: 'highAvgRounds',
                width: 100,
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});