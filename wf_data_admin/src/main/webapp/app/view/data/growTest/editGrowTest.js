Ext.define('WF.view.data.growTest.editGrowTest', {
    extend: 'Ext.window.Window',
    id:"editGrowTestWindow",
    alias: 'editGrowTest',
    title: '编辑',
    modal: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var labelWidth = 100;
        var testTypeStore = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: '/data/admin/common/data/getValueByType.do',
            fields: ['label', 'value'],
            baseParams: {
                type: 'test_type'
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
                name: 'point',
                colspan: 2,
                labelWidth: labelWidth,
                fieldLabel: '测试点'
            },{
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'testTypeId',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'label',
                valueField: "value",
                editable: false,
                queryMode: "local",
                store: testTypeStore,
                colspan: 2,
                fieldLabel: '测试类型'
            },{

                readOnly:true,
                name: 'docUrl',
                colspan: 1,
                fieldLabel: '测试文档'
            },{
                xtype: 'button',
                text: '上传文档',
                colspan: 1,
                handler: function () {
                    var me = this;
                    uploadWord("data/admin/common/data/uploadWord.do", function (re) {
                        console.info(re)
                        var dataform = me.up('dataform');
                        dataform.down("[name='docUrl']").setValue(re.data.data);
                        dataform.down("[name='docShowUrl']").setValue(re.data.url);
                    })
                }
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                name: 'target',
                labelWidth: labelWidth,
                colspan: 2,
                fieldLabel: '测试目标'
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                readOnly: true,
                name: 'testChannel',
                colspan: 1,
                fieldLabel: '测试渠道'
            }, {
                xtype: 'button',
                iconCls: 'icon-tip',
                text: '测试渠道',
                colspan: 1,
                handler: function () {
                    Ext.create("WF.store.editTestChannelStore", {
                        title: '测试渠道'
                    }).show();
                }
            }, {

                readOnly: true,
                name: 'userIds',
                colspan: 1,
                fieldLabel: '用户群'
            }, {
                xtype: 'button',
                iconCls: 'icon-tip',
                text: '用户群',
                colspan: 1,
                handler: function () {
                    Ext.create("WF.store.editUserIdStore", {
                        title: '用户群'
                    }).show();
                }
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                xtype: 'datetimefield',
                name: 'startTime',
                format: 'Y-m-d H:i:s',
                labelWidth: labelWidth,
                colspan: 2,
                fieldLabel: '开始时间',
                editable: false
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                xtype: 'datetimefield',
                name: 'endTime',
                format: 'Y-m-d H:i:s',
                labelWidth: labelWidth,
                fieldLabel: '结束时间',
                colspan: 2,
                editable: false
            }, {
                afterLabelTextTpl: required,
                allowBlank: false,
                xtype: 'textarea',
                name: 'result',
                labelWidth: labelWidth,
                colspan: 2,
                fieldLabel: '测试结果'
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
            callapi("/data/growtest/view/save.do", form.getValues(),
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