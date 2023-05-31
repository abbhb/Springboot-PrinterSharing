package com.qc.printers.service.impl;


import com.qc.printers.common.MyString;
import com.qc.printers.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class IRedisServiceImpl implements IRedisService {

    private RedisTemplate redisTemplate;

    //指定用redis的序列化方式进行序列化
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();//序列化为String
        //不能反序列化
        //Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);//序列化为Json
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(serializer);
        this.redisTemplate = redisTemplate;
    }

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
    public void addApiCount() {
        redisTemplate.opsForValue().increment(MyString.pre_api_count);
    }

    @Override
    public void cleanApiCount() {
        //每日清零
        redisTemplate.opsForValue().set(MyString.pre_api_count_latday,redisTemplate.opsForValue().get(MyString.pre_api_count));
        redisTemplate.opsForValue().set(MyString.pre_api_count,0);
    }

    @Override
    public int getLastDayCountApi() {
        Object o =  redisTemplate.opsForValue().get(MyString.pre_api_count_latday);
        if (o == null){
            return 0;
        }
        return (int)o;
    }

    @Override
    public int getCountApi() {
        int o = (int) redisTemplate.opsForValue().get(MyString.pre_api_count);
        return o;
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
