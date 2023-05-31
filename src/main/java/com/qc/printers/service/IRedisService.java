package com.qc.printers.service;

public interface IRedisService {
//    String getTokenId(String token);

    void setTokenWithTime(String token,String value,Long time);

    void del(String token);

    Long getTokenTTL(String uuid);
    String getValue(String key);

    void hashPut(String key,String hashKey,Object object);

    Object getHash(String key, String hashKey);

    void addApiCount();

    void cleanApiCount();

    int getLastDayCountApi();

    int getCountApi();
}
