package com.example.administrator.mychatdemo;

/**
 * Created by Administrator on 2016/9/27.
 */
public class VideoFilePath {
    private  String  videoPath;
    private  long  videoAccess;
    private  long   videoLength;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public long getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(long videoLength) {
        this.videoLength = videoLength;
    }

    public long getVideoAccess() {
        return videoAccess;
    }

    public void setVideoAccess(long videoAccess) {
        this.videoAccess = videoAccess;
    }
}
