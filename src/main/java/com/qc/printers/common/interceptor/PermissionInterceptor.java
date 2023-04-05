package com.qc.printers.common.interceptor;

import com.qc.printers.common.CustomException;
import com.qc.printers.common.annotation.NeedToken;
import com.qc.printers.common.annotation.PermissionCheck;

import com.qc.printers.pojo.entity.User;
import com.qc.printers.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 在拦截器中，如果请求为OPTIONS请求，则返回true，表示可以正常访问，然后就会收到真正的GET/POST请求
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
        if (method.getAnnotation(PermissionCheck.class) == null){
            return true;
        }
        //权限校验
        //需要的权限
        String annotationValue = method.getAnnotation(PermissionCheck.class).value();
        User currentUser = ThreadLocalUtil.getCurrentUser();

        if (currentUser==null){
            throw new CustomException("没有权限");
        }
        /**
         * 10为系统管理员
         */
        if ((!currentUser.getPermission().equals(10))&&(!currentUser.getPermission().equals(Integer.valueOf(annotationValue)))){
            throw new CustomException("没有权限");
        }
        return true;
    }
}
