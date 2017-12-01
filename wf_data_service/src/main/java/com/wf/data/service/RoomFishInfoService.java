
package com.wf.data.service;


import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.utils.JdbcUtils;
import com.wf.data.dao.data.entity.ReportFishBettingInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomFishInfoService  {
    private final JdbcUtils jdbcUtils = SpringContextHolder.getBean(JdbcUtils.class);
 /*   public ReportGameInfo findBettingInfoByDate(Map<String, Object> params) {
        return dao.findBettingInfoByDate(params);
    }

    public List<Long> findBettingUsersByDate(Map<String, Object> params) {
        return dao.findBettingUsersByDate(params);
    }

*/
    public List<ReportFishBettingInfo> findFishDateByDate(Map<String,Object> map, String dbName){

        String sql =  "SELECT\n" +
                "\t\tt.user_id userId,\n" +
                "\t\tt.channel_id channelId,\n" +
                "\t\tt.fish_config_id fishConfigId,\n" +
                "\t\tt.amount amount,\n" +
                "\t\tIFNULL(COUNT(t.user_id),0) bettingCount,\n" +
                "\t\tIFNULL(SUM(t.amount),0) bettingAmount,\n" +
                "\t\tIFNULL(SUM(t.return_amount),0) resultAmount\n" +
                "\t\tFROM room_fish_info  t\n" +
                "\t\tWHERE 1=1\n" ;


        String beginDate = "";
        String endDate = "";
        if(null != map.get("beginDate")){
            beginDate = (String)map.get("beginDate");
        }
        if(null != map.get("endDate")){
            endDate = (String)map.get("endDate");
        }

        if(StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)){
            sql = sql+ "\t\tAND t.create_time BETWEEN '"+beginDate+"' AND '"+endDate+"'\n";
        }
        sql = sql +  "\t\tGROUP BY channel_id,user_id ,fish_config_id,amount";


        List<Map<String, Object>> resultList = jdbcUtils.query(sql,dbName);

        return null;
    }
}
