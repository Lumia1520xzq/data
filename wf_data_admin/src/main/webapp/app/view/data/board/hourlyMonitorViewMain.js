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
            url:'data/filter/view/getList.do',
            autoload:false,
            fields:[]
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
                name: 'searchDate',
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
                title: '当天累计',align:'stretch',height:160,width:"99.99%",xtype:"panel",layout:'hbox',bodyStyle:'border-color:black',forceFit:true,
                items:[
                    {width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black',layout:'vbox',
                       items:[
                           {width:"100%",height:35,forceFit:true},
                           {width:"100%",height:40,forceFit:true},
                           {width:"100%",height:85,forceFit:true}
                       ]
                    },
                    {width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black'},
                    {width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black'},
                    {width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black'},
                    {width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black'},
                    {width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black'},
                    {width:"14.28%",height:"100%",xtype:"panel",forceFit:true,bodyStyle:'border-color:black'}
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
            // var month=[];
            // var sumRecharge=[];
            // var avgDau=[];
            // var avgDarpu=[];
            // var sumCost=[];
            // var costRate=[];
            //
            // for(var i=0;i<store.getCount();i++){
            //     var re=store.getAt(i);
            //     month[i]=re.get('month');
            //     sumRecharge[i]=re.get('sumRecharge');
            //     avgDau[i]=re.get('avgDau');
            //     avgDarpu[i]=re.get('avgDarpu');
            //     sumCost[i]=re.get('sumCost');
            //     costRate[i]=re.get('costRate');
            // }
            //
            // var option = [
            //     {
            //         // title: {text: '当月累计充值'},
            //         tooltip: {trigger: 'axis',
            //             formatter: function (params) {
            //                 var str='';
            //                 for(var i = 0; i < params.length; i++){
            //                     str += '月份:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
            //                 }
            //                 return str;
            //             }
            //         },
            //         toolbox: {
            //             show : true,
            //             feature : {
            //                 mark : {show: true}
            //             }
            //         },
            //         calculable : true,
            //         grid:{
            //             left:'13%',
            //             y:'5%'
            //         },
            //         xAxis: {
            //             type : 'category',
            //             data: month
            //         },
            //         yAxis: {
            //             type : 'value',
            //             axisLabel : {
            //                 formatter: '{value}'
            //             },
            //             splitLine: {           // 控制Y轴的分隔线(辅助线)
            //                 show: false      // 默认显示，属性show控制显示与
            //             }
            //         },
            //         series: [{
            //             name: '当月累计充值',
            //             type: 'bar',
            //             smooth:true,
            //             data: sumRecharge,
            //             barWidth: 50,
            //             itemStyle:{
            //                 normal:{color:'cornflowerblue',   //柱状颜色
            //                     label : {
            //                         show : true,  //柱头数字
            //                         position : 'top',
            //                         textStyle : {
            //                             fontSize : '14',
            //                             fontWeight:'bold'
            //                         }
            //                     }
            //                 }
            //             }
            //         }]
            //     }
            // ];
            //
            // for (var j=0;j<option.length;j++) {
            //     me.echarts = echarts.init(Ext.get("monthKpi"+j).dom);
            //     me.echarts.setOption(option[j]);
            // }
        }


    }
});

