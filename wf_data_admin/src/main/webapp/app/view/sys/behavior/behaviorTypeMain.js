Ext.define('WF.view.sys.behavior.behaviorTypeMain', {
    extend: 'Ext.panel.Panel',
    title: '埋点管理',
    xtype: 'behaviorTypeMain',
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
            url: 'data/admin/behaviorType/list.do',
            fields: ['id', 'name', 'eventId', 'parentEventId', 'fullName', 'channelId', 'behaviorCatagory', 'behaviorLevel', 'isShow', 'subEventId']
        });

        var catagoryStor = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'behavior_type'
            }
        });

        var isShowStor = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'yes_no'
            }
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
            items: [{
                name: 'name',
                fieldLabel: '事件名称'
            }, {
                name: 'eventId',
                fieldLabel: '事件ID'
            }, {
                name: 'parentEventId',
                fieldLabel: '父事件ID'
            }, {
                name: 'behaviorCatagory',
                fieldLabel: '埋点类型',
                xtype: 'combobox',
                store: catagoryStor,
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false
            }, {
                name: 'behaviorLevel',
                fieldLabel: '埋点等级'
            }, {
                name: 'isShow',
                fieldLabel: '是否启用',
                xtype: 'combobox',
                store: isShowStor,
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false
            }]
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '事件名称',
                dataIndex: 'name',
                width: 100,
                menuDisabled: true,
                sortable: false
            }, {
                text: '事件ID',
                width: 50,
                dataIndex: 'eventId',
                menuDisabled: true,
                sortable: false
            }, {
                text: '父事件ID',
                width: 50,
                dataIndex: 'parentEventId',
                menuDisabled: true,
                sortable: false
            }, {
                text: '组合名称',
                dataIndex: 'fullName',
                width: 120,
                menuDisabled: true,
                sortable: false
            }, {
                text: '埋点类型',
                width: 120,
                dataIndex: 'behaviorCatagory',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var record = catagoryStor.findRecord('value', value, 0, false, false, true);
                    if (record == null) {
                        return '--';
                    } else {
                        return record.data.label;
                    }
                }
            }, {
                text: '埋点等级',
                width: 120,
                dataIndex: 'behaviorLevel',
                menuDisabled: true,
                sortable: false
            }, {
                text: '是否启用',
                width: 120,
                dataIndex: 'isShow',
                menuDisabled: true,
                sortable: false,
                renderer: function (value) {
                    var record = isShowStor.findRecord('value', value, 0, false, false, true);
                    if (record == null) {
                        return '--';
                    } else {
                        return record.data.label;
                    }
                }
            }
            ]
        });
    }
});