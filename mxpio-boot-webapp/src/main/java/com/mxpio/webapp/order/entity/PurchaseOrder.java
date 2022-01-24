package com.mxpio.webapp.order.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_ERP_PO")
@ApiModel(value="采购订单")
public class PurchaseOrder extends BizOrder {

	private static final long serialVersionUID = 1L;

}
