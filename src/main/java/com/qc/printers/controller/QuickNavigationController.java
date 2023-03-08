package com.qc.printers.controller;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qc.printers.common.Code;
import com.qc.printers.common.NeedToken;
import com.qc.printers.common.R;
import com.qc.printers.pojo.QuickNavigationResult;
import com.qc.printers.service.QuickNavigationCategorizeService;
import com.qc.printers.service.QuickNavigationItemService;
import com.qc.printers.service.QuickNavigationService;
import com.qc.printers.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController//@ResponseBody+@Controller
@RequestMapping("/quicknavigation")
@Slf4j
public class QuickNavigationController {


    private final QuickNavigationService quickNavigationService;

    @Autowired
    public QuickNavigationController(QuickNavigationService quickNavigationService) {
        this.quickNavigationService = quickNavigationService;
    }

    @NeedToken
    @GetMapping("/list")
    //后期可以传回token拿到用户信息
    public R<List<QuickNavigationResult>> list(@RequestHeader(value="Authorization", defaultValue = "") String token) {
        if (StringUtils.isEmpty(token)){
            return R.error(Code.DEL_TOKEN,"未登录");
        }
        try {
            DecodedJWT decodedJWT = JWTUtil.deToken(token);
            Claim id = decodedJWT.getClaim("id");
            Long userId = Long.valueOf(id.asString());
            return quickNavigationService.list(userId);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.error(Code.DEL_TOKEN,"未登录");
        }

    }
}
