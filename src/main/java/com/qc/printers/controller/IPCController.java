package com.qc.printers.controller;


import com.qc.printers.common.annotation.NeedToken;
import com.qc.printers.common.annotation.PermissionCheck;
import com.qc.printers.service.IIPCService;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "ipc")
@RequestMapping("/ipc")
@RestController
public class IPCController {
    @Autowired
    private IIPCService iipcService;

    @GetMapping(value = "/flv/{channel}")
    @NeedToken
    @PermissionCheck("1")
    public void open4(@PathVariable(value = "channel") Integer channel, HttpServletResponse response,
                      HttpServletRequest request) {
        String url = "rtsp://admin:aizhineng1404@192.168.12.242:554/stream1";
        if (channel.equals(1)){
            url = "rtsp://admin:aizhineng1404@192.168.12.242:554/stream1";
        }else if (channel.equals(2)){
            url = "rtsp://admin:aizhineng1404@192.168.12.243:554/stream1";
        }
        if(!StringUtils.isEmpty(url)){
            iipcService.open(url, response, request);
        }
    }
}
