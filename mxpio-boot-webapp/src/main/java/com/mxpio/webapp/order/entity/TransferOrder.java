package com.mxpio.webapp.order.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_ERP_TO")
@ApiModel(value="调拨单")
public class TransferOrder extends BizOrder {

	private static final long serialVersionUID = 1L;

}
