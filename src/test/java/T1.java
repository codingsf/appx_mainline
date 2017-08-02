import org.jsoup.Jsoup;
import org.junit.Test;
import top.appx.util.DateUtil;

import java.util.Calendar;
import java.util.Date;

public class T1 {
    @Test
    public void test1()throws Exception{
        Date date = DateUtil.parse("2017-08-02 00:00:00");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(date.getHours()==0 && date.getMinutes()==0 && date.getSeconds()==0){
            calendar.add(Calendar.DAY_OF_MONTH, 1);//+1今天的时间加一天
        }else{
            calendar.add(Calendar.HOUR, +3);
        }

        System.out.println(DateUtil.dateToString(calendar.getTime()));
        if(new Date().before(calendar.getTime())){
            System.out.println("超市");
        }

    }


}
