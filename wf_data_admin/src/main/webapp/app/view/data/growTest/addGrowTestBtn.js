Ext.define("WF.view.data.growTest.addGrowTestBtn", {
    extend: "Ext.Button",
    alias: "addGrowTestBtn",
    text : '新增',
    iconCls : 'icon-add',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
            var main = Ext.ComponentQuery.query("growTestMain")[0];
            var doRefresh = main.down('datagrid').store;
            var win = Ext.create("WF.view.data.growTest.addGrowTest", {doRefresh: doRefresh});
            win.show();
        }
    }
});