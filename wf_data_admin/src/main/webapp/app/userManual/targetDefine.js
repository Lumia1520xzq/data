Ext.define('WF.userManual.targetDefine', {
    extend : 'Ext.panel.Panel',
    title : '指标定义',
    plain : true,
    layout : {
        type : 'vbox',
        align : 'stretch'
    },
    border : false,
    statics : {
        openAl : function(code) {

        }
    },
    initComponent : function() {
        var me=this;
        this.callParent(arguments);
        var me = this;
        var targetTypeStore = Ext.create('DCIS.Store', {
            url: '/data/admin/common/data/getValueByType.do',
            fields: ['label', 'value'],
            baseParams: {
                type: 'target_type'
            }
        });
        targetTypeStore.load();
        var store = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: '/data/usermanual/targetdefine/list.do',
            fields: ['id', 'targetType', 'targetName', 'targetDefine']
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
                allowBlank: true,
                name: 'targetType',
                fieldLabel: '指标类型',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false,
                queryMode: "local",
                store: targetTypeStore
            }],
            export: function () {
                window.location.href = '/data/usermanual/targetdefine/export.do?targetType=' + me.down(("[name='targetType']")).value;
            },
        });
        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: '序号',
                dataIndex: 'id',
                width: 10,
                menuDisabled: true,
                sortable: false
            }, {
                text: '指标类型',
                width: 30,
                dataIndex: 'targetType',
                renderer: function (value) {
                    var index = targetTypeStore.find('value', value);
                    if (index != -1) {
                        var record = targetTypeStore.getAt(index);
                        return record.data.label;
                    }
                    return '--';
                }
            }, {
                text: '指标名称',
                width: 30,
                dataIndex: 'targetName',
                menuDisabled: true,
                sortable: false
            }, {
                text: '指标定义',
                width: 60,
                dataIndex: 'targetDefine',
                menuDisabled: true,
                sortable: false
            }
            ]
        });
    }

});