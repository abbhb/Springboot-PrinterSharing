package com.qc.printers.service.impl;


import com.qc.printers.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class IRedisServiceImpl implements IRedisService {

    private RedisTemplate redisTemplate;

    @Autowired
    public IRedisServiceImpl(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
//    @Override
//    public String getTokenId(String token) {
//        return (String)redisTemplate.opsForValue().get(token);
//    }

    @Override
    public void setTokenWithTime(String token, String value, Long time) {
        redisTemplate.opsForValue().set(token, value, time, TimeUnit.SECONDS);
    }

    @Override
    public void del(String token) {
        redisTemplate.delete(token);

    }
    @Override
    public void hashPut(String key,String hashKey,Object object) {
        redisTemplate.opsForHash().put(key,hashKey,object);
    }

    @Override
    public Object getHash(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key,hashKey);
    }

    @Override
    public Long getTokenTTL(String uuid) {
        Long expire = redisTemplate.getExpire(uuid);
        return expire;
    }

    @Override
    public String getValue(String key) {
        return (String)redisTemplate.opsForValue().get(key);
    }
}
