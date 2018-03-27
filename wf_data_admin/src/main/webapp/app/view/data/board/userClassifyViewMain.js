Ext.define('WF.view.data.board.userClassifyViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-用户分层分析',
    xtype: 'userClassifyViewMain',
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
            url:'data/user/classify/getList.do',
            autoload:false,
            fields: ["0","1","2","3","4","5","6","dateList"]
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
                width:275,
                store: parentChannelStore
            },
            {
                name: 'startTime',
                id:"viewStart",
                fieldLabel: '开始时间',
                xtype: 'datefield',
                format: 'Y-m-d',
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-14),"Y-m-d")
            },
            {
                name: 'endTime',
                id:"viewEnd",
                fieldLabel: '结束时间',
                xtype: 'datefield',
                format: 'Y-m-d',
                value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d")
            }]
        });

        me.add({
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0 0 0 0;',
            items: [{
                title:'活跃数据',collapsible:true,align:'stretch',height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                items:[
                    {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                        {id:'userClassify0',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                        {id:'userClassify1',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                        {width:"33.33%",height:400,xtype:"panel",forceFit:true,bodyStyle:'border-width:0;'}
                    ]
                    }
                  ]
                },
                {
                    height:400,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                    items:[
                        {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                            {id:'userClassify2',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                            {id:'userClassify3',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0;'},
                            {id:'userClassify4',width:"33.33%",height:400,xtype:"panel",forceFit:true,bodyStyle:'border-width:0;'}
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
        var dateList = store.getAt(0).get("dateList");
        var newUserList = store.getAt(0).get("0");
        var rechargeOneList = store.getAt(0).get("1");
        var rechargeTwoList = store.getAt(0).get("2");
        var rechargeThreeList = store.getAt(0).get("3");
        var rechargeFourList = store.getAt(0).get("4");
        var rechargeFiveList = store.getAt(0).get("5");
        var rechargeSixList = store.getAt(0).get("6");

        var size = newUserList.length;

        //1、DAU
        var newUserDauList = [];
        var rechargeOneDauList = [];
        var rechargeTwoDauList = [];
        var rechargeThreeDauList = [];
        var rechargeFourDauList = [];
        var rechargeFiveDauList = [];
        var rechargeSixDauList = [];

        //2、日活占比
        var newUserDauRateList = [];
        var rechargeOneDauRateList = [];
        var rechargeTwoDauRateList = [];
        var rechargeThreeDauRateList = [];
        var rechargeFourDauRateList = [];
        var rechargeFiveDauRateList = [];
        var rechargeSixDauRateList = [];

        //3、累计用户数
        var newUserTotalCountList = [];
        var rechargeOneTotalCountList = [];
        var rechargeTwoTotalCountList = [];
        var rechargeThreeTotalCountList = [];
        var rechargeFourTotalCountList = [];
        var rechargeFiveTotalCountList = [];
        var rechargeSixTotalCountList = [];

        //4、活跃率
        var newUserTotalRateList = [];
        var rechargeOneTotalRateList = [];
        var rechargeTwoTotalRateList = [];
        var rechargeThreeTotalRateList = [];
        var rechargeFourTotalRateList = [];
        var rechargeFiveTotalRateList = [];
        var rechargeSixTotalRateList = [];

        //5、人均登陆次数
        var newUserLoginCountList = [];
        var rechargeOneLoginCountList = [];
        var rechargeTwoLoginCountList = [];
        var rechargeThreeLoginCountList = [];
        var rechargeFourLoginCountList = [];
        var rechargeFiveLoginCountList = [];
        var rechargeSixLoginCountList = [];

        for (var i=0;i<size;i++){
            newUserDauList[i] = newUserList[i].dau;
            rechargeOneDauList[i] = rechargeOneList[i].dau;
            rechargeTwoDauList[i] = rechargeTwoList[i].dau;
            rechargeThreeDauList[i] = rechargeThreeList[i].dau;
            rechargeFourDauList[i] = rechargeFourList[i].dau;
            rechargeFiveDauList[i] = rechargeFiveList[i].dau;
            rechargeSixDauList[i] = rechargeSixList[i].dau;

            newUserDauRateList[i] = newUserList[i].dauRate;
            rechargeOneDauRateList[i] = rechargeOneList[i].dauRate;
            rechargeTwoDauRateList[i] = rechargeTwoList[i].dauRate;
            rechargeThreeDauRateList[i] = rechargeThreeList[i].dauRate;
            rechargeFourDauRateList[i] = rechargeFourList[i].dauRate;
            rechargeFiveDauRateList[i] = rechargeFiveList[i].dauRate;
            rechargeSixDauRateList[i] = rechargeSixList[i].dauRate;

            newUserTotalCountList[i] = newUserList[i].totalUserCount;
            rechargeOneTotalCountList[i] = rechargeOneList[i].totalUserCount;
            rechargeTwoTotalCountList[i] = rechargeTwoList[i].totalUserCount;
            rechargeThreeTotalCountList[i] = rechargeThreeList[i].totalUserCount;
            rechargeFourTotalCountList[i] = rechargeFourList[i].totalUserCount;
            rechargeFiveTotalCountList[i] = rechargeFiveList[i].totalUserCount;
            rechargeSixTotalCountList[i] = rechargeSixList[i].totalUserCount;

            newUserTotalRateList[i] = newUserList[i].totalUserRate;
            rechargeOneTotalRateList[i] = rechargeOneList[i].totalUserRate;
            rechargeTwoTotalRateList[i] = rechargeTwoList[i].totalUserRate;
            rechargeThreeTotalRateList[i] = rechargeThreeList[i].totalUserRate;
            rechargeFourTotalRateList[i] = rechargeFourList[i].totalUserRate;
            rechargeFiveTotalRateList[i] = rechargeFiveList[i].totalUserRate;
            rechargeSixTotalRateList[i] = rechargeSixList[i].totalUserRate;

            newUserLoginCountList[i] = newUserList[i].loginCount;
            rechargeOneLoginCountList[i] = rechargeOneList[i].loginCount;
            rechargeTwoLoginCountList[i] = rechargeTwoList[i].loginCount;
            rechargeThreeLoginCountList[i] = rechargeThreeList[i].loginCount;
            rechargeFourLoginCountList[i] = rechargeFourList[i].loginCount;
            rechargeFiveLoginCountList[i] = rechargeFiveList[i].loginCount;
            rechargeSixLoginCountList[i] = rechargeSixList[i].loginCount;
        }
            var legArr =  ['新注册','累充0','累充1-100','累充100-1000','累充1000-10000','累充10000-100000','累充10万以上'];

            var dauOption = template("DAU",dateList,legArr);
            dauOption.series.push(areaStyleSeries(legArr[0],newUserDauList));
            dauOption.series.push(areaStyleSeries(legArr[1],rechargeOneDauList));
            dauOption.series.push(areaStyleSeries(legArr[2],rechargeTwoDauList));
            dauOption.series.push(areaStyleSeries(legArr[3],rechargeThreeDauList));
            dauOption.series.push(areaStyleSeries(legArr[4],rechargeFourDauList));
            dauOption.series.push(areaStyleSeries(legArr[5],rechargeFiveDauList));
            dauOption.series.push(areaStyleSeries(legArr[6],rechargeSixDauList));

            var dauRateOption = template("日活占比",dateList,legArr);
            dauRateOption.series.push(stackBarStyleSeries(legArr[0],newUserDauRateList));
            dauRateOption.series.push(stackBarStyleSeries(legArr[1],rechargeOneDauRateList));
            dauRateOption.series.push(stackBarStyleSeries(legArr[2],rechargeTwoDauRateList));
            dauRateOption.series.push(stackBarStyleSeries(legArr[3],rechargeThreeDauRateList));
            dauRateOption.series.push(stackBarStyleSeries(legArr[4],rechargeFourDauRateList));
            dauRateOption.series.push(stackBarStyleSeries(legArr[5],rechargeFiveDauRateList));
            dauRateOption.series.push(stackBarStyleSeries(legArr[6],rechargeSixDauRateList));

            var totalCountOption = template("累计用户数",dateList,legArr);
            totalCountOption.series.push(areaStyleSeries(legArr[0],newUserTotalCountList));
            totalCountOption.series.push(areaStyleSeries(legArr[1],rechargeOneTotalCountList));
            totalCountOption.series.push(areaStyleSeries(legArr[2],rechargeTwoTotalCountList));
            totalCountOption.series.push(areaStyleSeries(legArr[3],rechargeThreeTotalCountList));
            totalCountOption.series.push(areaStyleSeries(legArr[4],rechargeFourTotalCountList));
            totalCountOption.series.push(areaStyleSeries(legArr[5],rechargeFiveTotalCountList));
            totalCountOption.series.push(areaStyleSeries(legArr[6],rechargeSixTotalCountList));

            var totalRateOption = template("活跃率",dateList,legArr);
            totalRateOption.series.push(lineStyleSeries(legArr[0],newUserTotalRateList));
            totalRateOption.series.push(lineStyleSeries(legArr[1],rechargeOneTotalRateList));
            totalRateOption.series.push(lineStyleSeries(legArr[2],rechargeTwoTotalRateList));
            totalRateOption.series.push(lineStyleSeries(legArr[3],rechargeThreeTotalRateList));
            totalRateOption.series.push(lineStyleSeries(legArr[4],rechargeFourTotalRateList));
            totalRateOption.series.push(lineStyleSeries(legArr[5],rechargeFiveTotalRateList));
            totalRateOption.series.push(lineStyleSeries(legArr[6],rechargeSixTotalRateList));

            var loginCountOption = template("人均登录次数",dateList,legArr);
            loginCountOption.series.push(lineStyleSeries(legArr[0],newUserLoginCountList));
            loginCountOption.series.push(lineStyleSeries(legArr[1],rechargeOneLoginCountList));
            loginCountOption.series.push(lineStyleSeries(legArr[2],rechargeTwoLoginCountList));
            loginCountOption.series.push(lineStyleSeries(legArr[3],rechargeThreeLoginCountList));
            loginCountOption.series.push(lineStyleSeries(legArr[4],rechargeFourLoginCountList));
            loginCountOption.series.push(lineStyleSeries(legArr[5],rechargeFiveLoginCountList));
            loginCountOption.series.push(lineStyleSeries(legArr[6],rechargeSixLoginCountList));

            // 核心代码
            var option = [dauOption,dauRateOption,totalCountOption,totalRateOption,loginCountOption];
            for (var j=0;j<option.length;j++) {
                me.echarts = echarts.init(Ext.get("userClassify"+j).dom);
                me.echarts.setOption(option[j]);
            }
        }

        function template(title,dateList,legArr) {
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
                    formatter: function (params) {
                    var str='';
                    for(var i = 0; i < params.length; i++){
                        str += '日期:'+params[i].name +"\t"+ params[i].seriesName +':' + params[i].value+"<br/>";
                    }
                    return str;
                }
                },
                legend: {
                        data:legArr,
                        top:'10%'
                },
                toolbox: {
                    show : true,
                        feature : {
                        mark : {show: true}
                    }
                },
                calculable : true,
                    grid:{
                left:'11%',
                    y:'25%'
            },
                xAxis: {
                    type : 'category',
                        boundaryGap : false,
                        data: dateList
                },
                yAxis: {
                    type : 'value',
                        axisLabel : {
                        formatter: '{value}'
                    }},
                series: []
            };
            return option;
        }

        function areaStyleSeries(title,dataList){
            var series =  {
                name: title,
                type: 'line',
                smooth:true,
                data: dataList,
                itemStyle: {normal: {areaStyle: {type: 'default'}}},
                stack:'总量'
            };
            return series;
        }

        function stackBarStyleSeries(title,dataList){
            var series = {
                name: title,
                type: 'bar',
                smooth:true,
                data: dataList,
                stack:'占比'
            };
            return series;
        }

        function lineStyleSeries(title,dataList) {
            var series = {
                name: title,
                type: 'line',
                smooth:true,
                data: dataList
            };
            return series;
        }


    }
});

