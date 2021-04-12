package com.mxpio.mxpioboot.erp.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpio.mxpioboot.erp.entity.enums.Flag;
import com.mxpio.mxpioboot.erp.entity.enums.SourceType;
import com.mxpio.mxpioboot.jpa.BaseEntity;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "MB_MRP_MATERIEL")
public class Materiel extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ITEM_CODE_", length = 64)
	@ApiModelProperty(value = "物料编码")
	private String itemCode;
	
	@Column(name = "ITEM_NAME_", length = 255)
	@ApiModelProperty(value = "物料名称")
	private String itemName;
	
	@Column(name = "DRAW_ID_", length = 255)
	@ApiModelProperty(value = "图号")
	private String drawId;
	
	@Column(name = "UNIT_", length = 64)
	@ApiModelProperty(value = "单位")
	private String unit;
	
	@Column(name = "MAT_TYPE_", length = 255)
	@ApiModelProperty(value = "物料类型")
	private String matType;
	
	@Column(name = "SOURCE_", length = 255)
	@Enumerated(EnumType.STRING)
	@ApiModelProperty(value = "物料来源")
	private SourceType source;
	
	@Column(name = "LOT_SIZE_", precision = 20, scale = 6)
	@ApiModelProperty(value = "批量")
	private BigDecimal lotSize;
	
	@Column(name = "ORDER_MIN_", precision = 20, scale = 6)
	@ApiModelProperty(value = "最小起订量")
	private BigDecimal orderMin;
	
	@Column(name = "ORDER_MUL_", precision = 20, scale = 6)
	@ApiModelProperty(value = "订货倍量")
	private BigDecimal orderMul;
	
	@Column(name = "LEAD_TIME_", precision = 20, scale = 6)
	@ApiModelProperty(value = "固定提前期")
	private BigDecimal leadTime;
	
	@Column(name = "V_LEAD_TIME_", precision = 20, scale = 6)
	@ApiModelProperty(value = "可变提前期")
	private BigDecimal vLeadTime;
	
	@Column(name = "SAFE_STOCK", precision = 20, scale = 6)
	@ApiModelProperty(value = "安全库存")
	private BigDecimal safeStock;
	
	@Column(name = "MPS_FLAG")
	@Enumerated(EnumType.STRING)
	@ApiModelProperty(value = "MPS物料")
	private Flag mpsFlag;
	
	@Column(name = "PH_FLAG")
	@Enumerated(EnumType.STRING)
	@ApiModelProperty(value = "虚拟物料")
	private Flag phFlag;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDrawId() {
		return drawId;
	}

	public void setDrawId(String drawId) {
		this.drawId = drawId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMatType() {
		return matType;
	}

	public void setMatType(String matType) {
		this.matType = matType;
	}

	public SourceType getSource() {
		return source;
	}

	public void setSource(SourceType source) {
		this.source = source;
	}

	public BigDecimal getLotSize() {
		return lotSize;
	}

	public void setLotSize(BigDecimal lotSize) {
		this.lotSize = lotSize;
	}

	public BigDecimal getOrderMin() {
		return orderMin;
	}

	public void setOrderMin(BigDecimal orderMin) {
		this.orderMin = orderMin;
	}

	public BigDecimal getOrderMul() {
		return orderMul;
	}

	public void setOrderMul(BigDecimal orderMul) {
		this.orderMul = orderMul;
	}

	public BigDecimal getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(BigDecimal leadTime) {
		this.leadTime = leadTime;
	}

	public BigDecimal getvLeadTime() {
		return vLeadTime;
	}

	public void setvLeadTime(BigDecimal vLeadTime) {
		this.vLeadTime = vLeadTime;
	}

	public BigDecimal getSafeStock() {
		return safeStock;
	}

	public void setSafeStock(BigDecimal safeStock) {
		this.safeStock = safeStock;
	}

	public Flag getMpsFlag() {
		return mpsFlag;
	}

	public void setMpsFlag(Flag mpsFlag) {
		this.mpsFlag = mpsFlag;
	}

	public Flag getPhFlag() {
		return phFlag;
	}

	public void setPhFlag(Flag phFlag) {
		this.phFlag = phFlag;
	}
}
