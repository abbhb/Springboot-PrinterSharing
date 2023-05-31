package com.qc.printers.aspect;

import cn.hutool.core.util.TypeUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qc.printers.common.annotation.NeedToken;
import com.qc.printers.common.R;
import com.qc.printers.mapper.LogMapper;
import com.qc.printers.pojo.entity.Log;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.service.IRedisService;
import com.qc.printers.utils.JWTUtil;
import com.qc.printers.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Autowired
    private LogMapper logDao;

    @Autowired
    private IRedisService iRedisService;

    /**
     * 设置操作日志切入点   在注解的位置切入代码
     */
    @Pointcut("@annotation(com.qc.printers.common.annotation.NeedToken)")
    public void operLogPoinCut() {
    }

    /**
     * 记录操作日志
     * @param joinPoint 方法的执行点
     * @param result  方法返回值
     * @throws Throwable
     */
    @AfterReturning(returning = "result", value = "operLogPoinCut()")
    public void saveOperLog(JoinPoint joinPoint, Object result) throws Throwable {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        try {
            Log sysLog = new Log();
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取切入点所在的方法
            Method method = signature.getMethod();
            //获取操作
            NeedToken annotation = method.getAnnotation(NeedToken.class);
//            if (annotation != null) {
//                sysLog.setModel(annotation.operModul());
//                sysLog.setType(annotation.operType());
//                sysLog.setDescription(annotation.operDesc());
//            }
            iRedisService.addApiCount();

            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            sysLog.setMethod(methodName); // 类名.请求方法

            sysLog.setCreateTime(LocalDateTime.now()); //操作时间
            //操作用户 --登录时有把用户的信息保存在session中，可以直接取出
            User currentUser = ThreadLocalUtil.getCurrentUser();//线程安全
            sysLog.setUserId(String.valueOf(currentUser.getId()));
            sysLog.setIp(ServletUtil.getClientIP(request)); //操作IP IPUtils工具类网上大把的，比如工具类集锦的hutool.jar
            sysLog.setUrl(request.getRequestURI()); // 请求URI
            // 方法请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);
            //获取json的请求参数
            if (rtnMap == null || rtnMap.size() == 0) {
                params = getJsonStrByRequest(request);
            }
            sysLog.setParams(params); // 请求参数
            R dataResult = (R)result;  //返回值信息
            //需要先判断返回值是不是Map <String, Object>，如果不是會拋異常，需要控制层的接口返回数据格式统一
            //如果嫌返回格式统一太麻烦建议日志保存时去掉操作结果
            sysLog.setType(request.getMethod());
            sysLog.setModel(dataResult.getData().toString());
            sysLog.setResult(dataResult.getMsg()); //獲取方法返回值中的msg，如果上面的類型錯誤就拿不到msg就會拋異常
            logDao.insert(sysLog);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("日誌記錄異常，請檢查返回值是否是Map <String, Object>類型");
        }
    }

    /**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 获取json格式 请求参数
     */
    public String getJsonStrByRequest(HttpServletRequest request) {
        String param = null;

        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
            param = jsonObject.toJSONString();
            System.out.println(param);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return param;
    }


    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        return message;
    }

}
