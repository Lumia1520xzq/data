Ext.define("WF.view.sys.config.delSysConfigBtn", {
    extend: "Ext.Button",
    alias: "delSysConfigBtn",
    text : '删除',
    iconCls : 'icon-remove',//样式请参考webapp/resources/css/icon.css
    listeners : {
        click : function() {
           var main = Ext.ComponentQuery.query("sysConfigMain")[0];
           var grid = main.down('datagrid');
           var records = grid.getSelectionModel().getSelection();
           if (records.length == 0) {
              Ext.MessageBox.alert('提示', '请选择一条记录');
              return;
           }
           Ext.MessageBox.confirm('警告', '确定删除 ' + records[0].data.name + ' ?', function(btn) {
               if (btn == 'yes') {
                   callapi('data/admin/sysconfig/delete.do', records[0].data, function(result) {
                       if (result.success) {
                           Ext.MessageBox.show({
                               title: '提示',
                               msg: '删除成功',
                               modal: true,
                               icon: Ext.Msg.INFO,
                               buttons: Ext.Msg.OK
                           });
                           grid.store.reload();
                       }
                   });
               }
           });
        }
    }
});