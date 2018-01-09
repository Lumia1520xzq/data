Ext.define('WF.view.data.board.wholeDataViewMain1', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-整体数据概览',
    xtype: 'wholeDataViewMain',
    closable: true,
    autoScroll: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getViewChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });
        var me = this;
        var contextType = {1: 'bug修复', 2: '新需求', 3: '优化'};
        var p = Ext.create('Ext.panel.Panel', {
            height: 500,
            renderTo: document.body,
            name: 'onlineApplyCount',
            myChart: {},
            tbar: [
                {
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
                xtype: 'datefield',
                name: 'startTime',
                format: 'Y-m-d',
                fieldLabel: '开始时间'
                },
                {
                xtype: 'datefield',
                name: 'endTime',
                format: 'Y-m-d',
                fieldLabel: '结束时间'
                },
                {
                text: '查询',
                iconCls: "icon-search",
                handler: function () {
                    var myChat = me.down('panel[name="onlineApplyCount"]');
                    var startTime = myChat.query('[name="startTime"]')[0].getValue();
                    var endTime = myChat.query('[name="endTime"]')[0].getValue();
                    myChat.myChart = echarts.init(myChat.body.dom, 'macarons');
                    callapi("data/board/view/getList.do", {
                        startTime: Ext.Date.format(startTime, 'Y-m-d'),
                        endTime: Ext.Date.format(endTime, 'Y-m-d')
                    }, function (res) {
                        console.log("返回的数据为:"+res.data);
                        if (!res.success) {
                            return;
                        }
                        var rows = res.data;
                        var yAxis = [];
                        var xAxis = [];
                        for (var i = 0; i < rows.length; i++) {
                            var r = rows[i];
                            for (var p in r) {
                                if (p === 'project') {
                                    yAxis.push(r[p]);
                                } else if (p === 'onlinecount') {
                                    xAxis.push(r[p]);
                                }
                            }
                        }
                        myChat.myChart.setOption({
                            title: {
                                text: '上线次数统计'
                            },
                            tooltip: {
                                trigger: 'axis',
                                axisPointer: {
                                    type: 'shadow'
                                }
                            },
                            grid: {
                                left: '3%',
                                right: '4%',
                                bottom: '3%',
                                containLabel: true
                            },
                            xAxis: {
                                type: 'value',
                                boundaryGap: [0, 0.01]
                            },
                            yAxis: {
                                type: 'category',
                                data: yAxis
                            },
                            series: [
                                {
                                    name:'上线次数',
                                    type: 'bar',
                                    itemStyle: {
                                        normal: {
                                            color: '#3FA7DC'
                                        },
                                        emphasis: {
                                            color: '#319cdc'
                                        }
                                    },
                                    data: xAxis
                                }
                            ]
                        })
                    });

                }
            }, {
                text: '重置',
                iconCls: "icon-undo",
                handler: function () {
                    var myChat = me.down('panel[name="onlineApplyCount"]');
                    myChat.down('[name="startTime"]')[0].setValue("");
                    myChat.down('[name="endTime"]')[0].setValue("");
                }
            }],
            listeners: {
                afterlayout: function (pl, layout, opts) {
                    var me = this;
                    me.myChart = echarts.init(me.body.dom, 'macarons');
                    callapi("data/board/view/getList.do", {
                    }, function (res) {
                        if (!res.success) {
                            console.log(res.data);
                            return;
                        }
                        var rows = res.data;
                        var yAxis = [];
                        var xAxis = [];
                        for (var i = 0; i < rows.length; i++) {
                            var r = rows[i];
                            for (var p in r) {
                                if (p === 'project') {
                                    yAxis.push(r[p]);
                                } else if (p === 'onlinecount') {
                                    xAxis.push(r[p]);
                                }
                            }
                        }
                        me.myChart.setOption({
                            title: {
                                text: '上线次数统计'
                            },
                            tooltip: {
                                trigger: 'axis',
                                axisPointer: {
                                    type: 'shadow'
                                }
                            },
                            grid: {
                                left: '3%',
                                right: '4%',
                                bottom: '3%',
                                containLabel: true
                            },
                            xAxis: {
                                type: 'value',
                                boundaryGap: [0, 0.01]
                            },
                            yAxis: {
                                type: 'category',
                                data: yAxis
                            },
                            series: [
                                {
                                    name:'上线次数',
                                    type: 'bar',
                                    itemStyle: {
                                        normal: {
                                            color: '#3FA7DC'
                                        },
                                        emphasis: {
                                            color: '#319cdc'
                                        }
                                    },
                                    data: xAxis
                                }
                            ]
                        })
                    });
                }
            }
        });

        var p2 = Ext.create('Ext.panel.Panel', {
            height: 500,
            renderTo: document.body,
            name: 'onlineApplyContextTypeCount',
            myChart: {},
            tbar: [{
                xtype: 'datefield',
                name: 'startTime',
                format: 'Y-m-d',
                // maxValue: addDate(new Date(), 1),
                fieldLabel: '时间',
                editable: false
            }, {
                xtype: 'datefield',
                name: 'endTime',
                format: 'Y-m-d',
                // maxValue: addDate(new Date(), 1),
                fieldLabel: '至',
                editable: false
            }, {
                text: '查询',
                iconCls: "icon-search",
                handler: function () {
                    var myChat = me.down('panel[name="onlineApplyContextTypeCount"]');
                    var startTime = myChat.down('[name="startTime"]').getValue();
                    var endTime = myChat.down('[name="endTime"]').getValue();
                    myChat.myChart = echarts.init(myChat.body.dom, 'macarons');

                    callapi("data/board/view/getList.do", {
                        startTime: Ext.Date.format(startTime, 'Y-m-d'),
                        endTime: Ext.Date.format(endTime, 'Y-m-d')
                    }, function (res) {
                        if (!res.success) {
                            console.log(res.data);
                            return;
                        }
                        var rows = res.data;
                        var yAxis = [];
                        var xAxis = [];
                        for (var i = 0; i < rows.length; i++) {
                            var r = rows[i];
                            for (var p in r) {
                                if (p === 'contexttype') {
                                    yAxis.push(contextType[r[p]]);
                                } else if (p === 'onlinecount') {
                                    xAxis.push(r[p]);
                                }
                            }
                        }

                        myChat.myChart.setOption({
                            title: {
                                text: '上线类容类型统计'
                            },
                            tooltip: {
                                trigger: 'axis',
                                axisPointer: {
                                    type: 'shadow'
                                }
                            },
                            grid: {
                                left: '3%',
                                right: '4%',
                                bottom: '3%',
                                containLabel: true
                            },
                            xAxis: {
                                type: 'value',
                                boundaryGap: [0, 0.01]
                            },
                            yAxis: {
                                type: 'category',
                                data: yAxis
                            },
                            series: [
                                {
                                    name:'上线类容类型',
                                    type: 'bar',
                                    itemStyle: {
                                        normal: {
                                            color: '#3FA7DC'
                                        },
                                        emphasis: {
                                            color: '#319cdc'
                                        }
                                    },
                                    data: xAxis
                                }
                            ]
                        })
                    });

                }
            }, {
                text: '重置',
                iconCls: "icon-undo",
                handler: function () {
                    var myChat = me.down('panel[name="onlineApplyContextTypeCount"]');
                    myChat.query('[name="startTime"]')[0].setValue('');
                    myChat.query('[name="endTime"]')[0].setValue('');
                }
            }],
            listeners: {
                afterlayout: function (pl, layout, opts) {
                    var me = this;
                    me.myChart = echarts.init(me.body.dom, 'macarons');
                    callapi("data/board/view/getList.do", {}, function (res) {
                        if (!res.success) {
                            console.log(res.data);
                            return;
                        }
                        var rows = res.data;
                        var yAxis = [];
                        var xAxis = [];
                        for (var i = 0; i < rows.length; i++) {
                            var r = rows[i];
                            for (var p in r) {
                                if (p === 'contexttype') {
                                    yAxis.push(contextType[r[p]]);
                                } else if (p === 'onlinecount') {
                                    xAxis.push(r[p]);
                                }
                            }
                        }

                        me.myChart.setOption({
                            title: {
                                text: '类容类型统计'
                            },
                            tooltip: {
                                trigger: 'axis',
                                axisPointer: {
                                    type: 'shadow'
                                }
                            },
                            grid: {
                                left: '3%',
                                right: '4%',
                                bottom: '3%',
                                containLabel: true
                            },
                            xAxis: {
                                type: 'value',
                                boundaryGap: [0, 0.01]
                            },
                            yAxis: {
                                type: 'category',
                                data: yAxis
                            },
                            series: [
                                {
                                    name:'类型上线次数',
                                    type: 'bar',
                                    itemStyle: {
                                        normal: {
                                            color: '#3FA7DC'
                                        },
                                        emphasis: {
                                            color: '#319cdc'
                                        }
                                    },
                                    data: xAxis
                                }
                            ]
                        })
                    })
                }
            }
        });

        me.add(p, p2);

    }
});