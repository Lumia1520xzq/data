Ext.define('WF.view.data.board.wholeDataViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-整体数据概览',
    xtype: 'wholeDataViewMain',
    closable: true,
    autoScroll:true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function () {
        var me = this;
        me.on("boxready", function () {
            for (var i = 1; i <= 3; i++) {
                me.echarts = echarts.init(Ext.get("kpi"+i).dom);
                me.echarts.setOption(me.option[i]);
            }
        });
        me.callParent(arguments);
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
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0 0 0 0;',
            items: [{
                title: '核心指标',align:'stretch',height:900,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                items:[
                    {title:'DAU',width:"33.33%",height:300,id:"kpi1"},
                    {title:'充值金额',width:"33.33%",height:300,id:"kpi2"},
                    {title:'投注人数',width:"33.33%",height:300,id:"kpi3"}]
            }, {
                title: '投注数据', html: 'height:150',height:300, align : 'stretch',width:"100%", xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                items:[
                    {title:'每日投注流水',html:'11111',width:"33.33%",bodyStyle:'border-width:0 0 0 0;'},
                    {title:'返奖率',html:'2222',width:"33.33%",bodyStyle:'border-width:0 0 0 0;'},
                    // {title:'投注人数',html:'3333',width:"33.33%",bodyStyle:'border-width:0 0 0 0;'}
                    ]
            },  {
                title: '付费数据', html: 'flex:2',height:300, align : 'stretch', width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                items:[
                    {title:'DAU',html:'11111',width:"33.33%",bodyStyle:'border-width:0 0 0 0;'},
                    {title:'充值金额',html:'2222',width:"33.33%",bodyStyle:'border-width:0 0 0 0;'},
                    {title:'投注人数',html:'3333',width:"33.33%",bodyStyle:'border-width:0 0 0 0;'
                    }]
            }]
        });
    },
    config:{
        option:
            {
           1:{
               title: {text: 'dau'},
               tooltip: {},
               legend: {data: ['日活']},
               xAxis: {data: [1, 2, 3, 4, 5, 6]},
               yAxis: {},
               series: [{
                   name: '日活',
                   type: 'line',
                   data: [5, 20, 36, 10, 10, 20]
               }]
             },
            2:{
                title: {text: '充值金额'},
                tooltip: {},
                legend: {data: ['充值金额']},
                xAxis: {data: [3, 4, 5, 6, 7, 8]},
                yAxis: {},
                series: [{
                    name: '充值金额',
                    type: 'line',
                    data: [1, 2, 3, 4, 5, 6]
                }]
            },
            3:{
                title: {text: '充值金额'},
                tooltip: {},
                legend: {data: ['充值金额']},
                xAxis: {data: [3, 4, 5, 6, 7, 8]},
                yAxis: {},
                series: [{
                    name: '充值金额',
                    type: 'line',
                    data: [1, 2, 3, 4, 5, 6]
                }]
            },
            }
    }
});