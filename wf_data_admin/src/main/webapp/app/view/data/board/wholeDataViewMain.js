Ext.define('WF.view.data.board.wholeDataViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-整体数据概览',
    xtype: 'wholeDataViewMain',
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
            fields: ['businessDate','dau','rechargeAmount','rechargeCount',
                'newUsers','userCount','bettingRate',
                'dauPayRate','bettingPayRate','userBettingRate',
                'bettingAmount','resultRate',
                'payArpu','payArppu',
                'usersDayRetention','dayRetention','usersRate',
                'totalCost','costRate'
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
                // {
                // xtype:'searchfield',
                // store:parentChannelStore,
                // displayField : 'name',
                // valueField : 'id'
                // },
                {
                    name: 'startTime',
                    id:"viewStart",
                    fieldLabel: '开始时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-7),"Y-m-d")
                },{
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
                title: '核心指标',align:'stretch',height:300,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                items:[
                    {title:'',width:"33.33%",height:300,id:"kpi0"},
                    {title:'',width:"33.33%",height:300,id:"kpi1"},
                    {title:'',width:"33.33%",height:300,id:"kpi2"}]
            },
                {
                    align:'stretch',height:300,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {title:'',width:"33.33%",height:300,id:"kpi3"},
                        {title:'',width:"33.33%",height:300,id:"kpi4"},
                        {title:'',width:"33.33%",height:300,id:"kpi5"}]
                },
                {
                    align:'stretch',height:300,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {title:'',width:"33.33%",height:300,id:"kpi6"},
                        {title:'',width:"33.33%",height:300,id:"kpi7"},
                        {title:'',width:"33.33%",height:300,id:"kpi8"}]
                },
                {
                    title: '投注数据',height:300,align:'stretch',width:"100%", xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {title:'',width:"33.33%",height:300,id:"kpi9"},
                        {title:'',width:"33.33%",height:300,id:"kpi10"}
                    ]
                },
                {
                    title: '留存数据',height:300,align:'stretch',width:"100%", xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {title:'',width:"33.33%",height:300,id:"kpi13"},
                        {title:'',width:"33.33%",height:300,id:"kpi14"},
                        {title:'',width:"33.33%",height:300,id:"kpi15"}
                    ]
                },
                {
                    title: '付费数据',height:300,align:'stretch', width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {title:'',width:"33.33%",height:300,id:"kpi11"},
                        {title:'',width:"33.33%",height:300,id:"kpi12"}
                    ]
                },
                {
                    title: '成本数据',height:300,align:'stretch', width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {title:'',width:"33.33%",height:300,id:"kpi16"},
                        {title:'',width:"33.33%",height:300,id:"kpi17"}
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
            var businessDate=[];
            var dau=[];
            var rechargeAmount=[];
            var rechargeCount=[];
            var newUsers=[];
            var userCount=[];
            var bettingRate=[];
            var dauPayRate=[];
            var bettingPayRate=[];
            var userBettingRate=[];
            var bettingAmount=[];
            var resultRate=[];
            var payArpu=[];
            var payArppu=[];
            var usersDayRetention=[];
            var dayRetention=[];
            var usersRate=[];
            var totalCost=[];
            var costRate=[];
            for(var i=0;i<store.getCount();i++){
                var re=store.getAt(i);
                var d = re.get('businessDate');
                var x = d.indexOf('-');
                businessDate[i] = d.substring(x+1);
                dau[i]=re.get('dau');
                rechargeAmount[i]=re.get('rechargeAmount');
                rechargeCount[i]=re.get('rechargeCount');
                newUsers[i]=re.get('newUsers');
                userCount[i]=re.get('userCount');
                bettingRate[i]=re.get('bettingRate');
                dauPayRate[i]=re.get('dauPayRate');
                bettingPayRate[i]=re.get('bettingPayRate');
                userBettingRate[i]=re.get('userBettingRate');
                bettingAmount[i]=re.get('bettingAmount');
                resultRate[i]=re.get('resultRate');
                payArpu[i]=re.get('payArpu');
                payArppu[i]=re.get('payArppu');
                usersDayRetention[i]=re.get('usersDayRetention');
                dayRetention[i]=re.get('dayRetention');
                usersRate[i]=re.get('usersRate');
                totalCost[i]=re.get('totalCost');
                costRate[i]=re.get('costRate');
            }
            var option = [
                {
                    title: {text: 'DAU'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                        },
                    // legend: {data: ['DAU']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: 'DAU',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: dau
                    }]
                },
                {
                    title: {text: '充值金额'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['充值金额']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: '充值金额',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: rechargeAmount
                    }]
                },
                {
                    title: {text: '充值人数'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['充值人数']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: '充值人数',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: rechargeCount
                    }]
                },
                {
                    title: {text: '新增用户'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['新增用户']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: '新增用户',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: newUsers
                    }]
                },
                {
                    title: {text: '投注人数'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['投注人数']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: '投注人数',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: userCount
                    }]
                },
                {
                    title: {text: '投注转化率'},
                    tooltip: {trigger: 'axis',
                    formatter: function (params) {
                        var str='';
                        for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value +'%';
                        }
                        return str;
                    }
                        },
                    // legend: {data: ['投注转化率']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: [{
                        name: '投注转化率',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: bettingRate
                    }]
                },
                {
                    title: {text: 'DAU付费转化率'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['DAU付费转化率']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: [{
                        name: 'DAU付费转化率',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: dauPayRate
                    }]
                },
                {
                    title: {text: '投注付费转化率'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['投注付费转化率']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: [{
                        name: '投注付费转化率',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: bettingPayRate
                    }]
                },
                {
                    title: {text: '新用户投注转化率'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['新用户投注转化率']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: [{
                        name: '新用户投注转化率',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: userBettingRate
                    }]
                },
                {
                    title: {text: '投注流水'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['投注流水']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'17%'//组件距离容器左边的距离
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: '投注流水',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: bettingAmount
                    }]
                },
                {
                    title: {text: '返奖率'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['返奖率']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        min:60,
                        axisLabel : {
                            formatter: '{value}%'
                        }
                        },
                    series: [{
                        name: '返奖率',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: resultRate
                    }]
                },
                {
                    title: {text: 'ARPU'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['ARPU']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: 'ARPU',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        data: payArpu
                    }]
                },
                {
                    title: {text: 'ARPPU'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['ARPPU']},
                    toolbox: {
                        show : true,
                        x:450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: 'ARPPU',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        itemStyle: {normal: {}},
                        data: payArppu
                    }]
                },
                {
                    title: {text: '新用户留存'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['新用户留存']},
                    toolbox: {
                        show : true,
                        x : 450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }
                    },
                    series: [{
                        name: '新用户留存',
                        type: 'line',
                        smooth:true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        itemStyle: {normal: {}},
                        data: usersDayRetention
                    }]
                },
                {
                    title: {text: '全量用户留存'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['全量用户留存']},
                    toolbox: {
                        show : true,
                        x : 450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }
                    },
                    series: [{
                        name: '全量用户留存',
                        type: 'line',
                        smooth: true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        itemStyle: {normal: {}},
                        data: dayRetention
                    }]
                },
                {
                    title: {text: '新用户占比'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['新用户占比']},
                    toolbox: {
                        show : true,
                        x : 450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }
                    },
                    series: [{
                        name: '新用户占比',
                        type: 'line',
                        smooth: true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        itemStyle: {normal: {}},
                        data: usersRate
                    }]
                },
                {
                    title: {text: '新用户占比'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['新用户占比']},
                    toolbox: {
                        show : true,
                        x : 450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }
                    },
                    series: [{
                        name: '新用户占比',
                        type: 'line',
                        smooth: true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        itemStyle: {normal: {}},
                        data: usersRate
                    }]
                },
                {
                    title: {text: '成本'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    // legend: {data: ['成本']},
                    toolbox: {
                        show : true,
                        x : 450,
                        feature : {
                            mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType : {show: true, type: ['line', 'bar']},
                            saveAsImage : {show: true}
                        }
                    },
                    calculable : true,
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }
                    },
                    series: [{
                        name: '成本',
                        type: 'line',
                        smooth: true,
                        // itemStyle: {normal: {areaStyle: {type: 'default'}}},
                        itemStyle: {normal: {}},
                        data:totalCost
                    }]
                }
            ];
            for (var j=0;j<option.length;j++) {
                me.echarts = echarts.init(Ext.get("kpi"+j).dom);
                me.echarts.setOption(option[j]);
            }
        }
    }
});

