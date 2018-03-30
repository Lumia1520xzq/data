Ext.define("WF.store.awardsInfoStore", {
    extend: "DCIS.Store",
    url: 'data/admin/common/data/awardsInfoList.do',
    fields: [
        {name: 'id', type: 'string', display: '奖品ID', show: true},
        {name: 'name', type: 'string', display: '奖品名称', show: true}],
    autoLoad: true
});