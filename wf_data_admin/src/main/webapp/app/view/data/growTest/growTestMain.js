Ext.define('WF.view.data.growTest.growTestMain', {
    extend: 'Ext.panel.Panel',
    title: '增长测试点管理',
    xtype: 'growTestMain',
    closable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        /*var testChannelStore = Ext.create('DCIS.Store', {
            url: '/data/admin/common/data/getValueByType.do',
            fields: ['label', 'value'],
            baseParams: {
                type: 'test_channel'
            }
        });
        testChannelStore.load();*/
        var childChannelStore = Ext.create('DCIS.Store', {
            url: '/data/admin/common/data/getChildChannels.do',
            fields: ['id', 'name'],
            baseParams: {
                type: 'test_channel'
            }
        });
        childChannelStore.load();
        var testTypeStore = Ext.create('DCIS.Store', {
            url: '/data/admin/common/data/getValueByType.do',
            fields: ['label', 'value'],
            baseParams: {
                type: 'test_type'
            }
        });
        testTypeStore.load();
        var store = Ext.create('DCIS.Store', {
            autoLoad: true,
            url: '/data/growtest/view/list.do',
            fields: ['id', 'point', 'testTypeId', 'docUrl', 'docShowUrl', 'testChannel', 'target', 'userIds', 'startTime', 'endTime', 'result', 'showUpdateTime', 'operationUsername']
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
            export: function () {
                window.location.href = '/data/growtest/view/export.do?point=' + me.down(("[name='point']")).value +
                    '&testChannel=' + me.down(("[name='testChannel']")).value +
                    '&beginDate=' + me.down(("[name='beginDate']")).value +
                    '&operationUsername=' + me.down(("[name='operationUsername']")).value;
            },
            items: [{
                name: 'point',
                fieldLabel: '测试点'
            }, {
                allowBlank: true,
                name: 'testChannel',
                fieldLabel: '测试渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "name",
                editable: false,
                queryMode: "local",
                store: childChannelStore
            }, {
                xtype: 'datetimefield',
                name: 'beginDate',
                format: 'Y-m-d H:i:s',
                fieldLabel: '开始时间',
                editable: false
            }, {
                name: 'operationUsername',
                fieldLabel: '维护人'
            }]
        });

        me.add({
            xtype: 'datagrid',
            store: store,
            buildField: "Manual",
            forceFit: true,
            columns: [{
                text: 'ID',
                dataIndex: 'id',
                width: 10,
                menuDisabled: true,
                sortable: false
            }, {
                text: '测试点',
                dataIndex: 'point',
                width: 50,
                menuDisabled: true,
                sortable: false
            }, {
                text: '测试类型',
                width: 30,
                dataIndex: 'testTypeId',
                renderer: function (value) {
                    var index = testTypeStore.find('value', value);
                    if (index != -1) {
                        var record = testTypeStore.getAt(index);
                        return record.data.label;
                    }
                    return '--';
                }
            },  {
                text: '测试文档',
                width: 30,
                menuDisabled: true,
                sortable: false,
                align : 'center',
                renderer: function (value, meta, record) {
                    if(record.data.docUrl != ''){
                        return "<a href=\"https://file.beeplay123.com"+record.data.docUrl+"\">" + "点击下载" + "</href>";
                    }
                }
            }, {
                text: '测试渠道',
                dataIndex: 'testChannel',
                width: 50,
                menuDisabled: true,
                sortable: false
            }, {
                text: '测试目标',
                dataIndex: 'target',
                width: 80,
                menuDisabled: true,
                sortable: false
            }, {
                text: '用户群（ID尾号）',
                dataIndex: 'userIds',
                width: 40,
                menuDisabled: true,
                sortable: false
            }, {
                text: '开始时间',
                dataIndex: 'startTime',
                width: 40,
                menuDisabled: true,
                sortable: false
            }, {
                text: '结束时间',
                dataIndex: 'endTime',
                width: 40,
                menuDisabled: true,
                sortable: false
            }, {
                xtype: 'linkColumn',
                header: '测试结果',
                width: 30,
                links: [{
                    linkText: '结果',
                    handler: function (grid, rowIndex, colIndex, record) {
                        var win = Ext.create("WF.view.data.growTest.viewResult", {
                            store: store,
                            id: record.data.id
                        });
                        win.down('dataform').setValues(record.data);
                        win.show();
                    }
                }]
            }, {
                text: '维护日期',
                dataIndex: 'showUpdateTime',
                width: 40,
                menuDisabled: true,
                sortable: false
            },{
                text: '维护人',
                dataIndex: 'operationUsername',
                width: 40,
                menuDisabled: true,
                sortable: false
            }]/*,
            listeners: {
                itemclick: function(view, record, item, index, e) {
                    var colIndex = e.getTarget(view.cellSelector).cellIndex;
                    if (colIndex != 9) {
                        return;
                    }
                    if (pictureFile) {
                        pictureFile.close();
                    }
                    pictureFile = Ext.create('Ext.window.Window', {
                        title: '结果',
                        height: 300,
                        width: 500,
                        layout: 'fit',
                        readOnly: true,
                        items: {  // Let's put an empty grid in just to illustrate fit layout
                            xtype: 'textarea',
                            border: false,
                            /!*store: Ext.create('Ext.data.ArrayStore', {})*!/ // 一个假的空的数据存储
                            emptyText: record.data.result
                        }
                    });
                    pictureFile.show();
                }
            }*/

        });
    }
})
;
