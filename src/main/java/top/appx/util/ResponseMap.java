package top.appx.util;

import java.util.HashMap;

public class ResponseMap extends HashMap<String,Object> {

    public static ResponseMap instance(){
        return new ResponseMap();
    }

    public ResponseMap p(String key,Object object){
        this.put(key,object);
        return this;
    }

    public static ResponseMap message(String msg){
        return instance().p("msg",msg);
    }
}
