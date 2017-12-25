Ext.define("JDD.store.jclqStore", {
    extend: "DCIS.Store",
    url: '/basedata/private/basedataMatchController/listBasedataMatchJclqByHostTeamAndVisitTeam.do',
    fields: [
        {name: 'id', type: 'int', display: 'ID', show: false},
        {name: 'matchId', type: 'int', display: '比赛Id', show: false},
        {name: 'issueMatchName', type: 'string', display: '比赛编号', show: true},
        {name: 'hostTeam', type: 'string', display: '主队名称'},
        {name: 'visitTeam', type: 'string', display: '客队名称'},
        {name: 'weekdayName', type: 'string', display: '星期'}],
    autoLoad: true
});