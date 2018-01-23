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
            url:'data/board/view/getList.do',
            autoload:false,
            fields: []
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
                name: 'month',
                fieldLabel: '开始时间',
                xtype: 'datefield',
                format: 'Y-m',
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
                    title: '月度指标监控',height:300,align:'stretch',width:"100%", xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:"monthTitle0",width:"100%",height:40,forceFit:true},
                                {id:"monthKpi0",width:"100%",height:260,forceFit:true}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:"monthTitle1",width:"100%",height:40,forceFit:true},
                                {id:"monthKpi1",width:"100%",height:260,forceFit:true}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {id:"monthTitle2",width:"100%",height:40,forceFit:true},
                                {id:"monthKpi2",width:"100%",height:260,forceFit:true}
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
            console.log(123);
            // var businessDate=[];
            // var dau=[];
            // var rechargeAmount=[];
            // var rechargeCount=[];
            // var newUsers=[];
            // var userCount=[];
            // var bettingRate=[];
            // var dauPayRate=[];
            // var bettingPayRate=[];
            // var userBettingRate=[];
            // var bettingAmount=[];
            // var resultRate=[];
            // var payArpu=[];
            // var payArppu=[];
            //
            // var usersDayRetention=[];
            // var dayRetention=[];
            // var usersRate=[];
            // var totalCost=[];
            // var costRate=[];
            // for(var i=0;i<store.getCount();i++){
            //     var re=store.getAt(i);
            //     var d = re.get('businessDate');
            //     var x = d.indexOf('-');
            //     businessDate[i] = d.substring(x+1);
            //     dau[i]=re.get('dau');
            //     rechargeAmount[i]=re.get('rechargeAmount');
            //     rechargeCount[i]=re.get('rechargeCount');
            //     newUsers[i]=re.get('newUsers');
            //     userCount[i]=re.get('userCount');
            //     bettingRate[i]=re.get('bettingRate');
            //     dauPayRate[i]=re.get('dauPayRate');
            //     bettingPayRate[i]=re.get('bettingPayRate');
            //     userBettingRate[i]=re.get('userBettingRate');
            //     bettingAmount[i]=re.get('bettingAmount');
            //     resultRate[i]=re.get('resultRate');
            //     payArpu[i]=re.get('payArpu');
            //     payArppu[i]=re.get('payArppu');
            //     usersDayRetention[i]=re.get('usersDayRetention');
            //     dayRetention[i]=re.get('dayRetention');
            //     usersRate[i]=re.get('usersRate');
            //     totalCost[i]=re.get('totalCost');
            //     costRate[i]=re.get('costRate');
            // }
            //
            // //最后一天日期是否为昨天以后的数据
            // var lastRecord = store.getAt(store.getCount()-1);
            // var businessDate1 = [];
            // if(lastRecord != undefined) {
            //    var lastDay = lastRecord.get("businessDate");
            //    var now = new Date();
            //     now.setTime(now.getTime()-24*60*60*1000);
            //     var yesterday = now.getFullYear()+"-"+(now.getMonth()+1) + "-" + now.getDate();
            //     if(new Date(lastDay)>=new Date(yesterday)){
            //          usersDayRetention = usersDayRetention.slice(0,store.getCount()-1);
            //          dayRetention = dayRetention.slice(0,store.getCount()-1);
            //          usersRate = usersRate.slice(0,store.getCount()-1);
            //          businessDate1 = businessDate.slice(0,store.getCount()-1);
            //     }else{
            //          businessDate1 = businessDate;
            //     }
            // }
            //
            // var option = [
            //     {
            //         // title: {text: 'DAU'},
            //         tooltip: {trigger: 'axis',
            //             formatter: function (params) {
            //                 var str='';
            //                 for(var i = 0; i < params.length; i++){
            //                     str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
            //                 }
            //                 return str;
            //             }
            //             },
            //         toolbox: {
            //             show : true,
            //             feature : {
            //                 mark : {show: true}
            //             }
            //         },
            //         calculable : true,
            //         grid:{
            //             left:'11%',
            //             y:'2.8%'
            //         },
            //         xAxis: {
            //             type : 'category',
            //             boundaryGap : false,
            //             data: businessDate
            //         },
            //         yAxis: {
            //             type : 'value',
            //             axisLabel : {
            //                 formatter: '{value}'
            //             }},
            //         series: [{
            //             name: 'DAU',
            //             type: 'line',
            //             smooth:true,
            //             data: dau
            //         }]
            //     }
            // ]
            //
            //
            // for (var j=0;j<option.length;j++) {
            //     me.echarts = echarts.init(Ext.get("kpi"+j).dom);
            //     me.echarts.setOption(option[j]);
            // }
        }
    }
});

