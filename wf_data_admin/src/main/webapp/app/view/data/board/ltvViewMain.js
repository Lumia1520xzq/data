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
                   "200001","300001","100002"
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
                title: '用户LTV',align:'stretch',height:400,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                items:[
                    {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                        {width:"50%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:'100%',height:40,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                    items:[
                                        {id:'ltvTitle0',width:'20%',height:40,forceFit:true},
                                        {width:'60%',height:40,forceFit:true},
                                        {id:'ltvDate',width:'20%',height:40,forceFit:true}
                                    ]
                                },
                                {width:'100%',height:360,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                    items:[
                                        {id:"ltvBoard0",width:"95%",height:360,forceFit:true,bodyStyle:'border-width:0'},
                                        {width:"5%",height:360,forceFit:true,bodyStyle:'border-width:0'}
                                    ]
                                }
                            ]
                        },
                        {width:"48%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:'ltvTitle1',width:'100%',height:60,forceFit:true,bodyStyle:'border-width:0'},
                                {id:'ltvBoard1',width:'100%',height:340,forceFit:true,bodyStyle:'border-width:0'}
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
            var doyo = store.getAt(0).get("100002");
            var ios = store.getAt(0).get("300001");
            var android = store.getAt(0).get("200001");
            var zhuazhuale = store.getAt(0).get("100018");
            var woqu = store.getAt(0).get("100016");
            var quanmin = store.getAt(0).get("100015");
            var okooo = store.getAt(0).get("100006");
            var jinshan = store.getAt(0).get("100001");
            var total = store.getAt(0).get("1");

            var userLtv = [];
            var parentId = [];

            if(total.length != 0){
                var index = total.length-1;
                console.log(total[index].userLtv);
                console.log(total[index].parentId);
            }

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
                };
            me.echarts = echarts.init(Ext.get("ltvBoard0").dom);
            me.echarts.setOption(option);
        }



        function getLtv(data) {
            if(data.length != 0) {
                var index = total.length-1;
                return total[index].userLtv;
            }
        }


    }
});

