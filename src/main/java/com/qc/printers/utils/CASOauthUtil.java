package com.qc.printers.utils;

import com.alibaba.fastjson.JSONObject;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import com.qc.printers.pojo.entity.Token;
import com.qc.printers.pojo.entity.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class CASOauthUtil {

    public static Token getTokenByST(RestTemplate restTemplate,String st){
        if (restTemplate==null){
            throw new CustomException("认证失败");
        }
        String url2 = "http://10.15.245.153:55555/api2/oauth/";
        //LinkedMultiValueMap一个键对应多个值，对应format-data的传入类型
        Map<String, String> map = new HashMap<>();
        map.put("st",st);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
        JSONObject jsonObject = restTemplate.postForObject(url2,request, JSONObject.class);
        Integer code = jsonObject.getInteger("code");
        if (code!=1){
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
    public static User getUserByToken(RestTemplate restTemplate,Token token){
        if (restTemplate==null||token==null){
            throw new CustomException("认证失败");
        }
        String url2 = "http://10.15.245.153:55555/api2/oauth/accesstoken/";
        Map<String, String> map = new HashMap<>();
        map.put("accessToken",token.getAccessToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
        JSONObject jsonObject = restTemplate.postForObject(url2,request, JSONObject.class);
        Integer code = jsonObject.getInteger("code");
        if (code!=1){
            return null;
        }
        JSONObject userJSONObject = jsonObject.getObject("data",JSONObject.class);
        String studentId = userJSONObject.getString("studentId");
        String phone = userJSONObject.getString("phone");
        String permission = userJSONObject.getString("permission");
        User user = new User();
        user.setId(Long.valueOf(userJSONObject.getString("id")));
        user.setStatus(userJSONObject.getInteger("status"));
        user.setSex(userJSONObject.getString("sex"));
        user.setName(userJSONObject.getString("name"));
        user.setPermissionName(userJSONObject.getString("permissionName"));

        user.setUsername(userJSONObject.getString("username"));
        if (!StringUtils.isEmpty(studentId)){
            user.setStudentId(Long.valueOf(studentId));
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

    /**
     * 返回为null时前端强制重定向
     * @param restTemplate
     * @param token
     * @return
     */
    public static Token refreshToken(RestTemplate restTemplate,Token token){
        if (restTemplate==null||token==null){
            throw new CustomException("认证失败");
        }
        String url2 = "http://10.15.245.153:55555/api2/oauth/refreshtoken/";
        //LinkedMultiValueMap一个键对应多个值，对应format-data的传入类型
        Map<String, String> map = new HashMap<>();
        map.put("refreshToken",token.getRefreshToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
        JSONObject jsonObject = restTemplate.postForObject(url2,request, JSONObject.class);
        Integer code = jsonObject.getInteger("code");
        Token tokenr = jsonObject.getObject("data",Token.class);
        if (tokenr==null|| StringUtils.isEmpty(tokenr.getAccessToken())){
            throw new CustomException("认证失败");
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
    public static String cookieGetValue(HttpServletRequest request, String key){
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new CustomException("好奇怪，出错了");
        }
        String tgc = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                tgc = cookie.getValue();
                break;
            }
        }
        if (StringUtils.isEmpty(tgc)){
            throw new CustomException("出错了");
        }
        return tgc;
    }

}
