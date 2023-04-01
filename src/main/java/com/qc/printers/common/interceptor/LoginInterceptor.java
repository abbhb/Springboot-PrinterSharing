package com.qc.printers.common.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.annotation.NeedToken;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.service.UserService;
import com.qc.printers.utils.JWTUtil;
import com.qc.printers.utils.ThreadLocalUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
@Api("此拦截器用于获取用户基本信息存在threadlocal内")
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            System.out.println("OPTIONS请求，放行");
            return true;
        }
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.getAnnotation(NeedToken.class) == null){
            //如果没有前置条件 需要登陆
            //权限校验直接通过
            return true;
        }
        String authorization =  request.getHeader("Authorization");
        DecodedJWT decodedJWT = JWTUtil.deToken(authorization);
        Claim cid = decodedJWT.getClaim("id");
        if (cid==null){
            throw new CustomException("无法获取用户信息");
        }
        Long userId = Long.valueOf(cid.asString());
        User user = userService.getById(userId);
        if (user==null){
            throw new CustomException("无法获取用户信息");
        }
        ThreadLocalUtil.addCurrentUser(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //防止内存泄露，对ThreadLocal里的对象进行清除
        ThreadLocalUtil.remove();
    }
}
