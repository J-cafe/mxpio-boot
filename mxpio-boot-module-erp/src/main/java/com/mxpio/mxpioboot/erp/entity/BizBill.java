package com.mxpio.mxpioboot.erp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.mxpio.mxpioboot.jpa.annotation.Generator;
import com.mxpio.mxpioboot.jpa.policy.impl.CreatedDatePolicy;
import com.mxpio.mxpioboot.jpa.policy.impl.UpdatedDatePolicy;

import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
public class BizBill implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "BIZ_CODE_", updatable = false, unique = true)
	@ApiModelProperty(value = "单据编号")
	private String bizCode;
	
	@Column(name = "BIZ_TYPE_", updatable = false)
	@ApiModelProperty(value = "单据类型")
	private String bizType;
	
	@Column(name = "STATUS_")
	@ApiModelProperty(value = "单据状态")
	private String status;

	@Column(name = "CREATE_BY_", updatable = false)
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @Column(name = "UPDATE_BY_")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;

    @Generator(policy = CreatedDatePolicy.class)
    @Column(name = "CREATE_TIME_", updatable = false)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @Generator(policy = UpdatedDatePolicy.class)
    @Column(name = "UPDATE_TIME_")
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateTime;
    
}
