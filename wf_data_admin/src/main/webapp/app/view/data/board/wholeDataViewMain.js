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
    me.callParent(arguments);

    var store= Ext.create('DCIS.Store', {
        url:'data/board/view/getList.do',
        autoload:false,
        fields: ['businessDate','dau','rechargeAmount','rechargeCount',
                  'newUsers','userCount','bettingRate',
                  'dauPayRate','bettingPayRate','userBettingRate',
                  'bettingAmount','resultRate',
                  'payArpu','payArppu'
        ]
    });
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
                store: parentChannelStore
            },{
                xtype:'searchfield',
                store:parentChannelStore,
                displayField : 'name',
                valueField : 'id'
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
                title: '核心指标',align:'stretch',height:300,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                items:[
                    {title:'',width:"33.33%",height:300,id:"kpi0"},
                    {title:'',width:"33.33%",height:300,id:"kpi1"},
                    {title:'',width:"33.33%",height:300,id:"kpi2"}]
            },
                {
                    align:'stretch',height:300,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {title:'',width:"33.33%",height:300,id:"kpi3"},
                        {title:'',width:"33.33%",height:300,id:"kpi4"},
                        {title:'',width:"33.33%",height:300,id:"kpi5"}]
                },
                {
                    align:'stretch',height:300,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {title:'',width:"33.33%",height:300,id:"kpi6"},
                        {title:'',width:"33.33%",height:300,id:"kpi7"},
                        {title:'',width:"33.33%",height:300,id:"kpi8"}]
                },
                {
                title: '投注数据',height:300,align:'stretch',width:"100%", xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                items:[
                    {title:'',width:"33.33%",height:300,id:"kpi9"},
                    {title:'',width:"33.33%",height:300,id:"kpi10"}
                    ]
                },
                {
                title: '付费数据',height:300,align:'stretch', width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                items:[
                    {title:'',width:"33.33%",height:300,id:"kpi11"},
                    {title:'',width:"33.33%",height:300,id:"kpi12"}
                    ]
                }
            ]
        });

        store.load ({
            callback:function(){
                var businessDate=[];
                var dau=[];
                var rechargeAmount=[];
                var rechargeCount=[];
                var newUsers=[];
                var userCount=[];
                var bettingRate=[];
                var dauPayRate=[];
                var bettingPayRate=[];
                var userBettingRate=[];
                var bettingAmount=[];
                var resultRate=[];
                var payArpu=[];
                var payArppu=[];
                for(var i=0;i<store.getCount();i++){
                    var re=store.getAt(i);
                    businessDate[i]=re.data.businessDate;
                    dau[i]=re.data.dau;
                    rechargeAmount[i]=re.data.rechargeAmount;
                    rechargeCount[i]=re.data.rechargeCount;
                    newUsers[i]=re.data.newUsers;
                    userCount[i]=re.data.userCount;
                    bettingRate[i]=re.data.bettingRate;
                    dauPayRate[i]=re.data.dauPayRate;
                    bettingPayRate[i]=re.data.bettingPayRate;
                    userBettingRate[i]=re.data.userBettingRate;
                    bettingAmount[i]=re.data.bettingAmount;
                    resultRate[i]=re.data.resultRate;
                    payArpu[i]=re.data.payArpu;
                    payArppu[i]=re.data.payArppu;
                }
                var option = [
                    {
                        title: {text: 'DAU'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['DAU']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate},
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                            formatter: '{value}'
                        }},
                        series: [{
                            name: 'DAU',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: dau
                        }]
                    },
                    {
                        title: {text: '充值金额'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['充值金额']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate},
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: '充值金额',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: rechargeAmount
                        }]
                    },
                    {
                        title: {text: '充值人数'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['充值人数']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate},
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: '充值人数',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: rechargeCount
                        }]
                    },
                    {
                        title: {text: '新增用户'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['新增用户']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate
                        },
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: '新增用户',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: newUsers
                        }]
                    },
                    {
                        title: {text: '投注人数'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['投注人数']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate
                        },
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: '投注人数',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: userCount
                        }]
                    },
                    {
                        title: {text: '投注转化率'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['投注转化率']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate
                        },
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: '投注转化率',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: bettingRate
                        }]
                    },
                    {
                        title: {text: 'DAU付费转化率'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['DAU付费转化率']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate
                        },
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: 'DAU付费转化率',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: dauPayRate
                        }]
                    },
                    {
                        title: {text: '投注付费转化率'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['投注付费转化率']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate
                        },
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: '投注付费转化率',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: bettingPayRate
                        }]
                    },
                    {
                        title: {text: '新用户投注转化率'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['新用户投注转化率']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate
                        },
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: '新用户投注转化率',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: userBettingRate
                        }]
                    },
                    {
                        title: {text: '投注流水'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['投注流水']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate
                        },
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: '投注流水',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: bettingAmount
                        }]
                    },
                    {
                        title: {text: '返奖率'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['返奖率']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate
                        },
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: '返奖率',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: resultRate
                        }]
                    },
                    {
                        title: {text: 'ARPU'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['ARPU']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate
                        },
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: 'ARPU',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            data: payArpu
                        }]
                    },
                    {
                        title: {text: 'ARPPU'},
                        tooltip: {trigger: 'axis'},
                        // legend: {data: ['ARPPU']},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                // dataView : {show: true, readOnly: false},
                                magicType : {show: true, type: ['line', 'bar']},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        xAxis: {
                            type : 'category',
                            boundaryGap : false,
                            data: businessDate
                        },
                        yAxis: {
                            type : 'value',
                            axisLabel : {
                                formatter: '{value}'
                            }},
                        series: [{
                            name: 'ARPPU',
                            type: 'line',
                            smooth:true,
                            // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                            itemStyle: {normal: {}},
                            data: payArppu
                        }]
                    }
                ];
                for (var j=0;j<option.length;j++) {
                    me.echarts = echarts.init(Ext.get("kpi"+j).dom);
                    me.echarts.setOption(option[j]);
                }
            }
        });
    }
});