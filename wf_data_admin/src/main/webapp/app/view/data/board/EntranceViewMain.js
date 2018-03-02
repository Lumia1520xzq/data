Ext.define('WF.view.data.board.EntranceViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据看板-奖多多各入口用户分析',
    xtype: 'EntranceViewMain',
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
            url:'/data/entrance/analysis/getList.do',
            autoload:false,
            fields:['entranceDauRateList','eventNameList','searchDate','dateList',
                'duoDuoDauList','duoduoDauRate',
                'iconDauList','iconDauRate',

                'bannerDauList','bannerDauRate',
                'advDauList','advDauRate',
                'gameDauList','gameDauRate',
                'rollDauList','rollDauRate',
                'pointDauList','pointDauRate',
                'actCenterDauList','actCenterDauRate',
                'pushDauList','pushDauRate',
                'strongAdvDauList','strongAdvDauRate'
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
                    name: 'searchDate',
                    fieldLabel: '日期',
                    xtype: 'datefield',
                    format: 'Y-m-d'
                    // value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-30),"Y-m-d")
                }
            ]
        });

        me.add({
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0 0 0 0;',
            items: [{
                title: '各入口数据',align:'stretch',height:380,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                items:[
                    {width:"100%",height:380,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                        {id:'entPie',width:"45%",height:380,xtype:"panel",layout:'vbox',forceFit:true
                            // items:[
                            //     {id:'entPie',width:'100%',height:300,forceFit:true,}
                            // ]
                        },

                        {id:'entMix',width:"45%",height:380,xtype:"panel",layout:'vbox',forceFit:true
                            // items:[
                            //     {id:'entMix',width:'100%',height:300,forceFit:true,bodyStyle:'border-width:0'}
                            // ]
                        },
                        {width:"10%",height:380,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'}
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

        function fun1(){
            var entranceDauRateList = store.getAt(0).get("entranceDauRateList");
            var eventNameList = store.getAt(0).get("eventNameList");
            var searchDate = store.getAt(0).get("searchDate");
            var dateList = store.getAt(0).get("dateList");



            var duoDuoDauList = store.getAt(0).get("duoDuoDauList");
            var iconDauList = store.getAt(0).get("iconDauList");
            var bannerDauList = store.getAt(0).get("bannerDauList");
            var advDauList = store.getAt(0).get("advDauList");
            var gameDauList = store.getAt(0).get("gameDauList");
            var rollDauList = store.getAt(0).get("rollDauList");
            var pointDauList = store.getAt(0).get("pointDauList");
            var actCenterDauList = store.getAt(0).get("actCenterDauList");
            var pushDauList = store.getAt(0).get("pushDauList");
            var strongAdvDauList = store.getAt(0).get("strongAdvDauList");

            var duoduoDauRate = store.getAt(0).get("duoduoDauRate");
            var iconDauRate = store.getAt(0).get("iconDauRate");
            var bannerDauRate = store.getAt(0).get("bannerDauRate");
            var advDauRate = store.getAt(0).get("advDauRate");
            var gameDauRate = store.getAt(0).get("gameDauRate");
            var rollDauRate = store.getAt(0).get("rollDauRate");
            var pointDauRate = store.getAt(0).get("pointDauRate");
            var actCenterDauRate = store.getAt(0).get("actCenterDauRate");
            var pushDauRate = store.getAt(0).get("pushDauRate");
            var strongAdvDauRate = store.getAt(0).get("strongAdvDauRate");

            var dauList=[];
            dauList.push(duoDuoDauList);
            dauList.push(iconDauList);
            dauList.push(bannerDauList);
            dauList.push(advDauList);
            dauList.push(gameDauList);
            dauList.push(rollDauList);
            dauList.push(pointDauList);
            dauList.push(actCenterDauList);
            dauList.push(pushDauList);
            dauList.push(strongAdvDauList);

            var dauRate=[];
            dauRate.push(duoduoDauRate);
            dauRate.push(iconDauRate);
            dauRate.push(bannerDauRate);
            dauRate.push(advDauRate);
            dauRate.push(gameDauRate);
            dauRate.push(rollDauRate);
            dauRate.push(pointDauRate);
            dauRate.push(actCenterDauRate);
            dauRate.push(pushDauRate);
            dauRate.push(strongAdvDauRate);

            var titleList = [];
            titleList.push("发现页多多游戏入口--DAU及占比");
            titleList.push("首页顶部banner右侧小图标入口--DAU及占比");
            titleList.push("首页顶部banner入口--DAU及占比");
            titleList.push("新增广告位入口--DAU及占比");
            titleList.push("我的福利任务(玩游戏)入口--DAU及占比");
            titleList.push("幸运大转盘(金叶子跳转)入口--DAU及占比");
            titleList.push("我的积分商城(金叶子跳转)入口--DAU及占比");
            titleList.push("发现页活动中心入口--DAU及占比");
            titleList.push("push入口--DAU及占比");
            titleList.push("强广位入口--DAU及占比");

            var arrays =[];
            for(var i = 0;i < entranceDauRateList.length;i++){
                arrays[i] = {
                    value:entranceDauRateList[i],
                    name:eventNameList[i]
                }
            }

            var pieOption = {
                title : {
                    text: searchDate +'活跃用户各入口来源比例',
                    x:'center',
                    textStyle:{ //标题内容的样式
                        fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                        fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                        fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                        fontSize:22//主题文字字体大小，默认为18px
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a}<br/>{b}:{c}({d}%)"
                },
                legend: {
                    orient:'vertical',
                    x:'3%',
                    top:'5%',
                    data:eventNameList
                },
                calculable : true,
                series : [
                    {
                        name:'入口来源',
                        type:'pie',
                        radius:'55%',
                        center: ['50%','60%'],
                        data:arrays
                    }
                ]
            };
            fun2(titleList[0],dateList,dauList[0],dauRate[0]);
            me.echarts = echarts.init(Ext.get("entPie").dom);
            me.echarts.setOption(pieOption);

            // 增加监听事件
            function eConsole(param) {
                if (typeof param.seriesIndex != 'undefined') {
                    if (param.type == 'click') {
                        var pieLenght= pieOption.legend.data.length;
                        for(var i=0;i<pieLenght;i++){
                            everyClick(param,i,pieOption.legend.data[i],titleList[i],dateList,dauList[i],dauRate[i]);
                        }
                    }
                }
            }

            function everyClick(param,i,txt,title,dateList,dauList,dauRate){
                if(param.seriesIndex==0 && param.dataIndex==i){
                    fun2(title,dateList,dauList,dauRate);
                }
            }
            me.echarts.on("click", eConsole);
        }

        function fun2(title,dateList,dauList,dauRateList){
            var mixOption =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            // fontWeight:"bold",
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:18//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str +=  params[i].seriesName +':'+params[i].value +"<br/>";
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
                        left:'4%',
                        top:'20%',
                        right:'6%',
                        bottom:'13%'
                    },
                    xAxis: {
                        type : 'category',
                        data: dateList,
                        "axisLabel":{
                            interval: 0,
                            rotate:20
                        }
                    },
                    yAxis: [
                        {type : 'value',
                        name:"DAU",
                        axisLabel : {
                            formatter: '{value}'
                        }
                            // splitLine: {          // 控制Y轴的分隔线(辅助线)
                            //     show: false      // 默认显示，属性show控制显示与
                            // }
                        },
                        {type : 'value',
                            name:"DAU占比",
                            axisLabel : {
                                formatter: '{value}%'
                            },
                            splitLine: {          // 控制Y轴的分隔线(辅助线)
                                show: false      // 默认显示，属性show控制显示与
                            }
                        }
                    ],
                    series: [{
                        name: 'DAU',
                        type: 'bar',
                        smooth:true,
                        data: dauList,
                        barWidth: 30,
                        itemStyle:{
                            normal:{
                                color:'cornflowerblue'
                            }
                        }
                    },
                    {
                        name: 'DAU占比',
                        type: 'line',
                        yAxisIndex: 1,
                        smooth:true,
                        data: dauRateList,
                        itemStyle:{
                            normal:{
                                color:'green'
                            }
                        }
                    }
                    ]
                };
              me.echarts = echarts.init(Ext.get("entMix").dom);
              me.echarts.setOption(mixOption);
           }
      }
});



