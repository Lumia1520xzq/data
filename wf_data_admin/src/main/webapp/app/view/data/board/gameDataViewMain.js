Ext.define('WF.view.data.board.gameDataViewMain', {
    extend: 'Ext.panel.Panel',
    title: '游戏数据图形分析',
    xtype: 'gameDataViewMain',
    closable: true,
    autoScroll: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var store = Ext.create('DCIS.Store', {
            url: 'data/game/data/getList.do',
            autoload: false,
            fields: ["titles", "dateList", "chartsData"]
        });
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
        });
        var gameTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'game_type'
            }
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
            forceFit: false,
            todoReload: function () {
                doSearch(me.down(("[name='myTabs']")).activeTab.items.items)
            },
            items: [{
                name: 'parentId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "",
                displayField: 'name',
                valueField: "id",
                editable: false,
                multiSelect: true,
                queryMode: "local",
                store: parentChannelStore
            },
                {
                    name: 'gameType',
                    fieldLabel: '游戏',
                    xtype: 'combobox',
                    store: gameTypeStore,
                    emptyText: "",
                    displayField: 'label',
                    multiSelect: true,
                    valueField: "value",
                    editable: false
                },
                {
                    name: 'tabId',
                    xtype: 'hiddenfield',
                    value: '',
                },
                {
                    name: 'startTime',
                    fieldLabel: '开始时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -14), "Y-m-d")
                }, {
                    name: 'endTime',
                    fieldLabel: '结束时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -1), "Y-m-d")
                }]
        });
        me.add({
            name: 'myTabs',
            xtype: 'tabpanel',
            layout: 'column',
            align: 'stretch',
            bodyStyle: 'border-width:0',
            items: [{
                title: '全量用户数据',
                itemId: 'allUsers',
                width: "16.5%",
                height: "100%",
                xtype: "panel",
                forceFit: true,
                bodyStyle: 'border-color:black',
                layout: 'column',
                items: getHtml(["DAU", "投注人数", "投注转化率", "投注流水", "流水差", "返奖率", "投注笔数", "投注ARPU", "投注ASP"]),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch(tab.items.items);

                    }
                }
            }, {
                title: '新增用户数据',
                itemId: 'newUsers',
                autoScroll: true,
                closable: false,
                items: getHtml(["新增用户数","新增投注用户数","新增投注转化率","投注流水","流水差","返奖率","投注笔数","投注ARPU","投注ASP"]),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch(tab.items.items);
                    }
                }

            }, {
                title: '留存数据',
                itemId: 'retention',
                autoScroll: true,
                closable: false,
                items: getHtml(["新增次留","新增三留","新增七留","全量次留","全量三留","全量七留"]),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch(tab.items.items);
                    }
                }
            }, {
                title: '其它数据',
                itemId: 'other',
                autoScroll: true,
                closable: false,
                items: getHtml(["导入率","累计用户数"]),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch(tab.items.items);
                    }
                }
            }]
        });

        function getHtml(names) {
            var items = [];
            for (var i = 0; i < names.length; i++) {
                items.push({
                    xtype: "panel",
                    layout: 'hbox',
                    name: names[i],
                    id: names[i],
                    width: 550,
                    height: 300,
                    forceFit: true,
                    bodyStyle: 'border-width:0'
                });
            }
            return items;
        };

        function doSearch(items) {
            var value = {
                parentId: me.down("[name='parentId']").value,
                gameType: me.down("[name='gameType']").value,
                tabId: me.down("[name='tabId']").value,
                startTime: Ext.util.Format.date(me.down("[name='startTime']").value, "Y-m-d"),
                endTime: Ext.util.Format.date(me.down("[name='endTime']").value, "Y-m-d"),
            }
            store.load({
                params: value,
                callback: function () {
                    toDoCreateCharts(items, store.getProxy().getReader().jsonData)
                }
            });
        }

        function toDoCreateCharts(items, data) {
            for (var i = 0; i < items.length; i++) {
                me.echarts = echarts.init(Ext.get(items[i].id).dom);
                var myOption = option;
                myOption.legend.data = data.chartsData.legends;
                myOption.title.text = data.titles[i];
                myOption.xAxis = [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: data.dateList
                    }
                ];

                myOption.series = getSeries(data, items[i].id);


                me.echarts.setOption(myOption);
            }

        }

        function getSeries(data, item) {
            var series = [];
            var seriesData = [];
            seriesData = data.chartsData.series;
            for (var key in seriesData) {
                if (key == item) {

                    for (var i = 0; i < seriesData[key].length; i++) {
                        var aa =[];
                        for (var j = 0; j < seriesData[key][i].length; j++) {
                            var str = [];
                            str = seriesData[key][i][j].split(";");
                            aa.push({value : str[0], name : key+":"+seriesData[key][i][j]})
                        }
                        var temp = {
                            name: data.chartsData.legends[i],
                            type: 'line',
                            // data: seriesData[key][i]
                            data: aa
                        }
                        series.push(temp)
                    }
                }

            }

            return series;
        }
    }

});

var option = {
    title: {
        text: ''
    },
    tooltip: {
        trigger: 'axis',
        formatter: function (datas) {
            var str='';
            str +='时间 :'+datas[0].name+'<br/>';
            for(var i = 0; i < datas.length; i++){
                if(datas[i].data != undefined && datas[i].data.name != 0)
                    str += datas[i].seriesName +'<br/>'+datas[i].data.name+'<br/>';
            }
            return str;
        }
    },
    legend: {
        orient: 'horizontal',      // 布局方式，默认为水平布局，可选为：
        x: 'center',               // 水平安放位置，默认为全图居中，可选为：
                                   // 'center' ¦ 'left' ¦ 'right'
                                   // ¦ {number}（x坐标，单位px）
        y: 'top',                  // 垂直安放位置，默认为全图顶端，可选为：
                                   // 'top' ¦ 'bottom' ¦ 'center'
                                   // ¦ {number}（y坐标，单位px）
        width: 350,
        backgroundColor: 'rgba(0,0,0,0)',
        borderColor: '#ccc',       // 图例边框颜色
        borderWidth: 0,            // 图例边框线宽，单位px，默认为0（无边框）
        padding: 5,                // 图例内边距，单位px，默认各方向内边距为5，
                                   // 接受数组分别设定上右下左边距，同css
        itemGap: 10,               // 各个item之间的间隔，单位px，默认为10，
                                   // 横向布局时为水平间隔，纵向布局时为纵向间隔
        itemWidth: 5,             // 图例图形宽度
        itemHeight: 14,            // 图例图形高度
        textStyle: {
            color: '#333'          // 图例文字颜色
        },
        data: []
    },
    toolbox: {
        show: true,
        feature: {
            mark: {show: true},
        }
    },
    calculable: true,
    xAxis: [
        {
            type: 'category',
            boundaryGap: false,
            data: []
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: []
};