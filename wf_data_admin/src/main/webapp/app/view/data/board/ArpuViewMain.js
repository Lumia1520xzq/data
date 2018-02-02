Ext.define('WF.view.data.board.ArpuViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-用户累计ARPU',
    xtype: 'ArpuViewMain',
    closable: true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    initComponent: function () {
        this.callParent(arguments);
        var me = this;
        var store = Ext.create('DCIS.Store', {
            autoLoad:false,
            url: 'data/arpu/view/getList.do',
            fields: ["channelName","businessDate",'newUsers',
                'arpu1','arpu2','arpu3','arpu4','arpu5','arpu6',
                'arpu7','arpu15','arpu30','arpu60','arpu90']
        });
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getParentChannels.do',
            autoLoad: true,
            fields: ['id', 'name'
            ]
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
            items: [
            {
                name: 'parentId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: parentChannelStore
            },{
                name: 'beginDate',
                fieldLabel: '开始日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            },{
                name: 'endDate',
                fieldLabel: '结束日期',
                xtype: 'datefield',
                format: 'Y-m-d'

            }]
        });


        store.load({
            callback:function(){
                fun1();
            }
        });

        // store.addListener('datachanged',function(){
        //         fun1();
        //     }
        // );


        function fun1(){
            me.add({
                xtype: 'datagrid',
                store: store,
                buildField: "Manual",
                columns: [
                    {
                        text: '渠道',
                        width:100,
                        dataIndex: 'channelName',
                        menuDisabled: true,
                        sortable: false
                    }
                    , {
                        text: '注册日期',
                        width:100,
                        dataIndex: 'businessDate',
                        menuDisabled: true,
                        sortable: false
                    },
                    {
                        text: '新用户数',
                        width:100,
                        dataIndex: 'newUsers',
                        menuDisabled: true,
                        sortable: false
                    },
                    {
                        text:'用户累计ARPU',
                        width:880,
                        columns:[
                            {
                                text: '1天',
                                width: 80,
                                dataIndex: 'arpu1',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '2天',
                                width: 80,
                                dataIndex: 'arpu2',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '3天',
                                width: 80,
                                dataIndex: 'arpu3',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '4天',
                                width: 80,
                                dataIndex: 'arpu4',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '5天',
                                width: 80,
                                dataIndex: 'arpu5',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '6天',
                                width: 80,
                                dataIndex: 'arpu6',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '7天',
                                width: 80,
                                dataIndex: 'arpu7',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '15天',
                                width: 80,
                                dataIndex: 'arpu15',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '30天',
                                width: 80,
                                dataIndex: 'arpu30',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '60天',
                                width: 80,
                                dataIndex: 'arpu60',
                                menuDisabled: true,
                                sortable: false
                            },
                            {
                                text: '90天',
                                width: 80,
                                dataIndex: 'arpu90',
                                menuDisabled: true,
                                sortable: false
                            }
                        ]
                    }
                ]
            });
        }

    }
});