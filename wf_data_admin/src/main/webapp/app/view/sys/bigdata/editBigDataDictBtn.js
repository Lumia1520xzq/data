Ext.define("WF.view.sys.bigdata.editBigDataDictBtn", {
    extend: "Ext.Button",
    alias: "editBigDataDictBtn",
    text: '编辑',
    iconCls: 'icon-edit',//样式请参考webapp/resources/css/icon.css
    listeners: {
        click: function () {
            var main = Ext.ComponentQuery.query("sysBigDataDictMain")[0];
            var grid = main.down('datagrid');
            var records = grid.getSelectionModel().getSelection();
            if (records.length == 0) {
                Ext.MessageBox.alert('提示', '请选择一条记录');
                return;
            }
            var win = Ext.create("WF.view.sys.bigdata.editBigDataDict", {doRefresh: grid.store});
            win.down('dataform').setValues(records[0].data);
            win.show();
        }
    }
});