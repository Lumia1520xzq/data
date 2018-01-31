Ext.define('WF.view.data.event.eventMain', {
    extend: 'Ext.panel.Panel',
    title: '事记管理',
    xtype: 'eventMain',
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
            url: 'data/admin/event/list.do',
            fields: ['id', 'channelId', 'beginDate', 'endDate', 'eventType', 'title',
                     'content', 'creater', 'createrName', 'createTime', 'updater', 'updaterName', 'updateTime']
        });

        var eventTypeStor = Ext.create('DCIS.Store', {
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'event_type'
            }
        });

        var allChannelStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/common/data/getAllChannels.do',
            fields: ['id', 'name']
        });

        allChannelStore.load({
            callback: function () {
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
                name: 'eventType',
                fieldLabel: '类别',
                xtype: 'combobox',
                store: eventTypeStor,
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false
            }, {
                xtype: 'datefield',
                name: 'beginDate',
                format: 'Y-m-d',
                fieldLabel: '开始时间'
            }, {
                xtype: 'datefield',
                name: 'endDate',
                format: 'Y-m-d',
                fieldLabel: '结束时间'
            }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '渠道',
                width: 100,
                dataIndex: 'channelId',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var index = allChannelStore.find('id', value);
                    if (index != -1) {
                        var record = allChannelStore.getAt(index);
                        return record.data.name;
                    }
                    return '--';
                }
            }, {
                text: '开始日期',
                width: 100,
                dataIndex: 'beginDate',
                menuDisabled: true,
                sortable: false
            }, {
                text: '结束日期',
                dataIndex: 'endDate',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '类别',
                width: 120,
                dataIndex: 'eventType',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var record = eventTypeStor.findRecord('value', value, 0, false, false, true);
                    if (record == null) {
                        return '--';
                    } else {
                        return record.data.label;
                    }
                }
            }, {
                text: '事记',
                width: 200,
                dataIndex: 'content',
                menuDisabled: true,
                sortable: false
            }, {
                text: '录入时间',
                width: 100,
                dataIndex: 'createTime',
                menuDisabled: true,
                sortable: false
            }, {
                text: '录入人',
                width: 100,
                dataIndex: 'creater',
                menuDisabled: true,
                sortable: false
            }, {
                text: '修改时间',
                width: 100,
                dataIndex: 'updateTime',
                menuDisabled: true,
                sortable: false
            }, {
                text: '修改人',
                width: 100,
                dataIndex: 'updater',
                menuDisabled: true,
                sortable: false
            }]
        });
    }
});