Ext.define('WF.view.data.event.editEvent', {
    extend: 'Ext.window.Window',
    alias: 'editEvent',
    title: '编辑',
    modal: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;

        var eventTypeStor = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'event_type'
            }
        });

        me.add({
            xtype: 'dataform',
            baseCls: 'x-plain',
            border: true,
            columns: 2,
            items: [{
                allowBlank: true,
                colspan: 2,
                name: 'channelId',
                xtype: 'searchfield',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: false,
                queryMode: "local",
                store: 'channelStore',
                fieldLabel: '渠道'
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
            },{
                afterLabelTextTpl: required,
                allowBlank: false,
                colspan: 2,
                name: 'eventType',
                fieldLabel: '埋点类型',
                xtype: 'combobox',
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false,
                queryMode: "local",
                store: eventTypeStor
            },{
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'title',
                colspan: 2,
                fieldLabel: '标题'
            },{
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'content',
                xtype: 'textarea',
                colspan: 2,
                fieldLabel: '事记'
            }, {
                xtype: 'hidden',
                name: 'id'
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
            callapi("data/admin/event/save.do", form.getValues(),
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