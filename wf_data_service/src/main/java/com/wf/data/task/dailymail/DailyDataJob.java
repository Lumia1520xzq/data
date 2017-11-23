package com.wf.data.task.dailymail;

import com.wf.core.email.EmailHander;
import com.wf.core.log.LogExceptionStackTrace;
import com.wf.core.utils.TraceIdUtils;
import com.wf.core.utils.core.SpringContextHolder;
import com.wf.core.utils.type.BigDecimalUtil;
import com.wf.core.utils.type.DateUtils;
import com.wf.core.utils.type.NumberUtils;
import com.wf.core.utils.type.StringUtils;
import com.wf.data.common.constants.DataConstants;
import com.wf.data.dao.trans.entity.TransChangeNote;
import com.wf.data.service.DataConfigService;
import com.wf.data.service.TransConvertService;
import com.wf.data.service.elasticsearch.EsUicBuryingPointService;
import com.wf.data.service.elasticsearch.EsUicPlatformService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 数据日检报表(定时发送邮件)
 *
 * @author jianjian.huang
 */
public class DailyDataJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TransConvertService transConvertService = SpringContextHolder.getBean(TransConvertService.class);
    private final EsUicBuryingPointService buryingPointService = SpringContextHolder.getBean(EsUicBuryingPointService.class);
    private final EsUicPlatformService platService = SpringContextHolder.getBean(EsUicPlatformService.class);
    private final EmailHander emailHander = SpringContextHolder.getBean(EmailHander.class);
    private final DataConfigService dataConfigService = SpringContextHolder.getBean(DataConfigService.class);


    private final String CONTENT_ONE = "<table border='1' style='text-align: center ; border-collapse: collapse' >"

            + "<tr><td colspan='4' bgcolor='#DDDDDD' width='1200' style='font-weight:bold'>充值金额/人数/ARPU</td></tr>"
            + "<tr style='font-weight:bold'><td>项目</td><td>昨日</td><td>今日</td><td>日环比</td></tr>"
            + "<tr><td>充值金额</td><td>yesRechargeAmount</td><td>todRechargeAmount</td><td>rechargeAmountRate</td></tr>"
            + "<tr><td>充值人数</td><td>yesRechargePerson</td><td>todRechargePerson</td><td>rechargePersonRate</td></tr>"
            + "<tr><td>充值ARPU</td><td>yesRechargeARPU</td><td>todRechargeARPU</td><td>rechargeARPURate</td></tr>"

            + "<tr><td colspan='4' bgcolor='#DDDDDD' width='120' style='font-weight:bold'>活跃付费/非付费用户</td></tr>"
            + "<tr style='font-weight:bold' ><td>项目</td><td>昨日</td><td>今日</td><td>日环比</td></tr>"
            + "<tr><td>活跃付费用户数</td><td>yesActiveRechragePerson</td><td>todActiveRechragePerson</td><td>activeRechrageRate</td></tr>"
            + "<tr><td>活跃非付费用户数</td><td>yesActiveNoRechargePerson</td><td>todActiveNoRechargePerson</td><td>activeNoRechargeRate</td></tr>"

            + "<tr><td colspan='4' bgcolor='#DDDDDD' width='120' style='font-weight:bold'>不同用户付费渗透率</td></tr>"
            + "<tr style='font-weight:bold' ><td>项目</td><td>昨日</td><td>今日</td><td>日环比</td></tr>"
            + "<tr><td>新增用户</td><td>yesNewUser</td><td>todNewUser</td><td>newUserRate</td></tr>"
            + "<tr><td>老用户(非付费)</td><td>yesNoRechargeOldUser</td><td>todNoRechargeOldUser</td><td>noRechargeOldUserRate</td></tr>"
            + "<tr><td>老用户(付费)</td><td>yesRechargeOldUser</td><td>todRechargeOldUser</td><td>rechargeOldUserRate</td></tr>"
            + "</table><br/>";

    private final String CONTENT_TWO = "<table border='1' style='text-align: center ; border-collapse: collapse' >"
            + "<tr><td colspan='4' bgcolor='#DDDDDD' width='1200' style='font-weight:bold'>title</td></tr>"
            + "<tr style='font-weight:bold'><td>历史充值金额区间</td><td>昨日</td><td>今日</td><td>日环比</td></tr>"
            + "<tr><td>0</td><td>yesZero</td><td>todZero</td><td>zeroRate</td></tr>"
            + "<tr><td>1~100</td><td>yes1To100</td><td>tod1To100</td><td>1To100Rate</td></tr>"
            + "<tr><td>100~1000</td><td>yes100To1000</td><td>tod100To1000</td><td>100To1000Rate</td></tr>"
            + "<tr><td>1000~10000</td><td>yes1000To10000</td><td>tod1000To10000</td><td>1000To10000Rate</td></tr>"
            + "<tr><td>10000~100000</td><td>yes10000To100000</td><td>tod10000To100000</td><td>10000To100000Rate</td></tr>"
            + "<tr><td>100000以上 </td><td>yesOver100000</td><td>todOver100000</td><td>over100000Rate</td></tr>"
            + "</table><br/>";

    public void execute() {
        logger.info("开始进行付费项数据分析:traceId={}", TraceIdUtils.getTraceId());
        byte count = 0;
        String today = DateUtils.getYesterdayDate();
        String yesterday = DateUtils.formatDate(DateUtils.getPrevDate(new Date(), 2));
        while (count <= 5) {
            try {
                // 获取收件人 ------ 收件人要改
                String receivers = dataConfigService.findByName(DataConstants.DAILY_DATA_RECEIVER).getValue();
                if (StringUtils.isNotEmpty(receivers)) {
                    StringBuilder content = new StringBuilder();
                    content.append(buildDailyDataInfo(yesterday, today));
                    content.insert(0, today + "数据如下" + "<br/><br/>");
                    content.append(connectContents(yesterday, today));
                    // 发送邮件
                    for (String to : receivers.split(",")) {
                        try {
                            emailHander.sendHtml(to, String.format("付费项数据分析汇总(%s)", DateUtils.getDate()),
                                    content.toString());
                        } catch (MessagingException e) {
                            logger.error("付费项数据分析邮件发送失败，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                        }
                    }
                } else {
                    logger.error("付费项数据分析邮件未设置收件人，traceId={}", TraceIdUtils.getTraceId());
                }
                logger.info("付费项数据分析邮件发送成功:traceId={}", TraceIdUtils.getTraceId());
                break;
            } catch (Exception e) {
                count++;
                if (count <= 5) {
                    logger.error("付费项数据分析邮件发送失败，重新发送{}，ex={}，traceId={}",count,LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                } else {
                    logger.error("付费项数据分析邮件发送失败，停止发送，ex={}，traceId={}", LogExceptionStackTrace.erroStackTrace(e), TraceIdUtils.getTraceId());
                }
            }
        }
    }

    /**
     * 构建数据日检报表
     *
     * @param
     * @return
     */
    private String buildDailyDataInfo(String yesterday, String today) {
        String temp = replaceContentOne(yesterday, today);
        return temp;
    }

    /**
     * 替换table的内容 考虑其通用性
     *
     * @param yesData
     * @param todData
     * @return
     */
    private String replaceContentTwo(List<Integer> yesData, List<Integer> todData, String title) {
        String temp = CONTENT_TWO
                .replace("title", title)
                .replace("yesZero", yesData.get(0).toString())
                .replace("todZero", todData.get(0).toString())
                .replace("zeroRate", getDailyRate(yesData.get(0), todData.get(0)).toString())

                .replace("yes1To100", yesData.get(1).toString()).replace("tod1To100", todData.get(1).toString())
                .replace("1To100Rate", getDailyRate(yesData.get(1), todData.get(1)).toString())

                .replace("yes100To1000", yesData.get(2).toString()).replace("tod100To1000", todData.get(2).toString())
                .replace("100To1000Rate", getDailyRate(yesData.get(2), todData.get(2)).toString())

                .replace("yes1000To10000", yesData.get(3).toString())
                .replace("tod1000To10000", todData.get(3).toString())
                .replace("1000To10000Rate", getDailyRate(yesData.get(3), todData.get(3)).toString())

                .replace("yes10000To100000", yesData.get(4).toString())
                .replace("tod10000To100000", todData.get(4).toString())
                .replace("10000To100000Rate", getDailyRate(yesData.get(4), todData.get(4)).toString())

                .replace("yesOver100000", yesData.get(5).toString())
                .replace("todOver100000", todData.get(5).toString())
                .replace("over100000Rate", getDailyRate(yesData.get(5), todData.get(5)).toString());

        return temp;
    }


    /**
     * 替换table的内容 考虑其通用性
     *
     * @param yesData
     * @param todData
     * @return
     */
    private String replaceContentThree(List<Double> yesData, List<Double> todData, String title) {
        String temp = CONTENT_TWO
                .replace("title", title)
                .replace("yesZero", yesData.get(0).toString())
                .replace("todZero", todData.get(0).toString())
                .replace("zeroRate", getDailyRate(yesData.get(0), todData.get(0)).toString())

                .replace("yes1To100", yesData.get(1).toString()).replace("tod1To100", todData.get(1).toString())
                .replace("1To100Rate", getDailyRate(yesData.get(1), todData.get(1)).toString())

                .replace("yes100To1000", yesData.get(2).toString()).replace("tod100To1000", todData.get(2).toString())
                .replace("100To1000Rate", getDailyRate(yesData.get(2), todData.get(2)).toString())

                .replace("yes1000To10000", yesData.get(3).toString())
                .replace("tod1000To10000", todData.get(3).toString())
                .replace("1000To10000Rate", getDailyRate(yesData.get(3), todData.get(3)).toString())

                .replace("yes10000To100000", yesData.get(4).toString())
                .replace("tod10000To100000", todData.get(4).toString())
                .replace("10000To100000Rate", getDailyRate(yesData.get(4), todData.get(4)).toString())

                .replace("yesOver100000", yesData.get(5).toString())
                .replace("todOver100000", todData.get(5).toString())
                .replace("over100000Rate", getDailyRate(yesData.get(5), todData.get(5)).toString());

        return temp;
    }


    /**
     * 替换table的内容 考虑其通用性
     *
     * @param yesData
     * @param todData
     * @return
     */
    private String replaceContentFour(List<Double> yesData, List<Double> todData, String title) {
        String temp = CONTENT_TWO
                .replace("title", title)
                .replace("yesZero", NumberUtils.format(yesData.get(0), "#.##%").toString())
                .replace("todZero", NumberUtils.format(todData.get(0), "#.##%").toString())
                .replace("zeroRate", getDailyRate(yesData.get(0), todData.get(0)).toString())

                .replace("yes1To100", NumberUtils.format(yesData.get(1), "#.##%").toString())
                .replace("tod1To100", NumberUtils.format(todData.get(1), "#.##%").toString())
                .replace("1To100Rate", getDailyRate(yesData.get(1), todData.get(1)).toString())

                .replace("yes100To1000", NumberUtils.format(yesData.get(2), "#.##%").toString())
                .replace("tod100To1000", NumberUtils.format(todData.get(2), "#.##%").toString())
                .replace("100To1000Rate", getDailyRate(yesData.get(2), todData.get(2)).toString())

                .replace("yes1000To10000", NumberUtils.format(yesData.get(3), "#.##%").toString())
                .replace("tod1000To10000", NumberUtils.format(todData.get(3), "#.##%").toString())
                .replace("1000To10000Rate", getDailyRate(yesData.get(3), todData.get(3)).toString())

                .replace("yes10000To100000", NumberUtils.format(yesData.get(4), "#.##%").toString())
                .replace("tod10000To100000", NumberUtils.format(todData.get(4), "#.##%").toString())
                .replace("10000To100000Rate", getDailyRate(yesData.get(4), todData.get(4)).toString())

                .replace("yesOver100000", NumberUtils.format(yesData.get(5), "#.##%").toString())
                .replace("todOver100000", NumberUtils.format(todData.get(5), "#.##%").toString())
                .replace("over100000Rate", getDailyRate(yesData.get(5), todData.get(5)).toString());

        return temp;
    }


    /**
     * 连接各字段的内容
     *
     * @param
     * @return
     */
    private String connectContents(String yesterday, String today) {
        StringBuilder content = new StringBuilder();
        Map<String, Object> yesMap = getAllData(yesterday);
        Map<String, Object> todMap = getAllData(today);
        //1、活跃人数构成
        List<Integer> yesActive = (List<Integer>) yesMap.get("activeList");
        List<Integer> todActive = (List<Integer>) todMap.get("activeList");
        String active_temp = replaceContentTwo(yesActive, todActive, "活跃用户构成");
        content.append(active_temp);
        //2、该区间用户当日充值人数
        List<Integer> yesRecharge = (List<Integer>) yesMap.get("rechargeList");
        List<Integer> todRecharge = (List<Integer>) todMap.get("rechargeList");
        String recharge_temp = replaceContentTwo(yesRecharge, todRecharge, "该区间用户当日充值人数 ");
        content.append(recharge_temp);
        //3、付费渗透率
        List<Double> yesRate = (List<Double>) yesMap.get("rechargeRate");
        List<Double> todRate = (List<Double>) todMap.get("rechargeRate");
        String rate_temp = replaceContentFour(yesRate, todRate, "该区间用户当日付费渗透率 ");
        content.append(rate_temp);
        //4、付费渗透率
        List<Double> yesRechargeSum = (List<Double>) yesMap.get("rechargeSum");
        List<Double> todRechargeSum = (List<Double>) todMap.get("rechargeSum");
        String sum_temp = replaceContentThree(yesRechargeSum, todRechargeSum, "该区间用户当日充值金额");
        content.append(sum_temp);
        //5、充值ARPU
        List<Double> yesRchargeARPU = (List<Double>) yesMap.get("rechargeARPU");
        List<Double> todRchargeARPU = (List<Double>) todMap.get("rechargeARPU");
        String recharge_ARPU_temp = replaceContentThree(yesRchargeARPU, todRchargeARPU, "该区间用户当日充值ARPU");
        content.append(recharge_ARPU_temp);

        return content.toString().replace("昨日", yesterday).replace("今日", today);
    }

    private Map<String, Object> getAllData(String date) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Integer> activeList = new ArrayList<Integer>(); //活跃用户
        List<Integer> rechargeList = new ArrayList<Integer>(); //充值人数
        List<Double> rechargeRate = new ArrayList<Double>(); //付费渗透率
        List<Double> rechargeSum = new ArrayList<Double>();//充值金额
        List<Double> rechargeARPU = new ArrayList<Double>();//充值ARPU 充值金额/充值人数
        //1、先查出当天活跃人的id
        List<Long> activeUserIds = buryingPointService.getActiveUserIds(date, null);
        //2、查出当天充值的用户 id
        List<Long> todayRechargeUserIds = transConvertService.getRechargeUserIdsByDay(toMap(date));
        //3、付费渗透率(充值人数/活跃人数)
        //4、查出当天每个充值用户的充值金额
        List<TransChangeNote> todayRechargeSum = transConvertService.getRechargeSumByUserIdAndDate(toMap(date));
        //5、充值ARPU(充值金额/充值人数)
        // 参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("date", date + " 00:00:00");

        // 历史充值为1~100的用户id
        params.put("startNum", 1);
        params.put("endNum", 100);
        List<Long> rechargeUserIds = transConvertService.findUserIdsBySumRecharge(params);
        int active_two = countUser(activeUserIds, rechargeUserIds);
        int recharge_two = countUser(todayRechargeUserIds, rechargeUserIds);
        double rate_two = active_two == 0 ? 0 : BigDecimalUtil.div(recharge_two, active_two);
//		String rate_two=NumberUtils.format(recharge_rate_two, "#.#%");
        double recharge_sum_two = countRechargeSum(todayRechargeSum, rechargeUserIds);
        double recharge_ARPU_two = recharge_two == 0 ? 0 : BigDecimalUtil.div(recharge_sum_two, recharge_two);
        activeUserIds = countUserIds(activeUserIds, rechargeUserIds);
        todayRechargeUserIds = countUserIds(todayRechargeUserIds, rechargeUserIds);
        todayRechargeSum = countRechargeSums(todayRechargeSum, rechargeUserIds);

        // 历史充值为100~1000的用户id
        params.put("startNum", 100);
        params.put("endNum", 1000);
        rechargeUserIds = transConvertService.findUserIdsBySumRecharge(params);
        int active_three = countUser(activeUserIds, rechargeUserIds);
        int recharge_three = countUser(todayRechargeUserIds, rechargeUserIds);
        double rate_three = active_three == 0 ? 0 : BigDecimalUtil.div(recharge_three, active_three);
//		String rate_three=NumberUtils.format(recharge_rate_three, "#.#%");
        double recharge_sum_three = countRechargeSum(todayRechargeSum, rechargeUserIds);
        double recharge_ARPU_three = recharge_three == 0 ? 0 : BigDecimalUtil.div(recharge_sum_three, recharge_three);
        activeUserIds = countUserIds(activeUserIds, rechargeUserIds);
        todayRechargeUserIds = countUserIds(todayRechargeUserIds, rechargeUserIds);
        todayRechargeSum = countRechargeSums(todayRechargeSum, rechargeUserIds);

        // 历史充值为1000~10000的用户id
        params.put("startNum", 1000);
        params.put("endNum", 10000);
        rechargeUserIds = transConvertService.findUserIdsBySumRecharge(params);
        int active_four = countUser(activeUserIds, rechargeUserIds);
        int recharge_four = countUser(todayRechargeUserIds, rechargeUserIds);
        double rate_four = active_four == 0 ? 0 : BigDecimalUtil.div(recharge_four, active_four);
//		String rate_four=NumberUtils.format(recharge_rate_four, "#.#%");
        double recharge_sum_four = countRechargeSum(todayRechargeSum, rechargeUserIds);
        double recharge_ARPU_four = recharge_four == 0 ? 0 : BigDecimalUtil.div(recharge_sum_four, recharge_four);
        activeUserIds = countUserIds(activeUserIds, rechargeUserIds);
        todayRechargeUserIds = countUserIds(todayRechargeUserIds, rechargeUserIds);
        todayRechargeSum = countRechargeSums(todayRechargeSum, rechargeUserIds);

        // 历史充值为10000~100000的用户id
        params.put("startNum", 10000);
        params.put("endNum", 100000);
        rechargeUserIds = transConvertService.findUserIdsBySumRecharge(params);
        int active_five = countUser(activeUserIds, rechargeUserIds);
        int recharge_five = countUser(todayRechargeUserIds, rechargeUserIds);
        double rate_five = active_five == 0 ? 0 : BigDecimalUtil.div(recharge_five, active_five);
//		String rate_five=NumberUtils.format(recharge_rate_five, "#.#%");
        double recharge_sum_five = countRechargeSum(todayRechargeSum, rechargeUserIds);
        double recharge_ARPU_five = recharge_five == 0 ? 0 : BigDecimalUtil.div(recharge_sum_five, recharge_five);
        activeUserIds = countUserIds(activeUserIds, rechargeUserIds);
        todayRechargeUserIds = countUserIds(todayRechargeUserIds, rechargeUserIds);
        todayRechargeSum = countRechargeSums(todayRechargeSum, rechargeUserIds);

        // 历史充值为100000以上的用户id
        params.put("startNum", 100000);
        params.remove("endNum");
        rechargeUserIds = transConvertService.findUserIdsBySumRecharge(params);
        int active_six = countUser(activeUserIds, rechargeUserIds);
        int recharge_six = countUser(todayRechargeUserIds, rechargeUserIds);
        double rate_six = active_six == 0 ? 0 : BigDecimalUtil.div(recharge_six, active_six);
//		String rate_six=NumberUtils.format(recharge_rate_six, "#.#%");
        double recharge_sum_six = countRechargeSum(todayRechargeSum, rechargeUserIds);
        double recharge_ARPU_six = recharge_six == 0 ? 0 : BigDecimalUtil.div(recharge_sum_six, recharge_six);
        activeUserIds = countUserIds(activeUserIds, rechargeUserIds);
        todayRechargeUserIds = countUserIds(todayRechargeUserIds, rechargeUserIds);
        todayRechargeSum = countRechargeSums(todayRechargeSum, rechargeUserIds);

        // 历史充值为0的用户id(应该放到最后算)
        int active_one = CollectionUtils.isEmpty(activeUserIds) ? 0 : activeUserIds.size();
        int recharge_one = CollectionUtils.isEmpty(todayRechargeUserIds) ? 0 : todayRechargeUserIds.size();
        double rate_one = active_one == 0 ? 0 : BigDecimalUtil.div(recharge_one, active_one);
//		String rate_one=NumberUtils.format(recharge_rate_one, "#.#%");
        double recharge_sum_one = 0.0;
        if (CollectionUtils.isNotEmpty(todayRechargeSum)) {
            for (TransChangeNote recharge : todayRechargeSum) {
                recharge_sum_one += recharge.getBusinessMoney();
            }
        }
        double recharge_ARPU_one = recharge_one == 0 ? 0 : BigDecimalUtil.div(recharge_sum_one, recharge_one);

        activeList.add(active_one);
        activeList.add(active_two);
        activeList.add(active_three);
        activeList.add(active_four);
        activeList.add(active_five);
        activeList.add(active_six);

        rechargeList.add(recharge_one);
        rechargeList.add(recharge_two);
        rechargeList.add(recharge_three);
        rechargeList.add(recharge_four);
        rechargeList.add(recharge_five);
        rechargeList.add(recharge_six);

        rechargeRate.add(rate_one);
        rechargeRate.add(rate_two);
        rechargeRate.add(rate_three);
        rechargeRate.add(rate_four);
        rechargeRate.add(rate_five);
        rechargeRate.add(rate_six);

        rechargeSum.add(recharge_sum_one);
        rechargeSum.add(recharge_sum_two);
        rechargeSum.add(recharge_sum_three);
        rechargeSum.add(recharge_sum_four);
        rechargeSum.add(recharge_sum_five);
        rechargeSum.add(recharge_sum_six);

        rechargeARPU.add(formatDouble(recharge_ARPU_one));
        rechargeARPU.add(formatDouble(recharge_ARPU_two));
        rechargeARPU.add(formatDouble(recharge_ARPU_three));
        rechargeARPU.add(formatDouble(recharge_ARPU_four));
        rechargeARPU.add(formatDouble(recharge_ARPU_five));
        rechargeARPU.add(formatDouble(recharge_ARPU_six));

        map.put("activeList", activeList);
        map.put("rechargeList", rechargeList);
        map.put("rechargeRate", rechargeRate);
        map.put("rechargeSum", rechargeSum);
        map.put("rechargeARPU", rechargeARPU);
        return map;
    }


    private int countUser(List<Long> activeUserIds, List<Long> rechargeUserIds) {
        int count = 0;
        if (CollectionUtils.isNotEmpty(activeUserIds)) {
            for (Long activeUserId : activeUserIds) {
                if (rechargeUserIds.contains(activeUserId)) {
                    count++;
                }
            }
        }
        return count;
    }

    //
    private List<Long> countUserIds(List<Long> activeUserIds, List<Long> rechargeUserIds) {
        List<Long> list = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(activeUserIds)) {
            for (Long activeUserId : activeUserIds) {
                if (rechargeUserIds.contains(activeUserId)) {
                    list.add(activeUserId);
                }
            }
        }
        activeUserIds.removeAll(list);
        return activeUserIds;
    }

    private double countRechargeSum(List<TransChangeNote> transList, List<Long> rechargeUserIds) {
        double sum = 0;
        if (CollectionUtils.isNotEmpty(transList)) {
            for (TransChangeNote trans : transList) {
                if (rechargeUserIds.contains(trans.getUserId())) {
                    sum += trans.getBusinessMoney();
                }
            }
        }
        return sum;
    }

    private List<TransChangeNote> countRechargeSums(List<TransChangeNote> transList, List<Long> rechargeUserIds) {
        List<TransChangeNote> list = new ArrayList<TransChangeNote>();
        if (CollectionUtils.isNotEmpty(transList)) {
            for (TransChangeNote trans : transList) {
                if (rechargeUserIds.contains(trans.getUserId())) {
                    list.add(trans);
                }
            }
        }
        transList.removeAll(list);
        return transList;
    }

    private Map<String, Object> toMap(String date) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("beginDate", date + " 00:00:00");
        params.put("endDate", date + " 23:59:59");
        return params;
    }

    private String getDailyRate(double yesData, double todData) {
        if (0 == yesData) {
            return "0.0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(todData - yesData, yesData, 6), "#.##%");
    }

    private String getDailyRate(int yesData, int todData) {
        if (0 == yesData) {
            return "0.0%";
        }
        return NumberUtils.format(BigDecimalUtil.div(todData - yesData, yesData, 6), "#.##%");
    }

    private List<Integer> getUsersCount(List<Long> activeUserIds, List<Long> rechargeUserIds) {
        List<Integer> list = new ArrayList<Integer>();
        int activeRechragePerson = 0;
        int activeNoRechargePerson = 0;
        if (!CollectionUtils.isEmpty(activeUserIds)) {
            if (CollectionUtils.isEmpty(rechargeUserIds)) {
                activeNoRechargePerson = activeUserIds.size();
            } else {
                for (Long id : activeUserIds) {
                    if (rechargeUserIds.contains(id)) {
                        activeRechragePerson++;
                    } else {
                        activeNoRechargePerson++;
                    }
                }
            }
        }
        list.add(activeRechragePerson);
        list.add(activeNoRechargePerson);
        return list;
    }

    /**
     * 替换table content_one内容
     *
     * @param
     * @return
     */
    private String replaceContentOne(String yesterday, String today) {
        Map<String, Object> yesParams = toMap(yesterday);
        Map<String, Object> todParams = toMap(today);
        // 1、充值金额(昨日、今日)、日环比 (今日-昨日)/昨日的值
        Double yesRechargeAmount = transConvertService.findSumRechargeByTime(yesParams);
        Double todRechargeAmount = transConvertService.findSumRechargeByTime(todParams);
        String rechargeAmountRate = getDailyRate(yesRechargeAmount, todRechargeAmount);

        // 2、充值人数(昨日、今日)、日环比
        List<Long> yesRecharge = transConvertService.getRechargeUserIdsByDay(yesParams);
        List<Long> todRecharge = transConvertService.getRechargeUserIdsByDay(todParams);
        Integer yesRechargePerson = CollectionUtils.isEmpty(yesRecharge) ? 0 : yesRecharge.size();
        Integer todRechargePerson = CollectionUtils.isEmpty(todRecharge) ? 0 : todRecharge.size();
        String rechargePersonRate = getDailyRate(yesRechargePerson, todRechargePerson);

        // 3、充值ARPU(昨日、今日)、日环比
        Double yesRechargeARPU = yesRechargePerson == 0 ? 0.0 : Double.parseDouble(NumberUtils.format(BigDecimalUtil.div(yesRechargeAmount, yesRechargePerson, 6), "#.##"));
        Double todRechargeARPU = todRechargePerson == 0 ? 0.0 : Double.parseDouble(NumberUtils.format(BigDecimalUtil.div(todRechargeAmount, todRechargePerson, 6), "#.##"));
        String rechargeARPURate = getDailyRate(yesRechargeARPU, todRechargeARPU);
        // 先查出昨日和今日的活跃用户的id
        List<Long> yesActiveUserIds = buryingPointService.getActiveUserIds(yesterday, null);
        List<Long> todActiveUserIds = buryingPointService.getActiveUserIds(today, null);
        // 然后查出截至昨日/今日的充值过的用户
        List<Long> yesRechargeUserIds = transConvertService.findRechargeUserIdsTillTime(yesterday + " 00:00:00");
        List<Long> todRechargeUserIds = transConvertService.findRechargeUserIdsTillTime(today + " 00:00:00");
        // 4、活跃付费用户数(昨日、今日)、日环比
        Integer yesActiveRechragePerson = getUsersCount(yesActiveUserIds, yesRechargeUserIds).get(0);
        Integer todActiveRechragePerson = getUsersCount(todActiveUserIds, todRechargeUserIds).get(0);
        String activeRechrageRate = getDailyRate(yesActiveRechragePerson, todActiveRechragePerson);

        // 5、活跃非付费用户数(昨日、今日)、日环比
        Integer yesActiveNoRechargePerson = getUsersCount(yesActiveUserIds, yesRechargeUserIds).get(1);
        Integer todActiveNoRechargePerson = getUsersCount(todActiveUserIds, todRechargeUserIds).get(1);
        String activeNoRechargeRate = getDailyRate(yesActiveNoRechargePerson, todActiveNoRechargePerson);

        // 6、新增用户(昨日、今日)、日环比 (新增用户 :当日新增用户并付费人数 /当日新增用户数)
        // 先查出当日新增用户数
        List<Long> yesNewUserList = platService.getNewUserList(yesterday);
        List<Long> todNewUserList = platService.getNewUserList(today);

        int yesNewUserCount = yesNewUserList.size();
        int todNewUserCount = todNewUserList.size();

        // 当日新增并付费人数
        int yesNewAndRechargeUser = countUser(yesRecharge, yesNewUserList);
        int todNewAndRechargeUser = countUser(todRecharge, todNewUserList);

        double yesNewUserRate = yesNewUserCount == 0 ? 0 : BigDecimalUtil.div(yesNewAndRechargeUser, yesNewUserCount);
        double todNewUserRate = todNewUserCount == 0 ? 0 : BigDecimalUtil.div(todNewAndRechargeUser, todNewUserCount);
        // 6、新增用户(昨日、今日)、日环比 (新增用户 :当日新增用户并付费人数 /当日新增用户数)
        String yesNewUser = NumberUtils.format(yesNewUserRate, "#.##%");
        String todNewUser = NumberUtils.format(todNewUserRate, "#.##%");
        String newUserRate = getDailyRate(yesNewUserRate, todNewUserRate);

        // 7、老用户(非付费)(昨日、今日)、日环比 (老用户中当日有过充值行为的人数/当日活跃没有过充值行为的老用户数)
        int yesRechargeUser = CollectionUtils.isEmpty(yesRecharge) ? 0 : yesRecharge.size();
        // 老用户中当日有过充值行为的人数
        int yesOldRechargeUser = yesRechargeUser - yesNewAndRechargeUser;
        // 当日活跃没有过充值行为的老用户
        int yesActiveNoRechargeOldUser = CollectionUtils.isEmpty(yesActiveUserIds) ? 0 : yesActiveUserIds.size() - yesNewUserCount - yesOldRechargeUser;
        double yesNoRechargeOldUserRate = yesActiveNoRechargeOldUser == 0 ? 0 : BigDecimalUtil.div(yesOldRechargeUser, yesActiveNoRechargeOldUser);
        int todRechargeUser = CollectionUtils.isEmpty(todRecharge) ? 0 : todRecharge.size();
        // 老用户中当日有过充值行为的人数
        int todOldRechargeUser = todRechargeUser - todNewAndRechargeUser;
        // 当日活跃没有过充值行为的老用户
        int todActiveNoRechargeOldUser = CollectionUtils.isEmpty(todActiveUserIds) ? 0 : todActiveUserIds.size() - todNewUserCount - todOldRechargeUser;
        double todNoRechargeOldUserRate = todActiveNoRechargeOldUser == 0 ? 0 : BigDecimalUtil.div(todOldRechargeUser, todActiveNoRechargeOldUser);
        // 7、老用户(非付费)(昨日、今日)、日环比 (老用户中当日有过充值行为的人数/当日活跃没有过充值行为的老用户数)
        String yesNoRechargeOldUser = NumberUtils.format(yesNoRechargeOldUserRate, "#.##%");
        String todNoRechargeOldUser = NumberUtils.format(todNoRechargeOldUserRate, "#.##%");
        String noRechargeOldUserRate = getDailyRate(yesNoRechargeOldUserRate, todNoRechargeOldUserRate);

        // 8、老用户(付费)(昨日、今日)、日环比 (历史充值用户中当日有充值的人数/历史充值用户人数)
        int yesRechargeUserCount = CollectionUtils.isEmpty(yesRechargeUserIds) ? 0 : yesRechargeUserIds.size();
        double yesRechargeOldUserRate = yesRechargeUserCount == 0 ? 0 : BigDecimalUtil.div(yesRechargePerson, yesRechargeUserCount);
        int todRechargeUserCount = CollectionUtils.isEmpty(todRechargeUserIds) ? 0 : todRechargeUserIds.size();
        double todRechargeOldUserRate = todRechargeUserCount == 0 ? 0 : BigDecimalUtil.div(todRechargePerson, todRechargeUserCount);
        // 8、老用户(付费)(昨日、今日)、日环比 (历史充值用户中当日有充值的人数/历史充值用户人数)
        String yesRechargeOldUser = NumberUtils.format(yesRechargeOldUserRate, "#.##%");
        String todRechargeOldUser = NumberUtils.format(todRechargeOldUserRate, "#.##%");
        String rechargeOldUserRate = getDailyRate(yesRechargeOldUserRate, todRechargeOldUserRate);

        String temp = CONTENT_ONE.replace("昨日", yesterday).replace("今日", today)

                .replace("yesRechargeAmount", yesRechargeAmount.toString())
                .replace("todRechargeAmount", todRechargeAmount.toString())
                .replace("rechargeAmountRate", rechargeAmountRate.toString())

                .replace("yesRechargePerson", yesRechargePerson.toString())
                .replace("todRechargePerson", todRechargePerson.toString())
                .replace("rechargePersonRate", rechargePersonRate.toString())

                .replace("yesRechargeARPU", yesRechargeARPU.toString())
                .replace("todRechargeARPU", todRechargeARPU.toString())
                .replace("rechargeARPURate", rechargeARPURate.toString())

                .replace("yesActiveRechragePerson", yesActiveRechragePerson.toString())
                .replace("todActiveRechragePerson", todActiveRechragePerson.toString())
                .replace("activeRechrageRate", activeRechrageRate.toString())

                .replace("yesActiveNoRechargePerson", yesActiveNoRechargePerson.toString())
                .replace("todActiveNoRechargePerson", todActiveNoRechargePerson.toString())
                .replace("activeNoRechargeRate", activeNoRechargeRate.toString())

                .replace("yesNewUser", yesNewUser.toString()).replace("todNewUser", todNewUser.toString())
                .replace("newUserRate", newUserRate.toString())

                .replace("yesNoRechargeOldUser", yesNoRechargeOldUser.toString())
                .replace("todNoRechargeOldUser", todNoRechargeOldUser.toString())
                .replace("noRechargeOldUserRate", noRechargeOldUserRate.toString())

                .replace("yesRechargeOldUser", yesRechargeOldUser.toString())
                .replace("todRechargeOldUser", todRechargeOldUser.toString())
                .replace("rechargeOldUserRate", rechargeOldUserRate.toString());

        return temp;
    }

    private static double formatDouble(double num) {
        DecimalFormat df = new DecimalFormat("#.0");
        return Double.parseDouble(df.format(num));
    }

}
