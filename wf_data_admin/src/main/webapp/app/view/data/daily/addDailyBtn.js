Ext.define("WF.view.data.daily.addDailyBtn", {
    extend: "Ext.Button",
    alias: "addDailyBtn",
    text : '新增',
    iconCls : 'icon-add',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
            var main = Ext.ComponentQuery.query("dailyMain")[0];
            var doRefresh = main.down('datagrid').store;
            var win = Ext.create("WF.view.data.daily.addDaily", {doRefresh: doRefresh});
            win.show();
        }
    }
});