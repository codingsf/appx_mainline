package top.appx.zutil;

import org.junit.Test;

import java.util.Date;

public class DateUtilTest {
    @Test
    public void parse() throws Exception {
        Date now = DateUtil.parse("2017-07-16 11:47:21");
        System.out.println(now.getTime());

        now = DateUtil.parse("2017-07-12 21:17:00");

        System.out.println(now.getTime());
    }

}