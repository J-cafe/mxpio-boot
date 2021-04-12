package com.mxpio.mxpioboot.erp.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.mxpio.mxpioboot.jpa.BaseEntity;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "MB_MRP_BOM")
public class Bom extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ITEM_CODE_", length = 64)
	@ApiModelProperty(value = "物料编码")
	private String itemCode;
	
	@Column(name = "FA_ITEM_CODE_", length = 64)
	@ApiModelProperty(value = "父物料编码")
	private String faItemCode;
	
	@Column(name = "QUANTITY_", precision = 20, scale = 6)
	@ApiModelProperty(value = "数量关系")
	private BigDecimal quantity;
	
	@Column(name = "STEP_")
	@ApiModelProperty(value = "层级")
	private Integer step;
	
	@Column(name = "VERSION_")
	@ApiModelProperty(value = "版本")
	private String version;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getFaItemCode() {
		return faItemCode;
	}

	public void setFaItemCode(String faItemCode) {
		this.faItemCode = faItemCode;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
