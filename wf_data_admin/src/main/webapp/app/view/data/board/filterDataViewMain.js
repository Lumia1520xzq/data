Ext.define('WF.view.data.board.filterDataViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-转化漏斗分析',
    xtype: 'filterDataViewMain',
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
            url:'data/filter/view/getList.do',
            autoload:false,
            fields:[
            "businessDate",
            "dauCount","gamedauCount","bettingCount","rechargeCount",
            "gamedauRate","bettingRate","rechargeRate","payRate",

            "registeredCount","dauRegistered","gamedauRegistered","bettingRegistered","rechargeRegistered",
            "dauRegisteredRate","gamedauRegisteredRate","bettingRegisteredRate","rechargeRegisteredRate","payRegisteredRate",

            "dauOlder","gamedauOlder","bettingOlder","rechargeOlder",
            "gamedauOlderRate","bettingOlderRate","rechargeOlderRate","payOlderRate"
            ]
        });
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getFilterChannels.do',
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
                name: 'searchDate',
                fieldLabel: '日期',
                xtype: 'datefield',
                format: 'Y-m-d',
                value: Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d")
            }
            ]
        });

        me.add({
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0 0 0 0;',
            items: [{
                title: '转化漏斗分析',align:'stretch',height:420,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                items:[
                    {width:"100%",height:20,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'},
                    {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                        {width:"47%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:'100%',height:80,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                    items:[
                                        {width:'40%',height:80,forceFit:true,bodyStyle:'border-width:0'},
                                        {width:'20%',height:80,forceFit:true,id:'filterRate0',bodyStyle:'border-width:0'},
                                        {width:'40%',height:80,forceFit:true,bodyStyle:'border-width:0'}
                                    ]
                                },
                                {width:'100%',height:320,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                    items:[
                                        {width:"90%",height:320,forceFit:true,id:"filter0",bodyStyle:'border-width:0'},
                                        {width:"9%",height:320,forceFit:true,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:'100%',height:40,forceFit:true,bodyStyle:'border-width:0'},
                                                {width:'100%',height:30,forceFit:true,bodyStyle:'border-width:0',id:"filterBtn0"},
                                                {width:'100%',height:35,forceFit:true,bodyStyle:'border-width:0'},
                                                {width:'100%',height:30,forceFit:true,bodyStyle:'border-width:0',id:"filterBtn1"},
                                                {width:'100%',height:35,forceFit:true,bodyStyle:'border-width:0'},
                                                {width:'100%',height:30,forceFit:true,bodyStyle:'border-width:0',id:"filterBtn2"},
                                                {width:'100%',height:120,forceFit:true,bodyStyle:'border-width:0'}
                                            ]
                                        }
                                    ]
                                }
                            ]
                        },
                        {width:"3%",height:400,xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true},
                        {width:"45%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:'filterBoardTitle0',width:'100%',height:80,forceFit:true,bodyStyle:'border-width:0'},
                                {id:'filterBoard0',width:'100%',height:320,forceFit:true,bodyStyle:'border-width:0'}
                            ]
                        }
                     ] }
                   ]
                },
                {
                    title: '新用户转化漏斗分析',align:'stretch',height:420,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                    items:[
                        {width:"100%",height:20,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'},
                        {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                            {width:"47%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                                items:[
                                    {width:'100%',height:80,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                        items:[
                                            {width:'40%',height:80,forceFit:true,bodyStyle:'border-width:0'},
                                            {width:'20%',height:80,forceFit:true,id:'filterRate1',bodyStyle:'border-width:0'},
                                            {width:'40%',height:80,forceFit:true,bodyStyle:'border-width:0'}
                                        ]
                                    },
                                    {width:'100%',height:320,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                        items:[
                                            {width:"90%",height:320,forceFit:true,id:"filter1",bodyStyle:'border-width:0'},
                                            {width:"9%",height:320,forceFit:true,layout:'vbox',bodyStyle:'border-width:0',
                                                items:[
                                                    {width:'100%',height:40,forceFit:true,bodyStyle:'border-width:0'},
                                                    {width:'100%',height:30,forceFit:true,bodyStyle:'border-width:0',id:"filterBtn3"},
                                                    {width:'100%',height:20,forceFit:true,bodyStyle:'border-width:0'},
                                                    {width:'100%',height:30,forceFit:true,bodyStyle:'border-width:0',id:"filterBtn4"},
                                                    {width:'100%',height:20,forceFit:true,bodyStyle:'border-width:0'},
                                                    {width:'100%',height:30,forceFit:true,bodyStyle:'border-width:0',id:"filterBtn5"},
                                                    {width:'100%',height:20,forceFit:true,bodyStyle:'border-width:0'},
                                                    {width:'100%',height:30,forceFit:true,bodyStyle:'border-width:0',id:"filterBtn6"},
                                                    {width:'100%',height:100,forceFit:true,bodyStyle:'border-width:0'}
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            },
                            {width:"3%",height:400,xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true},
                            {width:"45%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                                items:[
                                    {id:'filterBoardTitle1',width:'100%',height:80,forceFit:true,bodyStyle:'border-width:0'},
                                    {id:'filterBoard1',width:'100%',height:320,forceFit:true,bodyStyle:'border-width:0'}
                                ]
                            }
                        ] }
                    ]
                },

                {
                    title: '老用户转化漏斗分析',align:'stretch',height:420,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                    items:[
                        {width:"100%",height:20,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'},
                        {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                            {width:"47%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                                items:[
                                    {width:'100%',height:80,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                        items:[
                                            {width:'40%',height:80,forceFit:true,bodyStyle:'border-width:0'},
                                            {width:'20%',height:80,forceFit:true,id:'filterRate2',bodyStyle:'border-width:0'},
                                            {width:'40%',height:80,forceFit:true,bodyStyle:'border-width:0'}
                                        ]
                                    },
                                    {width:'100%',height:320,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                        items:[
                                            {width:"90%",height:320,forceFit:true,id:"filter2",bodyStyle:'border-width:0'},
                                            {width:"9%",height:320,forceFit:true,layout:'vbox',bodyStyle:'border-width:0',
                                                items:[
                                                    {width:'100%',height:40,forceFit:true,bodyStyle:'border-width:0'},
                                                    {width:'100%',height:30,forceFit:true,bodyStyle:'border-width:0',id:"filterBtn7"},
                                                    {width:'100%',height:35,forceFit:true,bodyStyle:'border-width:0'},
                                                    {width:'100%',height:30,forceFit:true,bodyStyle:'border-width:0',id:"filterBtn8"},
                                                    {width:'100%',height:35,forceFit:true,bodyStyle:'border-width:0'},
                                                    {width:'100%',height:30,forceFit:true,bodyStyle:'border-width:0',id:"filterBtn9"},
                                                    {width:'100%',height:120,forceFit:true,bodyStyle:'border-width:0'}
                                                ]
                                            }
                                        ]
                                    }
                                ]
                            },
                            {width:"3%",height:400,xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true},
                            {width:"45%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                                items:[
                                    {id:'filterBoardTitle2',width:'100%',height:80,forceFit:true,bodyStyle:'border-width:0'},
                                    {id:'filterBoard2',width:'100%',height:320,forceFit:true,bodyStyle:'border-width:0'}
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



        function fun1() {
            var last = store.getAt(store.getCount()-1);
            var lastDauCount = 0;
            var lastGamedauCount = 0;
            var lastBettingCount = 0;
            var lastRechargeCount = 0;
            var lastGamedauRate = 0;
            var lastBettingRate = 0;
            var lastRechargeRate = 0;
            var lastPayRate = 0;

            var	lastRegisteredCount = 0;
            var lastDauRegistered= 0;
            var	lastGamedauRegistered= 0;
            var	lastBettingRegistered= 0;
            var lastRechargeRegistered= 0;
            var	lastDauRegisteredRate= 0;
            var	lastGamedauRegisteredRate= 0;
            var lastBettingRegisteredRate= 0;
            var	lastRechargeRegisteredRate= 0;
            var	lastPayRegisteredRate= 0;

            var lastDauOlder = 0;
            var lastGamedauOlder = 0;
            var lastBettingOlder = 0;
            var lastRechargeOlder = 0;
            var lastGamedauOlderRate = 0;
            var lastBettingOlderRate = 0;
            var lastRechargeOlderRate = 0;
            var lastPayOlderRate = 0;


            if(last != undefined) {
                 lastDauCount = last.get("dauCount");
                 lastGamedauCount = last.get("gamedauCount");
                 lastBettingCount = last.get("bettingCount");
                 lastRechargeCount = last.get("rechargeCount");
                 lastGamedauRate = last.get("gamedauRate");
                 lastBettingRate = last.get("bettingRate");
                 lastRechargeRate = last.get("rechargeRate");
                 lastPayRate = last.get("payRate");

                lastRegisteredCount = last.get("registeredCount");
                lastDauRegistered = last.get("dauRegistered");
                lastGamedauRegistered = last.get("gamedauRegistered");
                lastBettingRegistered = last.get("bettingRegistered");
                lastRechargeRegistered = last.get("rechargeRegistered");
                lastDauRegisteredRate = last.get("dauRegisteredRate");
                lastGamedauRegisteredRate = last.get("gamedauRegisteredRate");
                lastBettingRegisteredRate = last.get("bettingRegisteredRate");
                lastRechargeRegisteredRate = last.get("rechargeRegisteredRate");
                lastPayRegisteredRate = last.get("payRegisteredRate");


                lastDauOlder = last.get("dauOlder");
                lastGamedauOlder = last.get("gamedauOlder");
                lastBettingOlder = last.get("bettingOlder");
                lastRechargeOlder = last.get("rechargeOlder");
                lastGamedauOlderRate = last.get("gamedauOlderRate");
                lastBettingOlderRate = last.get("bettingOlderRate");
                lastRechargeOlderRate = last.get("rechargeOlderRate");
                lastPayOlderRate = last.get("payOlderRate");
            }
            Ext.get('filterRate0').dom.innerHTML = "<div align='center' style='line-height:35px;font-size:16px;cursor:pointer'>活跃>充值转化率<strong style='font-size:24px;color:#3c94db'><br/>"+lastPayRate +"%</strong></div>";
            Ext.get('filterRate1').dom.innerHTML = "<div align='center' style='line-height:35px;font-size:16px;cursor:pointer'>活跃>充值转化率<strong style='font-size:24px;color:#3c94db'><br/>"+lastPayRegisteredRate +"%</strong></div>";
            Ext.get('filterRate2').dom.innerHTML = "<div align='center' style='line-height:35px;font-size:16px;cursor:pointer'>活跃>充值转化率<strong style='font-size:24px;color:#3c94db'><br/>"+lastPayOlderRate +"%</strong></div>";

            Ext.get('filterBtn0').dom.innerHTML = "<div align='center' style='height:30px;line-height:30px;background-color:darkorange;color:white;cursor:pointer;'>"+lastGamedauRate+"%</strong></div>";
            Ext.get('filterBtn1').dom.innerHTML = "<div align='center' style='height:30px;line-height:30px;background-color:darkorange;color:white;cursor:pointer;'>"+lastBettingRate+"%</strong></div>";
            Ext.get('filterBtn2').dom.innerHTML = "<div align='center' style='height:30px;line-height:30px;background-color:darkorange;color:white;cursor:pointer;'>"+lastRechargeRate+"%</strong></div>";

            Ext.get('filterBtn3').dom.innerHTML = "<div align='center' style='height:30px;line-height:30px;background-color:darkorange;color:white;cursor:pointer;'>"+lastDauRegisteredRate+"%</strong></div>";
            Ext.get('filterBtn4').dom.innerHTML = "<div align='center' style='height:30px;line-height:30px;background-color:darkorange;color:white;cursor:pointer;'>"+lastGamedauRegisteredRate+"%</strong></div>";
            Ext.get('filterBtn5').dom.innerHTML = "<div align='center' style='height:30px;line-height:30px;background-color:darkorange;color:white;cursor:pointer;'>"+lastBettingRegisteredRate+"%</strong></div>";
            Ext.get('filterBtn6').dom.innerHTML = "<div align='center' style='height:30px;line-height:30px;background-color:darkorange;color:white;cursor:pointer;'>"+lastRechargeRegisteredRate+"%</strong></div>";

            Ext.get('filterBtn7').dom.innerHTML = "<div align='center' style='height:30px;line-height:30px;background-color:darkorange;color:white;cursor:pointer;'>"+lastGamedauOlderRate+"%</strong></div>";
            Ext.get('filterBtn8').dom.innerHTML = "<div align='center' style='height:30px;line-height:30px;background-color:darkorange;color:white;cursor:pointer;'>"+lastBettingOlderRate+"%</strong></div>";
            Ext.get('filterBtn9').dom.innerHTML = "<div align='center' style='height:30px;line-height:30px;background-color:darkorange;color:white;cursor:pointer;'>"+lastRechargeOlderRate+"%</strong></div>";


            var option = [
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
                        left: '3%',
                        right: '7%',
                        top:'1%',
                        containLabel: true
                    },
                    xAxis: {
                        type : 'value',
                        show :false,
                        // boundaryGap : false
                        axisLine: {
                            show: false
                        },
                        axisTick: {
                            show: false
                        }
                    },
                    yAxis: {
                        type : 'category',
                        // axisLabel : {
                        //     formatter: '{value}%'
                        // }
                        data:['充值','投注','进入游戏','DAU'],
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
                        // name: '',
                        type: 'bar',
                        smooth:true,
                        data:[lastRechargeCount,lastBettingCount,lastGamedauCount,lastDauCount],
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
                },
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
                        left: '3%',
                        right: '7%',
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
                        data:['充值','投注','进入游戏','DAU','注册'],
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
                        data:[lastRechargeRegistered,lastBettingRegistered,lastGamedauRegistered,lastDauRegistered,lastRegisteredCount],
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
                },

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
                        left: '3%',
                        right: '7%',
                        top:'1%',
                        containLabel: true
                    },
                    xAxis: {
                        type : 'value',
                        show :false,
                        // boundaryGap : false
                        axisLine: {
                            show: false
                        },
                        axisTick: {
                            show: false
                        }
                    },
                    yAxis: {
                        type : 'category',
                        // axisLabel : {
                        //     formatter: '{value}%'
                        // }
                        data:['充值','投注','进入游戏','DAU'],
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
                        // name: '',
                        type: 'bar',
                        smooth:true,
                        data:[lastRechargeOlder,lastBettingOlder,lastGamedauOlder,lastDauOlder],
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
                }
            ];
            for (var p=0;p<option.length;p++) {
                me.echarts = echarts.init(Ext.get("filter"+p).dom);
                me.echarts.setOption(option[p]);
            }

            var businessDate=[];

            var gamedauRate=[];
            var bettingRate=[];
            var rechargeRate=[];
            var payRate=[];

            var	dauRegisteredRate=[];
            var	gamedauRegisteredRate=[];
            var bettingRegisteredRate=[];
            var	rechargeRegisteredRate=[];
            var	payRegisteredRate=[];

            var gamedauOlderRate=[];
            var bettingOlderRate=[];
            var rechargeOlderRate=[];
            var payOlderRate=[];


            for(var i=0;i<store.getCount();i++){
            var re=store.getAt(i);
            var d = re.get('businessDate');
            var x = d.indexOf('-');
            businessDate[i] = d.substring(x+1);

            gamedauRate[i]=re.get('gamedauRate');
            bettingRate[i]=re.get('bettingRate');
            rechargeRate[i]=re.get('rechargeRate');
            payRate[i]=re.get('payRate');

            dauRegisteredRate[i]=re.get('dauRegisteredRate');
            gamedauRegisteredRate[i]=re.get('gamedauRegisteredRate');
            bettingRegisteredRate[i]=re.get('bettingRegisteredRate');
            rechargeRegisteredRate[i]=re.get('rechargeRegisteredRate');
            payRegisteredRate[i]=re.get('payRegisteredRate');

            gamedauOlderRate[i]=re.get('gamedauOlderRate');
            bettingOlderRate[i]=re.get('bettingOlderRate');
            rechargeOlderRate[i]=re.get('rechargeOlderRate');
            payOlderRate[i]=re.get('payOlderRate');
            }
            //初始化数据
            var title = "活跃>充值转化率";
            var option1 = fun2(businessDate,payRate,title);
            me.echarts = echarts.init(Ext.get("filterBoard0").dom);
            me.echarts.setOption(option1);
            Ext.get('filterBoardTitle0').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";

            var option2 = fun2(businessDate,payRegisteredRate,title);
            me.echarts = echarts.init(Ext.get("filterBoard1").dom);
            me.echarts.setOption(option2);
            Ext.get('filterBoardTitle1').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";

            var option3 = fun2(businessDate,payOlderRate,title);
            me.echarts = echarts.init(Ext.get("filterBoard2").dom);
            me.echarts.setOption(option3);
            Ext.get('filterBoardTitle2').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";

            Ext.get("filterRate0").on("click", function(){
                var option = fun2(businessDate,payRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard0").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle0').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });

            Ext.get("filterRate1").on("click", function(){
                var option = fun2(businessDate,payRegisteredRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard1").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle1').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });

            Ext.get("filterRate2").on("click", function(){
                var option = fun2(businessDate,payOlderRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard2").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle2').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });


            //按钮点击事件
            Ext.get("filterBtn0").on("click", function(){
                var title = "进入游戏>DAU转化率";
                var option = fun2(businessDate,gamedauRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard0").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle0').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });
            Ext.get("filterBtn1").on("click", function(){
                var title = "投注>进入游戏转化率";
                var option = fun2(businessDate,bettingRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard0").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle0').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });
            Ext.get("filterBtn2").on("click", function(){
                var title = "充值>投注转化率";
                var option = fun2(businessDate,rechargeRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard0").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle0').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });


            Ext.get("filterBtn3").on("click", function(){
                var title = "DAU>注册转化率";
                var option = fun2(businessDate,dauRegisteredRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard1").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle1').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });
            Ext.get("filterBtn4").on("click", function(){
                var title = "进入游戏>DAU转化率";
                var option = fun2(businessDate,gamedauRegisteredRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard1").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle1').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });

            Ext.get("filterBtn5").on("click", function(){
                var title = "投注>进入游戏转化率";
                var option = fun2(businessDate,bettingRegisteredRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard1").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle1').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });
            Ext.get("filterBtn6").on("click", function(){
                var title = "充值>投注转化率";
                var option = fun2(businessDate,rechargeRegisteredRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard1").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle1').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });


            Ext.get("filterBtn7").on("click", function(){
                var title = "进入游戏>DAU转化率";
                var option = fun2(businessDate,gamedauOlderRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard2").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle2').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });
            Ext.get("filterBtn8").on("click", function(){
                var title = "投注>进入游戏转化率";
                var option = fun2(businessDate,bettingOlderRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard2").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle2').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });
            Ext.get("filterBtn9").on("click", function(){
                var title = "充值>投注转化率";
                var option = fun2(businessDate,rechargeOlderRate,title);
                me.echarts = echarts.init(Ext.get("filterBoard2").dom);
                me.echarts.setOption(option);
                Ext.get('filterBoardTitle2').dom.innerHTML = "<div align='center' style='line-height:70px;font-size:25px'>"+title+"</strong></div>";
            });
        }

        function fun2(businessDate,data,name){
            var singleOption = {
                tooltip: {trigger: 'axis',
                    formatter: function (params) {
                        var str='';
                        for(var i = 0; i < params.length; i++){
                            str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%' ;
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
                    left:'5%',
                    y:'2.8%'
                },
                xAxis: {
                    type : 'category',
                    boundaryGap : false,
                    data: businessDate
                },
                yAxis: {
                    type : 'value',
                    axisLabel : {
                        formatter: '{value}%'
                    },
                    // min: function(value) {
                    //      return value.min;
                    // }
                },
                series: [{
                    name: name,
                    type: 'line',
                    smooth:true,
                    data: data,
                    itemStyle: {
                        normal: {
                            color: "cornflowerblue",
                            lineStyle: {
                                color: "cornflowerblue"
                            }
                        }
                    }
                }]
            };
            return singleOption;
        }
    }
});

