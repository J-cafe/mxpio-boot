package com.mxpioframework.log.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mxpioframework.jpa.annotation.DictAble;
import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.security.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_LOG")
@Schema(description="LOG对象")
public class MxpioLog implements DictAble, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Schema(description = "ID")
	private Long id;

    @Column(name = "TYPE_")
    @Schema(description = "日志类型")
    private String type;

    @Column(name = "SUB_TYPE_")
    @Schema(description = "日志子类型")
    private String subType;

    @Column(name = "BIZ_NO_")
    @Schema(description = "业务标识")
    private String bizNo;

    @Column(name = "OPERATOR_",length = 64)
    @Schema(description = "操作人")
    @Dict(dicCode = "username", dicEntity = User.class, dicText = "nickname")
    private String operator;

    @Column(name = "ACTION_",length = 512)
    @Schema(description = "日志内容")
    private String action;

    @Lob
    @Column(name = "EXTRA_")
    @Schema(description = "日志额外信息")
    private String extra;

    @Column(name = "CREATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    private Date createTime;

    @Column(name = "SUCCESS_")
    @Schema(description = "操作是否成功 1-成功 0-失败")
    private String success;

    @Column(name = "CLAZZ_NAME_")
    @Schema(description = "类名")
    private String clazzName;

    @Column(name = "METHOD_NAME_")
    @Schema(description = "方法名")
    private String methodName;

    @Transient
    private Map<String, String> textMap;

    public String putText(String key, String value) {
        if (textMap == null) {
            textMap = new HashMap<>();
        }
        return textMap.put(key, value);
    }
}
