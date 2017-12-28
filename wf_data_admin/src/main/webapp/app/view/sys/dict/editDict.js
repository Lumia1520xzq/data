Ext.define('WF.view.sys.dict.editDict', {
    extend: 'Ext.window.Window',
    alias: 'editDict',
    title: '编辑',
    modal: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        me.add({
            xtype: 'dataform',
            baseCls: 'x-plain',
            border: true,
            columns: 2,
            items: [{
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'label',
                colspan: 2,
                fieldLabel: '展示值'
            },{
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'value',
                colspan: 2,
                fieldLabel: '值'
            },{
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'type',
                colspan: 2,
                fieldLabel: 'Key'
            },{
                allowBlank: true,
                name: 'description',
                xtype: 'textarea',
                colspan: 2,
                fieldLabel: '描述'
            },{
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'sort',
                colspan: 2,
                fieldLabel: '排序'
            },{
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
            callapi("data/admin/dict/save.do", form.getValues(),
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