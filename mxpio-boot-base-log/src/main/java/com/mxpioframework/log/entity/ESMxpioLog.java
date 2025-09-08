package com.mxpioframework.log.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@Schema(description="LOG对象")
@Document(indexName = "mxpio_log")
public class ESMxpioLog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @Field(type = FieldType.Text)
	@Schema(description = "ID")
	private String id;

    @Field(type = FieldType.Text)
    @Schema(description = "日志类型")
    private String type;

    @Field(type = FieldType.Text)
    @Schema(description = "日志子类型")
    private String subType;

    @Field(type = FieldType.Text)
    @Schema(description = "业务标识")
    private String bizNo;

    @Field(type = FieldType.Text)
    @Schema(description = "操作人")
    private String operator;

    @Field(type = FieldType.Text)
    @Schema(description = "日志内容")
    private String action;

    @Field(type = FieldType.Text)
    @Schema(description = "日志额外信息")
    private String extra;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Field(type = FieldType.Date)
    @Schema(description = "创建时间")
    private Date createTime;

    @Field(type = FieldType.Keyword)
    @Schema(description = "操作是否成功 1-成功 0-失败")
    private String success;

    @Field(type = FieldType.Keyword)
    @Schema(description = "类名")
    private String clazzName;

    @Field(type = FieldType.Keyword)
    @Schema(description = "方法名")
    private String methodName;

}
