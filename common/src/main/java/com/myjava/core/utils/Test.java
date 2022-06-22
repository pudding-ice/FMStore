package com.myjava.core.utils;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Templates;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class Test {
    @Autowired
    RedisTemplate template;

    @org.junit.Test
    public void write() {
        template.boundHashOps("testKey").put("name", "guochun");
    }

    @org.junit.Test
    public void read() {
        String o = (String) template.boundHashOps("testKey").get("name");
        System.out.println(o);
    }
}
