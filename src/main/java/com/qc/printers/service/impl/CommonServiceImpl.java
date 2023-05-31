package com.qc.printers.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qc.printers.common.Code;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import com.qc.printers.config.MinIoProperties;
import com.qc.printers.mapper.LogMapper;
import com.qc.printers.pojo.entity.Log;
import com.qc.printers.pojo.entity.ToEmail;
import com.qc.printers.pojo.entity.User;
import com.qc.printers.service.CommonService;
import com.qc.printers.service.IRedisService;
import com.qc.printers.service.UserService;
import com.qc.printers.utils.JWTUtil;
import com.qc.printers.utils.MinIoUtil;
import com.qc.printers.utils.VerCodeGenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Autowired
    MinIoProperties minIoProperties;
    private final IRedisService iRedisService;

    @Autowired
    private LogMapper logMapper;
    private final UserService userService;
//    @Autowired
//    private JavaMailSender mailSender;

    @Autowired
    public CommonServiceImpl(IRedisService iRedisService, UserService userService) {
        this.iRedisService = iRedisService;
        this.userService = userService;
    }

    public R<String> uploadFileTOMinio(MultipartFile file) {
        try {
            String fileUrl = MinIoUtil.upload(minIoProperties.getBucketName(), file);
            log.info("imageUrl={}",fileUrl);
            String[] split = fileUrl.split("\\?");

            return R.successOnlyObject(split[0]);
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public String getImageUrl(String imageKey) {
        if (imageKey.contains("http")){
            return imageKey;
        }
        return minIoProperties.getUrl()+"/"+minIoProperties.getBucketName()+"/"+imageKey;
    }

    @Override
    public R<String> sendEmailCode(ToEmail toEmail) {
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        message.setFrom("3482238110@qq.com");
//
//        message.setTo(toEmail.getTos());
//
//        message.setSubject("您本次的验证码是");
//
//        String verCode = VerCodeGenerateUtil.generateVerCode();
//        //需要同时存入redis,key使用emailcode:userid
//        Claim id = null;
//        try {
//            DecodedJWT decodedJWT = JWTUtil.deToken(token);
//            id = decodedJWT.getClaim("id");
//
//        }catch (Exception e){
//            return R.error(Code.DEL_TOKEN,"登陆过期");
//        }
//        User byId = userService.getById(Long.valueOf(id.asString()));
//
//        if (byId==null){
//            return R.error("err");
//        }
//        message.setText("尊敬的"+byId.getName()
//                +",您好:\n"
//                + "\n本次请求的邮件验证码为:" + verCode + ",本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）\n"
//                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");
//
//        if (id==null){
//            return R.error("登陆过期");
//        }
//        mailSender.send(message);
//        iRedisService.setTokenWithTime("emailcode:"+id.asString(),verCode, 300L);
        return R.success("业务暂停");
    }

    @Override
    public Integer countApi() {
//      获取日志表当日请求数
        return iRedisService.getCountApi();
    }

    @Override
    public Integer apiCountLastday() {
        Integer lastDayCountApi = iRedisService.getLastDayCountApi();
        if (lastDayCountApi!=null){
            return lastDayCountApi;
        }
        return 0;
    }
}