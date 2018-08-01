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
            fields:["channelDataMap","channelNames","ltv","endDate"
            ]
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
            columns: 3,
            buildField: "Manual",
            forceFit: true,
            items: [
                {
                    name: 'parentId',
                    fieldLabel: '主渠道',
                    xtype: 'combo',
                    // emptyText: "--请选择--",
                    displayField: 'name',
                    valueField: "id",
                    editable: true,
                    multiSelect: true,
                    queryMode: "local",
                    width:275,
                    store: parentChannelStore
                },
                {
                    name: 'startTime',
                    fieldLabel: '开始时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-30),"Y-m-d")
                },{
                    name: 'endTime',
                    fieldLabel: '结束时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d")
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
                                str += params[i].name+ ':' + params[i].value;
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
                    grid:{
                        left:'6%',
                        top:'5%',
                        right:'2%',
                        bottom:'15%'
                    },
                    xAxis: {
                        type : 'category',
                        data: channelNames,
                        "axisLabel":{
                            interval: 0,
                            rotate:20
                        }
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        },
                        splitLine: {          // 控制Y轴的分隔线(辅助线)
                            show: false      // 默认显示，属性show控制显示与
                        }
                    },
                    series: [{
                        // name: '当月累计充值',
                        type: 'bar',
                        smooth:true,
                        data: ltv,
                        barWidth: 30,
                        itemStyle:{
                            normal:{
                                color:'cornflowerblue',
                                label : {
                                    show : true,  //柱头数字
                                    position : 'top',
                                    textStyle : {
                                        fontSize : '14',
                                        // fontWeight:'bold',
                                        color: 'black'
                                    }
                                }
                            }
                        }
                    }]
                };

            me.echarts = echarts.init(Ext.get("ltvBoard0").dom);
            me.echarts.setOption(option);
            var map = store.getAt(0).get("channelDataMap");
            var data=[];
            for(var key in map){
                if(judge(map[key])){
                    data.push(map[key]);
                }
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



