package com.mxpioframework.security.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "字段信息")
public class FieldInfo {

	@Schema(description = "字段名称")
	private String fieldName;

	@Schema(description = "字段类型")
	private String fieldType;

	@Schema(description = "字段描述")
	private String description;

}
