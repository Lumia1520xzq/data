Ext.define('WF.view.sys.behavior.editBehaviorType', {
    extend: 'Ext.window.Window',
    alias: 'editSysConfig',
    title: '编辑',
    modal: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var catagoryStor = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: [
                {name: 'value', type: 'string', show: true},
                {name: 'label', type: 'string', show: true}
                ],
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
            xtype: 'dataform',
            baseCls: 'x-plain',
            border: true,
            columns: 2,
            items: [{
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'name',
                colspan: 2,
                fieldLabel: '事件名称'
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                colspan: 2,
                name: 'parentEventId',
                fieldLabel: '父集ID',
                xtype: 'searchfield',
                displayField: function (value) {
                    var record = parentEventStor.findRecord('value', value, 0, false, false, true);
                    if (record == null) {
                        return '';
                    } else {
                        return record.data.label;
                    }
                },
                valueField: "eventId",
                editable: false,
                queryMode: "local",
                store: 'behaviorTypeStore'
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'subEventId',
                colspan: 2,
                fieldLabel: '事件ID'
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                colspan: 2,
                name: 'behaviorCatagory',
                fieldLabel: '埋点类型',
                xtype: 'combobox',
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false,
                queryMode: "local",
                store: catagoryStor
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                colspan: 2,
                name: 'isShow',
                fieldLabel: '启用',
                xtype: 'combobox',
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false,
                queryMode: "local",
                store: isShowStor
            }, {
                xtype: 'hidden',
                name: 'id'
            },{
                xtype: 'hidden',
                name: 'eventId'
            }]
        });
    },
    buttons: [{
        text: '保存',
        iconCls: "icon-ok",
        handler: function () {
            var currentWindow = this.up('window');
            var form = currentWindow.down('dataform').getForm();
            if (!form.isValid()) {
                return;
            }
            var doRefresh = currentWindow.doRefresh;
            callapi("data/admin/behaviorType/save.do", form.getValues(),
                function (result) {
                    if (result.success) {
                        Ext.MessageBox.show({
                            title: "提示",
                            msg: "保存成功",
                            modal: true,
                            icon: Ext.Msg.INFO,
                            buttons: Ext.Msg.OK
                        });
                        doRefresh.reload();
                        currentWindow.close();
                    }
                    else {
                        Ext.Msg.show({
                            title: '错误',
                            msg: result.data.msg,
                            buttons: Ext.Msg.OK,
                            icon: Ext.MessageBox.ERROR,
                            modal: true
                        });
                    }
                });
        }
    }]
});