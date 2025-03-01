package com.mxpioframework.jpa.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Criteria {

	@JsonProperty(value = "criterions")
	private List<Object> criterions = new ArrayList<>();

	@JsonProperty(value = "orders")
	private List<Order> orders = new ArrayList<>();
	
	@JsonIgnore
	private Stack<JunctionStack> stack;

	/**
	 * 构建查询构造器对象
	 * @return 构造器
	 */
	public static Criteria create() {
		Criteria c = new Criteria();
        return create(c);
	}
	
	/**
	 * 构建查询构造器对象
	 * @param c 源构造器
	 * @return 构造器
	 */
	public static Criteria create(Criteria c) {
		c.setStack(new Stack<JunctionStack>());
		return c;
	}
	
	/**
	 * 添加Or联合条件
	 * @return 构造器
	 */
	public Criteria or() {
		JunctionStack junctionStack = JunctionStack.create(JunctionType.OR);
		stack.add(junctionStack);
		return this;
	}
	
	/**
	 * 添加and联合条件
	 * @return 构造器
	 */
	public Criteria and() {
		JunctionStack junctionStack = JunctionStack.create(JunctionType.AND);
		stack.add(junctionStack);
		return this;
	}
	
	/**
	 * 结束联合添加
	 * @return 构造器
	 */
	public Criteria end() {
		JunctionStack junctionStack = stack.pop();
		Junction junction = new Junction(junctionStack.getType());
		junction.setCriterions(junctionStack.getCriterions());
		if(stack.empty()) {
			this.criterions.add(junction);
		} else {
			stack.peek().getCriterions().add(junction);
		}
		return this;
	}

	/**
	 * 增加简单条件表达式
	 * 
	 * @param criterion 条件
	 * @return 构造器
	 */
	public Criteria addCriterion(Object criterion) {
		if (criterion != null) {
			if(stack.empty()) {
				criterions.add(criterion);
			}else {
				stack.peek().getCriterions().add(criterion);
			}
		}
		return this;
	}
	
	/**
	 * 增加简单条件表达式
	 * @param propertyName 属性名
	 * @param filterOperator 过滤类别
	 * @param value 属性值
	 * @return 构造器
	 */
	public Criteria addCriterion(String propertyName, Operator filterOperator, Object value) {
		if(stack.empty()) {
			criterions.add(new SimpleCriterion(propertyName, filterOperator, value));
		}else {
			stack.peek().getCriterions().add(new SimpleCriterion(propertyName, filterOperator, value));
		}
		return this;
	}
	
	/**
	 * 以and形式添加多个条件
	 * 
	 * @param junction 条件载体
	 * @return 条件载体
	 */
	public Criteria addJunction(Junction junction) {
		if (junction != null) {
			if(stack.empty()) {
				criterions.add(junction);
			}else {
				stack.peek().getCriterions().add(junction);
			}
		}
		return this;
	}
	
	/**
	 * 添加Order
	 * @param order 排序对象
	 * @return 构造器
	 */
	public Criteria addOrder(Order order) {
		if (order != null) {
			orders.add(order);
		}
		return this;
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

	public Stack<JunctionStack> getStack() {
		return stack;
	}

	public void setStack(Stack<JunctionStack> stack) {
		this.stack = stack;
	}

}
