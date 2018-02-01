Ext.define('WF.view.data.daily.dailyMain', {
    extend: 'Ext.panel.Panel',
    title: '日报数据管理',
    xtype: 'dailyMain',
    closable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var store = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dailyRecord/list.do',
            fields: ['id', 'channelId', 'dataTime', 'indicatorType', 'phenomenon', 'analysisSummary', 'followUp',
                'followUpUser', 'creater', 'createTime', 'updater', 'updateTime']
        });

        var indicatorTypeStor = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'indicator_type'
            }
        });

        var allChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getAllChannels.do',
            fields: ['id', 'name']
        });

        allChannelStore.load({
            callback: function () {
                sync:true
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
            forceFit: true,
            items: [{
                name: 'channelId',
                fieldLabel: '渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: allChannelStore
            }, {
                name: 'indicatorType',
                fieldLabel: '类别',
                xtype: 'combobox',
                store: indicatorTypeStor,
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false
            }, {
                xtype: 'datefield',
                name: 'beginDate',
                format: 'Y-m-d',
                fieldLabel: '开始时间',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -1), "Y-m-d")
            }, {
                xtype: 'datefield',
                name: 'endDate',
                format: 'Y-m-d',
                fieldLabel: '结束时间',
                value: Ext.util.Format.date(Ext.Date.add(new Date(), Ext.Date.DAY, -1), "Y-m-d")
            }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                xtype: 'datecolumn',
                text: '数据日期',
                width: 80,
                dataIndex: 'dataTime',
                format: 'Y-m-d',
                menuDisabled: true,
                sortable: false
            }, {
                text: '渠道',
                width: 150,
                dataIndex: 'channelId',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    console.log("channelID:" + value);
                    var index = allChannelStore.find('id', value);
                    if (index != -1) {
                        var record = allChannelStore.getAt(index);
                        return record.data.name;
                    }
                    return '--';
                }
            }, {
                text: '指标',
                width: 80,
                dataIndex: 'indicatorType',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var record = indicatorTypeStor.findRecord('value', value, 0, false, false, true);
                    if (record == null) {
                        return '--';
                    } else {
                        return record.data.label;
                    }
                }
            }, {
                text: '现象',
                width: 150,
                dataIndex: 'phenomenon',
                menuDisabled: true,
                sortable: false
            }, {
                text: '分析总结',
                width: 300,
                dataIndex: 'analysisSummary',
                menuDisabled: true,
                sortable: false
            }, {
                text: '录入时间',
                width: 130,
                dataIndex: 'createTime',
                menuDisabled: true,
                sortable: false
            }, {
                text: '录入人',
                width: 70,
                dataIndex: 'creater',
                menuDisabled: true,
                sortable: false
            }, {
                text: '修改时间',
                width: 130,
                dataIndex: 'updateTime',
                menuDisabled: true,
                sortable: false
            }, {
                text: '修改人',
                width: 70,
                dataIndex: 'updater',
                menuDisabled: true,
                sortable: false
            }, {
                text: '待跟进',
                width: 150,
                dataIndex: 'followUp',
                menuDisabled: true,
                sortable: false
            }, {
                text: '跟进人',
                width: 100,
                dataIndex: 'followUpUser',
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});