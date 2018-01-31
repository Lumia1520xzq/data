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
            fields:[]
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
                                {width:'100%',height:80,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                    items:[
                                        {id:'ltvTitle0',width:'20%',height:80,forceFit:true},
                                        {width:'60%',height:80,forceFit:true},
                                        {id:'ltvDate',width:'20%',height:80,forceFit:true}
                                    ]
                                },
                                {width:'100%',height:320,forceFit:true,layout:'hbox',bodyStyle:'border-width:0',
                                    items:[
                                        {id:"ltvBoard0",width:"95%",height:320,forceFit:true,bodyStyle:'border-width:0'},
                                        {width:"5%",height:320,forceFit:true,bodyStyle:'border-width:0'}
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
            Ext.get('filterRate0').dom.innerHTML = "<div align='center' style='line-height:35px;font-size:16px;cursor:pointer'>(DAU-->充值) 转化率<strong style='font-size:24px;color:#3c94db'><br/>"+lastPayRate +"%</strong></div>";
            Ext.get('filterRate1').dom.innerHTML = "<div align='center' style='line-height:35px;font-size:16px;cursor:pointer'>(DAU-->充值) 转化率<strong style='font-size:24px;color:#3c94db'><br/>"+lastPayRegisteredRate +"%</strong></div>";
            Ext.get('filterRate2').dom.innerHTML = "<div align='center' style='line-height:35px;font-size:16px;cursor:pointer'>(DAU-->充值) 转化率<strong style='font-size:24px;color:#3c94db'><br/>"+lastPayOlderRate +"%</strong></div>";
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

            me.echarts = echarts.init(Ext.get("filter"+p).dom);
            me.echarts.setOption(option[p]);


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

        }


    }
});

