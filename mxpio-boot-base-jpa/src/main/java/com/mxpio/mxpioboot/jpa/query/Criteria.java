package com.mxpio.mxpioboot.jpa.query;

import java.util.ArrayList;
import java.util.List;

public class Criteria {

	private List<Object> criterions = new ArrayList<Object>();
	
	private List<Order> orders = new ArrayList<Order>();

	/*public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (!criterions.isEmpty()) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (Criterion c : criterions) {
				predicates.add(c.toPredicate(root, query, builder));
			}
			// 将所有条件用 and 联合起来
			if (predicates.size() > 0) {
				return builder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}
		return builder.conjunction();
	}*/

	/**
	 * 增加简单条件表达式
	 * 
	 * @Methods Name add
	 * @Create In 2012-2-8 By lee
	 * @param expression0
	 *            void
	 */
	public void add(Object criterion) {
		if (criterion != null) {
			criterions.add(criterion);
		}
	}
	
	public List<Object> getCriterions() {
		return criterions;
	}

	public void setCriterions(List<Object> criterions) {
		this.criterions = criterions;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

}
