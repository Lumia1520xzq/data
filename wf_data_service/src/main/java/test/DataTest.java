package test;

import org.apache.commons.collections.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author: lcs
 * @date: 2017/12/21
 */
public class DataTest {
    public static void main(String[] args) throws Exception {
        /*Calendar cal = Calendar.getInstance();
        //当前日期向前推1小时，保证统计昨天数据
        cal.add(Calendar.HOUR_OF_DAY, -80);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        String startTime = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        String endtime = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");

        List<String> datelist = DateUtils.getDateList(startTime,endtime);
        System.out.println(datelist);
        if(DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd").equals(DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd"))){
            System.out.println(startTime);
            System.out.println(endtime);
        }else{
            for(String dat : datelist){
                if(datelist.get(0) == dat){
                    System.out.println(startTime);
                    System.out.println(DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDateTime(startTime)), "yyyy-MM-dd HH:mm:ss"));

                }else if (dat == datelist.get(datelist.size()-1)){
                    System.out.println(DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDateTime(endtime)), "yyyy-MM-dd HH:mm:ss"));
                    System.out.println(endtime);
                }else{
                    System.out.println(DateUtils.formatDate(DateUtils.getDayStartTime(DateUtils.parseDate(dat,"yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
                    System.out.println(DateUtils.formatDate(DateUtils.getDayEndTime(DateUtils.parseDate(dat,"yyyy-MM-dd")), "yyyy-MM-dd HH:mm:ss"));
                }
            }

        }*/

        List<Long> userIDS = Arrays.asList(1l,2l,3l,4l,5l,6l,7l,8l,9l,10l);
        List<Long> userID = Arrays.asList(8l,9l,10l);

        Collection interColl = CollectionUtils.intersection(userIDS, userID);
        List<Long> user = (List)interColl;
        System.out.println(user);



    }
}
