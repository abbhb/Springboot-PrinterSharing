package com.qc.printers.config;


import com.qc.printers.common.JacksonObjectMapper;
import com.qc.printers.common.JwtTokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
//        log.info("开始静态资源映射");
//        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");//此处classpath:/front/必须得在末尾加/，否则无法访问
//        registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/dist/");
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截规则
        InterceptorRegistration ir = registry.addInterceptor(jwtTokenInterceptor);
        // 拦截路径，员工请求的路径都拦截
//        ir.addPathPatterns("/employee/**");
//        ir.addPathPatterns("/store/**");
        ir.addPathPatterns("/**");

        // 不拦截路径，如：注册、登录、忘记密码等
        ir.excludePathPatterns("/user/login","/user/logout","/swagger-ui/**");//可以直接逗号往后加
    }
    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
    }
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html") .setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
        super.addViewControllers(registry);
    }
}
