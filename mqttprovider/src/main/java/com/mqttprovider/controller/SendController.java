package com.mqttprovider.controller;

import com.mqttprovider.domain.Data;
import com.mqttprovider.service.DataService;
import com.mqttprovider.service.MqttProviderConfig;
import com.mqttprovider.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;

@Controller
public class SendController {
    @Autowired
    private MqttProviderConfig providerClient;
    @Autowired
    private DataService dataService;

    @RequestMapping("/sendMessage")
    @ResponseBody
    public String sendMessage(int qos,boolean retained,String topic){
        List<Data> dataset = dataService.query();
        Iterator<Data> iterator = dataset.iterator();
        while(iterator.hasNext()) {
            Data data = iterator.next();
            String s = JsonUtil.objectToJson(data);
            try {
                providerClient.publish(qos, retained, topic, s);
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
                return "发送失败";
            }
        }
        return "数据全部发送成功";
    }
}
