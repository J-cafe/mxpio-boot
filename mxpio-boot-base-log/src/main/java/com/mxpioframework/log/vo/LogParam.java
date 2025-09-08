package com.mxpioframework.log.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class LogParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "日志类型")
    private String type;

    @Schema(description = "日志子类型")
    private String subType;

    @Schema(description = "业务标识")
    private String bizNo;

    @Schema(description = "操作人")
    private String operator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间起")
    private Date createTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间止")
    private Date createTimeEnd;

    @Schema(description = "操作是否成功 1-成功 0-失败")
    private String success;

}
