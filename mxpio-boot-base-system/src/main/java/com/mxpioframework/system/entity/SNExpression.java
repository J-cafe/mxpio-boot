package com.mxpioframework.system.entity;

import lombok.Data;

/**
 * @Author : wpp
 * @Datetime : 2023/8/11 18:21
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc :
 */
@Data
public class SNExpression {
    /*
    SN  ${YYYY} ${MM} ${DD} ####
    前缀 年份     月份   日    序列值位数
    */
    private String snExpression;
}
