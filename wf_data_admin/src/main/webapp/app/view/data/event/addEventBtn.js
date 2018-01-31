Ext.define("WF.view.data.event.addEventBtn", {
    extend: "Ext.Button",
    alias: "addEventBtn",
    text : '新增',
    iconCls : 'icon-add',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
            var main = Ext.ComponentQuery.query("eventMain")[0];
            var doRefresh = main.down('datagrid').store;
            var win = Ext.create("WF.view.data.event.addEvent", {doRefresh: doRefresh});
            win.show();
        }
    }
});