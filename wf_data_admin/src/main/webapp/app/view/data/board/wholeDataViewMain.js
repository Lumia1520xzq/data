Ext.define('WF.view.data.board.wholeDataViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-整体数据概览',
    xtype: 'wholeDataViewMain',
    closable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    config:{
        option: {
            0:{
                title: {
                    text: 'ECharts 入门示例'
                },
                tooltip: {},
                legend: {
                    data:['销量']
                },
                xAxis: {
                    data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
                },
                yAxis: {
                },
                series: [{
                    name: '销量',
                    type: 'bar',
                    data: [5, 20, 36, 10, 10, 20]
                }]
            },
            1:{
                title: {
                    text: 'ECharts 入门示例'
                },
                tooltip: {},
                legend: {
                    data:['销量']
                },
                xAxis: {
                    data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
                },
                yAxis: {
                },
                series: [{
                    name: '销量',
                    type: 'bar',
                    data: [5, 20, 36, 10, 10, 30]
                }]
            }
        }
    },
    initComponent: function () {
        var me = this;
        // me.on("boxready", function () {
        //     me.echarts = echarts.init(me.getEl().dom);
        //     if (me.option) {
        //         me.echarts.setOption(me.option[0]);
        //     }
        // });
        me.callParent();
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
            tbar: [{
                text: '核心指标',
            }],
            columns: [ {
                text: '日期',
                width: 100,
                dataIndex: 'searchDate',
                option:me.option[0],
                menuDisabled: true,
                sortable: false
            }, {
                text: 'DAU',
                dataIndex: 'dauCount',
                width: 100,
                option:me.option[1],
                menuDisabled: true,
                sortable: false
            }, {
                text: '投注人数',
                dataIndex: 'userCount',
                width: 100,
                menuDisabled: true,
                sortable: false
            }]
        });

    }
});