package com.mxpioframework.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.jpa.policy.impl.CreatedDatePolicy;
import com.mxpioframework.jpa.policy.impl.UpdatedDatePolicy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "CREATE_BY", updatable = false)
	@ApiModelProperty(value = "创建人", hidden = true)
	private String createBy;

	@Column(name = "UPDATE_BY")
	@ApiModelProperty(value = "更新人", hidden = true)
	private String updateBy;

	@Generator(policy = CreatedDatePolicy.class)
	@Column(name = "CREATE_TIME", updatable = false)
	@ApiModelProperty(value = "创建时间", hidden = true)
	private Date createTime;

	@Generator(policy = UpdatedDatePolicy.class)
	@Column(name = "UPDATE_TIME")
	@ApiModelProperty(value = "更新时间", hidden = true)
	private Date updateTime;

}
