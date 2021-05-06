package com.mxpio.mxpioboot.jpa.lin;

import javax.persistence.criteria.CriteriaDelete;

/**
 * 语言集成删除
 */
public interface Lind extends Lin<Lind, CriteriaDelete<?>>{

	/**
	 * 批量删除
	 * @return 删除条数
	 */
	int delete();

}
