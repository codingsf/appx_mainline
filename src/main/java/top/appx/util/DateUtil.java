package top.appx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Date parse(String timeStr) {
        timeStr = timeStr.replace('年','-');
        timeStr = timeStr.replace('月','-');
        timeStr = timeStr.replace('日',' ');
        timeStr = timeStr.replace("&nbsp;","");
        timeStr = timeStr.trim();

        if(timeStr.length()==10){
            timeStr+=" 00:00:00";
        }
        else if(timeStr.length()==16){
            timeStr +=":00";
        }

        try {
            Date date = sdf.parse(timeStr);
            return date;
        }catch (Exception ex){

            SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
            try {
                return sdf.parse(timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException("timeStr:"+timeStr+" is error");
            }
        }
    }
    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);

        return ctime;
    }

}
