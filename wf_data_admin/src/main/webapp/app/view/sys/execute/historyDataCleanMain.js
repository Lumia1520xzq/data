Ext.define('WF.view.sys.execute.historyDataCleanMain', {
    extend: 'Ext.panel.Panel',
    title: '历史数据清洗',
    xtype: 'historyDataCleanMain',
    closable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var store = Ext.create('DCIS.Store', {
            autoLoad: false,
        });
        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_final_channel_info_hour',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_final_channel_info_hour历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='startDate']").getValue(),'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='endDate']").getValue(),'Y-m-d H:i:s')
                        };
                        callapi("data/admin/dataClean/channelInfoHour.do", data, function (result) {
                            if (result.success) {
                                Ext.Msg.show({
                                    title: "提示",
                                    msg: result.data.msg + "成功",
                                    modal: true,
                                    icon: Ext.Msg.INFO,
                                    buttons: Ext.Msg.OK
                                });
                            } else {
                                Ext.Msg.show({
                                    title: '错误',
                                    msg: result.data.msg + "失败",
                                    buttons: Ext.Msg.OK,
                                    icon: Ext.Msg.ERROR,
                                    modal: true
                                });
                            }
                        }, null, null, false);
                    }
                });

            },
            items: [{
                name: 'startDate',
                fieldLabel: '开始日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }, {
                name: 'endDate',
                fieldLabel: '结束日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }]
        });


    }
});