package com.qc.printers.controller;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qc.printers.common.Code;
import com.qc.printers.common.NeedToken;
import com.qc.printers.common.R;
import com.qc.printers.pojo.QuickNavigationCategorizeResult;
import com.qc.printers.pojo.QuickNavigationItemResult;
import com.qc.printers.pojo.QuickNavigationResult;
import com.qc.printers.pojo.entity.PageData;
import com.qc.printers.pojo.entity.QuickNavigationCategorize;
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


    private final QuickNavigationCategorizeService quickNavigationCategorizeService;
    private final QuickNavigationService quickNavigationService;
    private final QuickNavigationItemService quickNavigationItemService;

    @Autowired
    public QuickNavigationController(QuickNavigationCategorizeService quickNavigationCategorizeService, QuickNavigationService quickNavigationService, QuickNavigationItemService quickNavigationItemService) {
        this.quickNavigationCategorizeService = quickNavigationCategorizeService;
        this.quickNavigationService = quickNavigationService;
        this.quickNavigationItemService = quickNavigationItemService;
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
            Long quickNavigationId = Long.valueOf(id.asString());
            return quickNavigationService.list(quickNavigationId);
        }catch (Exception e){
            log.error(e.getMessage());
            return R.error(Code.DEL_TOKEN,"未登录");
        }

    }

    /**
     * 导航分类管理系统
     * @return
     */
    @NeedToken
    @GetMapping("/listnavfenlei")
    //后期可以传回token拿到用户信息
    public R<PageData<QuickNavigationCategorizeResult>> listNavFenLei(Integer pageNum, Integer pageSize, String name) {
        return quickNavigationCategorizeService.listNavFenLei(pageNum,pageSize,name);

    }

    /**
     * 导航分类管理系统
     * @return
     */
    @NeedToken
    @GetMapping("/listnavfenleiitem")
    //后期可以传回token拿到用户信息
    public R<PageData<QuickNavigationItemResult>> listNavFenLeiItem(Integer pageNum, Integer pageSize, String name) {

        return quickNavigationItemService.listNavFenLeiItem(pageNum,pageSize,name);

    }

    @NeedToken
    @PutMapping("/updataforquicknavigationcategorize")
    public R<String> updataForQuickNavigationCategorize(@RequestBody QuickNavigationCategorize quickNavigation){

        if (StringUtils.isEmpty(quickNavigation.getName())){
            return R.error("更新失败");
        }
        if (quickNavigation.getId()==null){
            return R.error("更新失败");
        }
        return quickNavigationCategorizeService.updataForQuickNavigationCategorize(quickNavigation);
    }

    @NeedToken
    @DeleteMapping("/delete")
    public R<String> deleteNavigationCategorize(String id){
        log.info("id = {}",id);
        if (StringUtils.isEmpty(id)){
            return R.error("无操作对象");
        }
        return quickNavigationCategorizeService.deleteNavigationCategorize(id);

    }

}
