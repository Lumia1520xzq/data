Ext.define('WF.view.data.daily.addDaily', {
    extend: 'Ext.window.Window',
    alias: 'addDaily',
    title: '新增',
    modal: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;

        var indicatorTypeStor = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: 'data/admin/dict/getListByType.do',
            fields: ['value', 'label'],
            baseParams: {
                type: 'indicator_type'
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
                afterLabelTextTpl: required,
                allowBlank: false,
                xtype: 'datefield',
                name: 'dataTime',
                format: 'Y-m-d',
                fieldLabel: '数据日期'
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                colspan: 2,
                name: 'indicatorType',
                fieldLabel: '指标',
                xtype: 'combobox',
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false,
                queryMode: "local",
                store: indicatorTypeStor
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'phenomenon',
                colspan: 2,
                fieldLabel: '现象'
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'analysisSummary',
                xtype: 'textarea',
                colspan: 2,
                fieldLabel: '分析总结'
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'followUp',
                colspan: 2,
                fieldLabel: '待跟进'
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'followUpUser',
                colspan: 2,
                fieldLabel: '跟进人'
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
            callapi("data/admin/dailyRecord/save.do", form.getValues(),
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