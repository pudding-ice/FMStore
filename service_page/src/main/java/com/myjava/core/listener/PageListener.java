package com.myjava.core.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.core.service.CmsService;
import com.myjava.core.service.CmsServiceImpl;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Map;

public class PageListener implements MessageListener {
    @Autowired
    private CmsServiceImpl cmsService;

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;
        try {
            String goodsId = atm.getText();
            Map<String, Object> goodsData = cmsService.findGoodsData(Long.parseLong(goodsId));
            cmsService.createStaticPage(Long.parseLong(goodsId), goodsData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}