package top.appx.util;

import org.junit.Test;

public class PasswordUtilTest {
    @Test
    public void md5() throws Exception {
        System.out.println(PasswordUtil.md532("123"));
    }

}