package com.mxpio.mxpioboot.erp.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mxpio.mxpioboot.jpa.BaseEntity;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "MB_MRP_MPS")
public class Mps extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "MPS_CODE_", length = 64)
	@ApiModelProperty(value = "代号")
	private String mpsCode;
	
	@Column(name = "ITEM_CODE_", length = 64)
	@ApiModelProperty(value = "物料编码")
	private String itemCode;
	
	@Column(name = "DEMAND_NUM_", precision = 20, scale = 6)
	@ApiModelProperty(value = "需求数量")
	private BigDecimal demandNum;
	
	@Column(name = "ORDER_TIME_")
	@ApiModelProperty(value = "订货日期")
	private Date orderTime;
	
	@Column(name = "DEMAND_TIME_")
	@ApiModelProperty(value = "需求日期")
	private Date demandTime;

	public String getMpsCode() {
		return mpsCode;
	}

	public void setMpsCode(String mpsCode) {
		this.mpsCode = mpsCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public BigDecimal getDemandNum() {
		return demandNum;
	}

	public void setDemandNum(BigDecimal demandNum) {
		this.demandNum = demandNum;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getDemandTime() {
		return demandTime;
	}

	public void setDemandTime(Date demandTime) {
		this.demandTime = demandTime;
	}
}
