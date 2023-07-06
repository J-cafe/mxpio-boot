package com.mxpioframework.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
	
	/**
     * 遍历文件夹（循环）
     * @param dir
     */
    public static List<File> getDirs(File dir) {
        //定义集合
        List<File> list = new ArrayList<>();
        List<File> result = new ArrayList<>();
        list.add(dir);
        //循环，条件：集合非空
        while (!list.isEmpty()) {
            //取出，并删除集合的第一个File对象
            File file = list.remove(0);
            result.add(file);
            //如果此File对象是一个目录
            if (file.isDirectory()) {
                //获取此File对象下所有子文件和子目录的数组，并添加到集合的前面
                list.addAll(0, Arrays.asList(file.listFiles()));
            }
            //继续下一次循环......
        }
        return result;
    }

}
