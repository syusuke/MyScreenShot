package com.gson8.myscreenshot.fileselector.config;


import android.os.Environment;

import com.gson8.myscreenshot.fileselector.utils.FileFilter;

public class FileConfig {
    //public static final String FILE_CONFIG = "fileConfig";

    public static boolean showHiddenFiles = false;
    public static int filterModel = FileFilter.FILTER_NONE;      //过滤文件
    public static String[] filter;
    public static boolean positiveFiter = true;
    //文件选择可以访问的最高路径,默认为"/"
    public static String rootPath = "/";
    //文件选择器的开始的路径，默认为"/"
//    public static String startPath = "/";
    public static String startPath = Environment.getExternalStorageDirectory() + "";

    //public static int selectType = FileType.All;  //选择文件的种类:文件夹、文件、全部


    //public static final String SELECT = "select";


}
