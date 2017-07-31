import org.jsoup.Jsoup;
import org.junit.Test;

public class T1 {
    @Test
    public void test1()throws Exception{
        Object str = Jsoup.connect("https://twitter.com/")
                //.proxy("10.22.40.32",8080)
                .get();

        System.out.println(str);

    }


}
