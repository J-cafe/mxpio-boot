package com.mxpio.webapp.order.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_ERP_SO_LINE")
@ApiModel(value="销售订单行")
public class SalesOrderLine extends BizLine {

	private static final long serialVersionUID = 1L;

}
