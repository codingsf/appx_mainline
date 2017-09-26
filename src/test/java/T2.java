import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.jsoup.Jsoup;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class T2 {

    public void ttt(){

    }
    @Test
    public void t3()throws Exception{
        String str = Jsoup.connect("https://twitter.com")
                .proxy("news.appx.top",8118)
                .get().html();
        System.out.println(str);

    }

    @Test
    public void t2(){

        try {
            String url="http://www.baidu.com/";
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Accept", "text/html, */*; q=0.01");
            headers.put("Accept-Encoding", "gzip, deflate, sdch");
            headers.put("Accept-Language", "zh-CN,zh;q=0.8");
            headers.put("Referer", url);
            headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:46.0) Gecko/20100101 Firefox/46.0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void t1()throws Exception{
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new MyConnectionSocketFactory(SSLContexts.createDefault()))
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        try {
            InetSocketAddress socksaddr = new InetSocketAddress("127.0.0.1", 1080);
            HttpClientContext context = HttpClientContext.create();
            context.setAttribute("socks.address", socksaddr);

            HttpHost target = new HttpHost("twitter.com", 443,"https");
            HttpGet request = new HttpGet("/");

            System.out.println("Executing request " + request + " to " + target + " via SOCKS proxy " + socksaddr);
            CloseableHttpResponse response = httpclient.execute(target, request, context);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));
                String result = "";
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }

                System.out.println(result);
            }catch (Exception ex){

            }

            finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }

    }

    static class MyConnectionSocketFactory extends SSLConnectionSocketFactory {

        public MyConnectionSocketFactory(final SSLContext sslContext) {
            super(sslContext);
        }

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }

    }
}
