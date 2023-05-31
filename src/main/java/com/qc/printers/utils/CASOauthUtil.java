package com.qc.printers.utils;

import com.alibaba.fastjson.JSONObject;
import com.qc.printers.common.Code;
import com.qc.printers.common.CustomException;
import com.qc.printers.config.CASConfig;
import com.qc.printers.pojo.entity.Token;
import com.qc.printers.pojo.entity.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 在Spring中不推荐使用静态方法
 * 无法进行注入
 * 可以使用@Component进行托管给IOC容器
 */
@Component
public class CASOauthUtil {
    @Autowired
    private CASConfig casConfig;

    public Token getTokenByCode(RestTemplate restTemplate, String code){
        if (restTemplate==null){
            throw new CustomException("认证失败");
        }
        String url2 = casConfig.getBaseUrl() + "oauth/access_token/";
        //LinkedMultiValueMap一个键对应多个值，对应format-data的传入类型
        Map<String, String> map = new HashMap<>();
        map.put("code",code);
        map.put("client_id",casConfig.getClientId());
        map.put("client_secret",casConfig.getClientSecret());
        map.put("grant_type","authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
        JSONObject jsonObject = restTemplate.postForObject(url2,request, JSONObject.class);
        if (!jsonObject.getInteger("code").equals(1)){
            throw new CustomException("认证失败");
        }
        Token token = jsonObject.getObject("data",Token.class);
        if (token==null|| StringUtils.isEmpty(token.getAccessToken())){
            throw new CustomException("认证失败");
        }
        return token;
    }

    /**
     * 如果返回null就尝试刷新token
     * @param restTemplate
     * @param token
     * @return
     */
    public User getUserByToken(RestTemplate restTemplate,Token token){
        if (restTemplate==null||token==null){
            throw new CustomException("认证失败", Code.DEL_TOKEN);
        }
        String url2 = casConfig.getBaseUrl() + "oauth/me/?access_token="+token.getAccessToken();
        JSONObject jsonObject = restTemplate.getForObject(url2, JSONObject.class);
        Integer code = jsonObject.getInteger("code");
        if (code!=1){
            return null;
        }
        JSONObject userJSONObject = jsonObject.getObject("data",JSONObject.class);
        String studentId = userJSONObject.getString("student_id");
        String phone = userJSONObject.getString("phone");
        String permission = userJSONObject.getString("permission");
        User user = new User();
        user.setId(Long.valueOf(userJSONObject.getString("id")));
        user.setStatus(userJSONObject.getInteger("status"));
        user.setSex(userJSONObject.getString("sex"));
        user.setName(userJSONObject.getString("name"));
        user.setUsername(userJSONObject.getString("username"));
        if (!StringUtils.isEmpty(studentId)){
            user.setStudentId(studentId);
        }
        if (!StringUtils.isEmpty(phone)){
            user.setPhone(phone);
        }
        user.setAvatar(userJSONObject.getString("avatar"));
        if (!StringUtils.isEmpty(permission)){
            user.setPermission(Integer.valueOf(permission));
        }
        return user;
    }

    public JSONObject getUserObjectByToken(RestTemplate restTemplate,Token token){
        if (restTemplate==null||token==null){
            throw new CustomException("认证失败", Code.DEL_TOKEN);
        }
        String url2 = casConfig.getBaseUrl() + "oauth/me/?access_token="+token.getAccessToken();
        JSONObject jsonObject = restTemplate.getForObject(url2, JSONObject.class);
        Integer code = jsonObject.getInteger("code");
        if (code!=1){
            return null;
        }
        JSONObject userJSONObject = jsonObject.getObject("data",JSONObject.class);
        return userJSONObject;
    }

    /**
     * 返回为null时前端强制重定向
     * @param restTemplate
     * @param token
     * @return
     */
    public Token refresh_token(RestTemplate restTemplate,Token token){
        if (restTemplate==null||token==null){
            throw new CustomException("认证失败",Code.DEL_TOKEN);
        }
        String url2 = casConfig.getBaseUrl() + "oauth/refresh_token/";
        //LinkedMultiValueMap一个键对应多个值，对应format-data的传入类型
        Map<String, String> map = new HashMap<>();
        map.put("refresh_token",token.getRefreshToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
        JSONObject jsonObject = restTemplate.postForObject(url2,request, JSONObject.class);
        Integer code = jsonObject.getInteger("code");
        Token tokenr = jsonObject.getObject("data",Token.class);
        if (tokenr==null|| StringUtils.isEmpty(tokenr.getAccessToken())){
            throw new CustomException("认证失败",Code.DEL_TOKEN);
        }
        if (code==1){
            //续期,token没过期
            return token;
        } else if (code==2) {
            //token过期了,处理方式都一样
            return token;
        }else {
            //返回900状态码 强制前端跳转登录
            return null;
        }

    }


}
