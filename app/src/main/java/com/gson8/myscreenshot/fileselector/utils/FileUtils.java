package com.gson8.myscreenshot.fileselector.utils;


import com.gson8.myscreenshot.R;
import com.gson8.myscreenshot.fileselector.config.FileConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class FileUtils {
    private static FileCompare fileCompare = new FileCompare();

    //获取文件列表
    public static ArrayList<HashMap<String, Object>> getFileList(String path) {
        File file;
        file = new File(path);

        if(file.exists()) {

            if(file.isDirectory()) {

                File[] files = file.listFiles();
                ArrayList<HashMap<String, Object>> ds = new ArrayList<>();
                ArrayList<HashMap<String, Object>> fs = new ArrayList<>();
                if(!path.equals(FileConfig.rootPath)) {
                    HashMap<String, Object> up = new HashMap<>(3);
                    up.put(FileSelectorConfig.NAME, "...返回上一级");
                    up.put(FileSelectorConfig.ICON, R.drawable.ic_back_file_selector);
                    up.put(FileSelectorConfig.PATH, path);
                    up.put(FileSelectorConfig.TYPE, FileType.UPTO);
                    //up.put(FileConfig.SELECT, FileType.forbid);
                    ds.add(up);
                }

                for(File e : files) {
                    HashMap<String, Object> value = new HashMap<>(3);
                    String p = e.getAbsolutePath();         //每个文件的路径
                    String name = getFileName(p);           //每个文件的名字

                    /**
                     * 隐藏文件处理
                     */
                    if(isHitFiles(name)) {
                        if(!FileConfig.showHiddenFiles) {
                            continue;
                        }
                    }

                    /**
                     * 文件夹处理
                     */
                    if(e.isDirectory()) {
                        value.put(FileSelectorConfig.NAME, name);
                        value.put(FileSelectorConfig.PATH, p);
                        value.put(FileSelectorConfig.ICON, R.drawable.ic_back_file_selector);
                        value.put(FileSelectorConfig.TYPE, FileType.FOLDER);
                        //value.put(FileConfig.SELECT, FileConfig.selectType == FileType.FILE ? FileType.forbid : FileType.unselect);
                        ds.add(value);
                    } else        //文件处理
                    {
                        String lastName = getFileLastName(name);

                        //如果需要过滤
                        if(FileConfig.filterModel != FileFilter.FILTER_NONE) {
                            //如果是positive过滤
                            if(FileConfig.positiveFiter) {
                                //如果匹配不成功,则跳过此次循环
                                if(!FileFilter.doFilter(lastName)) {
                                    continue;
                                }
                            } else {//native 过滤
                                //如果匹配成功则跳过此次循环
                                if(FileFilter.doFilter(lastName)) {
                                    continue;
                                }
                            }
                        }

                        int type = getFileType(name);
                        int icon;
                        switch(type) {
                            case FileType.AUDIO:
                                icon = R.drawable.ic_music_file_selector;
                                break;
                            case FileType.VIDEO:
                                icon = R.drawable.ic_movie_file_selector;
                                break;
                            case FileType.TEX:
                                icon = R.drawable.ic_txt_file_selector;
                                break;
                            case FileType.IMAGE:
                                icon = R.drawable.ic_photo_file_selector;
                                break;
                            case FileType.COMPRESSION:
                                icon = R.drawable.ic_compro_file_selector;
                                break;
                            default:
                                icon = R.drawable.ic_other_file_selector;
                                break;
                        }

                        value.put(FileSelectorConfig.NAME, name);
                        value.put(FileSelectorConfig.PATH, p);
                        value.put(FileSelectorConfig.ICON, icon);
                        value.put(FileSelectorConfig.TYPE, FileType.FILE);
                        //value.put(FileConfig.SELECT, FileConfig.selectType == FileType.FOLDER ? FileType.forbid : FileType.unselect);

                        fs.add(value);

                    }
                }

                Collections.sort(ds, fileCompare);
                Collections.sort(fs, fileCompare);

                ds.addAll(fs);
                return ds;
            }
        }
        return null;

    }

    //获取文件的种类
    public static int getFileType(String name) {
        int type = FileType.OTHER;

        String last = getFileLastName(name);
        if(last == null)
            return type;

        if(last.equals("jpg") | last.equals("png") | last.equals("bmp") | last.equals("gif")) {
            type = FileType.IMAGE;
        } else if(last.equals("avi") | last.equals("mp4") | last.equals("mkv") |
                last.equals("flv") | last.equals("rmvb")) {
            type = FileType.VIDEO;
        } else if(last.equals("txt") | last.equals("c") | last.equals("cpp") | last.equals("java") |
                last.equals("xml"))

        {
            type = FileType.TEX;
        } else if(last.equals("mp3") | last.equals("m4a") | last.equals("ogg") |
                last.equals("flac") | last.equals("ape") | last.equals("aac")) {
            type = FileType.AUDIO;
        } else if(last.equals("zip") || last.equals("rar")) {
            type = FileType.COMPRESSION;
        }
        return type;
    }

    //获取文件的后缀名
    public static String getFileLastName(String name) {
        if(!name.contains(".")) {
            return null;
        }
        return name.substring(name.lastIndexOf(".") + 1);
    }

    //通过文件路径获取文件的名字

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.length());
    }

    //判断文件是否是隐藏文件
    public static boolean isHitFiles(String name) {
        return name.substring(0, 1).equals(".");
    }

    //获取文件父目录
    public static String getParentPath(String path) {
        File file = new File(path);

        if(file.exists()) {
            return file.getParent();
        }

        return null;
    }

}
