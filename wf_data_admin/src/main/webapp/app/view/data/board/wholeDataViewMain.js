Ext.define('WF.view.data.board.wholeDataViewMain', {
    extend: 'Ext.panel.Panel',
    title: '数据概览-整体数据概览',
    xtype: 'wholeDataViewMain',
    closable: true,
    autoScroll:true,
    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    initComponent: function () {
        var me = this;
        me.callParent(arguments);
        var store= Ext.create('DCIS.Store', {
            url:'data/board/view/getList.do',
            autoload:false,
            fields: ['businessDate','dau','rechargeAmount','rechargeCount',
                'newUsers','userCount','bettingRate',
                'dauPayRate','bettingPayRate','userBettingRate',
                'bettingAmount','resultRate',
                'payArpu','payArppu',
                'usersDayRetention','dayRetention','usersRate',
                'totalCost','costRate',
                //以下是环比的数据字段
                "dayDauRate", "weekDauRate",
                "dayRechargeAmountRate", "weekRechargeAmountRate",
                "dayRechargeCountRate", "weekRechargeCountRate",
                "dayNewUsersRate", "weekNewUsersRate",
                "dayUserCountRate", "weekUserCountRate",
                "dayBettingRate", "weekBettingRate",
                "dayDauPayRate", "weekDauPayRate",
                "dayBettingPayRate", "weekBettingPayRate",
                "dayUserBettingRate", "weekUserBettingRate",
                "dayBettingAmountRate", "weekBettingAmountRate",
                "dayResultRate", "weekResultRate",
                "dayPayArpuRate", "weekPayArpuRate",
                "dayPayArppuRate", "weekPayArppuRate",
                "dayUsersDayRetentionRate", "weekUsersDayRetentionRate",
                "dayDayRetentionRate", "weekDayRetentionRate",
                "dayUsersRate", "weekUsersRate",
                "dayTotalCost", "weekTotalCost",
                "dayCostRate", "weekCostRate"
            ]
        });
        var parentChannelStore = Ext.create('DCIS.Store', {
            url: 'data/admin/common/data/getViewChannels.do',
            autoLoad: true,
            fields: ['id', 'name']
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
            items: [{
                name: 'parentId',
                fieldLabel: '主渠道',
                xtype: 'combo',
                emptyText: "--请选择--",
                displayField: 'name',
                valueField: "id",
                editable: true,
                queryMode: "local",
                store: parentChannelStore
            },
                // {
                // xtype:'searchfield',
                // store:parentChannelStore,
                // displayField : 'name',
                // valueField : 'id'
                // },
                {
                    name: 'startTime',
                    id:"viewStart",
                    fieldLabel: '开始时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-7),"Y-m-d")
                },{
                    name: 'endTime',
                    id:"viewEnd",
                    fieldLabel: '结束时间',
                    xtype: 'datefield',
                    format: 'Y-m-d',
                    value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.DAY,-1),"Y-m-d")
                }]
        });

        me.add({
            xtype:'panel',
            layout:'vbox',
            align : 'stretch',
            bodyStyle:'border-width:0 0 0 0;',
            items: [{
                title: '核心指标',align:'stretch',height:300,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0;',
                items:[
                    {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                        items:[
                               {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                items:[
                                    {width:"2%",height:60,bodyStyle:'border-width:0'},
                                    {width:"34.33%",height:60,html:'<h2>DAU</h2>',bodyStyle:'border-width:0'},
                                    {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                        items:[
                                            {width:"100%",height:10,bodyStyle:'border-width:0'},
                                            {id:'date0',width:"100%",height:20,bodyStyle:'border-width:0'},
                                            {id:'dau',width:"100%",height:30,bodyStyle:'border-width:0'}
                                        ]
                                    },
                                    {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                        items:[
                                            {width:"100%",height:20,bodyStyle:'border-width:0'},
                                            {id:'dayDauRate',width:"100%",height:20,bodyStyle:'border-width:0'},
                                            {id:'weekDauRate',width:"100%",height:20,bodyStyle:'border-width:0'}
                                        ]
                                    }
                                ]
                               },
                               {width:"100%",height:240,id:"kpi0"}
                        ]
                    },
                    {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                        items:[
                            {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                items:[
                                    {width:"2%",height:60,bodyStyle:'border-width:0'},
                                    {width:"34.33%",height:60,html:'<h2>充值金额</h2>',bodyStyle:'border-width:0'},
                                    {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                        items:[
                                            {width:"100%",height:10,bodyStyle:'border-width:0'},
                                            {id:'date1',width:"100%",height:20,bodyStyle:'border-width:0'},
                                            {id:'rechargeAmount',width:"100%",height:30,bodyStyle:'border-width:0'}
                                        ]
                                    },
                                    {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                        items:[
                                            {width:"100%",height:20,bodyStyle:'border-width:0'},
                                            {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                            {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                        ]
                                    }
                                ]
                            },
                            {width:"100%",height:240,id:"kpi1"}
                        ]
                    },
                    {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                        items:[
                            {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                items:[
                                    {width:"2%",height:60,bodyStyle:'border-width:0'},
                                    {width:"34.33%",height:60,html:'<h2>充值人数</h2>',bodyStyle:'border-width:0'},
                                    {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                        items:[
                                            {width:"100%",height:10,bodyStyle:'border-width:0'},
                                            {id:'date2',width:"100%",height:20,bodyStyle:'border-width:0'},
                                            {id:'rechargeCount',width:"100%",height:30,bodyStyle:'border-width:0'}
                                        ]
                                    },
                                    {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                        items:[
                                            {width:"100%",height:20,bodyStyle:'border-width:0'},
                                            {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                            {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                        ]
                                    }
                                ]
                            },
                            {width:"100%",height:240,id:"kpi2"}
                        ]
                      }
                    ]
            },
                {
                    align:'stretch',height:300,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>新增用户</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date3',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'newUsers',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi3"}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>投注人数</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date4',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'userCount',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi4"}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>投注转化率</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date5',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'bettingRate',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi5"}
                            ]
                        }
                    ]
                },
                {
                    align:'stretch',height:300,width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>DAU付费转化率</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date6',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'dauPayRate',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi6"}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>投注付费转化率</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date7',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'bettingPayRate',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi7"}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>新用户投注转化率</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date8',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'userBettingRate',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi8"}
                            ]
                        }
                        ]
                },
                {
                    title: '投注数据',height:300,align:'stretch',width:"100%", xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>投注流水</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date9',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'bettingAmount',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi9"}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>返奖率</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date10',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'resultRate',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi10"}
                            ]
                        }
                    ]
                },
                {
                    title: '留存数据',height:300,align:'stretch',width:"100%", xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>新用户留存</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date13',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'usersDayRetention',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi13"}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>全量用户留存</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date14',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'dayRetention',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi14"}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>新用户占比</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date15',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'usersRate',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi15"}
                            ]
                        }
                    ]
                },
                {
                    title: '付费数据',height:300,align:'stretch', width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>ARPU</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date11',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'payArpu',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi11"}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>ARPPU</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date12',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'payArppu',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi12"}
                            ]
                        }
                    ]
                },
                {
                    title: '成本数据',height:300,align:'stretch', width:"100%",xtype:"panel",layout:'hbox',forceFit:true,bodyStyle:'border-width:0 0 0 0;',
                    items:[
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>成本</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date16',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'totalCost',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi16"}
                            ]
                        },
                        {width:"33.33%",height:300,xtype:"panel",layout:'vbox',forceFit:true,bodyStyle:'border-width:0',
                            items:[
                                {width:"100%",height:60,layout:'hbox',forceFit:true,bodyStyle:'border-width:0',
                                    items:[
                                        {width:"2%",height:60,bodyStyle:'border-width:0'},
                                        {width:"34.33%",height:60,html:'<h2>成本占比</h2>',bodyStyle:'border-width:0'},
                                        {width:"30.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:10,bodyStyle:'border-width:0'},
                                                {id:'date17',width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {id:'costRate',width:"100%",height:30,bodyStyle:'border-width:0'}
                                            ]
                                        },
                                        {width:"33.33%",height:60,layout:'vbox',bodyStyle:'border-width:0',
                                            items:[
                                                {width:"100%",height:20,bodyStyle:'border-width:0'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'日环比:333'},
                                                {width:"100%",height:20,bodyStyle:'border-width:0',html:'周同比:444'}
                                            ]
                                        }
                                    ]
                                },
                                {width:"100%",height:240,id:"kpi17"}
                            ]
                        }
                    ]
                }
            ]
        });


        store.addListener('datachanged',function(){
                fun1();
            }
        );

        store.load ({
            callback:function(){
                fun1();
            }
        });

        function fun1(){
            var businessDate=[];
            var dau=[];
            var rechargeAmount=[];
            var rechargeCount=[];
            var newUsers=[];
            var userCount=[];
            var bettingRate=[];
            var dauPayRate=[];
            var bettingPayRate=[];
            var userBettingRate=[];
            var bettingAmount=[];
            var resultRate=[];
            var payArpu=[];
            var payArppu=[];
            var usersDayRetention=[];
            var dayRetention=[];
            var usersRate=[];
            var totalCost=[];
            var costRate=[];
            for(var i=0;i<store.getCount();i++){
                var re=store.getAt(i);
                var d = re.get('businessDate');
                var x = d.indexOf('-');
                businessDate[i] = d.substring(x+1);
                dau[i]=re.get('dau');
                rechargeAmount[i]=re.get('rechargeAmount');
                rechargeCount[i]=re.get('rechargeCount');
                newUsers[i]=re.get('newUsers');
                userCount[i]=re.get('userCount');
                bettingRate[i]=re.get('bettingRate');
                dauPayRate[i]=re.get('dauPayRate');
                bettingPayRate[i]=re.get('bettingPayRate');
                userBettingRate[i]=re.get('userBettingRate');
                bettingAmount[i]=re.get('bettingAmount');
                resultRate[i]=re.get('resultRate');
                payArpu[i]=re.get('payArpu');
                payArppu[i]=re.get('payArppu');
                usersDayRetention[i]=re.get('usersDayRetention');
                dayRetention[i]=re.get('dayRetention');
                usersRate[i]=re.get('usersRate');
                totalCost[i]=re.get('totalCost');
                costRate[i]=re.get('costRate');
            }

            var option = [
                {
                    // title: {text: 'DAU'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                        },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: 'DAU',
                        type: 'line',
                        smooth:true,
                        data: dau
                    }]
                },
                {
                    // title: {text: '充值金额'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: '充值金额',
                        type: 'line',
                        smooth:true,
                        data: rechargeAmount
                    }]
                },
                {
                    // title: {text: '充值人数'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: '充值人数',
                        type: 'line',
                        smooth:true,
                        data: rechargeCount
                    }]
                },
                {
                    // title: {text: '新增用户'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: '新增用户',
                        type: 'line',
                        smooth:true,
                        data: newUsers
                    }]
                },
                {
                    // title: {text: '投注人数'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: '投注人数',
                        type: 'line',
                        smooth:true,
                        data: userCount
                    }]
                },
                {
                    // title: {text: '投注转化率'},
                    tooltip: {trigger: 'axis',
                    formatter: function (params) {
                        var str='';
                        for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value +'%';
                        }
                        return str;
                    }
                        },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: [{
                        name: '投注转化率',
                        type: 'line',
                        smooth:true,
                        data: bettingRate
                    }]
                },
                {
                    // title: {text: 'DAU付费转化率'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: [{
                        name: 'DAU付费转化率',
                        type: 'line',
                        smooth:true,
                        data: dauPayRate
                    }]
                },
                {
                    // title: {text: '投注付费转化率'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: [{
                        name: '投注付费转化率',
                        type: 'line',
                        smooth:true,
                        data: bettingPayRate
                    }]
                },
                {
                    // title: {text: '新用户投注转化率'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }},
                    series: [{
                        name: '新用户投注转化率',
                        type: 'line',
                        smooth:true,
                        data: userBettingRate
                    }]
                },
                {
                    // title: {text: '投注流水'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'17%',//组件距离容器左边的距离
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: '投注流水',
                        type: 'line',
                        smooth:true,
                        data: bettingAmount
                    }]
                },
                {
                    // title: {text: '返奖率'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        min:60,
                        axisLabel : {
                            formatter: '{value}%'
                        }
                        },
                    series: [{
                        name: '返奖率',
                        type: 'line',
                        smooth:true,
                        data: resultRate
                    }]
                },
                {
                    // title: {text: 'ARPU'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: 'ARPU',
                        type: 'line',
                        smooth:true,
                        data: payArpu
                    }]
                },
                {
                    // title: {text: 'ARPPU'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }},
                    series: [{
                        name: 'ARPPU',
                        type: 'line',
                        smooth:true,
                        itemStyle: {normal: {}},
                        data: payArppu
                    }]
                },
                {
                    // title: {text: '新用户留存'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }
                    },
                    series: [{
                        name: '新用户留存',
                        type: 'line',
                        smooth:true,
                        itemStyle: {normal: {}},
                        data: usersDayRetention
                    }]
                },
                {
                    // title: {text: '全量用户留存'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }
                    },
                    series: [{
                        name: '全量用户留存',
                        type: 'line',
                        smooth: true,
                        itemStyle: {normal: {}},
                        data: dayRetention
                    }]
                },
                {
                    // title: {text: '新用户占比'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        }
                    },
                    series: [{
                        name: '新用户占比',
                        type: 'line',
                        smooth: true,
                        itemStyle: {normal: {}},
                        data: usersRate
                    }]
                },
                {
                    // title: {text: '成本'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value;
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true}
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'
                        }
                    },
                    series: [{
                        name: '成本',
                        type: 'line',
                        smooth: true,
                        itemStyle: {normal: {}},
                        data:totalCost
                    }]
                },
                {
                    // title: {text: '成本占比'},
                    tooltip: {trigger: 'axis',
                        formatter: function (params) {
                            var str='';
                            for(var i = 0; i < params.length; i++){
                                str += '日期:'+params[i].name+'<br/>'+ params[i].seriesName +':' + params[i].value + '%';
                            }
                            return str;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true},
                        }
                    },
                    calculable : true,
                    grid:{
                        left:'11%',
                        y:'2.8%'
                    },
                    xAxis: {
                        type : 'category',
                        boundaryGap : false,
                        data: businessDate
                    },
                    yAxis: {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}'+'%'
                        }
                    },
                    series: [{
                        name: '成本占比',
                        type: 'line',
                        smooth: true,
                        itemStyle: {normal: {}},
                        data:costRate
                    }]
                }
            ];
            for (var j=0;j<option.length;j++) {
                me.echarts = echarts.init(Ext.get("kpi"+j).dom);
                me.echarts.setOption(option[j]);
            }
            var r = store.getAt(store.getCount()-1);
            for(var k=0;k<option.length;k++){
                var date = Ext.get('date'+k).dom;
                date.innerHTML = r.get('businessDate');
            }
             Ext.get('dau').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('dau')+"</strong>";
             Ext.get('rechargeAmount').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('rechargeAmount')+"</strong>";
             Ext.get('rechargeCount').dom.innerHTML = "<strong style='font-size:18px'>&nbsp;&nbsp;"+r.get('rechargeCount')+"</strong>";
             Ext.get('newUsers').dom.innerHTML = "<strong style='font-size:18px'>&nbsp;&nbsp;"+r.get('newUsers')+"</strong>";
             Ext.get('userCount').dom.innerHTML = "<strong style='font-size:18px'>&nbsp;"+r.get('userCount')+"</strong>";
             Ext.get('bettingRate').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('bettingRate')+"%</strong>";
             Ext.get('dauPayRate').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('dauPayRate')+"%</strong>";
             Ext.get('bettingPayRate').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('bettingPayRate')+"%</strong>";
             Ext.get('userBettingRate').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('userBettingRate')+"%</strong>";
             Ext.get('bettingAmount').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('bettingAmount')+"</strong>";
            Ext.get('resultRate').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('resultRate')+"%</strong>";
            Ext.get('payArpu').dom.innerHTML = "<strong style='font-size:18px'>&nbsp;"+r.get('payArpu')+"</strong>";
            Ext.get('payArppu').dom.innerHTML = "<strong style='font-size:18px'>&nbsp;"+r.get('payArppu')+"</strong>";
            Ext.get('usersDayRetention').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('usersDayRetention')+"%</strong>";
            Ext.get('dayRetention').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('dayRetention')+"%</strong>";
            Ext.get('usersRate').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('usersRate')+"%</strong>";
            Ext.get('totalCost').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('totalCost')+"</strong>";
            Ext.get('costRate').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('costRate')+"%</strong>";

            Ext.get('dayDauRate').dom.innerHTML = "<strong style='font-size:18px'>"+r.get('dayDauRate')+"</strong>";



        }
    }
});

