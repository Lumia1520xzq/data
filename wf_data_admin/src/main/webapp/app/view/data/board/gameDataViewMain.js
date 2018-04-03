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
            url: '/game/data/getList.do',
            autoload: false,
            fields: ["todData", "yesData", "historyData"]
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
                itemId: 'home',
                width: "16.5%",
                height: "100%",
                xtype: "panel",
                forceFit: true,
                bodyStyle: 'border-color:black',
                layout: 'column',
                items: getHtml("name", 3),
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        console.dir("activate")
                        doSearch(tab.items.items);

                    }
                }
            }, {
                title: '新增用户数据',
                html: 'Users',
                itemId: 'users',
                autoScroll: true,
                closable: false,
                items: [],
                listeners: {
                    'activate': function (tab) {
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        // doSearch();
                        /*tab.removeAll();
                        tab.items.add(Ext.create("WF.view.data.board.monthlyDataViewMain", {height: 850}));*/
                    }
                }

            }, {
                title: '留存数据',
                html: 'Tickets',
                itemId: 'tickets',
                autoScroll: true,
                closable: false,
                items: [],
                listeners: {
                    'activate': function (tab) {
                        tab.removeAll();
                        me.down(("[name='tabId']")).setValue(tab.itemId);

                        doSearch();
                        /* tab.removeAll();
                         this.items.add(Ext.create("WF.view.data.board.filterDataViewMain", {height: 850}));*/
                    }
                }
            }, {
                title: '其它数据',
                html: 'aa',
                itemId: 'aa',
                autoScroll: true,
                closable: false,
                items: [],
                listeners: {
                    'activate': function (tab) {
                        tab.removeAll();
                        me.down(("[name='tabId']")).setValue(tab.itemId);
                        doSearch();
                        /*tab.removeAll();
                        this.items.add(Ext.create("WF.view.data.board.GameMonitorViewMain", {height: 850}));*/
                    }
                }
            }]
        });

        function getHtml(name, len) {
            var items = [];
            for (var i = 0; i < len; i++) {
                items.push({
                    xtype: "panel",
                    layout: 'hbox',
                    name: name + i,
                    id: name + i,
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
                    console.dir(store)
                    for (var i = 0; i < store.getCount(); i++) {
                        var re = store.getAt(i);
                        toDoCreateCharts(items, re.data)
                    }
                }
            });
        }

        function toDoCreateCharts(items, data) {
            for (var i = 0; i < items.length; i++) {
                me.echarts = echarts.init(Ext.get(items[i].id).dom);
                var myOption = option;
                myOption.legend.data = ['邮件营销', '联盟广告']
                me.echarts.setOption(myOption);
            }

        }
    }

});

var option = {
    title: {
        text: ''
    },
    tooltip: {
        trigger: 'axis',
        formatter: function (params) {
            var str='';
            for(var i = 0; i < params.length; i++){
                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
            }
            return str;
        }
    },
    legend: {
        data: []
    },
    toolbox: {
        show: true,
        feature: {
            mark: {show: true},
            dataView: {show: true, readOnly: false},
            // magicType: {show: true, type: ['line', 'bar', 'stack', 'tiled']},
            // restore: {show: true},
            // saveAsImage: {show: true}
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
