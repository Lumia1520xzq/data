Ext.define('WF.view.game.taurus.taurusAnalysisMain', {
    extend: 'Ext.panel.Panel',
    title: '乐赢-场次分析',
    xtype: 'taurusAnalysisMain',
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
            url: 'data/admin/taurus/getAnalysisList.do',
            fields: ['searchDate',
                'rookieBettingUser','lowBettingUser','midBettingUser', 'highBettingUser',
                'rookieTableFee','lowTableFee', 'midTableFee', 'highTableFee',
                'rookieTables', 'lowTables','midTables', 'highTables',
                'rookieAvgRounds','lowAvgRounds','midAvgRounds','highAvgRounds']
        });

        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
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
            columns: [ {
                text: '日期',
                width: 90,
                dataIndex: 'searchDate',
                menuDisabled: true,
                sortable: false
            },
                {
                   text:'投注人数',
                    width:400,
                    columns:[
                        {
                            text: '新手',
                            dataIndex: 'rookieBettingUser',
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
                        },
                        {
                            text: '初级',
                            dataIndex: 'lowBettingUser',
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
                            text: '中级',
                            dataIndex: 'midBettingUser',
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
                            text: '高级',
                            dataIndex: 'highBettingUser',
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
                        }
                    ]
                },
                {
                    text:'桌费',
                    width:400,
                    columns:[
                        {
                            text: '新手',
                            dataIndex: 'rookieTableFee',
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
                        },
                        {
                            text: '初级',
                            dataIndex: 'lowTableFee',
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
                            text: '中级',
                            dataIndex: 'midTableFee',
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
                            text: '高级',
                            dataIndex: 'highTableFee',
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
                        }
                    ]
                },
                {
                    text:'桌数',
                    width:400,
                    columns:[
                        {
                            text: '新手',
                            dataIndex: 'rookieTables',
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
                        },
                        {
                            text: '初级',
                            dataIndex: 'lowTables',
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
                            text: '中级',
                            dataIndex: 'midTables',
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
                            text: '高级',
                            dataIndex: 'highTables',
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
                        }
                    ]
                },
                {
                    text: '人均局数',
                    width:400,
                    columns:[
                        {
                            text: '新手',
                            dataIndex: 'rookieAvgRounds',
                            width: 100,
                            menuDisabled: true,
                            sortable: false
                        },
                        {
                            text: '初级',
                            dataIndex: 'lowAvgRounds',
                            width: 100,
                            menuDisabled: true,
                            sortable: false
                        }, {
                            text: '中级',
                            dataIndex: 'midAvgRounds',
                            width: 100,
                            menuDisabled: true,
                            sortable: false
                        }, {
                            text: '高级',
                            dataIndex: 'highAvgRounds',
                            width: 100,
                            menuDisabled: true,
                            sortable: false
                        }
                    ]
                }
                ]
        });
    }
});