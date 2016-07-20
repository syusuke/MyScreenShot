package com.gson8.myscreenshot.fileselector.utils;

import java.util.Comparator;
import java.util.HashMap;

/**
 * 文件比较的类
 */
public class FileCompare implements Comparator<HashMap<String, Object>>
{
    @Override
    public int compare(HashMap<String, Object> lhs, HashMap<String, Object> rhs)
    {
        String n1 = (String)lhs.get(FileSelectorConfig.NAME);
        String n2 = (String)rhs.get(FileSelectorConfig.NAME);
        return n1.toUpperCase().compareTo(n2.toUpperCase());
    }
}