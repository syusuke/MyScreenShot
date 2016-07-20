package com.gson8.myscreenshot.fileselector.utils;


import com.gson8.myscreenshot.fileselector.config.FileConfig;

import java.io.Serializable;

public class FileFilter implements Serializable {
    public static final int FILTER_IMAGE = 1;
    public static final int FILTER_TXT = 2;
    public static final int FILTER_AUDIO = 3;
    public static final int FILTER_VIDEO = 4;
    public static final int FILTER_CUSTOM = 10;
    public static final int FILTER_COMPROSSION = 16;
    public static final int FILTER_NONE = -1;

    public static final String[] IMAGE_FILTER = new String[]{"png", "jpg", "bmp", "gif"};

    public static final String[] TXT_FILTER = new String[]{"txt", "c", "cpp", "java", "xml"};

    public static final String[] AUDIO_FILTER =
            new String[]{"mp3", "m4a", "ogg", "flac", "ape", "aac"};

    public static final String[] VIDEO_FILTER = new String[]{"mp4", "avi", "flv", "rmvb", "mkv"};

    public static final String[] ZIP_FILTER = new String[]{"zip"};

    public static final String[] RAR_FILTER = new String[]{"rar"};

    /**
     * 过滤处理
     *
     * @param lastName 文件后缀名
     * @return 匹配成功：true
     */
    public static boolean doFilter(String lastName) {
        if(lastName == null)
            return false;

        int model = FileConfig.filterModel;
        String[] filter = null;
        switch(model) {
            case FILTER_AUDIO:
                filter = AUDIO_FILTER;
                break;
            case FILTER_IMAGE:
                filter = IMAGE_FILTER;
                break;
            case FILTER_TXT:
                filter = TXT_FILTER;
                break;
            case FILTER_VIDEO:
                filter = VIDEO_FILTER;
                break;
            case FILTER_COMPROSSION:
                filter = ZIP_FILTER;
                break;
            default:
                filter = FileConfig.filter;
                break;
        }

        for(String f : filter) {
            if(f.equals(lastName))
                return true;
        }

        return false;
    }

}
