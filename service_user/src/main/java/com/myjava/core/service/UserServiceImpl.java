package com.myjava.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.myjava.core.dao.user.UserDao;
import com.myjava.core.pojo.response.ResultMessage;
import com.myjava.core.pojo.user.User;
import com.myjava.core.pojo.user.UserQuery;
import com.myjava.core.utils.Constants;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    ActiveMQQueue smsDestination;
    @Autowired
    UserDao userDao;

    @Override
    public void sendCode(final String phone) {
        //1. 生成一个随机6为数字, 作为验证码
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < 7; i++) {
            int s = new Random().nextInt(10);
            sb.append(s);
        }
        //2. 手机号作为key, 验证码作为value保存到redis中, 生存时间为10分钟
        redisTemplate.boundValueOps(phone).set(sb.toString(), 60 * 10, TimeUnit.SECONDS);
        final String smsCode = sb.toString();

        //3. 将手机号, 短信内容, 模板编号, 签名封装成map消息发送给消息服务器
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("mobile", phone);//手机号
//                因为是模板发送,所以不需要模板代码和签名
//                message.setString("template_code", "SMS_169111508");//模板编码
//                message.setString("sign_name", "疯码");//签名
                Map map = new HashMap();
                map.put("code", smsCode);    //验证码
                message.setString("param", JSON.toJSONString(map));
                return (Message) message;
            }
        });
    }

    @Override
    public ResultMessage addUser(String smscode, User user) {
        String phone = user.getPhone();
        String redisSmsCode = (String) redisTemplate.boundValueOps(phone).get();
        if (redisSmsCode == null) {
            return new ResultMessage(false, "请先发送验证码或者验证码已失效");
        }
        if (!redisSmsCode.equals(smscode)) {
            return new ResultMessage(false, "验证码错误");
        }
        user.setCreated(new Date());
        user.setUpdated(new Date());
        userDao.insert(user);
        return new ResultMessage(true, "注册成功");
    }

    @Override
    public User getUserByName(String username) {
        if (Constants.ANONYMOUS_USER.equals(username)) {
            User user = new User();
            user.setUsername("游客");
            return user;
        }
        UserQuery query = new UserQuery();
        UserQuery.Criteria criteria = query.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> users = userDao.selectByExample(query);
        if (users == null || users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }
}
