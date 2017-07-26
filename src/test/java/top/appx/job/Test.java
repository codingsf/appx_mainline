package top.appx.job;

import org.jsoup.Jsoup;

public class Test {
    @org.junit.Test
    public void test1()throws Exception{
        String str = Jsoup.connect("http://weixin.sogou.com/weixin?query=gh_6884e172c1bc")
                .cookie("SNUID","BF84F9558A8CDD0DD34FCBC58AC5C81A")
                .cookie("SUID","350E70DC1F2D940A00000000596CCC68")
                .cookie("SUIR","BF84F9558A8CDD0DD34FCBC58AC5C81A")
                .cookie("SUV","00A44ADADC700E75596CCC70AE581085")
                .get().html();
        System.out.println(str);


    }
}
