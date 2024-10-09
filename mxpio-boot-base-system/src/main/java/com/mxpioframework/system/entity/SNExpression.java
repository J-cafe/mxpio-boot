package com.mxpioframework.system.entity;

import lombok.Data;

@Data
public class SNExpression {
    /*
    SN  ${YYYY} ${MM} ${DD} ####
    前缀 年份     月份   日    序列值位数
    */
    private String snExpression;
}
