package top.appx.job;

import org.junit.Test;
import top.appx.job.vo.JobParam;

public class SimpleJobTest {
    @Test
    public void execute1() throws Exception {
    }

    private SimpleJob simpleJob;
    @Test
    public void execute() throws Exception {
        JobParam jobParam = new JobParam();

        //聚币网
        jobParam.setListUrl("https://www.jubi.com");
        jobParam.setArticleGroupFlag("notice_jubi");
        jobParam.setContentSel(".assets_content .about_text");
        jobParam.setTitleSel("title");
        jobParam.setTimeSel(".pub_date");
        jobParam.setNoticeASel(".jubi_news .pp_list li:eq(0) a");


        //比特时代
        jobParam.setListUrl("http://www.btc38.com/");
        jobParam.setNoticeASel(".notice li:eq(0) a");
        jobParam.setArticleGroupFlag("notice_btc38");
        jobParam.setContentSel(".article .article_con");
        jobParam.setTitleSel("title");
        jobParam.setTimeSel(".container .header p span");

        //OKCoin

        jobParam.setListUrl("https://www.okcoin.cn/service.html");
        jobParam.setNoticeASel(".newsList li:eq(0) a");
        jobParam.setArticleGroupFlag("notice_okcoin");
        jobParam.setContentSel(".invitation_content");
        jobParam.setTitleSel("title");
        jobParam.setTimeSel("span.time");


        //元宝网
        jobParam.setListUrl("https://www.yuanbao.com/news/?corpid=0");
        jobParam.setNoticeASel("#list .hideli:eq(1) a");
        jobParam.setArticleGroupFlag("notice_yuanbao");
        jobParam.setContentSel(".paragraph_news");
        jobParam.setTitleSel("#main h2:eq(0)");
        jobParam.setTimeSel(".product p");


        //币交所

        jobParam.setListUrl("https://www.bijiaosuo.com/html/gglog.html?type=1");
        jobParam.setNoticeASel(".news-row:eq(0) h3 a");
        jobParam.setArticleGroupFlag("notice_bijiaosuo");
        jobParam.setContentSel(".aboutText");
        jobParam.setTitleSel(".PlateTitle");
        jobParam.setTimeSel(".aboutText span:matches(.{4}年.{1,2}月.{1,2}日$)");


        //火币网

       jobParam.setListUrl("https://www.huobi.com/p/content/notice");
        jobParam.setNoticeASel(".notice li:eq(0) a");
        jobParam.setArticleGroupFlag("notice_huobi");
        jobParam.setContentSel("ul.detail");
        jobParam.setTitleSel(".tit h1");
        jobParam.setTimeSel("ul.detail span");


        //币多宝
        jobParam.setListUrl("https://www.biduobao.com/announcement/");
        jobParam.setNoticeASel(".info_list li:eq(0) a");
        jobParam.setArticleGroupFlag("notice_biduobao");
        jobParam.setContentSel(".info_show");
        jobParam.setTitleSel(".assets_content h1:eq(0)");
        jobParam.setTimeSel(".info_show_t span:eq(2)");

        jobParam.setListUrl("http://weixin.sogou.com/weixin?type=1&s_from=input&query=gh_6884e172c1bc&ie=utf8&_sug_=y&_sug_type_=&w=01019900&sut=6109&sst0=1500293031039&lkt=0%2C0%2C0");
        jobParam.setNoticeASel("a[uigs=account_article_0]");
        jobParam.setArticleGroupFlag("weixin_kuangren");
        jobParam.setContentSel("#js_content section section:eq(1)");
        jobParam.setTitleSel("title");
        jobParam.setTimeSel("#post-date");

        new SimpleJob(jobParam).execute();

       // new SimpleJob(jobParam).execute();
    }

}