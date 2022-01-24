package com.mxpio.webapp.order.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_ERP_MO_ITEM")
@ApiModel(value="订单估计材料行")
public class ManufactureOrderItem extends BizLine {

	private static final long serialVersionUID = 1L;

}
