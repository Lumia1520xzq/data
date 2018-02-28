Ext.define('WF.view.data.board.GameMonitorViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-游戏数据每小时监控',
    xtype: 'GameMonitorViewMain',
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
            url:'data/game/view/getList.do',
            autoload:false,
            fields:["todData","yesData","historyData"]
        });
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });
        var gameTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'game_type'
            }
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
                name: 'gameType',
                fieldLabel: '游戏',
                xtype: 'combobox',
                store: gameTypeStore,
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false
            },
            {
                name: 'businessDate',
                fieldLabel: '日期',
                xtype: 'datefield',
                format: 'Y-m-d',
                value: Ext.util.Format.date(new Date(),"Y-m-d")
            }
            ]
        });

        me.add({
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0',
            items: [{
                title: '当天累计数据',align:'stretch',height:160,width:"99%",xtype:"panel",layout:'hbox',bodyStyle:'border-color:black',forceFit:true,
                items:[
                    {id:"gameBoard0",width:"16.5%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                       items:[
                           {id:'gameTitle0',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                           {id:'gameData0',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                           {id:'gameRate0',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"gameBoard1",width:"16.5%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'gameTitle1',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'gameData1',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'gameRate1',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"gameBoard2",width:"16.5%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'gameTitle2',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'gameData2',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'gameRate2',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"gameBoard3",width:"16.5%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'gameTitle3',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'gameData3',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'gameRate3',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"gameBoard4",width:"16.5%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'gameTitle4',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'gameData4',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'gameRate4',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"gameBoard5",width:"16.5%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'gameTitle5',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'gameData5',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'gameRate5',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    }
                   ]
                },
                {
                    title: '',align:'stretch',height:15,width:"99%",xtype:"panel",forceFit:true,bodyStyle:'border-width:0'
                },
                {
                    id:'gameBoard',align:'stretch',height:500,width:"99%",xtype:"panel",bodyStyle:'border-color:black',forceFit:true
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
            var todData = store.getAt(0).get("todData");
            var yesData = store.getAt(0).get("yesData");
            var hisData = store.getAt(0).get("historyData");

            var todSize = todData.length;
            var index = todSize-1;

            var todBusinessHour = [];
            var todDau = [];
            var todBettingUserCount = [];
            var todBettingCount = [];
            var todBettingAmount = [];
            var todMoneyGap = [];
            var todReturnRate =  [];

            for (var i=0;i<todSize;i++){
                todBusinessHour[i] = todData[i].businessHour;
                todDau[i] = todData[i].hourDau;
                todBettingUserCount[i] = todData[i].hourBettingUserCount;
                todBettingCount[i] = todData[i].hourBettingCount;
                todBettingAmount[i] = todData[i].hourBettingAmount;
                todMoneyGap[i] = todData[i].hourMoneyGap;
                todReturnRate[i] = todData[i].hourReturnRate;
            }

            var yesBusinessHour = [];
            var yesDau = [];
            var yesBettingUserCount = [];
            var yesBettingCount = [];
            var yesBettingAmount = [];
            var yesMoneyGap = [];
            var yesReturnRate =  [];

            for (var p=0;p<yesData.length;p++){
                yesBusinessHour[p] = yesData[p].businessHour;
                yesDau[p] = yesData[p].hourDau;
                yesBettingUserCount[p] = yesData[p].hourBettingUserCount;
                yesBettingCount[p] = yesData[p].hourBettingCount;
                yesBettingAmount[p] = yesData[p].hourBettingAmount;
                yesMoneyGap[p] = yesData[p].hourMoneyGap;
                yesReturnRate[p] = yesData[p].hourReturnRate;
            }

            var hisBusinessHour = [];
            var hisDau = [];
            var hisBettingUserCount = [];
            var hisBettingCount = [];
            var hisBettingAmount = [];
            var hisMoneyGap = [];
            var hisReturnRate =  [];

            for (var d=0;d<yesData.length;d++) {
                hisBusinessHour[d] = hisData[d].businessHour;
                hisDau[d] = hisData[d].hourDau;
                hisBettingUserCount[d] = hisData[d].hourBettingUserCount;
                hisBettingCount[d] = hisData[d].hourBettingCount;
                hisBettingAmount[d] = hisData[d].hourBettingAmount;
                hisMoneyGap[d] = hisData[d].hourMoneyGap;
                hisReturnRate[d] = hisData[d].hourReturnRate;
            }

            var titles = ["DAU","投注人数", "投注笔数","投注流水","流水差","返奖率"];
            fun2(titles[0],yesBusinessHour,todDau,yesDau,hisDau);

            for(var k=0;k<titles.length;k++){
                Ext.get('gameTitle'+k).dom.innerHTML = "<div align='center' style='line-height:35px;font-size:20px'>"+titles[k]+"</strong></div>";
            }

            if(todData.length != 0){
                var dataCss = "<div align='center'><strong style='font-size:24px;color:#3c94db;cursor:pointer;line-height:35px;'>";
                var end = "</strong></div>";
                Ext.get('gameData0').dom.innerHTML = dataCss + format(todData[index].dau) + end;
                Ext.get('gameData1').dom.innerHTML = dataCss + format(todData[index].bettingUserCount) +  end;
                Ext.get('gameData2').dom.innerHTML = dataCss + format(todData[index].bettingCount) + end;
                Ext.get('gameData3').dom.innerHTML = dataCss + format(todData[index].bettingAmount) + end;
                Ext.get('gameData4').dom.innerHTML = dataCss + format(todData[index].moneyGap) + end;
                Ext.get('gameData5').dom.innerHTML = dataCss + todData[index].returnRate + "%" + end;

                var rateCss = "<div align='center' style='line-height:25px;font-size:14px'>";
                Ext.get('gameRate0').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayDauRate) +"<br/>" + "周环比:"+ judge(todData[index].weekDauRate) + end;
                Ext.get('gameRate1').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayUserCountRate) +"<br/>" + "周环比:"+ judge(todData[index].weekUserCountRate) + end;
                Ext.get('gameRate2').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayBettingCountRate) +"<br/>" + "周环比:"+ judge(todData[index].weekBettingCountRate) + end;
                Ext.get('gameRate3').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayBettingAmountRate) +"<br/>" + "周环比:"+judge(todData[index].weekBettingAmountRate) + end;
                Ext.get('gameRate4').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayMoneyGapRate) +"<br/>" + "周环比:"+judge(todData[index].weekMoneyGapRate) + end;
                Ext.get('gameRate5').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayReturnRate) +"<br/>" + "周环比:"+judge(todData[index].weekReturnRate) + end;
            }

            else {
                var dataCss = "<div align='center'><strong style='font-size:24px;color:#3c94db;cursor:pointer;line-height:35px;'>";
                var end = "</strong></div>";
                Ext.get('gameData0').dom.innerHTML = dataCss + 0 + end;
                Ext.get('gameData1').dom.innerHTML = dataCss + 0 + end;
                Ext.get('gameData2').dom.innerHTML = dataCss + 0 + end;
                Ext.get('gameData3').dom.innerHTML = dataCss + 0 + end;
                Ext.get('gameData4').dom.innerHTML = dataCss + 0 + end;
                Ext.get('gameData5').dom.innerHTML = dataCss + 0 + "%" + end;

                var rateCss = "<div align='center' style='line-height:25px;font-size:14px'>";
                Ext.get('gameRate0').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%"+ end;
                Ext.get('gameRate1').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
                Ext.get('gameRate2').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
                Ext.get('gameRate3').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
                Ext.get('gameRate4').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
                Ext.get('gameRate5').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
            }


            Ext.get("gameBoard0").on("click",function(){
                 fun2(titles[0],yesBusinessHour,todDau,yesDau,hisDau);
            });
            Ext.get("gameBoard1").on("click",function(){
                fun2(titles[1],yesBusinessHour,todBettingUserCount,yesBettingUserCount,hisBettingUserCount);
            });
            Ext.get("gameBoard2").on("click",function(){
                fun2(titles[2],yesBusinessHour,todBettingCount,yesBettingCount,hisBettingCount);
            });
            Ext.get("gameBoard3").on("click",function(){
                fun2(titles[3],yesBusinessHour,todBettingAmount,yesBettingAmount,hisBettingAmount);
            });

            Ext.get("gameBoard4").on("click",function(){
                fun2(titles[4],yesBusinessHour,todMoneyGap,yesMoneyGap,hisMoneyGap);
            });

            Ext.get("gameBoard5").on("click",function(){
                fun2(titles[5],yesBusinessHour,todReturnRate,yesReturnRate,hisReturnRate);
            });

        }

        function judge(value){
            if(value.startsWith("-")){
             return "<span style='color:green'>"+value+"▼"+"</span>"
            }
            return "<span style='color:red'>"+value+"▲"+"</span>"
        }


        function fun2(title,hours,todData,yesData,hisData){
            var option =
                {
                    title: {text:title,left:'center',
                        textStyle:{//标题内容的样式
                            fontStyle:'normal',//主标题文字字体风格，默认normal，有italic(斜体),oblique(斜体)
                            fontWeight:"bold",//可选normal(正常)，bold(加粗)，bolder(加粗)，lighter(变细)，100|200|300|400|500...
                            fontFamily:"san-serif",//主题文字字体，默认微软雅黑
                            fontSize:22//主题文字字体大小，默认为18px
                        }},
                    tooltip: {trigger: 'axis',
                        formatter: function (params){
                            var str='';
                            for(var i = 0; i < params.length; i++) {
                                var j = params[i].name;
                                if(params[i].value != undefined) {
                                    var endStr = Number(j) + 1;
                                    if (endStr < 10) {
                                        endStr = "0" + endStr;
                                    }
                                    var k = j + "时-" + endStr + "时";
                                    str += params[i].seriesName + ':' + format(params[i].value) + '&nbsp;&nbsp;' + '时间:' + k + "<br/>";
                                }
                            }
                            return str;
                        }
                    },
                    legend: {
                        data:['今日'+title,'昨日'+title,'历史七日均值'],
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
                        data: hours
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }
                    },
                    series: [
                        {
                            name: '今日'+title,
                            type: 'line',
                            smooth:true,
                            data: todData,
                            itemStyle:{normal:{color:'cornflowerblue'}}
                        },
                        {
                            name: '昨日'+title,
                            type: 'line',
                            smooth:true,
                            data: yesData,
                            itemStyle:{normal:{
                                lineStyle:{
                                    type:"dotted"
                                }
                            }}
                        },
                        {
                            name: "历史七日均值",
                            type: 'line',
                            smooth:true,
                            data: hisData,
                            itemStyle:{normal:{
                                lineStyle:{
                                    type:"dotted"
                                },
                                color:'green'
                            }}
                        }
                    ]
                };
            me.echarts = echarts.init(Ext.get("gameBoard").dom);
            me.echarts.setOption(option);
        }

        function format(value){
            if(value >= 0){
                if(String(value).indexOf(".")>-1){
                    return Ext.util.Format.number(value,"0.00")+"%";
                }else{
                    return Ext.util.Format.number(value,"0,000");
                }
            }
            if(String(value).indexOf(".")>-1){
                return "-"+Ext.util.Format.number(Math.abs(value),"0.00")+"%";
            }
            return "-"+Ext.util.Format.number(Math.abs(value),"0,000");
        }
    }
});

