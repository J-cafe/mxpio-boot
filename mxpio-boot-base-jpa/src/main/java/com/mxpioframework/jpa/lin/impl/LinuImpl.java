package com.mxpioframework.jpa.lin.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.SingularAttribute;

import com.mxpioframework.jpa.lin.Linu;

public class LinuImpl extends LinImpl<Linu, CriteriaUpdate<?>> implements Linu {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LinuImpl(Class<?> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		criteria = cb.createCriteriaUpdate(domainClass);
		root = criteria.from((Class) domainClass);
	}

	public LinuImpl(Class<?> domainClass) {
		this(domainClass, null);
	}

	public LinuImpl(Linu parent, Class<?> domainClass) {
		super(parent, domainClass);
	}

	@Override
	public Linu createChild(Class<?> domainClass) {
		return new LinuImpl(this, domainClass);
	}

	@Override
	public Linu set(String attributeName, Object value) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria.set(attributeName, value);
		return this;
	}

	@Override
	public <Y> Linu set(Path<Y> attribute, Expression<? extends Y> value) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria.set(attribute, value);
		return this;
	}

	@Override
	public <Y, X extends Y> Linu set(Path<Y> attribute, X value) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria.set(attribute, value);
		return this;
	}

	@Override
	public <Y, X extends Y> Linu set(SingularAttribute<? super Object, Y> attribute, X value) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria.set(attribute, value);
		;
		return this;
	}

	@Override
	public <Y> Linu set(SingularAttribute<? super Object, Y> attribute, Expression<? extends Y> value) {
		if (!beforeMethodInvoke()) {
			return this;
		}
		criteria.set(attribute, value);
		return this;
	}

	@Override
	public int update() {
		if (parent != null) {
			applyPredicateToCriteria();
			return parent.update();
		}
		applyPredicateToCriteria();
		return em.createQuery(criteria).executeUpdate();
	}

	protected void applyPredicateToCriteria() {
		Predicate predicate = parsePredicate(junction);
		if (predicate != null) {
			if (sq != null) {
				sq.where(predicate);
			} else {
				criteria.where(predicate);
			}
		}
	}

}
