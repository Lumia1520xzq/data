Ext.define('WF.view.sys.execute.historyDataCleanMain', {
    extend: 'Ext.panel.Panel',
    title: '历史数据清洗',
    xtype: 'historyDataCleanMain',
    closable: true,
    autoScroll:true,
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
                            startTime: Ext.util.Format.date(me.down("[name='startDate']").getValue(), 'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='endDate']").getValue(), 'Y-m-d H:i:s')
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
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='start']").getValue(), 'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='end']").getValue(), 'Y-m-d H:i:s')
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
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='infoStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='infoEnd']").getValue(), 'Y-m-d')
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
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='bettingStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='bettingEnd']").getValue(), 'Y-m-d')
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
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='hourStart']").getValue(), 'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='hourEnd']").getValue(), 'Y-m-d H:i:s')
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


        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_burying_point_day',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_burying_point_day历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='buryingStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='buryingEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/buryingPointDay.do", data, function (result) {
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
                name: 'buryingStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'buryingEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_burying_point_hour',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_burying_point_hour历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='buryingHourStart']").getValue(), 'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='buryingHourEnd']").getValue(), 'Y-m-d H:i:s')
                        };
                        callapi("data/admin/dataClean/buryingPointHour.do", data, function (result) {
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
                name: 'buryingHourStart',
                fieldLabel: '开始日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }, {
                name: 'buryingHourEnd',
                fieldLabel: '结束日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }]
        });


        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_convert_day',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_convert_day历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='convertStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='convertEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/convertDay.do", data, function (result) {
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
                name: 'convertStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'convertEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_convert_hour',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_convert_hour历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='convertHourStart']").getValue(), 'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='convertHourEnd']").getValue(), 'Y-m-d H:i:s')
                        };
                        callapi("data/admin/dataClean/convertHour.do", data, function (result) {
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
                name: 'convertHourStart',
                fieldLabel: '开始日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }, {
                name: 'convertHourEnd',
                fieldLabel: '结束日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }]
        });


        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全ltv',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全ltv历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='ltvStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='ltvEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/historyLtv.do", data, function (result) {
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
                name: 'ltvStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'ltvEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全registeredArpu',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全registeredArpu历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='arpuStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='arpuEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/registeredArpu.do", data, function (result) {
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
                name: 'arpuStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'arpuEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });


        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全registeredRetention',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全registeredRetention历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='retentionStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='retentionEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/registeredRetention.do", data, function (result) {
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
                name: 'retentionStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'retentionEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全channelConversion',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全channelConversion历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='conversionStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='conversionEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/channelConversion.do", data, function (result) {
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
                name: 'conversionStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'conversionEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全channelCost',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全channelCost历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='costStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='costEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/channelCost.do", data, function (result) {
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
                name: 'costStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'costEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全channelRetention',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全channelRetention历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='channelRetentionStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='channelRetentionEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/channelRetention.do", data, function (result) {
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
                name: 'channelRetentionStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'channelRetentionEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_user_info',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_user_info历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='userInfoStart']").getValue(), 'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='userInfoEnd']").getValue(), 'Y-m-d H:i:s')
                        };
                        callapi("data/admin/dataClean/userInfo.do", data, function (result) {
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
                name: 'userInfoStart',
                fieldLabel: '开始日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }, {
                name: 'userInfoEnd',
                fieldLabel: '结束日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_game_betting_info_hour',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_game_betting_info_hour历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='gameBettingHourStart']").getValue(), 'Y-m-d H:i:s'),
                            endTime: Ext.util.Format.date(me.down("[name='gameBettingHourEnd']").getValue(), 'Y-m-d H:i:s')
                        };
                        callapi("data/admin/dataClean/gameBettingHourInfo.do", data, function (result) {
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
                name: 'gameBettingHourStart',
                fieldLabel: '开始日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }, {
                name: 'gameBettingHourEnd',
                fieldLabel: '结束日期',
                xtype: 'datetimefield',
                format: 'Y-m-d H:i:s'

            }]
        });


        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全all表的rechargeRate',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全rechargeRate历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='rechargeRateStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='rechargeRateEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/historyRate.do", data, function (result) {
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
                name: 'rechargeRateStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'rechargeRateEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });


        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_final_entrance_analysis',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_final_entrance_analysis历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='analysisStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='analysisEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/entranceAnalysis.do", data, function (result) {
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
                name: 'analysisStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'analysisEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '重置dataware_user_info_extend_base',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要重置dataware_user_info_extend_base的数据吗?", function (button) {
                    if (button == "yes") {
                        callapi("data/admin/dataClean/resetUserInfoBase.do", null, function (result) {
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

            }
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '重置dataware_user_info_extend_game',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要重置dataware_user_info_extend_game的数据吗?",function (button) {
                    if (button == "yes") {
                        callapi("data/admin/dataClean/resetUserInfoGame.do", null, function (result) {
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

            }
        });

        me.add({
            border: false,
            store: store,
            xtype: 'searchpanel',
            title: '补全dataware_final_recharge_tag_analysis',
            collapsible: true,
            collapsed: false,
            columns: 2,
            buildField: "Manual",
            forceFit: true,
            todoExec: function () {
                Ext.Msg.confirm("确认", "确定要补全dataware_final_recharge_tag_analysis历史数据吗?", function (button) {
                    if (button == "yes") {
                        var data = {
                            startTime: Ext.util.Format.date(me.down("[name='tagStart']").getValue(), 'Y-m-d'),
                            endTime: Ext.util.Format.date(me.down("[name='tagEnd']").getValue(), 'Y-m-d')
                        };
                        callapi("data/admin/dataClean/tagAnalysis.do", data, function (result) {
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
                name: 'tagStart',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }, {
                name: 'tagEnd',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });
    }
});