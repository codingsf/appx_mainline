import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;
import sun.security.ssl.SSLSocketImpl;
import top.appx.util.DateUtil;
import top.appx.util.HttpUtil;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Date;

import static com.sun.webkit.network.URLs.newURL;


public class T1 {
    @Test
    public void test1()throws Exception{
        InetSocketAddress socksaddr = new InetSocketAddress("news.appx.top",1080);


        InetSocketAddress socketAddress=new InetSocketAddress(
                InetAddress.getByName("59.110.171.129"),1080);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS,socksaddr);

       String str = Jsoup.connect("https://twitter.com").proxy(proxy).get().html();
      //  System.out.println(str);



    }
    @Test
    public void test2()throws Exception{


        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("username", "password"
                        .toCharArray());
            }
        });
        // 设置HTTP代理
        Proxy proxy = new Proxy(Proxy.Type.SOCKS,
                new InetSocketAddress("news.appx.top", 1080));
        Socket socket = new Socket(proxy);
        socket.connect(new InetSocketAddress("twitter.com", 80));
        OutputStream output = socket.getOutputStream();
        InputStreamReader isr = new InputStreamReader(socket.getInputStream(),
                "GBK");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder request = new StringBuilder();
        //request.append("GET /index.php HTTP/1.1\r\n");
       // request.append("Accept-Language: zh-cn\r\n");
       // request.append("Host: twitter.com\r\n");
        request.append("\r\n");
        output.write(request.toString().getBytes());
        output.flush();

        StringBuilder sb = new StringBuilder();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str + "\n");
            System.out.println(str);
        }
        System.out.println(sb.toString());
        br.close();
        isr.close();
        output.close();
    }


}
