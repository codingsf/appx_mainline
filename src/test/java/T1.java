import org.jsoup.Jsoup;
import org.junit.Test;
import top.appx.zutil.HttpUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

import static com.sun.webkit.network.URLs.newURL;


public class T1 {
    @Test
    public void test1()throws Exception{

        String str = HttpUtil.httpGet("https://btc018.com/service/ourService.html?id=1");
        System.out.println(str);


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
