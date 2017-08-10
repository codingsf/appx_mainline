import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;
import top.appx.util.DateUtil;
import top.appx.util.HttpUtil;

import javax.net.ssl.HttpsURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Date;

import static com.sun.webkit.network.URLs.newURL;


public class T1 {
    @Test
    public void test1()throws Exception{

        String str = Jsoup.connect("http://www.8btc.com/jzb").get().html();

        str = Jsoup.connect("http://www.8btc.com/overstock-now-accepts-cryptocurrencies-payment").get().html();
        System.out.println(str);


    }


}
