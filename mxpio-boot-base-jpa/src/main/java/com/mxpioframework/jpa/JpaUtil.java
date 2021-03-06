package com.mxpioframework.jpa;

import static org.springframework.data.jpa.repository.query.QueryUtils.DELETE_ALL_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.getQueryString;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import com.mxpioframework.jpa.lin.Lind;
import com.mxpioframework.jpa.lin.Linq;
import com.mxpioframework.jpa.lin.Linu;
import com.mxpioframework.jpa.lin.impl.LindImpl;
import com.mxpioframework.jpa.lin.impl.LinqImpl;
import com.mxpioframework.jpa.lin.impl.LinuImpl;
import com.mxpioframework.jpa.policy.CriteriaPolicy;
import com.mxpioframework.jpa.policy.CrudContext;
import com.mxpioframework.jpa.policy.CrudPolicy;
import com.mxpioframework.jpa.policy.impl.CrudType;
import com.mxpioframework.jpa.policy.impl.DirtyTreeCrudPolicy;
import com.mxpioframework.jpa.policy.impl.SmartCrudPolicy;
import com.mxpioframework.jpa.strategy.GetEntityManagerFactoryStrategy;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * JPA???????????????
 */
public abstract class JpaUtil {

	protected static GetEntityManagerFactoryStrategy getEntityManagerFactoryStrategy;
	protected static ApplicationContext applicationContext;
	private static CriteriaPolicy defaultQBCCriteriaPolicy;

	public static CriteriaPolicy getDefaultQBCCriteriaPolicy() {
		return defaultQBCCriteriaPolicy;
	}

	public static void setDefaultQBCCriteriaPolicy(CriteriaPolicy criteriaPolicy) {
		defaultQBCCriteriaPolicy = criteriaPolicy;
	}

	/**
	 * ??????Linq
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 * @return Linq
	 */
	public static <T> Linq linq(Class<T> domainClass) {
		return new LinqImpl(domainClass);
	}

	/**
	 * ??????Linq
	 * 
	 * @param domainClass   ????????????????????????
	 * @param entityManager ??????????????????
	 * @param <T>           ??????????????????????????????
	 * @return Linq
	 */
	public static <T> Linq linq(Class<T> domainClass, EntityManager entityManager) {
		return new LinqImpl(domainClass, entityManager);
	}

	/**
	 * ??????Linq
	 * 
	 * @param domainClass ????????????????????????
	 * @param resultClass ?????????
	 * @param <T>         ??????????????????????????????
	 * @return Linq
	 */
	public static <T> Linq linq(Class<T> domainClass, Class<?> resultClass) {
		return new LinqImpl(domainClass, resultClass);
	}

	/**
	 * ??????Linq
	 * 
	 * @param domainClass   ????????????????????????
	 * @param resultClass   ?????????
	 * @param entityManager ??????????????????
	 * @param <T>           ??????????????????????????????
	 * @return Linq
	 */
	public static <T> Linq linq(Class<T> domainClass, Class<?> resultClass, EntityManager entityManager) {
		return new LinqImpl(domainClass, resultClass, entityManager);
	}

	/**
	 * ??????Lind
	 * 
	 * @param domainClass ????????????????????????
	 * @return Lind
	 */
	public static Lind lind(Class<?> domainClass) {
		return new LindImpl(domainClass);
	}

	/**
	 * ??????Lind
	 * 
	 * @param domainClass   ????????????????????????
	 * @param entityManager ??????????????????
	 * @return Lind
	 */
	public static Lind lind(Class<?> domainClass, EntityManager entityManager) {
		return new LindImpl(domainClass, entityManager);
	}

	/**
	 * ??????Linu
	 * 
	 * @param domainClass ????????????????????????
	 * @return Linu
	 */
	public static Linu linu(Class<?> domainClass) {
		return new LinuImpl(domainClass);
	}

	/**
	 * ??????Linu
	 * 
	 * @param domainClass   ????????????????????????
	 * @param entityManager ??????????????????
	 * @return Linu
	 */
	public static Linu linu(Class<?> domainClass, EntityManager entityManager) {
		return new LinuImpl(domainClass, entityManager);
	}

	/**
	 * ??????????????????
	 * 
	 * @param name ???????????????
	 * @return Query
	 */
	public static Query namedQuery(String name) {
		return getEntityManager().createNamedQuery(name);
	}

	/**
	 * ??????????????????
	 * 
	 * @param name          ????????????
	 * @param entityManager ??????????????????
	 * @return Query
	 */
	public static Query namedQuery(String name, EntityManager entityManager) {
		return entityManager.createNamedQuery(name);
	}

	/**
	 * ??????????????????
	 * 
	 * @param sqlString ??????SQL???????????????
	 * @return Query
	 */
	public static Query nativeQuery(String sqlString) {
		return getEntityManager().createNativeQuery(sqlString);
	}

	/**
	 * ??????????????????
	 * 
	 * @param sqlString     ??????SQL???????????????
	 * @param entityManager ??????????????????
	 * @return Query
	 */
	public static Query nativeQuery(String sqlString, EntityManager entityManager) {
		return entityManager.createNativeQuery(sqlString);
	}

	/**
	 * ??????????????????
	 * 
	 * @param sqlString   ??????SQL???????????????
	 * @param resultClass ???????????????class
	 * @return Query
	 */
	public static Query nativeQuery(String sqlString, Class<?> resultClass) {
		return getEntityManager().createNativeQuery(sqlString, resultClass);
	}

	/**
	 * ??????????????????
	 * 
	 * @param sqlString     ??????SQL???????????????
	 * @param resultClass   ???????????????class
	 * @param entityManager ??????????????????
	 * @return Query
	 */
	public static Query nativeQuery(String sqlString, Class<?> resultClass, EntityManager entityManager) {
		return entityManager.createNativeQuery(sqlString, resultClass);
	}

	/**
	 * ??????????????????
	 * 
	 * @param sqlString        ??????SQL???????????????
	 * @param resultSetMapping ?????????????????????
	 * @return Query
	 */
	public static Query nativeQuery(String sqlString, String resultSetMapping) {
		return getEntityManager().createNativeQuery(sqlString, resultSetMapping);
	}

	/**
	 * ??????????????????
	 * 
	 * @param sqlString        ??????SQL???????????????
	 * @param resultSetMapping ?????????????????????
	 * @param entityManager    ??????????????????
	 * @return Query
	 */
	public static Query nativeQuery(String sqlString, String resultSetMapping, EntityManager entityManager) {
		return entityManager.createNativeQuery(sqlString, resultSetMapping);
	}

	/**
	 * ????????????????????????
	 * 
	 * @param domainClass ????????????????????????
	 * @param id          ??????ID
	 * @param <T>         ??????????????????????????????
	 * @param <ID>        ?????????ID??????
	 * @return ????????????
	 */
	public static <T, ID extends Serializable> T getOne(Class<T> domainClass, ID id) {
		EntityManager em = getEntityManager(domainClass);
		return em.find(domainClass, id);
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param entityList ????????????????????????????????????
	 * 
	 */
	public static <T> void save(Collection<T> entityList) {
		for (T entity : entityList) {
			save(entity, null);
		}
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param entityList ????????????????????????????????????
	 * 
	 */
	public static <T> void save(Collection<T> entityList, CrudPolicy savePolicy) {
		for (T entity : entityList) {
			save(entity, savePolicy);
		}
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param entity ????????????????????????????????????
	 * 
	 */
	public static void save(Object entity) {
		save(entity, null);
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * <p>
	 * 
	 * ????????????????????????????????????????????????EntityManager???<br>
	 * 
	 * ???????????????????????????????????????EntityManager???????????????????????????????????????<br>
	 * 
	 * ????????????????????????????????????????????????????????????????????????????????????savePolicy????????????????????????????????????
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param entity     ????????????????????????????????????
	 * 
	 * @param crudPolicy ????????????
	 * 
	 */
	public static <T> void save(T entity, CrudPolicy crudPolicy) {
		DirtyTreeCrudPolicy dirtyTreeCrudPolicy = new DirtyTreeCrudPolicy();
		if (crudPolicy == null) {
			dirtyTreeCrudPolicy.setCrudPolicy(new SmartCrudPolicy());
		} else {
			dirtyTreeCrudPolicy.setCrudPolicy(crudPolicy);
		}
		EntityManager entityManager = getEntityManager(entity);
		CrudContext context = new CrudContext();
		context.setEntity(entity);
		context.setCrudType(CrudType.SAVE);
		context.setEntityManager(entityManager);
		dirtyTreeCrudPolicy.apply(context);
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param entityList ????????????????????????????????????
	 * 
	 */
	public static <T> void update(Collection<T> entityList) {
		for (T entity : entityList) {
			update(entity, null);
		}
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param entityList ????????????????????????????????????
	 * 
	 */
	public static <T> void update(Collection<T> entityList, CrudPolicy savePolicy) {
		for (T entity : entityList) {
			update(entity, savePolicy);
		}
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param entity ????????????????????????????????????
	 * 
	 */
	public static void update(Object entity) {
		update(entity, null);
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * <p>
	 * 
	 * ????????????????????????????????????????????????EntityManager???<br>
	 * 
	 * ???????????????????????????????????????EntityManager???????????????????????????????????????<br>
	 * 
	 * ????????????????????????????????????????????????????????????????????????????????????savePolicy????????????????????????????????????
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param entity     ????????????????????????????????????
	 * 
	 * @param crudPolicy ????????????
	 * 
	 */
	public static <T> void update(T entity, CrudPolicy crudPolicy) {
		DirtyTreeCrudPolicy dirtyTreeCrudPolicy = new DirtyTreeCrudPolicy();
		if (crudPolicy == null) {
			dirtyTreeCrudPolicy.setCrudPolicy(new SmartCrudPolicy());
		} else {
			dirtyTreeCrudPolicy.setCrudPolicy(crudPolicy);
		}
		EntityManager entityManager = getEntityManager(entity);
		CrudContext context = new CrudContext();
		context.setEntity(entity);
		context.setCrudType(CrudType.UPDATE);
		context.setEntityManager(entityManager);
		dirtyTreeCrudPolicy.apply(context);
		flush(entity);
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param entityList ????????????????????????????????????
	 * 
	 */
	public static <T> void delete(Collection<T> entityList) {
		for (T entity : entityList) {
			delete(entity, null);
		}
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param entityList ????????????????????????????????????
	 * 
	 */
	public static <T> void delete(Collection<T> entityList, CrudPolicy savePolicy) {
		for (T entity : entityList) {
			delete(entity, savePolicy);
		}
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * @param entity ????????????????????????????????????
	 * 
	 */
	public static void delete(Object entity) {
		delete(entity, null);
	}

	/**
	 * 
	 * ?????????????????????
	 * 
	 * <p>
	 * 
	 * ????????????????????????????????????????????????EntityManager???<br>
	 * 
	 * ???????????????????????????????????????EntityManager???????????????????????????????????????<br>
	 * 
	 * ????????????????????????????????????????????????????????????????????????????????????savePolicy????????????????????????????????????
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param entity     ????????????????????????????????????
	 * 
	 * @param crudPolicy ????????????
	 * 
	 */
	public static <T> void delete(T entity, CrudPolicy crudPolicy) {
		DirtyTreeCrudPolicy dirtyTreeCrudPolicy = new DirtyTreeCrudPolicy();
		if (crudPolicy == null) {
			dirtyTreeCrudPolicy.setCrudPolicy(new SmartCrudPolicy());
		} else {
			dirtyTreeCrudPolicy.setCrudPolicy(crudPolicy);
		}
		EntityManager entityManager = getEntityManager(entity);
		CrudContext context = new CrudContext();
		context.setEntity(entity);
		context.setCrudType(CrudType.DELETE);
		context.setEntityManager(entityManager);
		dirtyTreeCrudPolicy.apply(context);
	}

	/**
	 * ?????????????????????
	 * 
	 * @param entity ????????????
	 * @param <T>    ??????????????????????????????
	 * @return ?????????????????????
	 */
	public static <T> T persist(T entity) {
		EntityManager em = getEntityManager(entity);
		em.persist(entity);
		return entity;
	}

	/**
	 * ??????????????????
	 * 
	 * @param entity ????????????
	 * @param <T>    ??????????????????????????????
	 * @return ?????????????????????
	 */
	public static <T> T merge(T entity) {
		EntityManager em = getEntityManager(entity);
		return em.merge(entity);
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param entities ??????????????????
	 * @param <T>      ??????????????????????????????
	 * @return ????????????????????????????????????
	 */
	public static <T> List<T> persist(Iterable<? extends T> entities) {
		List<T> result = new ArrayList<T>();

		if (entities == null) {
			return result;
		}

		for (T entity : entities) {
			result.add(persist(entity));
		}

		return result;
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param entities ??????????????????
	 * @param <T>      ??????????????????????????????
	 * @return ??????????????????????????????
	 */
	public static <T> List<T> merge(Iterable<? extends T> entities) {
		List<T> result = new ArrayList<T>();

		if (entities == null) {
			return result;
		}

		for (T entity : entities) {
			result.add(merge(entity));
		}

		return result;
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param entity ????????????
	 * @param <T>    ??????????????????????????????
	 * @return ????????????????????????
	 */
	public static <T> T persistAndFlush(T entity) {
		T result = persist(entity);
		flush(entity);

		return result;
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param entity ????????????
	 * @param <T>    ??????????????????????????????
	 * @return ????????????????????????
	 */
	public static <T> T mergeAndFlush(T entity) {
		T result = merge(entity);
		flush(entity);

		return result;
	}

	/**
	 * ??????????????????
	 * 
	 * @param entity ????????????
	 * @param <T>    ??????????????????????????????
	 */
	public static <T> void remove(T entity) {
		EntityManager em = getEntityManager(entity);
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}

	/**
	 * ????????????????????????
	 * 
	 * @param entities ??????????????????
	 * @param <T>      ??????????????????????????????
	 */
	public static <T> void remove(Iterable<? extends T> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		for (T entity : entities) {
			remove(entity);
		}
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 */
	public static <T> void removeAll(Class<T> domainClass) {
		EntityManager em = getEntityManager(domainClass);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(domainClass);
		cq.from(domainClass);
		List<T> result = findAll(cq);
		for (T element : result) {
			remove(element);
		}
	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 */
	public static <T> void removeAllInBatch(Class<T> domainClass) {
		EntityManager em = getEntityManager(domainClass);
		em.createQuery(getQueryString(DELETE_ALL_QUERY_STRING, em.getMetamodel().entity(domainClass).getName()))
				.executeUpdate();
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 * @return ????????????
	 */
	public static <T> T findOne(Class<T> domainClass) {
		EntityManager em = getEntityManager(domainClass);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(domainClass);
		cq.from(domainClass);
		return em.createQuery(cq).getSingleResult();
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 * @return ????????????
	 */
	public static <T> List<T> findAll(Class<T> domainClass) {
		EntityManager em = getEntityManager(domainClass);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(domainClass);
		cq.from(domainClass);
		return em.createQuery(cq).getResultList();
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param cq  ??????
	 * @param <T> ??????????????????????????????
	 * @return ????????????
	 */
	public static <T> List<T> findAll(CriteriaQuery<T> cq) {
		Class<T> domainClass = cq.getResultType();
		if (CollectionUtils.isEmpty(cq.getRoots())) {
			cq.from(domainClass);
		}
		EntityManager em = getEntityManager(domainClass);
		return em.createQuery(cq).getResultList();
	}

	/**
	 * ????????????
	 * 
	 * @param domainClass ????????????????????????
	 * @param pageable    ????????????
	 * @param <T>         ??????????????????????????????
	 * @return ??????????????????
	 */
	public static <T> Page<T> findAll(Class<T> domainClass, Pageable pageable) {
		EntityManager em = getEntityManager(domainClass);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(domainClass);
		cq.from(domainClass);
		return findAll(cq, pageable);
	}

	@SuppressWarnings("unchecked")
	/**
	 * ??????????????????
	 * 
	 * @param cq       ??????
	 * @param pageable ????????????
	 * @param <T>      ??????????????????????????????
	 * @return ????????????
	 */
	public static <T> Page<T> findAll(CriteriaQuery<T> cq, Pageable pageable) {
		Class<T> domainClass = cq.getResultType();
		Root<T> root;
		if (CollectionUtils.isEmpty(cq.getRoots())) {
			root = cq.from(domainClass);
		} else {
			root = (Root<T>) cq.getRoots().iterator().next();
		}
		EntityManager em = getEntityManager(domainClass);
		if (pageable == null) {
			List<T> list = findAll(cq);
			return new PageImpl<T>(list);
		} else {
			Sort sort = pageable.getSort();
			cq.orderBy(QueryUtils.toOrders(sort, root, em.getCriteriaBuilder()));
			TypedQuery<T> query = em.createQuery(cq);

			Long offset = pageable.getOffset();
			query.setFirstResult(offset.intValue());
			query.setMaxResults(pageable.getPageSize());

			Long total = count(cq);
			List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.<T>emptyList();

			return new PageImpl<T>(content, pageable, total);
		}
	}

	/**
	 * ???????????????????????????EmtityManager
	 * 
	 * @param entity ?????????
	 * @param <T>    ??????????????????????????????
	 * @return EntityManager
	 */
	public static <T> EntityManager getEntityManager(T entity) {
		Assert.notNull(entity, "entity can not be null.");
		return getEntityManager(entity.getClass());
	}

	/**
	 * ???????????????EntityManager
	 * 
	 * @return EntityManager
	 */
	public static EntityManager getEntityManager() {
		return getEntityManager((Class<?>) null);
	}

	/**
	 * ???????????????EntityManager
	 * 
	 * @return EntityManager
	 */
	public static EntityManager createEntityManager() {
		return createEntityManager((Class<?>) null);
	}

	/**
	 * ???????????????????????????????????????EmtityManager
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 * @return EntityManager
	 */
	public static <T> EntityManager getEntityManager(Class<T> domainClass) {
		EntityManagerFactory emf = getEntityManagerFactoryStrategy.getEntityManagerFactory(domainClass);
		return EntityManagerFactoryUtils.getTransactionalEntityManager(emf);
	}

	/**
	 * ????????????????????????spring?????????????????????EmtityManager
	 * 
	 * @param entityManagerFactoryName ????????????????????????spring????????????
	 * @return EntityManager
	 */
	public static EntityManager getEntityManager(String entityManagerFactoryName) {
		EntityManagerFactory emf = getEntityManagerFactory(entityManagerFactoryName);
		return EntityManagerFactoryUtils.getTransactionalEntityManager(emf);
	}

	/**
	 * ???????????????????????????EmtityManager
	 * 
	 * @param entity ?????????
	 * @param <T>    ??????????????????????????????
	 * @return EntityManager
	 */
	public static <T> EntityManager createEntityManager(T entity) {
		Assert.notNull(entity, "entity can not be null.");
		return createEntityManager(entity.getClass());
	}

	/**
	 * ???????????????????????????????????????EmtityManager
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 * @return EntityManager
	 */
	public static <T> EntityManager createEntityManager(Class<T> domainClass) {
		EntityManagerFactory emf = getEntityManagerFactoryStrategy.getEntityManagerFactory(domainClass);
		return emf.createEntityManager();
	}

	/**
	 * ????????????????????????spring?????????????????????EmtityManager
	 * 
	 * @param entityManagerFactoryName ????????????????????????spring????????????
	 * @return EntityManager
	 */
	public static EntityManager createEntityManager(String entityManagerFactoryName) {
		EntityManagerFactory emf = getEntityManagerFactory(entityManagerFactoryName);
		return emf.createEntityManager();
	}

	/**
	 * ????????????????????????spring?????????????????????EntityManagerFactory
	 * 
	 * @param entityManagerFactoryName ????????????????????????spring????????????
	 * @return EntityManagerFactory
	 */
	public static <T> EntityManagerFactory getEntityManagerFactory(String entityManagerFactoryName) {
		return (EntityManagerFactory) applicationContext.getBean(entityManagerFactoryName);
	}

	/**
	 * ???????????????????????????????????????EntityManagerFactory
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 * @return EntityManagerFactory
	 */
	public static <T> EntityManagerFactory getEntityManagerFactory(Class<T> domainClass) {
		return getEntityManagerFactoryStrategy.getEntityManagerFactory(domainClass);
	}

	/**
	 * ????????????EntityManagerFactory
	 * 
	 * @return EntityManagerFactory
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		return getEntityManagerFactory((Class<?>) null);
	}

	/**
	 * ??????????????????????????????????????????
	 * 
	 * @param domainClass ???
	 * @param <T>         ??????????????????????????????
	 * @return true???????????????????????????
	 */
	public static <T> boolean isEntityClass(Class<T> domainClass) {
		try {
			getEntityManagerFactory(domainClass);
			return true;
		} catch (IllegalArgumentException e) {
		}
		return false;
	}

	/**
	 * ????????????????????????????????????????????????
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 * @return ????????????
	 */
	public static <T> Long count(Class<T> domainClass) {
		EntityManager em = getEntityManager(domainClass);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		return count(cb.createQuery(domainClass));
	}

	/**
	 * ????????????????????????????????????????????????
	 * 
	 * @param cq  CriteriaQuery
	 * @param <T> ??????????????????????????????
	 * @return ????????????
	 */
	public static <T> Long count(CriteriaQuery<T> cq) {
		return executeCountQuery(getCountQuery(cq));
	}

	/**
	 * ??????????????????????????????????????????????????????
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 * @return true???????????????????????????
	 */
	public static <T> boolean exists(Class<T> domainClass) {
		EntityManager em = getEntityManager(domainClass);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		return exists(cb.createQuery(domainClass));
	}

	/**
	 * ????????????????????????????????????
	 * 
	 * @param cq  CriteriaQuery
	 * @param <T> ??????????????????????????????
	 * @return true???????????????????????????
	 */
	public static <T> boolean exists(CriteriaQuery<T> cq) {
		return count(cq) > 0;
	}

	/**
	 * ???????????????????????????EntityManager
	 * 
	 * @param entity ????????????
	 * @param <T>    ??????????????????????????????
	 */
	public static <T> void flush(T entity) {
		Assert.notNull(entity, "entity can not be null.");
		EntityManager em = getEntityManager(entity.getClass());
		em.flush();
	}

	/**
	 * ???????????????????????????????????????EntityManager
	 * 
	 * @param domainClass ????????????
	 * @param <T>         ??????????????????????????????
	 */
	public static <T> void flush(Class<T> domainClass) {
		EntityManager em = getEntityManager(domainClass);
		em.flush();
	}

	public static Long executeCountQuery(TypedQuery<Long> query) {

		Assert.notNull(query, "query can not be null.");

		List<Long> totals = query.getResultList();
		Long total = 0L;

		for (Long element : totals) {
			total += element == null ? 0 : element;
		}

		return total;
	}

	@SuppressWarnings("unchecked")
	public static <T> TypedQuery<Long> getCountQuery(CriteriaQuery<T> cq) {
		Class<T> domainClass = cq.getResultType();
		EntityManager em = getEntityManager(domainClass);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
		Root<T> root;
		if (cq.getRestriction() != null) {
			countCq.where(cq.getRestriction());
		}
		if (cq.getGroupRestriction() != null) {
			countCq.having(cq.getGroupRestriction());
		}
		if (cq.getRoots().isEmpty()) {
			root = countCq.from(domainClass);
		} else {
			countCq.getRoots().addAll(cq.getRoots());
			root = (Root<T>) countCq.getRoots().iterator().next();
		}
		countCq.groupBy(cq.getGroupList());
		if (cq.isDistinct()) {
			countCq.select(cb.countDistinct(root));
		} else {
			countCq.select(cb.count(root));
		}

		return em.createQuery(countCq);
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @param source       ???
	 * @param propertyName ?????????
	 * @param <T>          ??????
	 * @return source?????????????????????propertyName????????????????????????
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Set<T> collect(Collection<?> source, String propertyName) {
		if (CollectionUtils.isEmpty(source)) {
			return Collections.EMPTY_SET;
		}
		Set result = new HashSet(source.size());

		for (Object obj : source) {
			Object value = getValue(propertyName, obj);

			if (value != null) {
				result.add(value);
			}
		}
		return result;
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @param source ???
	 * @param <T>    ??????????????????????????????
	 * @return source?????????????????????propertyName????????????????????????
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> Set<T> collectId(Collection<?> source) {
		if (CollectionUtils.isEmpty(source)) {
			return Collections.EMPTY_SET;
		}
		String idName = getIdName(source.iterator().next().getClass());
		return collect(source, idName);
	}

	/**
	 * source???Map???Key???propertyName???????????????Value???source???propertyName????????????????????????
	 * 
	 * @param source       ???
	 * @param propertyName ?????????
	 * @param <K>          propertyName????????????????????????
	 * @param <V>          source??????????????????
	 * @return ??????Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K, V> Map<K, List<V>> classify(Collection<V> source, String propertyName) {
		if (CollectionUtils.isEmpty(source)) {
			return Collections.EMPTY_MAP;
		}
		Map result = new HashMap();

		for (Object obj : source) {
			Object value = getValue(propertyName, obj);
			Object target = result.get(value);
			if (target != null) {
				((List) target).add(obj);
			} else {
				List list = new ArrayList();
				list.add(obj);
				result.put(value, list);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private static Object getValue(String propertyName, Object obj) {
		if (obj instanceof Map) {
			return ((Map) obj).get(propertyName);
		} else if (obj instanceof Tuple) {
			return ((Tuple) obj).get(propertyName);
		} else if (obj != null) {
			PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(obj.getClass(), propertyName);
			try {
				return pd.getReadMethod().invoke(obj, new Object[] {});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * source???Map???Key???source?????????propertyName????????????Value????????????
	 * 
	 * @param source       ?????????
	 * @param propertyName ?????????
	 * @param <K>          propertyName????????????????????????
	 * @param <V>          source??????????????????
	 * @return ??????Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K, V> Map<K, V> index(Collection<V> source, String propertyName) {
		if (CollectionUtils.isEmpty(source)) {
			return Collections.EMPTY_MAP;
		}
		Map result = new HashMap();

		for (Object obj : source) {
			Object value = getValue(propertyName, obj);

			result.put(value, obj);
		}
		return result;
	}

	/**
	 * source???Map???Key???source??????????????????????????????Value????????????
	 * 
	 * @param source ?????????
	 * @param <K>    propertyName????????????????????????
	 * @param <V>    source??????????????????
	 * @return ??????Map
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> index(Collection<V> source) {
		if (CollectionUtils.isEmpty(source)) {
			return Collections.EMPTY_MAP;
		}
		String idName = getIdName(source.iterator().next().getClass());
		return index(source, idName);

	}

	/**
	 * ???????????????????????????????????????????????????<br>
	 * ?????????<br>
	 * ?????????????????????
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 * @return ID?????????
	 */
	public static <T> String getIdName(Class<T> domainClass) {
		EntityManagerFactory emf = getEntityManagerFactory(domainClass);
		EntityType<T> entityType = emf.getMetamodel().entity(domainClass);
		return entityType.getId(entityType.getIdType().getJavaType()).getName();
	}

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @param domainClass ????????????????????????
	 * @param <T>         ??????????????????????????????
	 * @return SingularAttribute
	 */
	public static <T> SingularAttribute<? super T, ?> getId(Class<T> domainClass) {
		EntityManagerFactory emf = getEntityManagerFactory(domainClass);
		EntityType<T> entityType = emf.getMetamodel().entity(domainClass);
		return entityType.getId(entityType.getIdType().getJavaType());
	}

	public static void setGetEntityManagerFactoryStrategy(
			GetEntityManagerFactoryStrategy getEntityManagerFactoryStrategy) {
		JpaUtil.getEntityManagerFactoryStrategy = getEntityManagerFactoryStrategy;

	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext ctx) {
		applicationContext = ctx;
	}

}
