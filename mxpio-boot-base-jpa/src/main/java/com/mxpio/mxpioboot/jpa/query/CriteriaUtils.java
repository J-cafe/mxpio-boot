package com.mxpio.mxpioboot.jpa.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mxpio.mxpioboot.jpa.lin.Linq;

public class CriteriaUtils {
	/**
	 * 以and形式添加多个条件
	 * 
	 * @param criteria 条件载体
	 * @param map map的key为属性名称（深层次属性用点.分隔），map的value为条件值
	 * @return 条件载体
	 */
	public static Criteria add(Criteria criteria, Map<String ,?> map) {
		if (criteria == null) {
			criteria = new Criteria();
		}
		if (map != null) {
			for (Entry<String, ?> entry : map.entrySet()) {
				SimpleCriterion criterion = new SimpleCriterion(entry.getKey(), entry.getValue(), Operator.EQ);
				criteria.add(criterion);
			}
		}
		return criteria;
	}
	
	/**
	 * 以and形式添加多个条件
	 * 
	 * @param junction 条件载体
	 * @param map map的key为属性名称（深层次属性用点.分隔），map的value为条件值
	 * @return 条件载体
	 */
	public static Junction add(Junction junction, Map<String ,?> map) {
		if (map != null) {
			for (Entry<String, ?> entry : map.entrySet()) {
				SimpleCriterion criterion = new SimpleCriterion(entry.getKey(), entry.getValue(), Operator.EQ);
				junction.add(criterion);
			}
		}
		return junction;
	}
	
	/**
	 * 添加条件
	 * 
	 * @param criteria 条件载体
	 * @param propertyName 属性名
	 * @param filterOperator 条件操作类型
	 * @param value 条件值
	 * @return 条件载体
	 */
	public static Criteria add(Criteria criteria, String propertyName, Operator filterOperator, Object value) {
		if (criteria == null) {
			criteria = new Criteria();
		}
		SimpleCriterion criterion = new SimpleCriterion(propertyName, value, filterOperator);
		criteria.add(criterion);
		return criteria;
	}
	
	/**
	 * 添加条件
	 * 
	 * @param junction 条件载体
	 * @param propertyName 属性名
	 * @param filterOperator 条件操作类型
	 * @param value 条件值
	 * @return 条件载体
	 */
	public static Junction add(Junction junction, String propertyName, Operator filterOperator, Object value) {
		SimpleCriterion criterion = new SimpleCriterion(propertyName, value, filterOperator);
		junction.add(criterion);
		return junction;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void parse(Linq linq, SimpleCriterion criterion) {
		String property = criterion.getFieldName();
		if (property.contains(".")) {
			property = StringUtils.substringAfterLast(property, ".");
		}
				
		Object value = criterion.getValue();
		Operator operator = criterion.getOperator();
		
		if (Operator.LIKE_END.equals(operator)) {
			linq.like(property, "%" + (String) value);
		} else if (Operator.LIKE_START.equals(operator)) {
			linq.like(property, (String) value + "%");
		} else if (Operator.GT.equals(operator)) {
			if (value instanceof Number) {
				linq.gt(property, (Number) value);
			} else if (value instanceof Comparable) {
				linq.greaterThan(property, (Comparable) value);
			}
		}  else if (Operator.LT.equals(operator)) {
			if (value instanceof Number) {
				linq.lt(property, (Number) value);
			} else if (value instanceof Comparable) {
				linq.lessThan(property, (Comparable) value);
			}
		} else if (Operator.GE.equals(operator)) {
			if (value instanceof Number) {
				linq.ge(property, (Number) value);
			} else if (value instanceof Comparable) {
				linq.greaterThanOrEqualTo(property, (Comparable) value);
			}
		} else if (Operator.LE.equals(operator)) {
			if (value instanceof Number) {
				linq.le(property, (Number) value);
			} else if (value instanceof Comparable) {
				linq.lessThanOrEqualTo(property, (Comparable) value);
			}
		} else if (Operator.EQ.equals(operator)) {
			linq.equal(property, value);
		} else if (Operator.NE.equals(operator)) {
			linq.notEqual(property, value);
		} else if (Operator.LIKE.equals(operator)) {
			linq.like(property, "%" + (String) value + "%");
		}
		
	}
	
	
	public static StringBuilder parseQL(SimpleCriterion criterion) {
		StringBuilder c = new StringBuilder();
		String p = criterion.getFieldName();
		String property = p;
		Object value = criterion.getValue();
		Operator operator = criterion.getOperator();
		c.append(property);
		if (Operator.LIKE_END.equals(operator)) {
			c.append(" like ");
			criterion.setValue("%" + value);
		} else if (Operator.LIKE_START.equals(operator)) {
			c.append(" like ");
			criterion.setValue(value + "%");
		} else if (Operator.GT.equals(operator)) {
			c.append(" > ");
			criterion.setValue(value);
		}  else if (Operator.LT.equals(operator)) {
			c.append(" < ");
			criterion.setValue(value);
		} else if (Operator.GE.equals(operator)) {
			c.append(" >= ");
			criterion.setValue(value);
		} else if (Operator.LE.equals(operator)) {
			c.append(" <= ");
			criterion.setValue(value);
		} else if (Operator.EQ.equals(operator)) {
			c.append(" = ");
			criterion.setValue(value);
		} else if (Operator.NE.equals(operator)) {
			c.append(" <> ");
			criterion.setValue(value);
		} else if (Operator.LIKE.equals(operator)) {
			c.append(" like ");
			criterion.setValue("%" + value + "%");
		}
		c.append(":" + property);
		return c;
	}

	/**
	 * 返回级联条件and
	 * @return And
	 */
	public static Junction and() {
		return new Junction(JunctionType.AND);
	}
	
	/**
	 * 返回级联条件or
	 * @return Or
	 */
	public static Junction or() {
		return new Junction(JunctionType.OR);
	}
	
	public static Criteria json2Criteria(String json){
		JSONObject object = JSON.parseObject(json);
		Criteria c = new Criteria();
		if(object.get("criterions") != null) {
			JSONArray jSONArray = (JSONArray) object.get("criterions");
			List<Object> criterions = new ArrayList<>();
			jSONArray.stream().forEach(obj->paserCriterions((JSONObject) obj, criterions));
			c.setCriterions(criterions);
			c.setOrders(object.getJSONArray("orders").toJavaList(Order.class));
		}
		
		return c;
	}
	
	public static void paserCriterions(JSONObject item, List<Object> criterions) {
		if(item.containsKey("criterions")) {
			List<Object> subCriterions = new ArrayList<>();
			JSONArray subArray = item.getJSONArray("criterions");
			subArray.stream().forEach(obj->paserCriterions((JSONObject) obj, subCriterions));
			Junction j = new Junction(Enum.valueOf(JunctionType.class, item.getString("type")));
			j.setCriterions(subCriterions);
			criterions.add(j);
		} else {
			SimpleCriterion sc = item.toJavaObject(SimpleCriterion.class);
			criterions.add(sc);
		}
	}
	
	public static void main(String[] args) {
		String json = "{\"criterions\":[{\"fieldName\":\"resourceType\",\"operator\":\"EQ\",\"value\":\"ELEMENT\"},{\"criterions\":[{\"fieldName\":\"roleId\",\"operator\":\"EQ\",\"value\":\"111\"},{\"fieldName\":\"id\",\"operator\":\"EQ\",\"value\":\"p111\"}],\"type\":\"OR\"}],\"orders\":[]}";
		json2Criteria(json);
	}
	
}
