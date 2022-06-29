package ${genSystem.rootPackage}.${genModel.packageName}.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpioframework.jpa.BaseEntity;
import com.mxpioframework.jpa.annotation.Generator;
import com.mxpioframework.jpa.policy.impl.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "${genModel.tableName}")
@ApiModel(value="${genModel.modelName}")
public class ${genModel.modelCode} extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
<#list genModel.genProperties as property>
	<#if property.key>
	@Id
	</#if>
	<#if (property.valueRule)??>
	@Generator(${property.valueRule}.class)
	</#if>
	@Column(name = "${property.columnName}"<#if property.unique>, unique = true</#if><#if property.nullable>, nullable = true</#if><#if (property.columnLength)??>, length = ${property.columnLength}</#if><#if (property.columnPrecision)??>, precision = ${property.columnPrecision}</#if><#if (property.columnScale)??>, scale = ${property.columnScale}</#if>)
	@ApiModelProperty(value = "${property.propertyName}")
	private ${property.columnType} ${property.propertyCode};
	
</#list>

}
