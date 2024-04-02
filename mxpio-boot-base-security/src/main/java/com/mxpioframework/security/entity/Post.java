package com.mxpioframework.security.entity;

import com.mxpioframework.jpa.annotation.Generator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "MB_POST")
@Schema(description="岗位对象")
public class Post extends BaseEntity implements Actor {

	private static final long serialVersionUID = 1L;

	@Id
	@Generator
	@Schema(description = "ID")
	@Column(name = "ID_")
	private String id;

	@Schema(description = "职位名称")
	@Column(name = "NAME_")
	private String name;

	@Schema(description = "职系")
	@Column(name = "POST_TYPE_")
	@com.mxpioframework.security.annotation.Dict(dicCode = "code", dicEntity = DataType.class, dicText = "name")
	private Integer postType;

	@Schema(description = "职级")
	@Column(name = "POST_GRADE_")
	private Integer postGrade;

	@Override
	public String getActorId() {
		return id;
	}

}
