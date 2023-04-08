package com.qc.printers.utils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.qc.printers.common.CustomException;
import com.qc.printers.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.qc.printers.common.MyString.public_file;

@Slf4j
public class WordPrintUtil {
    public static void wordToPDF(String filename) {

        System.out.println("启动 Word...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();
            doc = Dispatch.call(docs, "Open",public_file+"\\"+filename).toDispatch();
            log.info("打开文档:" + public_file+"\\"+filename);
            Dispatch.call(doc, "SaveAs", public_file+"\\"+filename+".pdf", // FileName
                    17);//17是pdf格式
            long end = System.currentTimeMillis();

        } catch (Exception e) {
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            Dispatch.call(doc, "Close", false);
            System.out.println("关闭文档");
            if (app != null)
                app.invoke("Quit", new Variant[]{});
        }
        // 如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
    }

    public static String saveComputer(MultipartFile file){
        //再是保存文件的文件夹
        File folder = new File(public_file);
        //如果不存在，就自己创建
        if(!folder.exists()){
            folder.mkdirs();
        }
        String originName = file.getOriginalFilename();
        String suffix = StringUtils.substringAfterLast(originName , ".");
        //随机文件名,后期做个记录，跟用户绑定
        String newName = UUID.randomUUID().toString() + "."+suffix;

        //然后就可以保存了
        try {
            file.transferTo(new File(folder,newName));
            //存入库加入打印队列
            log.info("路径为:{}",folder+"\\"+newName);
            return newName;

        } catch (IOException e) {
            //返回异常
            log.error(e.getMessage());
            throw new CustomException("异常");
        }
    }

}
