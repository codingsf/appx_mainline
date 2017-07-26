package top.appx.job;

import org.springframework.stereotype.Component;
import top.appx.job.vo.JobParam;
@Component
public class JuBiJob extends SimpleJob {
    public JuBiJob() {
        JobParam jobParam = new JobParam();
        jobParam.setListUrl("https://www.jubi.com");
        jobParam.setArticleGroupFlag("notice_jubi");
        jobParam.setContentSel(".assets_content .about_text");
        jobParam.setTitleSel("title");
        jobParam.setTimeSel(".pub_date");
        jobParam.setNoticeASel(".jubi_news .pp_list li:eq(0) a");
        setJobParam(jobParam);
    }
}
