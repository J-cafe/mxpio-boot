package com.mxpioframework.log.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LogVO implements Serializable {
    private static final long serialVersionUID = 1L;


    @Schema(description = "ID")
    private String id;

    @Schema(description = "日志类型")
    private String type;

    @Schema(description = "日志子类型")
    private String subType;

    @Schema(description = "业务标识")
    private String bizNo;

    @Schema(description = "操作人")
    private String operator;

    @Schema(description = "日志内容")
    private String action;

    @Schema(description = "日志额外信息")
    private String extra;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "操作是否成功 1-成功 0-失败")
    private String success;

    @Schema(description = "类名")
    private String clazzName;

    @Schema(description = "方法名")
    private String methodName;
}
