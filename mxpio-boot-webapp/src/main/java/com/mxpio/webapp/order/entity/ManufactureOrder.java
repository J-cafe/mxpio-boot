package com.mxpio.webapp.order.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_ERP_MO")
@ApiModel(value="生产订单")
public class ManufactureOrder extends BizOrder {

	private static final long serialVersionUID = 1L;

}
