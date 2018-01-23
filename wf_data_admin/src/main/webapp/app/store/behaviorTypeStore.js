Ext.define("WF.store.behaviorTypeStore", {
    extend: "DCIS.Store",
    url: 'data/admin/behaviorType/findAll.do',
    fields: [
        {name: 'eventId', type: 'integer', display: '事件ID', show: true},
        {name: 'name', type: 'string', display: '事件名称', show: true}],
    autoLoad: true
});