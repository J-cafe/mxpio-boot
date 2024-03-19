package com.mxpioframework.jpa.query;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxpioframework.jpa.lin.Linq;

public class CriteriaUtils {
	
	static ObjectMapper objectMapper = new ObjectMapper();
	
	private static String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
	
	private static String YMD = "yyyy-MM-dd";
	
	/**
	 * 以and形式添加多个条件
	 * 
	 * @param criteria 条件载体
	 * @param map      map的key为属性名称（深层次属性用点.分隔），map的value为条件值
	 * @return 条件载体
	 */
	public static Criteria add(Criteria criteria, Map<String, ?> map) {
		if (criteria == null) {
			criteria = Criteria.create();
		}
		if (map != null) {
			for (Entry<String, ?> entry : map.entrySet()) {
				SimpleCriterion criterion = new SimpleCriterion(entry.getKey(), Operator.EQ, entry.getValue());
				criteria.addCriterion(criterion);
			}
		}
		return criteria;
	}

	/**
	 * 以and形式添加多个条件
	 * 
	 * @param junction 条件载体
	 * @param map      map的key为属性名称（深层次属性用点.分隔），map的value为条件值
	 * @return 条件载体
	 */
	public static Junction add(Junction junction, Map<String, ?> map) {
		if (map != null) {
			for (Entry<String, ?> entry : map.entrySet()) {
				SimpleCriterion criterion = new SimpleCriterion(entry.getKey(), Operator.EQ, entry.getValue());
				junction.add(criterion);
			}
		}
		return junction;
	}

	/**
	 * 添加条件
	 * 
	 * @param criteria       条件载体
	 * @param propertyName   属性名
	 * @param filterOperator 条件操作类型
	 * @param value          条件值
	 * @return 条件载体
	 */
	public static Criteria add(Criteria criteria, String propertyName, Operator filterOperator, Object value) {
		if (criteria == null) {
			criteria = Criteria.create();
		}
		SimpleCriterion criterion = new SimpleCriterion(propertyName, filterOperator, value);
		criteria.addCriterion(criterion);
		return criteria;
	}

	/**
	 * 添加条件
	 * 
	 * @param junction       条件载体
	 * @param propertyName   属性名
	 * @param filterOperator 条件操作类型
	 * @param value          条件值
	 * @return 条件载体
	 */
	public static Junction add(Junction junction, String propertyName, Operator filterOperator, Object value) {
		SimpleCriterion criterion = new SimpleCriterion(propertyName, filterOperator, value);
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
		
		// 添加日期处理
		if(linq.root().get(property).getJavaType().equals(Date.class) && !(value instanceof Date)){
			SimpleDateFormat sdf = new SimpleDateFormat(YMD_HMS);
			try{
				value = sdf.parse(value.toString());
			}catch (ParseException e) {
				sdf = new SimpleDateFormat(YMD);
				try {
					value = sdf.parse(value.toString());
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
		
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
		} else if (Operator.LT.equals(operator)) {
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
		} else if (Operator.IN.equals(operator)) {
			if(value instanceof String) {
				linq.in(property, (Object[]) ((String) value).split(","));
			}else {
				linq.in(property, value);
			}
		} else if (Operator.NOT_IN.equals(operator)) {
			if(value instanceof String) {
				linq.notIn(property, (Object[]) ((String) value).split(","));
			}else {
				linq.notIn(property, value);
			}
		} else if (Operator.IS_NULL.equals(operator)) {
			linq.isNull(property);
		} else if (Operator.IS_NOT_NULL.equals(operator)) {
			linq.isNotNull(property);
		}else if (Operator.NOT_LIKE.equals(operator)) {
			linq.notLike(property, "%" + (String) value + "%");
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
		} else if (Operator.LT.equals(operator)) {
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
		} else if (Operator.NOT_LIKE.equals(operator)) {
			c.append(" not like ");
			criterion.setValue("%" + value + "%");
		}else if (Operator.IS_NULL.equals(operator)) {
			c.append(" is null ");
			criterion.setValue(value);
		}else if (Operator.IS_NOT_NULL.equals(operator)) {
			c.append(" is not null ");
			criterion.setValue(value);
		}
		c.append(":" + property);
		return c;
	}

	/*
	 * public static Criteria json2Criteria(String json) throws
	 * UnsupportedEncodingException { log.info("Criteria-->" + json);
	 * 
	 * Criteria c = Criteria.create(); if (StringUtils.isNotEmpty(json)) { if
	 * (json.startsWith("%")) { json = URLDecoder.decode(json, "utf-8");
	 * log.info("Criteria-->" + json); } JSONObject object = JSON.parseObject(json);
	 * if (object.get("criterions") != null) { JSONArray jSONArray = (JSONArray)
	 * object.get("criterions"); List<Object> criterions = new ArrayList<>();
	 * jSONArray.stream().forEach(obj -> paserCriterions((JSONObject) obj,
	 * criterions)); c.setCriterions(criterions); if (object.getJSONArray("orders")
	 * != null) {
	 * c.setOrders(object.getJSONArray("orders").toJavaList(Order.class)); } } }
	 * 
	 * return c; }
	 */

	public static Criteria json2Criteria(String json) {
		try {
			Criteria c = Criteria.create();
			if (StringUtils.isNotEmpty(json)) {
				if (json.startsWith("%")) {
					json = URLDecoder.decode(json, "utf-8");
				}
				c = Criteria.create(objectMapper.readValue(json, Criteria.class));
				if(c.getCriterions() != null) {
					List<Object> criterions = new ArrayList<>();
					c.getCriterions().stream().forEach(obj -> {
						try {
							paserCriterions(objectMapper.readTree(objectMapper.writeValueAsString(obj)) , criterions);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
					});
					c.setCriterions(criterions);
				}
				
			}
			return c;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	/*
	 * public static void paserCriterions(JSONObject item, List<Object> criterions)
	 * { if (item.containsKey("criterions")) { List<Object> subCriterions = new
	 * ArrayList<>(); JSONArray subArray = item.getJSONArray("criterions");
	 * subArray.stream().forEach(obj -> paserCriterions((JSONObject) obj,
	 * subCriterions)); Junction j = new Junction(Enum.valueOf(JunctionType.class,
	 * item.getString("type"))); j.setCriterions(subCriterions); criterions.add(j);
	 * } else { SimpleCriterion sc = item.toJavaObject(SimpleCriterion.class);
	 * criterions.add(sc); } }
	 */

	public static void paserCriterions(JsonNode item, List<Object> criterions) {
		if(item.hasNonNull("criterions")) {
			List<Object> subCriterions = new ArrayList<>();
			item.get("criterions").forEach(obj -> paserCriterions(obj, subCriterions));
			Junction j = new Junction(Enum.valueOf(JunctionType.class, item.get("type").asText()));
			j.setCriterions(subCriterions);
			criterions.add(j);
		} else {
			try {
				SimpleCriterion sc = objectMapper.readValue(objectMapper.writeValueAsString(item), SimpleCriterion.class);
				criterions.add(sc);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * public static void main(String[] args) throws JsonMappingException,
	 * JsonProcessingException, UnsupportedEncodingException { String json =
	 * "{\"criterions\":[{\"fieldName\":\"resourceType\",\"operator\":\"EQ\",\"value\":\"ELEMENT\"},{\"criterions\":[{\"fieldName\":\"roleId\",\"operator\":\"EQ\",\"value\":\"111\"},{\"fieldName\":\"id\",\"operator\":\"EQ\",\"value\":\"p111\"}],\"type\":\"OR\"}],\"orders\":[{\"desc\":false,\"fieldName\":\"username\"},{\"desc\":false,\"fieldName\":\"createTime\"}]}";
	 * Criteria c = Criteria.create(); c = json2Criteria(json);
	 * 
	 * Criteria c2 = Criteria.create().addCriterion("resourceType", Operator.EQ,
	 * "ELEMENT").or() .addCriterion("username", Operator.EQ,
	 * "admin").and().addCriterion("username", Operator.EQ, "admin")
	 * .addCriterion("username", Operator.EQ,
	 * "admin").end().addCriterion("username", Operator.EQ, "admin1")
	 * .end().addOrder(new Order("createTime", true)).addOrder(new
	 * Order("updateTime", true));
	 * 
	 * Criteria c3 = Criteria.create().addCriterion("resourceType", Operator.EQ,
	 * "ELEMENT"); String json3 = JSON.toJSONString(c3); System.out.println(json);
	 * System.out.println(JSON.toJSONString(c));
	 * 
	 * System.out.println(json3);
	 * System.out.println(JSON.toJSONString(json2Criteria(json3))); }
	 */

}
