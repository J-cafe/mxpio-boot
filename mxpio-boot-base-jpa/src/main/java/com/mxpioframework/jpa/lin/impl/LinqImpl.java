package com.mxpioframework.jpa.lin.impl;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

import com.mxpioframework.common.util.BeanReflectionUtils;
import com.mxpioframework.jpa.CollectInfo;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.filter.Filter;
import com.mxpioframework.jpa.filter.impl.BackfillFilter;
import com.mxpioframework.jpa.lin.Linq;
import com.mxpioframework.jpa.parser.CriterionParser;
import com.mxpioframework.jpa.parser.SmartSubQueryParser;
import com.mxpioframework.jpa.parser.SubQueryParser;
import com.mxpioframework.jpa.policy.LinqContext;
import com.mxpioframework.jpa.policy.impl.QBCCriteriaContext;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.transform.ResultTransformer;
import com.mxpioframework.jpa.transform.impl.Transformers;

import net.sf.cglib.beans.BeanMap;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public class LinqImpl extends LinImpl<Linq, CriteriaQuery<?>> implements Linq {

	private LinqContext linqContext = new LinqContext();
	private List<CollectInfo> collectInfos = new ArrayList<CollectInfo>();
	private Map<Class<?>, String[]> projectionMap = new HashMap<Class<?>, String[]>();
	private Filter filter;
	private boolean disableBackFillFilter;
	private List<CriterionParser> criterionParsers = new ArrayList<CriterionParser>();
	private Criteria c;
	private boolean disableSmartSubQueryCriterion;

	protected List<Order> orders = new ArrayList<Order>();
	protected boolean distinct;
	protected Class<?> resultClass;
	protected ResultTransformer resultTransformer;

	public LinqImpl(Class<?> domainClass) {
		this(domainClass, (EntityManager) null);
	}

	public LinqImpl(Class<?> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		criteria = cb.createQuery(domainClass);
		root = criteria.from(domainClass);
		resultClass = domainClass;
	}

	public LinqImpl(Class<?> domainClass, Class<?> resultClass) {
		super(domainClass);
		if (Tuple.class.isAssignableFrom(resultClass)) {
			criteria = cb.createTupleQuery();
		} else {
			criteria = cb.createQuery(resultClass);
		}
		root = criteria.from(domainClass);
		this.resultClass = resultClass;
	}

	@SuppressWarnings("rawtypes")
	public LinqImpl(Class<?> domainClass, Class<?> resultClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		if (Tuple.class.isAssignableFrom(resultClass)) {
			criteria = cb.createTupleQuery();
			root = criteria.from(domainClass);
		} else if (Map.class.isAssignableFrom(resultClass)) {
			criteria = cb.createQuery(Object[].class);
			root = criteria.from(domainClass);
			resultTransformer = Transformers.ALIAS_TO_MAP;
			Set<?> attrs = em.getMetamodel().entity(domainClass).getDeclaredSingularAttributes();
			String[] selections = new String[attrs.size()];
			int i = 0;
			for (Object attr : attrs) {
				selections[i] = ((SingularAttribute) attr).getName();
				i++;
			}
			select(selections);
		} else {
			criteria = cb.createQuery(resultClass);
			root = criteria.from(domainClass);
		}
		this.resultClass = resultClass;
	}

	public LinqImpl(Linq parent, Class<?> domainClass) {
		super(parent, domainClass);
	}

	@Override
	public Linq distinct() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		distinct = true;
		return this;
	}

	@Override
	public Linq groupBy(String... grouping) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		List<Expression<?>> expressions = new ArrayList<Expression<?>>();
		for (String property : grouping) {
			expressions.add(root.get(property));
		}
		if (sq != null) {
			sq.groupBy(expressions);
		} else {
			criteria.groupBy(expressions);
		}
		return this;
	}

	@Override
	public Linq desc(String... properties) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		for (String property : properties) {
			orders.add(cb.desc(root.get(property)));
		}
		return this;
	}

	@Override
	public Linq desc(Expression<?>... expressions) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		for (Expression<?> expression : expressions) {
			orders.add(cb.desc(expression));
		}
		return this;
	}

	@Override
	public Linq asc(String... properties) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		for (String property : properties) {
			orders.add(cb.asc(root.get(property)));
		}
		return this;
	}

	@Override
	public Linq asc(Expression<?>... expressions) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		for (Expression<?> expression : expressions) {
			orders.add(cb.asc(expression));
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T findOne() {
		if (parent != null) {
			beforeExecute(sq);
			return (T) parent.findOne();
		}
		beforeExecute(criteria);
		List<T> list = transform(em.createQuery(criteria), true);
		return list.get(0);
	}

	@Override
	public <T> List<T> list() {
		if (parent != null) {
			beforeExecute(sq);
			return parent.list();
		}
		beforeExecute(criteria);
		return transform(em.createQuery(criteria), false);
	}

	@Override
	public <T> Page<T> paging(Pageable pageable) {
		if (parent != null) {
			beforeExecute(sq);
			return parent.paging(pageable);
		}
		List<T> list;
		if (pageable == null) {
			list = list();
			return new PageImpl<T>(list);
		} else {
			Sort sort = pageable.getSort();
			if (sort != null) {
				orders.addAll(QueryUtils.toOrders(sort, root, cb));
			}
			beforeExecute(criteria);
			TypedQuery<?> query = em.createQuery(criteria);
			Long offset = pageable.getOffset();
			query.setFirstResult(offset.intValue());
			query.setMaxResults(pageable.getPageSize());

			Long total = JpaUtil.count(criteria);
			List<T> content = Collections.<T>emptyList();
			if (total > pageable.getOffset()) {
				content = transform(query, false);
			}

			return new PageImpl<T>(content, pageable, total);
		}
	}

	@Override
	public <T> List<T> list(Pageable pageable) {
		if (parent != null) {
			beforeExecute(sq);
			return parent.list(pageable);
		}
		if (pageable == null) {
			return list();
		} else {
			Sort sort = pageable.getSort();
			orders.addAll(QueryUtils.toOrders(sort, root, cb));
			beforeExecute(criteria);
			TypedQuery<?> query = em.createQuery(criteria);

			Long offset = pageable.getOffset();
			query.setFirstResult(offset.intValue());
			query.setMaxResults(pageable.getPageSize());

			return transform(query, false);
		}
	}

	@Override
	public <T> List<T> list(int page, int size) {
		if (parent != null) {
			beforeExecute(sq);
			return parent.list(page, size);
		}
		beforeExecute(criteria);
		TypedQuery<?> query = em.createQuery(criteria);

		query.setFirstResult(page * size);
		query.setMaxResults(size);

		return transform(query, false);
	}

	@Override
	public Long count() {
		if (parent != null) {
			beforeExecute(sq);
			return parent.count();
		}
		return executeCountQuery(getCountQuery());
	}

	@Override
	public boolean exists() {
		if (parent != null) {
			beforeExecute(sq);
			return parent.exists();
		}
		return count() > 0;
	}

	protected Long executeCountQuery(TypedQuery<Long> query) {

		Assert.notNull(query, "query can not be null.");

		List<Long> totals = query.getResultList();
		Long total = 0L;

		for (Long element : totals) {
			total += element == null ? 0 : element;
		}

		return total;
	}

	protected TypedQuery<Long> getCountQuery() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		criteria.getRoots().add(root);
		applyPredicateToCriteria(criteria);
		criteria.getOrderList().clear();
		if (distinct) {
			criteria.select(cb.countDistinct(root));
		} else {
			criteria.select(cb.count(root));
		}
		return em.createQuery(criteria);
	}

	protected void applyPredicateToCriteria(AbstractQuery<?> query) {

		Predicate predicate = parsePredicate(junction);
		if (predicate != null) {
			query.where(predicate);
		}

		predicate = parsePredicate(having);
		if (predicate != null) {
			query.having(predicate);
		}

		if (query instanceof CriteriaQuery) {
			if (!CollectionUtils.isEmpty(orders)) {
				((CriteriaQuery<?>) query).orderBy(orders);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> List<T> transform(TypedQuery<?> query, boolean single) {
		List<T> result;
		if (resultTransformer != null) {
			List tuples;
			if (single) {
				tuples = new ArrayList(1);
				tuples.add(query.getSingleResult());
			} else {
				tuples = query.getResultList();
			}
			result = new ArrayList<T>(tuples.size());
			String[] aliases = this.aliases.toArray(new String[this.aliases.size()]);
			for (Object tuple : tuples) {
				if (tuple != null) {
					if (tuple.getClass().isArray()) {
						result.add((T) resultTransformer.transformTuple((Object[]) tuple, aliases));
					} else {
						result.add((T) resultTransformer.transformTuple(new Object[] { tuple }, aliases));
					}
				}
			}
		} else {
			if (single) {
				result = new ArrayList<T>(1);
				result.add((T) query.getSingleResult());
			} else {
				result = (List<T>) query.getResultList();
			}
		}
		afterExecute(result);
		return result;
	}

	@Override
	public Linq createChild(Class<?> domainClass) {
		return new LinqImpl(this, domainClass);
	}

	@Override
	public Linq aliasToBean() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria = cb.createQuery(Object[].class);
		root = criteria.from(domainClass);
		resultTransformer = Transformers.aliasToBean(domainClass);
		return this;
	}

	@Override
	public Linq aliasToBean(Class<?> resultClass) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria = cb.createQuery(Object[].class);
		root = criteria.from(domainClass);
		this.resultClass = resultClass;
		resultTransformer = Transformers.aliasToBean(resultClass);
		return this;
	}

	@Override
	public Linq aliasToMap() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria = cb.createQuery(Object[].class);
		root = criteria.from(domainClass);
		this.resultClass = Map.class;
		resultTransformer = Transformers.ALIAS_TO_MAP;
		return this;
	}

	@Override
	public Linq aliasToTuple() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria = cb.createTupleQuery();
		root = criteria.from(domainClass);
		resultClass = Tuple.class;
		return this;
	}

	@Override
	public LinqContext getLinqContext() {
		return linqContext;
	}

	@Override
	public Linq collect(String... properties) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		collect(null, null, null, null, null, properties);
		return this;
	}

	@Override
	public Linq collect(Class<?> entityClass) {
		return collect(null, null, null, JpaUtil.getIdName(entityClass), entityClass, JpaUtil.getIdName(domainClass));
	}

	@Override
	public Linq collect(Class<?> entityClass, String... properties) {
		return collect(null, null, null, JpaUtil.getIdName(entityClass), entityClass, properties);
	}

	@Override
	public Linq collect(String otherProperty, Class<?> entityClass) {
		return collect(null, null, null, otherProperty, entityClass, JpaUtil.getIdName(domainClass));
	}

	@Override
	public Linq collect(String otherProperty, Class<?> entityClass, String... properties) {
		return collect(null, null, null, otherProperty, entityClass, properties);
	}

	@Override
	public Linq collect(Class<?> relationClass, Class<?> entityClass) {
		return collect(relationClass, Introspector.decapitalize(domainClass.getSimpleName()) + "Id",
				Introspector.decapitalize(entityClass.getSimpleName()) + "Id", JpaUtil.getIdName(entityClass),
				entityClass, JpaUtil.getIdName(domainClass));
	}

	@Override
	public Linq collect(Class<?> relationClass, String relationProperty, String relationOtherProperty,
			Class<?> entityClass) {
		return collect(relationClass, relationProperty, relationOtherProperty, JpaUtil.getIdName(entityClass),
				entityClass, JpaUtil.getIdName(domainClass));
	}

	@Override
	public Linq collect(Class<?> relationClass, String relationProperty, String relationOtherProperty,
			String otherProperty, Class<?> entityClass) {
		return collect(relationClass, relationProperty, relationOtherProperty, otherProperty, entityClass,
				JpaUtil.getIdName(domainClass));
	}

	@Override
	public Linq collect(Class<?> relationClass, String relationProperty, String relationOtherProperty,
			String otherProperty, Class<?> entityClass, String... properties) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		CollectInfo collectInfo = new CollectInfo();
		collectInfo.setEntityClass(entityClass);
		collectInfo.setRelationClass(relationClass);
		collectInfo.setRelationProperty(relationProperty);
		collectInfo.setRelationOtherProperty(relationOtherProperty);
		collectInfo.setOtherProperty(otherProperty);
		collectInfo.setProperties(properties);
		collectInfos.add(collectInfo);
		return this;
	}

	@Override
	public Linq collectSelect(Class<?> entityClass, String... projections) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		projectionMap.put(entityClass, projections);
		return this;
	}

	@SuppressWarnings("rawtypes")
	protected void doCollect(Collection list) {
		if (!collectInfos.isEmpty()) {
			initCollectInfos(list);
			buildMetadata();
		}
		doBackfill();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buildMetadata() {
		Map<Object, Object> metadata = linqContext.getMetadata();
		for (CollectInfo collectInfo : collectInfos) {
			Set collectSet = collectInfo.getSet();
			Map<Object, Object> relationMap = null;
			List collectList = null;

			if (!CollectionUtils.isEmpty(collectSet)) {
				if (collectInfo.getRelationClass() != null) {
					collectList = JpaUtil.linq(collectInfo.getRelationClass()).aliasToBean()
							.select(collectInfo.getRelationProperty(), collectInfo.getRelationOtherProperty())
							.in(collectInfo.getRelationProperty(), collectSet).list();
					relationMap = JpaUtil.index(collectList, collectInfo.getRelationOtherProperty());
					collectSet = relationMap.keySet();
				}
				for (String property : collectInfo.getProperties()) {
					if (!metadata.containsKey(property)) {
						Class<?> entityClass = collectInfo.getEntityClass();
						if (entityClass != null && !collectSet.isEmpty()) {
							if (metadata.containsKey(entityClass)) {
								metadata.put(property, metadata.get(entityClass));
							} else {
								String otherProperty = collectInfo.getOtherProperty();

								Linq linq = JpaUtil.linq(entityClass);
								if (ArrayUtils.isNotEmpty(projectionMap.get(entityClass))) {
									linq.aliasToBean();
									linq.select(projectionMap.get(entityClass));
								}
								//TODO in有超过限度的异常
								linq.in(otherProperty, collectSet);
								List result = linq.list();
								Map<Object, Object> resultMap = JpaUtil.index(result, otherProperty);
								Map<Object, List<Object>> map = new HashMap<Object, List<Object>>();
								if (collectList != null) {
									for (Object obj : collectList) {
										Object key = BeanReflectionUtils.getPropertyValue(obj,
												collectInfo.getRelationOtherProperty());
										Object other = resultMap.get(key);
										key = BeanReflectionUtils.getPropertyValue(obj,
												collectInfo.getRelationProperty());
										List<Object> list = map.get(key);
										if (list == null) {
											list = new ArrayList<Object>(5);
											map.put(key, list);
										}
										if (other != null) {
											list.add(other);
										}

									}
								} else {
									for (Object obj : result) {
										Object key = BeanReflectionUtils.getPropertyValue(obj, otherProperty);
										List<Object> list = map.get(key);
										if (list == null) {
											list = new ArrayList<Object>(5);
											map.put(key, list);
										}
										list.add(obj);
									}
								}

								metadata.put(property, map);
								metadata.put(entityClass, map);
							}

						} else {
							metadata.put(property, collectInfo.getSet());
						}
					}
				}

			}

		}
	}

	private void initCollectInfos(Collection<?> list) {
		for (Object entity : list) {
			BeanMap beanMap = BeanMap.create(entity);
			for (CollectInfo collectInfo : collectInfos) {
				for (String property : collectInfo.getProperties()) {
					Object value = beanMap.get(property);
					if (value != null) {
						collectInfo.add(value);
					}
				}
			}
		}
	}

	private void doBackfill() {
		if (!collectInfos.isEmpty() && !disableBackFillFilter) {
			this.filter = new BackfillFilter(this.filter, collectInfos);
		}

	}

	@Override
	public Linq where(Criteria criteria) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.c = criteria;
		return this;
	}

	@Override
	public Linq filter(Filter filter) {
		this.filter = filter;
		return this;
	}

	protected void beforeExecute(AbstractQuery<?> query) {
		if (!disableSmartSubQueryCriterion && c != null) {
			this.addParser(new SmartSubQueryParser(this, domainClass, collectInfos));
		}
		doParseCriteria();
		applyPredicateToCriteria(query);
	}

	protected void afterExecute(Collection<?> entities) {
		doCollect(entities);
		doFilter(entities);
	}

	protected void doParseCriteria() {
		if (c != null) {
			QBCCriteriaContext context = new QBCCriteriaContext();
			context.setCriteria(c);
			context.setEntityClass(domainClass);
			context.setLinq(this);
			context.setCriterionParsers(criterionParsers);
			JpaUtil.getDefaultQBCCriteriaPolicy().apply(context);
		}
	}

	@Override
	public Linq setDisableSmartSubQueryCriterion() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.disableSmartSubQueryCriterion = true;
		return this;
	}

	@Override
	public Linq setDisableBackFillFilter() {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.disableBackFillFilter = true;
		return this;
	}

	@SuppressWarnings({ "rawtypes" })
	protected void doFilter(Collection list) {
		if (filter != null) {
			Iterator<?> iterator = list.iterator();
			while (iterator.hasNext()) {
				Object entity = iterator.next();
				linqContext.setEntity(entity);
				if (!filter.invoke(linqContext)) {
					iterator.remove();
				}
			}
		}
	}

	@Override
	public Linq addParser(CriterionParser criterionParser) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.criterionParsers.add(criterionParser);
		return this;
	}

	@Override
	public Linq addSubQueryParser(Class<?>... entityClasses) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		for (Class<?> entityClass : entityClasses) {
			this.addParser(new SubQueryParser(this, entityClass));
		}
		return this;
	}

	@Override
	public Linq addSubQueryParser(Class<?> entityClass, String... foreignKeys) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		this.addParser(new SubQueryParser(this, entityClass, foreignKeys));
		return this;
	}

}
