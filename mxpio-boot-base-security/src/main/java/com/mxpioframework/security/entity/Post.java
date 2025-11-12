package com.mxpioframework.security.entity;

import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.security.annotation.Dict;
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

    @Schema(description = "职位编码")
    @Column(name = "CODE_")
    private String code;

    @Schema(description = "职位名称")
	@Column(name = "NAME_")
	private String name;

    @Schema(description = "所属部门")
    @Column(name = "DEPT_CODE_")
    @com.mxpioframework.security.annotation.Dict(dicCode = "deptCode", dicEntity = Dept.class, dicText = "deptName")
    private String deptCode;

	@Schema(description = "职系")
	@Column(name = "POST_TYPE_")
	@com.mxpioframework.security.annotation.Dict(dicCode = "code", dicEntity = PostType.class, dicText = "name")
	private String postType;

	@Schema(description = "职级")
	@Column(name = "POST_GRADE_")
	private String postGrade;

    @Schema(description = "是否关键岗位")
    @Dict(dicCode = "ERP_COMMON_YESNO")
    @Column(name = "KEY_POSITION_", columnDefinition="varchar(8) default '0'")//0否 1是
    private String keyPosition="0";

    @Schema(description = "是否上岗扫描")
    @Dict(dicCode = "ERP_COMMON_YESNO")
    @Column(name = "NEED_SCAN_", columnDefinition="varchar(8) default '0'")//0否 1是
    private String needScan="0";

    @Schema(description = "岗位职责")
    @Column(name = "DUTY_")
    private String duty;

	@Schema(description = "存储字符串的备用字段1")
	@Column(name = "RESERVED_STRING_FIELD1_")
	private String reservedStringField1;

	@Schema(description = "存储字符串的备用字段2")
	@Column(name = "RESERVED_STRING_FIELD2_")
	private String reservedStringField2;

	@Schema(description = "存储字符串的备用字段3")
	@Column(name = "RESERVED_STRING_FIELD3_")
	private String reservedStringField3;

	@Override
	public String getActorId() {
		return id;
	}

}
