Ext.define('WF.view.data.board.monthlyDataViewMain',{
    extend: 'Ext.panel.Panel',
    title: '数据看板-月度指标监控',
    xtype: 'monthlyDataViewMain',
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
            url:'data/month/view/getList.do',
            autoload:false,
            fields: ["month","sumRecharge",'avgDau','avgDarpu','sumCost','costRate']
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
                store: parentChannelStore
            },
            {
                name: 'startTime',
                fieldLabel: '开始时间',
                xtype: 'datefield',
                format: 'Y-m'
            },{
                name: 'endTime',
                fieldLabel: '结束时间',
                xtype: 'datefield',
                format: 'Y-m'
            }
            ]
        });

        me.add({
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0 0 0 0;',
            items: [
                {
                    title: '月度指标监控',height:350,align:'stretch',width:"100%", xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {width:"33.33%",height:350,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:"monthTitle0",width:"100%",height:50,forceFit:true,bodyStyle:'border-width:0'},
                                {id:"monthKpi0",width:"100%",height:300,forceFit:true,bodyStyle:'border-width:0'}
                            ]
                        },
                        {width:"33.33%",height:350,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:"monthTitle1",width:"100%",height:50,forceFit:true,bodyStyle:'border-width:0'},
                                {id:"monthKpi1",width:"100%",height:300,forceFit:true,bodyStyle:'border-width:0'}
                            ]
                        },
                        {width:"33.33%",height:350,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:"monthTitle2",width:"100%",height:50,forceFit:true,bodyStyle:'border-width:0'},
                                {id:"monthKpi2",width:"100%",height:300,forceFit:true,bodyStyle:'border-width:0'}
                            ]
                        }
                    ]
                },
                {
                    title: '',height:350,align:'stretch',width:"100%", xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {width:"33.33%",height:350,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:"monthTitle3",width:"100%",height:50,forceFit:true,bodyStyle:'border-width:0'},
                                {id:"monthKpi3",width:"100%",height:300,forceFit:true,bodyStyle:'border-width:0'}
                            ]
                        },
                        {width:"33.33%",height:350,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:"monthTitle4",width:"100%",height:50,forceFit:true,bodyStyle:'border-width:0'},
                                {id:"monthKpi4",width:"100%",height:300,forceFit:true,bodyStyle:'border-width:0'}
                            ]
                        }
                    ]
                }
            ]
        });

        store.addListener('datachanged',function(){
                fun1();
            }
        );

        store.load ({
            callback:function(){
                fun1();
            }
        });

        function fun1() {
            var month=[];
            var sumRecharge=[];
            var avgDau=[];
            var avgDarpu=[];
            var sumCost=[];
            var costRate=[];

            for(var i=0;i<store.getCount();i++){
                var re=store.getAt(i);
                month[i]=re.get('month');
                sumRecharge[i]=re.get('sumRecharge');
                avgDau[i]=re.get('avgDau');
                avgDarpu[i]=re.get('avgDarpu');
                sumCost[i]=re.get('sumCost');
                costRate[i]=re.get('costRate');
            }

            var option = [
                {
                    // title: {text: '当月累计充值'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '月份:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                        },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'13%',
                        y:'5%'
                    },
                    xAxis: {
                        type : 'category',
                        data: month
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        },
                        splitLine: {           // 控制Y轴的分隔线(辅助线)
                            show: false      // 默认显示，属性show控制显示与
                        }
                        },
                    series: [{
                        name: '当月累计充值',
                        type: 'bar',
                        smooth:true,
                        data: sumRecharge,
                        barWidth: 50,
                        itemStyle:{
                            normal:{color:'cornflowerblue',   //柱状颜色
                                label : {
                                    show : true,  //柱头数字
                                    position : 'top',
                                    textStyle : {
                                        fontSize : '14',
                                        fontWeight:'bold'
                                    }
                                }
                            }
                        }
                    }]
                },
                {
                    // title: {text: '当月累计DAU日均值'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '月份:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'13%',
                        y:'5%'
                    },
                    xAxis: {
                        type : 'category',
                        data: month
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        },
                        splitLine: {           // 控制Y轴的分隔线(辅助线)
                            show: false      // 默认显示，属性show控制显示与
                        }
                    },
                    series: [{
                        name: '当月累计DAU日均值',
                        type: 'bar',
                        smooth:true,
                        data: avgDau,
                        barWidth: 50,
                        itemStyle:{
                            normal:{color:'cornflowerblue',   //柱状颜色
                                label : {
                                    show : true,  //柱头数字
                                    position : 'top',
                                    textStyle : {
                                        fontSize : '14',
                                        fontWeight:'bold'
                                    }
                                }
                            }
                        }
                    }]
                },
                {
                    // title: {text: '当月累计DARPU日均值'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '月份:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'13%',
                        y:'5%'
                    },
                    xAxis: {
                        type : 'category',
                        data: month
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        },
                        splitLine: {           // 控制Y轴的分隔线(辅助线)
                            show: false      // 默认显示，属性show控制显示与
                        }
                    },
                    series: [{
                        name: '当月累计DARPU日均值',
                        type: 'bar',
                        smooth:true,
                        data: avgDarpu,
                        barWidth: 50,
                        itemStyle:{
                            normal:{color:'cornflowerblue',   //柱状颜色
                                label : {
                                    show : true,  //柱头数字
                                    position : 'top',
                                    textStyle : {
                                        fontSize : '14',
                                        fontWeight:'bold'
                                    }
                                }
                            }
                        }
                    }]
                },
                {
                    // title: {text: '当月累计成本'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '月份:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'13%',
                        y:'5%'
                    },
                    xAxis: {
                        type : 'category',
                        data: month
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        },
                        splitLine: {           // 控制Y轴的分隔线(辅助线)
                            show: false      // 默认显示，属性show控制显示与
                        }
                    },
                    series: [{
                        name: '当月累计成本',
                        type: 'bar',
                        smooth:true,
                        data: sumCost,
                        barWidth: 50,
                        itemStyle:{
                            normal:{color:'cornflowerblue',   //柱状颜色
                                label : {
                                    show : true,  //柱头数字
                                    position : 'top',
                                    textStyle : {
                                        fontSize : '14',
                                        fontWeight:'bold'
                                    }
                                }
                            }
                        }
                    }]
                },
                {
                    // title: {text: '当月累计成本占比'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '月份:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value+"%";
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'13%',
                        y:'5.8%'
                    },
                    xAxis: {
                        type : 'category',
                        data: month
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        },
                        splitLine: {           // 控制Y轴的分隔线(辅助线)
                            show: false      // 默认显示，属性show控制显示与
                        }
                    },
                    series: [{
                        name: '当月累计成本占比',
                        type: 'line',
                        smooth:true,
                        data:costRate,
                        // barWidth: 50,
                        itemStyle:{
                            normal:{color:'cornflowerblue',   //柱状颜色
                                label : {
                                    show : true,  //柱头数字
                                    position : 'top',
                                    textStyle : {
                                        fontSize : '14',
                                        fontWeight:'bold',
                                    },
                                    formatter:function (p) {
                                        return p.value+"%";
                                    }
                                }
                            }
                        }
                    }]
                }
            ];

            for (var j=0;j<option.length;j++) {
                me.echarts = echarts.init(Ext.get("monthKpi"+j).dom);
                me.echarts.setOption(option[j]);
            }

            var titles = ["当月累计充值","当月累计DAU日均值","当月累计DARPU日均值","当月累计成本","当月累计成本占比"];
            for (var k=0;k<option.length;k++) {
                var d =  Ext.get("monthTitle"+k).dom;
                d.innerHTML = "<div align='center' style='line-height:50px;font-size:20px'>"+titles[k]+"</strong></div>";
            }
        }
    }
});

