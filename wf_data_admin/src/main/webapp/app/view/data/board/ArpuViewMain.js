Ext.define('WF.view.data.board.ArpuViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-用户累计ARPU',
    xtype: 'ArpuViewMain',
    closable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var store = Ext.create('DCIS.Store', {
            autoLoad:false,
            url: 'data/arpu/view/getList.do',
            fields: ["channelName","businessDate",'newUsers',
                'arpu1','arpu2','arpu3','arpu4','arpu5','arpu6',
                'arpu7','arpu15','arpu30','arpu60','arpu90']
        });
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name'
            ]
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
            items: [
            {
                name: 'parentId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: parentChannelStore
            },{
                name: 'beginDate',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            },{
                name: 'endDate',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0 0 0 0;',
            items: [
                // {title: '',align:'stretch',height:50,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0'},
                {
                title: '用户累计ARPU和留存(最多查询七天)',align:'stretch',height:350,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                items:[
                    {width:"100%",height:350,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                        {width:"45%",height:350,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:'arpu0',width:'100%',height:300,forceFit:true}
                            ]
                        },
                        {width:"1%",height:350,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'},
                        {width:"45%",height:350,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:'arpu1',width:'100%',height:350,forceFit:true,bodyStyle:'border-width:0'}
                            ]
                        }
                    ] }
                ]
            }
            ]
        });


        store.load({
            callback:function(){
                fun1();
                fun2();
            }
        });

        store.addListener('datachanged',function(){
                fun2();
            }
        );

        function fun2() {
            var arpu1 = [];
            var arpu2 = [];
            var arpu3 = [];
            var arpu4 = [];
            var arpu5 = [];
            var arpu6 = [];
            var arpu7 = [];
            var arpu15 = [];
            var arpu30 = [];
            var arpu60 = [];
            var arpu90 = [];
            var businessDate = [];
            for(var i=0;i<store.getCount();i++) {
                var record = store.getAt(i);
                arpu1[i] = record.get("arpu1");
                arpu2[i] = record.get("arpu2");
                arpu3[i] = record.get("arpu3");
                arpu4[i] = record.get("arpu4");
                arpu5[i] = record.get("arpu5");
                arpu6[i] = record.get("arpu6");
                arpu7[i] = record.get("arpu7");
                arpu15[i] = record.get("arpu15");
                arpu30[i] = record.get("arpu30");
                arpu60[i] = record.get("arpu60");
                arpu90[i] = record.get("arpu90");
                businessDate[i] = record.get("businessDate");
            }

            var option =
                {
                    title: {text:'用户累计ARPU',left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:22//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis'
                        // formatter:
                    },
                    legend: {
                        data:businessDate,
                        top:'bottom'
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'5%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data:["D1","D2","D3","D4","D5","D6","D7","D15","D30","D60","D90"]
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }
                    },
                    series:[]
                };


                for(var k=0;k<businessDate.length;k++) {
                    var item =
                    {
                        name: businessDate[k],
                        type: 'line',
                        smooth:true,
                        data: [arpu1[k],arpu2[k],arpu3[k],
                               arpu4[k],arpu5[k],arpu6[k],
                               arpu7[k],arpu15[k],arpu30[k],
                               arpu60[k],arpu90[k]],
                        itemStyle:{normal:{}}
                    };
                    option.series.push(item);
                }
            me.echarts = echarts.init(Ext.get("arpu0").dom);
            me.echarts.setOption(option);

        }

        function fun1(){
            me.add({
                xtype: 'datagrid',
                store: store,
                buildField: "Manual",
                columns: [
                    {
                        text: '渠道',
                        width:100,
                        dataIndex: 'channelName',
                        menuDisabled: true,
                        sortable: false
                    }
                    , {
                        text: '注册日期',
                        width:100,
                        dataIndex: 'businessDate',
                        menuDisabled: true,
                        sortable: false
                    },
                    {
                        text: '新用户数',
                        width:100,
                        dataIndex: 'newUsers',
                        menuDisabled: true,
                        sortable: false
                    },
                    {
                        text:'用户累计ARPU',
                        width:880,
                        columns:[
                            {
                                text: '1天',
                                width: 80,
                                dataIndex: 'arpu1',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '2天',
                                width: 80,
                                dataIndex: 'arpu2',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '3天',
                                width: 80,
                                dataIndex: 'arpu3',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '4天',
                                width: 80,
                                dataIndex: 'arpu4',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '5天',
                                width: 80,
                                dataIndex: 'arpu5',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '6天',
                                width: 80,
                                dataIndex: 'arpu6',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '7天',
                                width: 80,
                                dataIndex: 'arpu7',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '15天',
                                width: 80,
                                dataIndex: 'arpu15',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '30天',
                                width: 80,
                                dataIndex: 'arpu30',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '60天',
                                width: 80,
                                dataIndex: 'arpu60',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '90天',
                                width: 80,
                                dataIndex: 'arpu90',
                                menuDisabled: true,
                                sortable: false
                            }
                        ]
                    }
                ]
            });
        }

    }
});