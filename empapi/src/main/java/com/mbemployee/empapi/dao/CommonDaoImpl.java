package com.mbemployee.empapi.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Transactional
@Repository
public class CommonDaoImpl implements CommonDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> class1) throws Exception {
		return sessionFactory.getCurrentSession().createCriteria(class1).list();

	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAllByIds(Class<T> class1, List<Integer> ids) throws Exception {
		return sessionFactory.getCurrentSession().createCriteria(class1).add(Restrictions.in("id", ids)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findOne(Class<T> class1, Object id) throws Exception {
		return (T) sessionFactory.getCurrentSession().createCriteria(class1).add(Restrictions.eq("id", id))
				.setMaxResults(1).uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAllByProperties(Class<T> class1, Map<String, Object> map) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(class1);
		map.keySet().stream().forEach(key -> {
			criteria.add(Restrictions.eq(key, map.get(key)));
		});
		return criteria.list();
	}

	@Override
	public <T> Serializable save(T t) throws Exception {
		return sessionFactory.getCurrentSession().save(t);

	}

	@Override
	public <T> void saveOrUpdate(T t) throws Exception {
		sessionFactory.getCurrentSession().saveOrUpdate(t);

	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAllWithDistinctRootEntity(Class<T> class1) throws Exception {
		return sessionFactory.getCurrentSession().createCriteria(class1)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findOneWithDistinctRootEntity(Class<T> class1, Long id) throws Exception {
		return (T) sessionFactory.getCurrentSession().createCriteria(class1).add(Restrictions.eq("id", id))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, B> List<T> findAllByInCondition(Class<T> class1, Map<String, Collection<B>> map) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(class1);
		map.keySet().stream().forEach(key -> {
			criteria.add(Restrictions.in(key, map.get(key)));
		});
		return criteria.list();

	}


	@Override
	public <T> Boolean checkExistOrNotOnProperties(Class<T> class1, Map<String, Object> map) throws Exception {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(class1);
		map.keySet().stream().forEach(key -> {
			criteria.add(Restrictions.eq(key, map.get(key)));
		});
		return StringUtils.isEmpty(criteria.setMaxResults(1).uniqueResult());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAllByPropertiesWithDistinctRootEntity(Class<T> class1, Map<String, Object> map)
			throws Exception {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(class1);
		map.keySet().stream().forEach(key -> {
			criteria.add(Restrictions.eq(key, map.get(key)));
		});
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	@Override
	public String changeOrderStatus(Long id, Integer code) throws Exception {
		String sql = "update CustomerOrder set order_status_code=:order_status_code where id=:id";
		sessionFactory.getCurrentSession().createQuery(sql).setParameter("order_status_code", code).setLong("id", id)
				.executeUpdate();

		return "success";

	}

}
