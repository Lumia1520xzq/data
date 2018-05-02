package com.wf.data.service.docking;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.APIUtils;
import com.wf.core.utils.GfJsonUtil;
import com.wf.core.utils.TraceIdUtils;
import com.wf.data.common.constants.ChannelConstants;
import com.wf.data.common.constants.JddTagIdConstants;
import com.wf.data.common.utils.DateUtils;
import com.wf.data.dao.datarepo.entity.DatawareBettingLogDay;
import com.wf.data.dto.JddUserTagDto;
import com.wf.data.service.BuryingPointService;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.UicUserService;
import com.wf.data.service.data.DatawareBettingLogDayService;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时推送奖多多彩票ID
 * 1、游戏平台的全量用户彩票ID
 * 2、昨日新增用户彩票ID
 *
 * @author chengsheng.liu
 * @date 2018年3月5日
 */
@Service
public class SendThirdIdToJddService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UicUserService uicUserService;

    @Autowired
    private BuryingPointService buryingPointService;

    @Autowired
    private TransConvertService transConvertService;

    @Autowired
    private DatawareBettingLogDayService bettingLogDayService;

    @Value("${jdd.baseUrl}")
    private String baseUrl;

    @Value("${jdd.thirdId.url}")
    private String url;

    private final long pageSize = 5000;

    private final String demarcationDate = "2017-12-01";

    private final long limit = 100000000;

    public void toDoAnalysis() {
        //昨日新增用户彩票ID
        Map<String, Object> params = new HashMap<>();
        String beginDate = DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.getNextDate(new Date(), -1)), DateUtils.DATE_TIME_PATTERN);
        String endDate = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.getNextDate(new Date(), -1)), DateUtils.DATE_TIME_PATTERN);
        params.put("userSource", 2);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        getThirdId(params, JddTagIdConstants.YESTERDAY_NEW_GAME_USER);

        //游戏平台的全量用户彩票ID
        Map<String, Object> map = new HashMap<>();
        String endTime = DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.getNextDate(new Date(), -1)), DateUtils.DATE_TIME_PATTERN);
        map.put("userSource", 2);
        map.put("endDate", endTime);
        getThirdId(map, JddTagIdConstants.ALL_GAME_USER);
    }

    /**
     * 昨日新增用户彩票ID
     */
    private void getThirdId(Map<String, Object> params, String tagId) {

        long count = uicUserService.getCountByDate(params);
        if (count == 0) {
            return;
        }
        //总页数
        long totalPage = getTotalPage(count);
        String uuid = APIUtils.getUUID();
        for (long i = 0; i < totalPage; i++) {
            long startIndex = getStartIndex(i);
            params.put("start", startIndex);
            params.put("length", pageSize);
            List<String> thirdIdList = uicUserService.getThirdIdList(params);

            if (CollectionUtils.isNotEmpty(thirdIdList)) {
                String batchEndFlag = "0";
                if (i + 1 == totalPage) {
                    batchEndFlag = "1";
                }
                sendThirdId(thirdIdList, uuid, batchEndFlag, tagId);
            }

        }
    }

    /**
     * 推送奖多多回流用户彩票ID
     * a. 奖多多渠道（100001）, 付费用户且30天未活跃，且为2017年12月1号之前注册用户。
     * b. 奖多多渠道（100001）, 付费用户且30天未活跃，且为2017年12月1号之后注册用户，注册时间超过30天，且付费超过1000元。
     */
    public void pushBackFlowUser() {
        //获取付费用户
        Map<String, Object> convertParams = new HashMap<>();
        convertParams.put("parentId", ChannelConstants.JS_CHANNEL);
        List<Long> payedUserList = transConvertService.getRechargeUserIdsByDay(convertParams);

        //按月获取30天内活跃用户
        Map<String, Object> behaviorParams = new HashMap<>();
        behaviorParams.put("channelId", ChannelConstants.JS_CHANNEL);
        behaviorParams.put("beginDate", DateUtils.formatDate(DateUtils.getPrevDate(new Date(), 30)) + " 00:00:00");
        behaviorParams.put("endDate", DateUtils.getYesterdayDate() + " 23:59:59");
        behaviorParams.put("limit", limit);
        List<Long> activeUsers = buryingPointService.getActiveUserWhinMonth(behaviorParams);

        //2017年12月1号之前注册用户。
        Map<String, Object> userParam = new HashMap<>();
        userParam.put("limit", limit);
        userParam.put("channelId", ChannelConstants.JS_CHANNEL);
        userParam.put("endDate", demarcationDate + " 00:00:00");
        List<Long> oldestUser1 = uicUserService.getUserByDate(userParam);

        //2017年12月1号之前注册且付费用户
        List<Long> pAndOUsers = (List<Long>) CollectionUtils.intersection(payedUserList, oldestUser1);

        //付费用户且30天未活跃，且为2017年12月1号之前注册用户。
        List<Long> requiredUser1 = (List<Long>) CollectionUtils.subtract(pAndOUsers, activeUsers);

        //2017年12月1号之后注册用户，注册时间超过30天
        userParam.put("beginDate", demarcationDate + " 00:00:00");
        userParam.put("endDate", DateUtils.formatDate(DateUtils.getPrevDate(new Date(), 30)) + " 00:00:00");
        List<Long> oldestUser2 = uicUserService.getUserByDate(userParam);

        //付费超过1000元。
        convertParams.put("sumConvertAmount", 1000);
        List<Long> payedUserList2 = transConvertService.getRechargeUserIdsByDay(convertParams);
        List<Long> requiredUser2 = (List<Long>) CollectionUtils.subtract(CollectionUtils.intersection(payedUserList2, oldestUser2), activeUsers);
        List<Long> resultList = (List<Long>) CollectionUtils.subtract(CollectionUtils.union(requiredUser1, requiredUser2), CollectionUtils.intersection(requiredUser1, requiredUser2));
        getThirdIdByUserIdList(resultList, JddTagIdConstants.BACK_FLOW_USER);
    }

    /**
     * 未付费老用户（拉付费率）奖多多渠道（100001）, 非2日内新用户，且未有过付费行为。
     */
    public void pushUnpayUser() {
        //获取付费用户
        Map<String, Object> convertParams = new HashMap<>();
        convertParams.put("parentId", ChannelConstants.JS_CHANNEL);
        List<Long> payedUserList = transConvertService.getRechargeUserIdsByDay(convertParams);

        //获取非两日内新用户
        Map<String, Object> newUserParam = new HashMap<>();
        newUserParam.put("limit", limit);
        newUserParam.put("channelId", ChannelConstants.JS_CHANNEL);
        newUserParam.put("endDate", DateUtils.formatDate(DateUtils.getPrevDate(new Date(), 2)) + " 00:00:00");
        List<Long> newUserInTwoDays = uicUserService.getUserByDate(newUserParam);

        List<Long> resultUserId2 = (List<Long>) CollectionUtils.subtract(newUserInTwoDays, payedUserList);

        getThirdIdByUserIdList(resultUserId2, JddTagIdConstants.UNPAY_OLD_USER);
    }

    /**
     * 奖多多渠道付费用户，7日内没有付费行为的用户彩票ID
     */
    public void pushPayedUser() {
        //获取付费用户
        Map<String, Object> convertParams = new HashMap<>();
        convertParams.put("parentId", ChannelConstants.JS_CHANNEL);
        List<Long> payedUserList = transConvertService.getRechargeUserIdsByDay(convertParams);

        //七日内有付费行为
        convertParams.put("beginDate", DateUtils.getPrevDate(DateUtils.getYesterdayDate(), 7));
        List<Long> payedUserInLastSevenDayList = transConvertService.getRechargeUserIdsByDay(convertParams);

        List<Long> resultUserId = (List<Long>) CollectionUtils.subtract(payedUserList, payedUserInLastSevenDayList);
        getThirdIdByUserIdList(resultUserId, JddTagIdConstants.UNPAY_SEVEN_DAY);
    }

    /**
     * 新注册用户7天有活跃且未付费（标签ID=417）
     */
    public void pushNewAndUnpayUser() {
        String yesterday = DateUtils.getYesterdayDate();
        String lastSevenDay = DateUtils.getPrevDate(DateUtils.getYesterdayDate(), 6);

        //获取T-7天注册用户
        Map<String, Object> userParam = new HashMap<>();
        userParam.put("channelId", ChannelConstants.JS_CHANNEL);
        userParam.put("beginDate", lastSevenDay + " 00:00:00");
        userParam.put("endDate", lastSevenDay + " 23:59:59");
        userParam.put("limit", limit);
        List<Long> conditionUserIdsOne = uicUserService.getUserByDate(userParam);

        //七日内活跃
        Map<String, Object> Param = new HashMap<>();
        Param.put("parentId", ChannelConstants.JS_CHANNEL);
        Param.put("channelId", ChannelConstants.JS_CHANNEL);
        Param.put("beginDate", lastSevenDay + " 00:00:00");
        Param.put("endDate", yesterday + " 23:59:59");
        Param.put("limit", limit);
        List<Long> conditionUserIdsTwo = buryingPointService.getActiveUserWhinMonth(Param);

        //七日内充值用户
        List<Long> conditionUserIdsThree = transConvertService.getRechargeUserIdsByDay(Param);

        List<Long> resultUserIds = (List<Long>) CollectionUtils.subtract(CollectionUtils.intersection(conditionUserIdsOne, conditionUserIdsTwo), conditionUserIdsThree);
        getThirdIdByUserIdList(resultUserIds, JddTagIdConstants.FOUR_ONE_SEVEN);
    }

    /**
     * 奖多多渠道7天未活跃用户彩票ID（标签ID=418）
     */
    public void pushUnActiveLastSevenDay() {
        String preEightDay = DateUtils.getPrevDate(DateUtils.getYesterdayDate(), 7);
        String beginDate = preEightDay + " 00:00:00";
        String endDate = preEightDay + " 23:59:59";

        //（T-8）天的活跃用户
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", ChannelConstants.JS_CHANNEL);
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        param.put("limit", limit);
        List<Long> activeUsersEight = buryingPointService.getActiveUserWhinMonth(param);

        //七天内活跃的用户
        beginDate = preEightDay + " 23:59:59";
        endDate = DateUtils.getYesterdayDate() + " 23:59:59";
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        param.put("limit", limit);
        List<Long> activeUsersLastSeven = buryingPointService.getActiveUserWhinMonth(param);

        //（T-8）天活跃且后连续7天内未活跃
        List<Long> resultUserId = (List<Long>) CollectionUtils.subtract(activeUsersEight, activeUsersLastSeven);
        getThirdIdByUserIdList(resultUserId, JddTagIdConstants.FOUR_ONE_EIGHT);
    }

    /**
     * 10天内有投注用户（标签ID=419）
     */
    public void pushActiveAndBettingLastTenDay() {
        //近十天投注用户
        Map<String, Object> param = new HashMap<>();
        param.put("beginDate", DateUtils.getPrevDate(DateUtils.getYesterdayDate(), 6));
        param.put("endDate", DateUtils.getYesterdayDate());
        param.put("channelId", ChannelConstants.JS_CHANNEL);
        List<Long> bettingUsers = bettingLogDayService.getBettingUserIdByDateRange(param);

        //新注册用户
        param.put("beginDate", DateUtils.getYesterdayDate() + " 00:00:00");
        param.put("endDate", DateUtils.getYesterdayDate() + " 23:59:59");
        param.put("limit", limit);
        List<Long> newUsers = uicUserService.getUserByDate(param);

        List<Long> resultUserIds = (List<Long>) CollectionUtils.subtract(bettingUsers,newUsers);
        getThirdIdByUserIdList(resultUserIds, JddTagIdConstants.FOUR_ONE_NINE);
    }

    /**
     * 根据userIdList获取三方ID
     *
     * @param resultList
     * @param lableId
     */
    private void getThirdIdByUserIdList(List<Long> resultList, String lableId) {

        long totalPage = getTotalPage(resultList.size());
        if (CollectionUtils.isNotEmpty(resultList)) {
            String uuid = APIUtils.getUUID();
            for (long i = 0; i < totalPage; i++) {
                long startIndex = i * pageSize;
                long endIndex = (i + 1) * pageSize;
                String batchEndFlag = "0";
                if (i + 1 == totalPage) {
                    batchEndFlag = "1";
                    endIndex = startIndex + resultList.size() % pageSize;
                }
                List<Long> splitUserId = resultList.subList((int) startIndex, (int) endIndex);

                Map<String, Object> params = new HashMap<>();
                params.put("ids", splitUserId);
                params.put("userSource", 2);
                params.put("start", 0);
                params.put("length", 100000000);
                List<String> thirdIdList = uicUserService.getThirdIdList(params);

                sendThirdId(thirdIdList, uuid, batchEndFlag, lableId);
            }
        }

    }

    public void sendThirdId(List<String> thirdIdList, String uuid, String batchEndFlag, String tagId) {
        JddUserTagDto dto = new JddUserTagDto();

        String userIds = String.join(",", thirdIdList).trim();
        dto.setUserIds(userIds);
        dto.setBatchId(uuid);
        dto.setBatchEndFlag(batchEndFlag);
        dto.setFrom("game");
        dto.setTagId(tagId);
        logger.info("奖多多推送接口参数：batchId:" + uuid + ";");
        request(baseUrl + url, dto);
    }

    /**
     * 请求
     *
     * @param uri
     * @param bean
     * @return
     */
    public void request(String uri, JddUserTagDto bean) {
        try {
            String bodyParams = "userIds=" + bean.getUserIds()
                    + "&batchId=" + bean.getBatchId()
                    + "&batchEndFlag=" + bean.getBatchEndFlag()
                    + "&from=" + bean.getFrom() + "&tagId=" + bean.getTagId();

            JsonNode response = Unirest.post(uri)
                    .header("content-type", "application/x-www-form-urlencoded")
                    .body(bodyParams).asJson().getBody();
            if (response.getObject() == null) {
                logger.error("请求返回值为空:uri={},json={}, ex={}, traceId={}", GfJsonUtil.toJSONString(uri), GfJsonUtil.toJSONString(bean.toString()), TraceIdUtils.getTraceId());
            } else {
                JSONObject object = new JSONObject(response);
                String data = object.getJSONObject("object").toString();
                if (!object.getJSONObject("object").get("code").equals(0)) {
                    logger.error("请求数据异常:uri={},data={},json={}, traceId={}", GfJsonUtil.toJSONString(uri), GfJsonUtil.toJSONString(data), GfJsonUtil.toJSONString(bean.toString()), TraceIdUtils.getTraceId());
                }
            }
        } catch (Exception e) {
            logger.error("请求数据异常:uri={},json={}, ex={}, traceId={}", GfJsonUtil.toJSONString(uri), GfJsonUtil.toJSONString(bean), LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
        }
    }

    /**
     * 获取分页开始条数ID
     *
     * @param pageNum 当前页
     * @return
     */
    private long getStartIndex(long pageNum) {
        return pageNum * pageSize;
    }

    /**
     * 获取总页数
     *
     * @param totalRecord
     * @return
     */
    private long getTotalPage(long totalRecord) {
        long totalPage = 0L;
        if (totalRecord % pageSize == 0) {
            totalPage = totalRecord / pageSize;
        } else {
            totalPage = totalRecord / pageSize + 1;
        }
        return totalPage;
    }

}
