package com.mxpio.webapp.order.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_ERP_WO")
@ApiModel(value="仓单")
public class WarehouseOrder extends BizOrder {

	private static final long serialVersionUID = 1L;

}
