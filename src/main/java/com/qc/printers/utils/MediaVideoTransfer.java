package com.qc.printers.utils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;

import java.io.OutputStream;

/**
 * 转换rtsp为flv
 *
 * @author IT_CREATE
 * @date 2021/6/8 12:00:00
 */
@Slf4j
public class MediaVideoTransfer {
    @Setter
    private OutputStream outputStream;

    @Setter
    private String rtspUrl;

    @Setter
    private String rtspTransportType;

    private FFmpegFrameGrabber grabber;

    private FFmpegFrameRecorder recorder;

    private boolean isStart = false;

    /**
     * 开启获取rtsp流
     */
    public void live() {
        log.info("连接rtsp：" + rtspUrl + ",开始创建grabber");
        boolean isSuccess = createGrabber(rtspUrl);
        if (isSuccess) {
            log.info("创建grabber成功");
        } else {
            log.info("创建grabber失败");
        }
        startCameraPush();
    }

    /**
     * 构造视频抓取器
     *
     * @param rtsp 拉流地址
     * @return 创建成功与否
     */
    private boolean createGrabber(String rtsp) {
        // 获取视频源
        try {
            grabber = FFmpegFrameGrabber.createDefault(rtsp);
            grabber.setOption("rtsp_transport", rtspTransportType);
            grabber.start();
            isStart = true;

            recorder = new FFmpegFrameRecorder(outputStream, grabber.getImageWidth(), grabber.getImageHeight(), grabber.getAudioChannels());
            //avcodec.AV_CODEC_ID_H264  //AV_CODEC_ID_MPEG4
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            recorder.setFormat("flv");
            recorder.setFrameRate(grabber.getFrameRate());
            recorder.setSampleRate(grabber.getSampleRate());
            recorder.setAudioChannels(grabber.getAudioChannels());
            recorder.setFrameRate(grabber.getFrameRate());
            return true;
        } catch (FrameGrabber.Exception e) {
            log.error("创建解析rtsp FFmpegFrameGrabber 失败");
            log.error("create rtsp FFmpegFrameGrabber exception: ", e);
            stop();
            reset();
            return false;
        }
    }

    /**
     * 推送图片（摄像机直播）
     */
    private void startCameraPush() {
        if (grabber == null) {
            log.info("重试连接rtsp：" + rtspUrl + ",开始创建grabber");
            boolean isSuccess = createGrabber(rtspUrl);
            if (isSuccess) {
                log.info("创建grabber成功");
            } else {
                log.info("创建grabber失败");
            }
        }
        try {
            if (grabber != null) {
                recorder.start();
                Frame frame;
                while (isStart && (frame = grabber.grabFrame()) != null) {
                    recorder.setTimestamp(grabber.getTimestamp());
                    recorder.record(frame);
                }
                stop();
                reset();
            }
        } catch (FrameGrabber.Exception | RuntimeException | FrameRecorder.Exception e) {
            log.error(e.getMessage(), e);
            stop();
            reset();
        }
    }

    private void stop() {
        try {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
            }
            if (grabber != null) {
                grabber.stop();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void reset() {
        recorder = null;
        grabber = null;
        isStart = false;
    }
}
