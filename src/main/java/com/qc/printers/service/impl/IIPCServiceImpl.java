package com.qc.printers.service.impl;

import com.google.common.collect.Lists;
import com.qc.printers.factories.Converter;
import com.qc.printers.factories.ConverterFactories;
import com.qc.printers.service.IIPCService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class IIPCServiceImpl implements IIPCService {

    private ConcurrentHashMap<String, Converter> converters = new ConcurrentHashMap<>();

    @Override
    public void open(String url, HttpServletResponse response, HttpServletRequest request) {
        String key = md5(url);
        AsyncContext async = request.startAsync();
        async.setTimeout(0);
        if (converters.containsKey(key)) {
            Converter c = converters.get(key);
            try {
                c.addOutputStreamEntity(key, async);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                response.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
                throw new IllegalArgumentException(e.getMessage());
            }
        } else {
            List<AsyncContext> outs = Lists.newArrayList();
            outs.add(async);
            ConverterFactories c = new ConverterFactories(url, key, converters, outs);
            c.start();
            converters.put(key, c);
        }
        response.setContentType("video/x-flv");
        response.setHeader("Connection", "keep-alive");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.flushBuffer();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public String md5(String plainText) {
        StringBuilder buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuilder("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return buf.toString();
    }

}
