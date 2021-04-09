package com.mxpio.mxpioboot.jpa.query;

public class SimpleCriterion implements Criterion {

	private String fieldName; // 属性名
	private Object value; // 对应值
	private Operator operator; // 计算符
	
	public SimpleCriterion() {
		
	}

	public SimpleCriterion(String fieldName, Object value, Operator operator) {  
        this.fieldName = fieldName;  
        this.value = value;  
        this.operator = operator;  
    }

	public String getFieldName() {
		return fieldName;
	}

	public Object getValue() {
		return value;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Operator getOperator() {
		return operator;
	}

	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Path expression = null;
		if (fieldName.contains(".")) {
			String[] names = fieldName.split(".");
			expression = root.get(names[0]);
			for (int i = 1; i < names.length; i++) {
				expression = expression.get(names[i]);
			}
		} else {
			expression = root.get(fieldName);
		}

		switch (operator) {
		case EQ:
			return builder.equal(expression, value);
		case NE:
			return builder.notEqual(expression, value);
		case LIKE:
			return builder.like((javax.persistence.criteria.Expression<String>) expression, "%" + value + "%");
		case NOT_LIKE:
			return builder.notLike((javax.persistence.criteria.Expression<String>) expression, "%" + value + "%");
		case LT:
			return builder.lessThan(expression, (Comparable) value);
		case GT:
			return builder.greaterThan(expression, (Comparable) value);
		case LE:
			return builder.lessThanOrEqualTo(expression, (Comparable) value);
		case GE:
			return builder.greaterThanOrEqualTo(expression, (Comparable) value);
		default:
			return null;
		}
	}*/
}
