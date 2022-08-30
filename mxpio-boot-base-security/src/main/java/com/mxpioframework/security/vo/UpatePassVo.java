package com.mxpioframework.security.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="修改密码")
public class UpatePassVo {

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "旧密码")
	private String oldPassword;
	
	@ApiModelProperty(value = "新密码")
	private String newPassword;
	
}
