package com.mbemployee.empapi.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CommonDao {

	public <T> List<T> findAll(Class<T> class1) throws Exception;

	public <T> List<T> findAllWithDistinctRootEntity(Class<T> class1) throws Exception;

	public <T> List<T> findAllByIds(Class<T> class1, List<Integer> ids) throws Exception;

	public <T> T findOne(Class<T> class1, Object id) throws Exception;

	public <T> T findOneWithDistinctRootEntity(Class<T> class1, Long id) throws Exception;

	public <T> List<T> findAllByProperties(Class<T> class1, Map<String, Object> map) throws Exception;

	public <T> Serializable save(T t) throws Exception;

	public <T> void saveOrUpdate(T t) throws Exception;

	public <T, B> List<T> findAllByInCondition(Class<T> class1, Map<String, Collection<B>> map) throws Exception;

	public <T> Boolean checkExistOrNotOnProperties(Class<T> class1, Map<String, Object> map) throws Exception;

	public <T> List<T> findAllByPropertiesWithDistinctRootEntity(Class<T> class1, Map<String, Object> map)
			throws Exception;

	public String changeOrderStatus(Long id, Integer code) throws Exception;

}
