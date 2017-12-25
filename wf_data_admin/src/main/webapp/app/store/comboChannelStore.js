//主渠道
Ext.define("WF.store.parentChannelStore", {
    extend: "DCIS.Store",
    url: 'data/admin/common/data/getParentChannels.do',
    fields: [
        {name: 'id', type: 'string', display: '渠道ID', show: true},
        {name: 'name', type: 'string', display: '渠道名称', show: true}],
    autoLoad: true
});

//子渠道
Ext.define("WF.store.childChannelStore", {
    extend: "DCIS.Store",
    url: 'data/admin/common/data/getChildChannels.do',
    fields: [
        {name: 'id', type: 'string', display: '渠道ID', show: true},
        {name: 'name', type: 'string', display: '渠道名称', show: true}],
    autoLoad: true
});


