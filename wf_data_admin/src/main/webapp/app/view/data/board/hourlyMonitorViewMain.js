Ext.define('WF.view.data.board.hourlyMonitorViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-实时预警',
    xtype: 'hourlyMonitorViewMain',
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
            url:'data/hour/view/getList.do',
            autoload:false,
            fields:["todData","yesData","historyData"]
        });
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
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
                    {id:"hourlyBoard0",width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                       items:[
                           {id:'hourlyTitle0',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                           {id:'hourlyData0',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                           {id:'hourlyRate0',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"hourlyBoard1",width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'hourlyTitle1',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyData1',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyRate1',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"hourlyBoard2",width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'hourlyTitle2',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyData2',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyRate2',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"hourlyBoard3",width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'hourlyTitle3',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyData3',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyRate3',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"hourlyBoard4",width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'hourlyTitle4',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyData4',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyRate4',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"hourlyBoard5",width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'hourlyTitle5',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyData5',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyRate5',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    },
                    {id:"hourlyBoard6",width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                        items:[
                            {id:'hourlyTitle6',width:"100%",height:35,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyData6',width:"100%",height:40,forceFit:true,bodyStyle:'border-width:0'},
                            {id:'hourlyRate6',width:"100%",height:85,forceFit:true,bodyStyle:'border-width:0'}
                        ]
                    }
                   ]
                },
                {
                    title: '',align:'stretch',height:15,width:"99%",xtype:"panel",forceFit:true,bodyStyle:'border-width:0'
                },
                {
                    id:'hourlyBoard',align:'stretch',height:500,width:"99%",xtype:"panel",bodyStyle:'border-color:black',forceFit:true
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
            var todUserCount = [];
            var todRechargeCount = [];
            var todRechargeAmount = [];
            var todNewUsers = [];
            var todBettingAmount = [];
            var todDiffAmount =  [];


            for (var i=0;i<todSize;i++){
                todBusinessHour[i] = todData[i].businessHour;
                todDau[i] = todData[i].hourDau;
                todUserCount[i] = todData[i].hourUserCount;
                todRechargeCount[i] = todData[i].hourRechargeCount;
                todRechargeAmount[i] = todData[i].hourRechargeAmount;
                todNewUsers[i] = todData[i].hourNewUsers;
                todBettingAmount[i] = todData[i].hourBettingAmount;
                todDiffAmount[i] = todData[i].hourDiffAmount;
            }

            var yesBusinessHour = [];
            var yesDau=[];
            var yesUserCount=[];
            var yesRechargeCount = [];
            var yesRechargeAmount = [];
            var yesNewUsers = [];
            var yesBettingAmount = [];
            var yesDiffAmount =  [];

            for (var p=0;p<yesData.length;p++){
                yesBusinessHour[p] = yesData[p].businessHour;
                yesDau[p] = yesData[p].hourDau;
                yesUserCount[p] = yesData[p].hourUserCount;

                yesRechargeCount[p] = yesData[p].hourRechargeCount;
                yesRechargeAmount[p] = yesData[p].hourRechargeAmount;
                yesNewUsers[p] = yesData[p].hourNewUsers;
                yesBettingAmount[p] = yesData[p].hourBettingAmount;
                yesDiffAmount[p] = yesData[p].hourDiffAmount;
            }


            var hisBusinessHour = [];
            var hisDau=[];
            var hisUserCount=[];
            var hisRechargeCount = [];
            var hisRechargeAmount = [];
            var hisNewUsers = [];
            var hisBettingAmount = [];
            var hisDiffAmount =  [];

            for (var d=0;d<yesData.length;d++){
                hisBusinessHour[d] = hisData[d].businessHour;
                hisDau[d] = hisData[d].hourDau;
                hisUserCount[d] = hisData[d].hourUserCount;
                hisRechargeCount[d] = hisData[d].hourRechargeCount;
                hisRechargeAmount[d] = hisData[d].hourRechargeAmount;
                hisNewUsers[d] = hisData[d].hourNewUsers;
                hisBettingAmount[d] = hisData[d].hourBettingAmount;
                hisDiffAmount[d] = hisData[d].hourDiffAmount;
            }

            var titles = ["DAU","投注人数", "充值人数","充值金额","新增用户数","投注流水","投注流水差"];
            fun2(titles[0],yesBusinessHour,todDau,yesDau,hisDau);

            for(var k=0;k<titles.length;k++){
                Ext.get('hourlyTitle'+k).dom.innerHTML = "<div align='center' style='line-height:35px;font-size:20px'>"+titles[k]+"</strong></div>";
            }

            if(todData.length != 0){
                var dataCss = "<div align='center'><strong style='font-size:24px;color:#3c94db;cursor:pointer;line-height:35px;'>";
                var end = "</strong></div>";
                Ext.get('hourlyData0').dom.innerHTML = dataCss + format(todData[index].dau) + end;
                Ext.get('hourlyData1').dom.innerHTML = dataCss + format(todData[index].userCount) +  end;
                Ext.get('hourlyData2').dom.innerHTML = dataCss + format(todData[index].rechargeCount) + end;
                Ext.get('hourlyData3').dom.innerHTML = dataCss + format(todData[index].rechargeAmount) + end;
                Ext.get('hourlyData4').dom.innerHTML = dataCss + format(todData[index].newUsers) + end;
                Ext.get('hourlyData5').dom.innerHTML = dataCss + format(todData[index].bettingAmount) + end;
                Ext.get('hourlyData6').dom.innerHTML = dataCss + format(todData[index].diffAmount) + end;

                var rateCss = "<div align='center' style='line-height:25px;font-size:14px'>";
                Ext.get('hourlyRate0').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayDauRate) +"<br/>" + "周环比:"+ judge(todData[index].weekDauRate) + end;
                Ext.get('hourlyRate1').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayUserCountRate) +"<br/>" + "周环比:"+ judge(todData[index].weekUserCountRate) + end;
                Ext.get('hourlyRate2').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayRechargeCountRate) +"<br/>" + "周环比:"+ judge(todData[index].weekRechargeCountRate) + end;
                Ext.get('hourlyRate3').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayRechargeAmountRate) +"<br/>" + "周环比:"+judge(todData[index].weekRechargeAmountRate) + end;
                Ext.get('hourlyRate4').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayNewUsersRate) +"<br/>" + "周环比:"+judge(todData[index].weekNewUsersRate) + end;
                Ext.get('hourlyRate5').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayBettingAmountRate) +"<br/>" + "周环比:"+judge(todData[index].weekBettingAmountRate) + end;
                Ext.get('hourlyRate6').dom.innerHTML = rateCss + "日环比:"+ judge(todData[index].dayDiffAmountRate) +"<br/>" + "周环比:"+ judge(todData[index].weekDiffAmountRate) + end;
            }
            else {
                var dataCss = "<div align='center'><strong style='font-size:24px;color:#3c94db;cursor:pointer;line-height:35px;'>";
                var end = "</strong></div>";
                Ext.get('hourlyData0').dom.innerHTML = dataCss + 0 + end;
                Ext.get('hourlyData1').dom.innerHTML = dataCss + 0 + end;
                Ext.get('hourlyData2').dom.innerHTML = dataCss + 0 + end;
                Ext.get('hourlyData3').dom.innerHTML = dataCss + 0 + end;
                Ext.get('hourlyData4').dom.innerHTML = dataCss + 0 + end;
                Ext.get('hourlyData5').dom.innerHTML = dataCss + 0 + end;
                Ext.get('hourlyData6').dom.innerHTML = dataCss + 0 + end;

                var rateCss = "<div align='center' style='line-height:25px;font-size:14px'>";
                Ext.get('hourlyRate0').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%"+ end;
                Ext.get('hourlyRate1').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
                Ext.get('hourlyRate2').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
                Ext.get('hourlyRate3').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
                Ext.get('hourlyRate4').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
                Ext.get('hourlyRate5').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
                Ext.get('hourlyRate6').dom.innerHTML = rateCss + "日环比:0%" +"<br/>" + "周环比:0%" + end;
            }

            Ext.get("hourlyBoard0").on("click",function(){
                 fun2(titles[0],yesBusinessHour,todDau,yesDau,hisDau);
            });
            Ext.get("hourlyBoard1").on("click",function(){
                fun2(titles[1],yesBusinessHour,todUserCount,yesUserCount,hisUserCount);
            });
            Ext.get("hourlyBoard2").on("click",function(){
                fun2(titles[2],yesBusinessHour,todRechargeCount,yesRechargeCount,hisRechargeCount);
            });
            Ext.get("hourlyBoard3").on("click",function(){
                fun2(titles[3],yesBusinessHour,todRechargeAmount,yesRechargeAmount,hisRechargeAmount);
            });

            Ext.get("hourlyBoard4").on("click",function(){
                fun2(titles[4],yesBusinessHour,todNewUsers,yesNewUsers,hisNewUsers);
            });

            Ext.get("hourlyBoard5").on("click",function(){
                fun2(titles[5],yesBusinessHour,todBettingAmount,yesBettingAmount,hisBettingAmount);
            });
            Ext.get("hourlyBoard6").on("click",function(){
                fun2(titles[6],yesBusinessHour,todDiffAmount,yesDiffAmount,hisDiffAmount);
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
            me.echarts = echarts.init(Ext.get("hourlyBoard").dom);
            me.echarts.setOption(option);
        }

        function format(value){
            if(value >= 0){
                return Ext.util.Format.number(value,"0,000");
            }
            return "-"+Ext.util.Format.number(Math.abs(value),"0,000");
        }
    }
});

