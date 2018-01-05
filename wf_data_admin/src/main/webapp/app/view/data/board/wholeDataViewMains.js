Ext.define('WF.view.data.board.wholeDataViewMains', {
    extend: 'Ext.panel.Panel',
    title:'服务状态',
    xtype: 'wholeDataViewMains',
    closable: true,
    autoScroll:true,
    insetPadding : 50,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    legend : {
        position : 'right'
    },
    initComponent: function () {
        var me = this;
        var store = Ext.create('DCIS.Store', {
            autoLoad: true,
            url:'data/board/view/getList.do',
        });

        var option = [
            {
                title: {text: 'dau'},
                tooltip: {},
                legend: {data: ['日活']},
                xAxis: {data:[1,2,3,4,5,6]},
                yAxis: {},
                series: [{
                    name: '日活',
                    type: 'line',
                    data: [5, 20, 36, 10, 10, 100]
                }]
            },
            {
                title: {text: 'dau'},
                tooltip: {},
                legend: {data: ['日活']},
                xAxis: {data: [1,1,1,1,1,1]},
                yAxis: {},
                series: [{
                    name: '日活',
                    type: 'line',
                    data: [5, 20, 36, 10, 10, 200]
                }]
            },
            {
                title: {text: 'dau'},
                tooltip: {},
                legend: {data: ['日活']},
                xAxis: {data: [1,1,1,1,1,1]},
                yAxis: {},
                series: [{
                    name: '日活',
                    type: 'line',
                    data: [5, 20, 36, 10, 10, 200]
                }]
            },
        ];

        me.on("boxready", function () {
            // for (var i = 0; i <= 2; i++) {
            //     me.echarts = echarts.init(Ext.get("kpi"+i).dom);
            //     me.echarts.setOption(option[i]);
            // }
        });
        me.callParent(arguments);
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getViewChannels.do',
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
            columns: 3,
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
                    {title:'DAU',width:"33.33%",height:300,id:"kpi0"},
                    {title:'充值金额',width:"33.33%",height:300,id:"kpi1"},
                    {title:'投注人数',width:"33.33%",height:300,id:"kpi2"}]
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
                    {title:'投注人数',html:'3333',width:"33.33%",bodyStyle:'border-width:0 0 0 0;'}
                ]
            }]
        });
    },

    listeners:{
                load:function() {
                Ext.Ajax.request({
                    url: 'data/board/view/getList.do',
                    method: 'post',
                    success: function (response, options) {
                        var result = Ext.JSON.decode(response.responseText);
                        console.log("result的结果是:" + result);
                    }

                    // var option = [
                    //     {
                    //         title: {text: 'dau'},
                    //         tooltip: {},
                    //         legend: {data: ['日活']},
                    //         xAxis: {data: [1, 2, 3, 4, 5, 6]},
                    //         yAxis: {},
                    //         series: [{
                    //             name: '日活',
                    //             type: 'line',
                    //             data: [5, 20, 36, 10, 10, 200]
                    //         }]
                    //     },
                    //     {
                    //         title: {text: 'dau'},
                    //         tooltip: {},
                    //         legend: {data: ['日活']},
                    //         xAxis: {data: [7, 8, 9, 10, 11, 12]},
                    //         yAxis: {},
                    //         series: [{
                    //             name: '日活',
                    //             type: 'line',
                    //             data: [5, 20, 36, 10, 10, 200]
                    //         }]
                    //     },
                    // ];
                    // for (var i = 0; i <= 1; i++) {
                    //     var e = echarts.init(Ext.get("kpi" + i).dom);
                    //     e.setOption(option[i]);
                    // }
                });
        }
    }
});