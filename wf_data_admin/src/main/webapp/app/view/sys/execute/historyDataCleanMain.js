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
                        var data= {
                            startTime: Ext.util.Format.date(me.down("[name='startDate']").getValue(),'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='endDate']").getValue(),'Y-m-d H:i:s')
                        };
                        callapi("data/admin/dataClean/channelInfoHour.do", data, function (result) {
                            if (result.success) {
                                Ext.Msg.show({
                                    title: "提示",
                                    msg: result.data.msg,
                                    modal: true,
                                    icon: Ext.Msg.INFO,
                                    buttons: Ext.Msg.OK
                                });
                            } else {
                                Ext.Msg.show({
                                    title: '错误',
                                    msg: result.data.msg,
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

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_user_sign_day',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_user_sign_day历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data= {
                            startTime: Ext.util.Format.date(me.down("[name='start']").getValue(),'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='end']").getValue(),'Y-m-d H:i:s')
                        };
                        callapi("data/admin/dataClean/platSignedUser.do", data, function (result) {
                            if (result.success) {
                                Ext.Msg.show({
                                    title: "提示",
                                    msg: result.data.msg,
                                    modal: true,
                                    icon: Ext.Msg.INFO,
                                    buttons: Ext.Msg.OK
                                });
                            } else {
                                Ext.Msg.show({
                                    title: '错误',
                                    msg: result.data.msg,
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
                name: 'start',
                fieldLabel: '开始日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }, {
                name: 'end',
                fieldLabel: '结束日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_final_channel_info_all',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_final_channel_info_all历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data= {
                            startTime: Ext.util.Format.date(me.down("[name='infoStart']").getValue(),'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='infoEnd']").getValue(),'Y-m-d')
                        };
                        callapi("data/admin/dataClean/channelInfoAll.do", data, function (result) {
                            if (result.success) {
                                Ext.Msg.show({
                                    title: "提示",
                                    msg: result.data.msg,
                                    modal: true,
                                    icon: Ext.Msg.INFO,
                                    buttons: Ext.Msg.OK
                                });
                            } else {
                                Ext.Msg.show({
                                    title: '错误',
                                    msg: result.data.msg,
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
                name: 'infoStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'infoEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_betting_log_day',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_betting_log_day历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data= {
                            startTime: Ext.util.Format.date(me.down("[name='bettingStart']").getValue(),'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='bettingEnd']").getValue(),'Y-m-d')
                        };
                        callapi("data/admin/dataClean/bettingLogDay.do", data, function (result) {
                            if (result.success) {
                                Ext.Msg.show({
                                    title: "提示",
                                    msg: result.data.msg,
                                    modal: true,
                                    icon: Ext.Msg.INFO,
                                    buttons: Ext.Msg.OK
                                });
                            } else {
                                Ext.Msg.show({
                                    title: '错误',
                                    msg: result.data.msg,
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
                name: 'bettingStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'bettingEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_betting_log_hour',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_betting_log_hour历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data= {
                            startTime: Ext.util.Format.date(me.down("[name='hourStart']").getValue(),'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='hourEnd']").getValue(),'Y-m-d H:i:s')
                        };
                        callapi("data/admin/dataClean/bettingLogHour.do", data, function (result) {
                            if (result.success) {
                                Ext.Msg.show({
                                    title: "提示",
                                    msg: result.data.msg,
                                    modal: true,
                                    icon: Ext.Msg.INFO,
                                    buttons: Ext.Msg.OK
                                });
                            } else {
                                Ext.Msg.show({
                                    title: '错误',
                                    msg: result.data.msg,
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
                name: 'hourStart',
                fieldLabel: '开始日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }, {
                name: 'hourEnd',
                fieldLabel: '结束日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }]
        });
    }
});