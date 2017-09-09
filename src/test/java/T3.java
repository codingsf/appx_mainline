import org.jsoup.Jsoup;
import org.junit.Test;
import top.appx.job.JubiNewTop;
import top.appx.job.Lian12Job;
import top.appx.util.DateUtil;
import top.appx.util.HttpUtil;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class T3 {
    @Test
    public void t1()throws Exception{

       new Lian12Job().execute();

    }
}
