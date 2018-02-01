Ext.define('WF.view.data.board.ltvViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-用户LTV',
    xtype: 'ltvViewMain',
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
            url:'data/ltv/view/getList.do',
            autoload:false,
            fields:["1","100001","100006",
                   "100015","100016","100018",
                   "200001","300001","100002",
                    "channelNames","ltv","endDate"
            ]
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
            items: [
                {
                    name:"channels",
                    fieldLabel:"全部",
                    xtype:"checkbox",
                    inputValue:"1"
                },
                {
                    name:"channels",
                    fieldLabel:"奖多多",
                    xtype:"checkbox",
                    inputValue:"100001"
                },
                {
                    name:"channels",
                    fieldLabel:"澳客",
                    xtype:"checkbox",
                    inputValue:"100006"
                },
                {
                    name:"channels",
                    fieldLabel:"全民彩票",
                    xtype:"checkbox",
                    inputValue:"100015"
                },
                {
                    name:"channels",
                    fieldLabel:"我去彩票站",
                    xtype:"checkbox",
                    inputValue:"100016"
                },
                {
                    name:"channels",
                    fieldLabel:"抓抓乐",
                    xtype:"checkbox",
                    inputValue:"100018"
                },
                {
                    name:"channels",
                    fieldLabel:"Android",
                    xtype:"checkbox",
                    inputValue:"200001"
                },
                {
                    name:"channels",
                    fieldLabel:"IOS",
                    xtype:"checkbox",
                    inputValue:"300001"
                },
                {
                    name:"channels",
                    fieldLabel:"逗游",
                    xtype:"checkbox",
                    inputValue:"100002"
                },
                {
                    name: 'startTime',
                    fieldLabel: '开始时间',
                    xtype: 'datefield',
                    format: 'Y-m-d'
                },{
                    name: 'endTime',
                    fieldLabel: '结束时间',
                    xtype: 'datefield',
                    format: 'Y-m-d'
                }
            ]
        });

        me.add({
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0 0 0 0;',
            items: [{
                title: '用户LTV',align:'stretch',height:500,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                items:[
                    {width:"100%",height:500,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                        {width:"45%",height:500,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:'100%',height:40,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                    items:[
                                        {id:'ltvTitle0',width:'20%',height:40,forceFit:true,bodyStyle:'border-width:0'},
                                        {width:'40%',height:40,forceFit:true,bodyStyle:'border-width:0'},
                                        {id:'ltvDate',width:'40%',height:40,forceFit:true,bodyStyle:'border-width:0'}
                                    ]
                                },
                                {width:'100%',height:460,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                    items:[
                                        {id:"ltvBoard0",width:"95%",height:460,forceFit:true,bodyStyle:'border-width:0'},
                                        {width:"5%",height:460,forceFit:true,bodyStyle:'border-width:0'}
                                    ]
                                }
                            ]
                        },
                        {width:"1%",height:500,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'},
                        {width:"50%",height:500,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:'ltvBoard1',width:'100%',height:440,forceFit:true,bodyStyle:'border-width:0'}
                            ]
                        }
                     ] }
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

        function fun1(){
            Ext.get('ltvTitle0').dom.innerHTML = "<div align='center'><span style='font-size:20px;line-height:35px;'>" + "用户LTV" + "</div>";
            var endDate = store.getAt(0).get("endDate");
            if(endDate != ''){
                Ext.get('ltvDate').dom.innerHTML = "<div align='center'><span style='font-size:12px;line-height:35px;'>截至" + endDate + "</div>";
            }else{
                Ext.get('ltvDate').dom.innerHTML = "";
            }

            var channelNames = store.getAt(0).get("channelNames");
            var ltv = store.getAt(0).get("ltv");
            var option =
                {
                    tooltip: {trigger: 'axis',
                    formatter: function (params) {
                        var str='';
                        for(var i = 0; i < params.length; i++){
                                str += params[i].name+":"+ params[i].value;
                        }
                        return str;
                    },
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid: {
                        left: '6%',
                        right: '1%',
                        top:'1%',
                        containLabel: true
                    },
                    xAxis: {
                        type : 'value',
                        show :false,
                        axisLine: {
                            show: false
                        },
                        axisTick: {
                            show: false
                        }
                    },
                    yAxis: {
                        type : 'category',
                        data: channelNames,
                        splitLine: {show: false},
                        axisLine: {
                            show: false
                        },
                        axisTick: {
                            show: false
                        },
                        offset: 10,
                        nameTextStyle: {
                            fontSize: 15
                        }
                        },
                    series: [{
                        type: 'bar',
                        smooth:true,
                        data:ltv,
                        barWidth: 18,
                        label: {
                            normal: {
                                show: true,
                                position: 'right',
                                offset: [1, -1],
                                textStyle: {
                                    color: 'black',
                                    fontSize: 13
                                }
                            }
                        },
                        itemStyle: {
                            emphasis: {
                                barBorderRadius: 7
                            },
                            normal: {
                                barBorderRadius: 7,
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 1, 0,
                                    [
                                        {offset: 0, color: 'cornflowerblue'}
                                    ]
                                )
                            }
                        }
                    }]
                };
            me.echarts = echarts.init(Ext.get("ltvBoard0").dom);
            me.echarts.setOption(option);

            var total = store.getAt(0).get("1");
            var jinshan = store.getAt(0).get("100001");
            var okooo = store.getAt(0).get("100006");
            var quanmin = store.getAt(0).get("100015");
            var woqu = store.getAt(0).get("100016");
            var zhuazhuale = store.getAt(0).get("100018");
            var android = store.getAt(0).get("200001");
            var ios = store.getAt(0).get("300001");
            var doyo = store.getAt(0).get("100002");
            var data=[];

            if(judge(total)){
               data.push(total);
            }
            if(judge(jinshan)){
                data.push(jinshan);
            }
            if(judge(okooo)){
                data.push(okooo);
            }
            if(judge(quanmin)){
                data.push(quanmin);
            }
            if(judge(woqu)){
                data.push(woqu);
            }
            if(judge(zhuazhuale)){
                data.push(zhuazhuale);
            }
            if(judge(android)){
                data.push(android);
        }
            if(judge(ios)){
                data.push(ios);
            }
            if(judge(doyo)){
                data.push(doyo);
            }

            var businessDate = [];
            if (data.length !=0) {
                var val = data[0];
                for(var k=0;k<val.length;k++) {
                   var date = val[k].businessDate;
                    var x = date.indexOf('-');
                    businessDate[k] = date.substring(x+1);
                }
            }


            var tendency =
                {
                    title: {text:'用户LTV趋势',left:'center',
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
                        // data:['今日','昨日','历史七日均值'],
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
                        data:businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }
                    },
                    series:[]
                };

            var channelNames = [];
            for(var j=0;j<data.length;j++){
                var val = data[j];
                channelNames.push(val[0].channelName);
                var userLtv = [];
                for(var k=0;k<val.length;k++) {
                    userLtv[k] = val[k].userLtv;
                }
                var item =
                {
                    name: val[0].channelName,
                    type: 'line',
                    smooth:true,
                    data: userLtv,
                    itemStyle:{normal:{}}
                };
                tendency.series.push(item);
            }
            tendency.legend.data = channelNames;
            me.echarts = echarts.init(Ext.get("ltvBoard1").dom);
            me.echarts.setOption(tendency);
        }


        function judge(data){
            if(data.length == 0){
                return false;
            }
            return true;
        }



    }
});



