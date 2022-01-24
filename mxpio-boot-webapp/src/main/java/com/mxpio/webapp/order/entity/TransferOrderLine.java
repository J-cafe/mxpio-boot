package com.mxpio.webapp.order.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_ERP_TO_LINE")
@ApiModel(value="调拨单行")
public class TransferOrderLine extends BizLine {

	private static final long serialVersionUID = 1L;

}
