package com.mb.common.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.mb.persistance.Employee;

public interface CommonService {

	public <T> List<T> findAll(Class<T> class1) throws Exception;

	public <T> List<T> findAllByIds(Class<T> class1, List<Integer> ids) throws Exception;

	public <T> T findOne(Class<T> class1, Integer id) throws Exception;

	public <T> List<T> findAllByProperties(Class<T> class1, Map<String, Object> map) throws Exception;

	public <T> void save(T t) throws Exception;

	public <T> void saveOrUpdate(T t) throws Exception;

	public <T> List<T> findAllWithDistinctRootEntity(Class<T> class1) throws Exception;

	public <T> T findOneWithDistinctRootEntity(Class<T> class1, Integer id) throws Exception;

	public <T, B> List<T> findAllByInCondition(Class<T> class1, Map<String, Collection<B>> map) throws Exception;

	public Employee findEmployeeByUsername(String email) throws Exception;

	public <T> Boolean checkExistOrNotOnProperties(Class<T> class1, Map<String, Object> map) throws Exception;

	public Map<String, String> saveFileOnHardrive(MultipartFile file, String filePath, String httpPath)
			throws Exception;

	public String changeOrderStatus(Long orderid, Integer code) throws Exception;

	public String assignBeauticianToOrder(Long orderid, Integer spid) throws Exception;
	
	public <T> T findOne(Class<T> class1, Long id) throws Exception;

}
