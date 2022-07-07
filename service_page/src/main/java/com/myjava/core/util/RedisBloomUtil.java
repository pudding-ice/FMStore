package com.myjava.core.util;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.PipedReader;
import java.util.List;
import java.util.Set;

@Component
public class RedisBloomUtil {
    public RBloomFilter<String> bloomFilter = null;
    private String REDIS_SERVICE_URL;

    public void createBloomFilter() {
        Config config = new Config();
        config.useSingleServer().setAddress(REDIS_SERVICE_URL).setPassword("sm19751011");
        //构造redisson
        RedissonClient redissonClient = Redisson.create(config);
        bloomFilter = redissonClient.getBloomFilter("bloom");
        //初始化布隆过滤器,预计元素数量1000000,误判率为1%
        bloomFilter.tryInit(1000000L, 0.01);
    }

    public void insertDataIntoBloom(Set data) {
        for (Object obj : data) {
            bloomFilter.add(String.valueOf(obj));
        }
    }

    public boolean containsObj(String key) {
        return bloomFilter.contains(key);
    }


    public void setREDIS_SERVICE_URL(String REDIS_SERVICE_URL) {
        this.REDIS_SERVICE_URL = REDIS_SERVICE_URL;
    }
}
