Ext.define("WF.store.userStore", {
    extend: "DCIS.Store",
    url: 'data/admin/common/data/channelUserList.do',
    fields: [
        {name: 'id', type: 'string', display: '用户ID', show: true},
        {name: 'nickname', type: 'string', display: '用户昵称', show: true}],
    autoLoad: true
});