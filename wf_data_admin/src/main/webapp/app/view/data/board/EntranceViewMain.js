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
            fields:['entranceDauRateList','eventNameList','searchDate','dateList','yesDateList','entranceDauList','yesterday',
                'duoDuoDauList','duoduoDauRate',
                'iconDauList','iconDauRate',
                'bannerDauList','bannerDauRate',
                'advDauList','advDauRate',
                'gameDauList','gameDauRate',
                'rollDauList','rollDauRate',
                'pointDauList','pointDauRate',
                'actCenterDauList','actCenterDauRate',
                'pushDauList','pushDauRate',
                'strongAdvDauList','strongAdvDauRate',
                'entranceSignRateList','entranceBettingRateList','entrancePayRateList','entranceDayRetentionList',
                'duoduoSignRate','iconSignRate','bannerSignRate','advSignRate','gameSignRate',
                'rollSignRate','pointSignRate','actCenterSignRate','pushSignRate','strongAdvSignRate',
                'duoduoBettingRate','iconBettingRate','bannerBettingRate','advBettingRate','gameBettingRate',
                'rollBettingRate','pointBettingRate','actCenterBettingRate','pushBettingRate','strongAdvBettingRate',
                'duoduoPayRate','iconPayRate','bannerPayRate','advPayRate','gamePayRate',
                'rollPayRate','pointPayRate','actCenterPayRate','pushPayRate','strongAdvPayRate',
                'duoduoRetentionRate','iconRetentionRate','bannerRetentionRate','advRetentionRate','gameRetentionRate',
                'rollRetentionRate','pointRetentionRate','actCenterRetentionRate','pushRetentionRate','strongAdvRetentionRate'
            ]
        });
        var activeUserTypeStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getUserType.do?type=user_active_type',
            autoLoad: true,
            fields: ['label', 'value']
        });
        var convertUserTypeStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getUserType.do?type=user_convert_type',
            autoLoad: true,
            fields: ['label', 'value']
        });

        var EVENT_NAMES = ["首页顶部banner","首页顶部banner右侧小图标","发现页活动中心", "发现页多多游戏","我的福利任务——玩游戏","幸运大转盘——金叶子跳转","我的积分商城——金叶子跳转", "强广位", "push","新增广告位"];

        var EVENT_IDS = ["139901", "139902", "139903", "139904", "139905", "139906", "139907", "139908", "139909", "139910"];

        var ENTRANCE_NAMES = {
            "139901":"首页顶部banner",
            "139902":"首页顶部banner右侧小图标",
            "139903":"发现页活动中心",
            "139904":"发现页多多游戏",
            "139905":"我的福利任务(玩游戏)",
            "139906":"幸运大转盘(金叶子跳转)",
            "139907":"我的积分商城(金叶子跳转)",
            "139908":"强广位",
            "139909":"push",
            "139910":"新增广告位"};

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
            export: function () {
                window.location.href = '/data/entrance/analysis/export.do?businessDate=' + me.down(("[name='searchDate']")).value;
            },
            items: [
                {
                    name: 'searchDate',
                    fieldLabel: '日期',
                    xtype: 'datefield',
                    format: 'Y-m-d'
                    // value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-30),"Y-m-d")
                },
                {
                    name: 'activeUserType',
                    fieldLabel: '按活跃对用户分类',
                    xtype: 'combo',
                    emptyText: "--请选择--",
                    displayField: 'label',
                    valueField: "value",
                    editable: true,
                    queryMode: "local",
                    width:250,
                    store: activeUserTypeStore,
                    listeners :{
                        change:function(obj,val) {
                            convertUserTypeStore.load({
                                params: {
                                    signal:val
                                }
                            });
                        }
                    }
                },
                {
                    name: 'convertUserType',
                    fieldLabel: '按充值对用户分类',
                    xtype: 'combo',
                    emptyText: "--请选择--",
                    displayField: 'label',
                    valueField: "value",
                    editable: true,
                    queryMode: "local",
                    width:250,
                    store: convertUserTypeStore,
                    listeners :{
                        change:function(obj,val) {
                            activeUserTypeStore.load({
                                params: {
                                    signal:val
                                }
                            });
                        }
                    }
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
                title: '各入口数据',align:'stretch',height:380,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                items:[
                    {width:"100%",height:380,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                        {id:'entPie',width:"45%",height:380,xtype:"panel",layout:'vbox',forceFit:true},
                        {id:'entMix',width:"45%",height:380,xtype:"panel",layout:'vbox',forceFit:true},
                        {width:"10%",height:380,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'}
                        ]
                      }
                   ]
                },
                {
                    title:'转化率',align:'stretch',height:400,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                    items:[
                        {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                            {id:'entRate0',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                            {id:'entRate1',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                            {id:'entRate2',width:"33.33%",height:400,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'}
                        ]
                        }
                    ]
                },
                {
                    title:'',align:'stretch',height:400,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                    items:[
                        {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                            {id:'entPic0',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                            {id:'entPic1',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                            {id:'entPic2',width:"33.33%",height:400,xtype:"panel",forceFit:true,bodyStyle:'border-width:0'}
                        ]
                        }
                    ]
                },
                {
                    title:'留存率',align:'stretch',height:400,width:"100%",xtype:"panel",layout:'vbox',bodyStyle:'border-width:0',forceFit:true,
                    items:[
                        {width:"100%",height:400,xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0',items:[
                            {id:'entRate3',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true},
                            {id:'entPic3',width:"33.33%",height:400,xtype:"panel",layout:'vbox',forceFit:true}
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
            // var entranceDauRateList = store.getAt(0).get("entranceDauRateList");
            var entranceDauList = store.getAt(0).get("entranceDauList");
            // 名称
            var eventNameList = store.getAt(0).get("eventNameList");
            var searchDate = store.getAt(0).get("searchDate");
            var yesterday = store.getAt(0).get("yesterday");
            var dateList = store.getAt(0).get("dateList");
            var yesDateList = store.getAt(0).get("yesDateList");

            // DAU
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

            // DAU占比
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

            var entranceSignRateList = store.getAt(0).get("entranceSignRateList");
            var entranceBettingRateList = store.getAt(0).get("entranceBettingRateList");
            var entrancePayRateList = store.getAt(0).get("entrancePayRateList");
            var entranceDayRetentionList = store.getAt(0).get("entranceDayRetentionList");

            // 签到转化率
            var duoduoSignRate = store.getAt(0).get("duoduoSignRate");
            var iconSignRate = store.getAt(0).get("iconSignRate");
            var bannerSignRate = store.getAt(0).get("bannerSignRate");
            var advSignRate = store.getAt(0).get("advSignRate");
            var gameSignRate = store.getAt(0).get("gameSignRate");
            var rollSignRate = store.getAt(0).get("rollSignRate");
            var pointSignRate = store.getAt(0).get("pointSignRate");
            var actCenterSignRate = store.getAt(0).get("actCenterSignRate");
            var pushSignRate = store.getAt(0).get("pushSignRate");
            var strongAdvSignRate = store.getAt(0).get("strongAdvSignRate");

            // 投注转化率
            var duoduoBettingRate = store.getAt(0).get("duoduoBettingRate");
            var iconBettingRate = store.getAt(0).get("iconBettingRate");
            var bannerBettingRate = store.getAt(0).get("bannerBettingRate");
            var advBettingRate = store.getAt(0).get("advBettingRate");
            var gameBettingRate = store.getAt(0).get("gameBettingRate");
            var rollBettingRate = store.getAt(0).get("rollBettingRate");
            var pointBettingRate = store.getAt(0).get("pointBettingRate");
            var actCenterBettingRate = store.getAt(0).get("actCenterBettingRate");
            var pushBettingRate = store.getAt(0).get("pushBettingRate");
            var strongAdvBettingRate = store.getAt(0).get("strongAdvBettingRate");

            // 付费渗透率
            var duoduoPayRate = store.getAt(0).get("duoduoPayRate");
            var iconPayRate = store.getAt(0).get("iconPayRate");
            var bannerPayRate = store.getAt(0).get("bannerPayRate");
            var advPayRate = store.getAt(0).get("advPayRate");
            var gamePayRate = store.getAt(0).get("gamePayRate");
            var rollPayRate = store.getAt(0).get("rollPayRate");
            var pointPayRate = store.getAt(0).get("pointPayRate");
            var actCenterPayRate = store.getAt(0).get("actCenterPayRate");
            var pushPayRate = store.getAt(0).get("pushPayRate");
            var strongAdvPayRate = store.getAt(0).get("strongAdvPayRate");

            // 次日留存率
            var duoduoRetentionRate = store.getAt(0).get("duoduoRetentionRate");
            var iconRetentionRate = store.getAt(0).get("iconRetentionRate");
            var bannerRetentionRate = store.getAt(0).get("bannerRetentionRate");
            var advRetentionRate = store.getAt(0).get("advRetentionRate");
            var gameRetentionRate = store.getAt(0).get("gameRetentionRate");
            var rollRetentionRate = store.getAt(0).get("rollRetentionRate");
            var pointRetentionRate = store.getAt(0).get("pointRetentionRate");
            var actCenterRetentionRate = store.getAt(0).get("actCenterRetentionRate");
            var pushRetentionRate = store.getAt(0).get("pushRetentionRate");
            var strongAdvRetentionRate = store.getAt(0).get("strongAdvRetentionRate");


            var dauList= [];
            dauList.push(bannerDauList);
            dauList.push(iconDauList);
            dauList.push(actCenterDauList);
            dauList.push(duoDuoDauList);
            dauList.push(gameDauList);
            dauList.push(rollDauList);
            dauList.push(pointDauList);
            dauList.push(strongAdvDauList);
            dauList.push(pushDauList);
            dauList.push(advDauList);

            var dauRate= [];
            dauRate.push(bannerDauRate);
            dauRate.push(iconDauRate);
            dauRate.push(actCenterDauRate);
            dauRate.push(duoduoDauRate);
            dauRate.push(gameDauRate);
            dauRate.push(rollDauRate);
            dauRate.push(pointDauRate);
            dauRate.push(strongAdvDauRate);
            dauRate.push(pushDauRate);
            dauRate.push(advDauRate);

            var signRate=[];
            signRate.push(bannerSignRate);
            signRate.push(iconSignRate);
            signRate.push(actCenterSignRate);
            signRate.push(duoduoSignRate);
            signRate.push(gameSignRate);
            signRate.push(rollSignRate);
            signRate.push(pointSignRate);
            signRate.push(strongAdvSignRate);
            signRate.push(pushSignRate);
            signRate.push(advSignRate);

            var bettingRate=[];
            bettingRate.push(bannerBettingRate);
            bettingRate.push(iconBettingRate);
            bettingRate.push(actCenterBettingRate);
            bettingRate.push(duoduoBettingRate);
            bettingRate.push(gameBettingRate);
            bettingRate.push(rollBettingRate);
            bettingRate.push(pointBettingRate);
            bettingRate.push(strongAdvBettingRate);
            bettingRate.push(pushBettingRate);
            bettingRate.push(advBettingRate);

            var payRate=[];
            payRate.push(bannerPayRate);
            payRate.push(iconPayRate);
            payRate.push(actCenterPayRate);
            payRate.push(duoduoPayRate);
            payRate.push(gamePayRate);
            payRate.push(rollPayRate);
            payRate.push(pointPayRate);
            payRate.push(strongAdvPayRate);
            payRate.push(pushPayRate);
            payRate.push(advPayRate);

            var retentionRate=[];
            retentionRate.push(bannerRetentionRate);
            retentionRate.push(iconRetentionRate);
            retentionRate.push(actCenterRetentionRate);
            retentionRate.push(duoduoRetentionRate);
            retentionRate.push(gameRetentionRate);
            retentionRate.push(rollRetentionRate);
            retentionRate.push(pointRetentionRate);
            retentionRate.push(strongAdvRetentionRate);
            retentionRate.push(pushRetentionRate);
            retentionRate.push(advRetentionRate);

            var arrays =[];
            for(var i = 0;i < entranceDauList.length;i++){
                arrays[i] = {
                    value:entranceDauList[i],
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
                    formatter: "{a}:{b}<br/>DAU:{c}<br/>DAU占比:{d}%"
                },
                legend: {
                    orient:'vertical',
                    right:'1%',
                    top:'15%',
                    data:eventNameList
                },
                calculable : true,
                series : [
                    {
                        name:'入口来源',
                        type:'pie',
                        radius:'55%',
                        center: ['40%','60%'],
                        data:arrays
                    }
                ]
            };
            entranceAndRate(ENTRANCE_NAMES[EVENT_IDS[0]] + "入口--DAU及占比",dateList,dauList[0],dauRate[0]);
            me.echarts = echarts.init(Ext.get("entPie").dom);
            me.echarts.setOption(pieOption);
            me.echarts.on("click", eConsole);

            transRate(searchDate+"各入口签到转化率",eventNameList,entranceSignRateList,0);
            me.echarts.on('click', function(param) {
                var idx = EVENT_NAMES.lastIndexOf(param.name);
                trendPic(ENTRANCE_NAMES[EVENT_IDS[idx]]+"入口--签到转化率",dateList,signRate[idx],0);
            });
            transRate(searchDate+"各入口投注转化率",eventNameList,entranceBettingRateList,1);
            me.echarts.on('click', function(param) {
                var idx = EVENT_NAMES.lastIndexOf(param.name);
                trendPic(ENTRANCE_NAMES[EVENT_IDS[idx]] + "入口--投注转化率",dateList,bettingRate[idx],1);
            });
            transRate(searchDate+"各入口付费渗透率",eventNameList,entrancePayRateList,2);
            me.echarts.on('click', function(param) {
                var idx = EVENT_NAMES.lastIndexOf(param.name);
                trendPic(ENTRANCE_NAMES[EVENT_IDS[idx]] + "入口--付费渗透率",dateList,payRate[idx],2);
            });
            transRate(yesterday+"各入口次日留存率",eventNameList,entranceDayRetentionList,3);
            me.echarts.on('click', function(param) {
                var idx = EVENT_NAMES.lastIndexOf(param.name);
                trendPic(ENTRANCE_NAMES[EVENT_IDS[idx]] + "入口--次日留存率",yesDateList,retentionRate[idx],3);
            });
            trendPic(ENTRANCE_NAMES[EVENT_IDS[0]] + "入口--签到转化率",dateList,signRate[0],0);
            trendPic(ENTRANCE_NAMES[EVENT_IDS[0]] + "入口--投注转化率",dateList,bettingRate[0],1);
            trendPic(ENTRANCE_NAMES[EVENT_IDS[0]] + "入口--付费渗透率",dateList,payRate[0],2);
            trendPic(ENTRANCE_NAMES[EVENT_IDS[0]] + "入口--次日留存率",yesDateList,retentionRate[0],3);

            // 增加饼图的监听事件
            function eConsole(param) {
                if (typeof param.seriesIndex != 'undefined' && param.type == 'click') {
                    var pieLenght= pieOption.legend.data.length;
                    for(var i=0;i<pieLenght;i++){
                        if(param.seriesIndex==0 && param.dataIndex==i) {
                            var idx = EVENT_NAMES.lastIndexOf(pieOption.legend.data[i]);
                            entranceAndRate(ENTRANCE_NAMES[EVENT_IDS[idx]] + "入口--DAU及占比", dateList, dauList[idx], dauRate[idx]);
                        }
                    }
                }
            }
        }

        function trendPic(title,dateList,transRate,id){
            var rateOption =
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
                                str += '日期:'+params[i].name+'<br/>'+ '转化率:' + params[i].value + '%';
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
                        left:'7%',
                        top:'20%',
                        right:'6%',
                        bottom:'15%'
                    },
                    xAxis: {
                        type : 'category',
                        data: dateList,
                        "axisLabel":{
                            interval: 0,
                            rotate:50
                        }
                    },
                    yAxis: {type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }
                    },
                    series: [{
                        name: title,
                        type: 'line',
                        smooth:true,
                        data: transRate,
                        itemStyle:{
                            normal:{
                                color:'cornflowerblue'
                            }
                        }
                    }
                    ]
                };
            me.echarts = echarts.init(Ext.get("entPic"+id).dom);
            me.echarts.setOption(rateOption);
        }

        function transRate(title,eventNameList,rateList,id){
            var rateOption =
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
                                str += '入口:'+params[i].name+'<br/>'+ '转化率:' + params[i].value + '%';
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
                        left:'7%',
                        top:'20%',
                        right:'6%',
                        bottom:'40%'
                    },
                    xAxis: {
                        type : 'category',
                        data: eventNameList,
                        "axisLabel":{
                            interval: 0,
                            rotate:50
                        }
                    },
                    yAxis: {type : 'value',
                            axisLabel : {
                                formatter: '{value}%'
                            }
                            },
                    series: [{
                        name: title,
                        type: 'bar',
                        smooth:true,
                        data: rateList,
                        barWidth: 30,
                        itemStyle:{
                            normal:{
                                color:'cornflowerblue'
                            }
                        }
                       }
                    ]
                };
            me.echarts = echarts.init(Ext.get("entRate"+id).dom);
            me.echarts.setOption(rateOption);
        }


        function entranceAndRate(title,dateList,dauList,dauRateList){
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
                                if (i == 0){
                                    str += '日期:'+params[i].name+'<br/>';
                                }
                                if(params[i].seriesName=='DAU占比'){
                                    str +=  params[i].seriesName +':'+params[i].value +"%<br/>";
                                }
                                else{
                                    str +=  params[i].seriesName +':'+params[i].value +"<br/>";
                                }
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
                        left:'10%',
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



