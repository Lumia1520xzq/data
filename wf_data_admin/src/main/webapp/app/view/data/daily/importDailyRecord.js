Ext.define("WF.view.data.daily.importDailyRecord", {
    extend: "Ext.Button",
    alias: "importEvent",
    text : '导入',
    iconCls : 'icon-add',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
            var main = Ext.ComponentQuery.query("dailyMain")[0];
            var grid = main.down('datagrid');
            uploadExcel("data/admin/dailyRecord/importFile.do", function (result) {
                if (result.success) {
                    Ext.MessageBox.show({
                        title: '提示',
                        msg: result.data.msg,
                        modal: true,
                        icon: Ext.Msg.INFO,
                        buttons: Ext.Msg.OK
                    });
                    grid.store.reload();
                } else {
                    alert("导入失败");
                }});
        }
    }
});