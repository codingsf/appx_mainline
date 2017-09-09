package top.appx.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.appx.entity.BtcOrder;
import top.appx.service.BtcOrderService;
import top.appx.util.HttpUtil;

import java.util.Date;

@Component
public class BtcOrderJob_jubi {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BtcOrderService btcOrderService;

    public void execute(){

        Date date = new Date();

        String str = HttpUtil.httpGet("https://www.jubi.com/coin/allcoin");
        JSONObject doc = JSONObject.parseObject(str);
        doc.keySet().forEach(coin->{
            deal(coin);
        });

        Date endDate = new Date();
        logger.info("订单获取耗时:"+(endDate.getTime()-date.getTime()));

    }
    private void deal(String coin){
        String str = HttpUtil.httpGet("https://www.jubi.com//api/v1/orders/?coin="+coin);
        JSONArray arr = JSONObject.parseArray(str);

        for (int i = 0; i <arr.size() ; i++) {
            JSONObject jsonObject = arr.getJSONObject(i);
            BtcOrder btcOrder = new BtcOrder();
            btcOrder.setExchange("jubi");
            btcOrder.setCoin(coin);
            btcOrder.setDate(new Date(jsonObject.getLong("date")*1000));
            btcOrder.setPrice(jsonObject.getDouble("price"));
            btcOrder.setAmount(jsonObject.getDouble("amount"));
            btcOrder.setTid(jsonObject.getLong("tid"));
            btcOrder.setType(jsonObject.getString("type"));
             if(btcOrderService!=null){
                 btcOrderService.insert(btcOrder);
             }
        }

    }
}
