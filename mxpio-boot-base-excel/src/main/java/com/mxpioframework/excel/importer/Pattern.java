package com.mxpioframework.excel.importer;

public class Pattern {
	/**
     * yyyy-MM-dd
     */
    public static String DATE = "yyyy-MM-dd";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static String DATETIME_MM = "yyyy-MM-dd HH:mm";
    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static String DATETIME_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * HH:mm
     */
    public static String TIME = "HH:mm";
    /**
     * HH:mm:ss
     */
    public static String TIME_SS = "HH:mm:ss";

    //
    // 系统时间格式
    // ----------------------------------------------------------------------------------------------------
    /**
     * yyyy/MM/dd
     */
    public static String SYS_DATE = "yyyy/MM/dd";
    /**
     * yyyy/MM/dd HH:mm:ss
     */
    public static String SYS_DATETIME = "yyyy/MM/dd HH:mm:ss";
    /**
     * yyyy/MM/dd HH:mm
     */
    public static String SYS_DATETIME_MM = "yyyy/MM/dd HH:mm";
    /**
     * yyyy/MM/dd HH:mm:ss.SSS
     */
    public static String SYS_DATETIME_SSS = "yyyy/MM/dd HH:mm:ss.SSS";

    //
    // 无连接符模式
    // ----------------------------------------------------------------------------------------------------
    /**
     * yyyyMMdd
     */
    public static String NONE_DATE = "yyyyMMdd";
    /**
     * yyyyMMddHHmmss
     */
    public static String NONE_DATETIME = "yyyyMMddHHmmss";
    /**
     * yyyyMMddHHmm
     */
    public static String NONE_DATETIME_MM = "yyyyMMddHHmm";
    /**
     * yyyyMMddHHmmssSSS
     */
    public static String NONE_DATETIME_SSS = "yyyyMMddHHmmssSSS";
}
