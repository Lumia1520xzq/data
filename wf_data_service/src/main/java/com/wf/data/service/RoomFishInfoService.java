
package com.wf.data.service;


import com.google.common.collect.Lists;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.utils.JdbcUtils;
import com.wf.data.dao.data.entity.ReportFishBettingInfo;
import com.wf.data.dao.data.entity.ReportGameInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lcs
 */
@Service
public class RoomFishInfoService {
    private final JdbcUtils jdbcUtils = SpringContextHolder.getBean(JdbcUtils.class);

    public ReportGameInfo findBettingInfoByDate(Map<String, Object> params, String dbName) {
        List<ReportGameInfo> infoList = new ArrayList<>();
        String sql = "\tSELECT\n";
        if (null != params.get("searchType")) {
            Integer searchType = Integer.valueOf(params.get("searchType").toString());

            if (1 == searchType) {
                sql = sql + "\t DATE_FORMAT(t.create_time,'%Y-%m-%d %H') search_date,\n";
            } else {
                sql = sql + "\t DATE_FORMAT(t.create_time,'%Y-%m-%d') search_date,\n";
            }
        } else {
            sql = sql + "\t DATE_FORMAT(t.create_time,'%Y-%m-%d') search_date,\n";
        }

        sql = sql + "\t IFNULL(COUNT(DISTINCT user_id),0) cathectic_user_num,\n" +
                "\t\tIFNULL(COUNT(user_id),0) cathectic_num,\n" +
                "\t\tIFNULL(SUM(t.amount),0) cathectic_money,\n" +
                "\t\tIFNULL(SUM(t.return_amount),0) win_money\n" +
                "\t\tFROM room_fish_info  t\n" +
                "\t\tWHERE 1=1\n" +
                "\t\tAND t.delete_flag = 0\n";

        if (null != params.get("parentId")) {
            Long parentId = Long.valueOf(params.get("parentId").toString());
            sql = sql + "\tAND t.channel_id LIKE CONCAT( " + parentId + ", '%')\n";
        }

        if (null != params.get("channelId")) {
            Long parentId = Long.valueOf(params.get("channelId").toString());
            sql = sql + "\tAND t.channel_id = " + parentId + "\n";
        }
        if (null != params.get("userIds")) {
            List<Long> userIds = (ArrayList<Long>) params.get("userIds");
            if(CollectionUtils.isNotEmpty(userIds)){
                String str = StringUtils.join(userIds.toArray(), ",");
                sql = sql + "\tAND t.user_id not in(" + str + ")\n";
            }
        }
        String beginDate = "";
        String endDate = "";
        if (null != params.get("beginDate")) {
            beginDate = (String) params.get("beginDate");
        }
        if (null != params.get("endDate")) {
            endDate = (String) params.get("endDate");
        }

        if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            sql = sql + "\t\tAND t.create_time BETWEEN '" + beginDate + "' AND '" + endDate + "'\n";
        }

        if (null != params.get("searchType")) {
            Integer searchType = Integer.valueOf(params.get("searchType").toString());

            if (1 == searchType) {
                sql = sql + "\t GROUP BY DATE_FORMAT(t.create_time,'%Y-%m-%d %H')\n";
            } else {
                sql = sql + "\t GROUP BY DATE_FORMAT(t.create_time,'%Y-%m-%d')\n";
            }
        } else {
            sql = sql + "\t GROUP BY DATE_FORMAT(t.create_time,'%Y-%m-%d')\n";
        }
        List<Map<String, Object>> resultList = jdbcUtils.query(sql, dbName);
        ReportGameInfo info = new ReportGameInfo();
        for (Map<String, Object> map : resultList) {
            ReportGameInfo items = new ReportGameInfo();
            if (null != map.get("search_date")) {
                items.setSearchDate(map.get("search_date").toString());
            }
            if (null != map.get("cathectic_user_num")) {
                items.setCathecticUserNum(Integer.valueOf(map.get("cathectic_user_num").toString()));
            }
            if (null != map.get("cathectic_num")) {
                items.setCathecticNum(Integer.valueOf(map.get("cathectic_num").toString()));
            }
            if (null != map.get("cathectic_money")) {
                items.setCathecticMoney(Long.valueOf(map.get("cathectic_money").toString()));
            }
            if (null != map.get("win_money")) {
                items.setWinMoney(Long.valueOf(map.get("win_money").toString()));
            }
            infoList.add(items);
        }
        if (CollectionUtils.isNotEmpty(infoList)) {
            info = infoList.get(0);
        }
        return info;
    }

    public List<Long> findBettingUsersByDate(Map<String, Object> params, String dbName) {
        List<Long> list = Lists.newArrayList();
        String sql = "\tSELECT DISTINCT user_id userId\n" +
                "\t\tFROM room_fish_info  t\n" +
                "\t\tWHERE 1=1\n" +
                "\t\tAND t.delete_flag = 0\n";

        if (null != params.get("parentId")) {
            Long parentId = Long.valueOf(params.get("parentId").toString());
            sql = sql + "\tAND t.channel_id LIKE CONCAT( " + parentId + ", '%')\n";
        }

        if (null != params.get("channelId")) {
            Long parentId = Long.valueOf(params.get("channelId").toString());
            sql = sql + "\tAND t.channel_id = " + parentId + "\n";
        }

        if (null != params.get("userIds")) {
            List<Long> userIds = (ArrayList<Long>) params.get("userIds");
            if(CollectionUtils.isNotEmpty(userIds)){
                String str = StringUtils.join(userIds.toArray(), ",");
                sql = sql + "\tAND t.user_id not in(" + str + ")\n";
            }
        }
        String beginDate = "";
        String endDate = "";
        if (null != params.get("beginDate")) {
            beginDate = (String) params.get("beginDate");
        }
        if (null != params.get("endDate")) {
            endDate = (String) params.get("endDate");
        }

        if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            sql = sql + "\t\tAND t.create_time BETWEEN '" + beginDate + "' AND '" + endDate + "'\n";
        }
        List<Map<String, Object>> resultList = jdbcUtils.query(sql, dbName);

        for (Map<String, Object> map : resultList) {
            Long userId = null;
            if (null != map.get("userId")) {
                userId = Long.valueOf(map.get("userId").toString());
            }
            list.add(userId);
        }
        return list;
    }

    public List<ReportFishBettingInfo> findFishDateByDate(Map<String, Object> map, String dbName) {
        List<ReportFishBettingInfo> list = Lists.newArrayList();
        String sql = "SELECT\n" +
                "\t\tt.user_id userId,\n" +
                "\t\tt.channel_id channelId,\n" +
                "\t\tt.fish_config_id fishConfigId,\n" +
                "\t\tt.amount amount,\n" +
                "\t\tIFNULL(COUNT(t.user_id),0) bettingCount,\n" +
                "\t\tIFNULL(SUM(t.amount),0) bettingAmount,\n" +
                "\t\tIFNULL(SUM(t.return_amount),0) resultAmount\n" +
                "\t\tFROM room_fish_info  t\n" +
                "\t\tWHERE 1=1\n";


        String beginDate = "";
        String endDate = "";
        if (null != map.get("beginDate")) {
            beginDate = (String) map.get("beginDate");
        }
        if (null != map.get("endDate")) {
            endDate = (String) map.get("endDate");
        }

        if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
            sql = sql + "\t\tAND t.create_time BETWEEN '" + beginDate + "' AND '" + endDate + "'\n";
        }
        sql = sql + "\t\tGROUP BY channel_id,user_id ,fish_config_id,amount";


        List<Map<String, Object>> resultList = jdbcUtils.query(sql, dbName);

        for (Map<String, Object> params : resultList) {
            ReportFishBettingInfo info = new ReportFishBettingInfo();
            if (null != params.get("userId")) {
                info.setUserId(Long.valueOf(params.get("userId").toString()));
            }
            if (null != params.get("channelId")) {
                info.setChannelId(Long.valueOf(params.get("channelId").toString()));
            }
            if (null != params.get("fishConfigId")) {
                info.setFishConfigId(Integer.valueOf(params.get("fishConfigId").toString()));
            }
            if (null != params.get("amount")) {
                info.setAmount(Double.valueOf(params.get("amount").toString()));
            }
            if (null != params.get("bettingCount")) {
                info.setBettingCount(Long.valueOf(params.get("bettingCount").toString()));
            }
            if (null != params.get("bettingAmount")) {
                info.setBettingAmount(Double.valueOf(params.get("bettingAmount").toString()));
            }
            if (null != params.get("resultAmount")) {
                info.setResultAmount(Double.valueOf(params.get("resultAmount").toString()));
            }
            list.add(info);
        }
        return list;
    }
}
