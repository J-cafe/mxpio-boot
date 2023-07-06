package com.mxpioframework.common.util;

import java.io.Closeable;
import java.io.IOException;

public class IOCloseUtil {

	/**
     *   IO流关闭工具类
     */
    public static void close(Closeable... io) {
        for (Closeable temp : io) {
            try {
                if (null != temp)
                    temp.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
    }
}
