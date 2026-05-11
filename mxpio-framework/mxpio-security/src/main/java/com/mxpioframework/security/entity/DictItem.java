package com.mxpioframework.security.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.mxpioframework.jpa.annotation.Generator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "MB_DICT_ITEM")
@Schema(description="字典项")
@ToString
public class DictItem extends BaseEntity implements Comparable<DictItem> {

	private static final long serialVersionUID = 1L;

	@Id
	@Generator
	@Column(name = "ID_", updatable = false)
	@Schema(description = "ID", hidden = true)
	private String id;

	@Column(name = "DICT_ID_")
	@Schema(description = "字典ID")
	private String dictId;

	@Column(name = "ITEM_TEXT_")
	@Schema(description = "显示文本")
	private String itemText;

	@Column(name = "ITEM_VALUE_")
	@Schema(description = "字典值")
	private String itemValue;

	@Column(name = "ITEM_DESC_")
	@Schema(description = "描述")
	private String itemDesc;

	@Column(name = "ITEM_SORT_")
	@Schema(description = "排序")
	private BigDecimal itemSort;

	@Column(name = "ITEM_STATUS_")
	@Schema(description = "状态")
	private String itemStatus;

	@Override
	public int compareTo(DictItem o) {
		BigDecimal thisSort = BigDecimal.ZERO;
		BigDecimal thatSort = BigDecimal.ZERO;
		if(itemSort != null){
			thisSort = itemSort;
		}
		if(o.getItemSort() != null){
			thatSort = o.getItemSort();
		}
		return thisSort.compareTo(thatSort);
	}

}
