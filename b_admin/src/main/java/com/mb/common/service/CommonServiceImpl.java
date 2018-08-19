package com.mb.common.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mb.common.dao.CommonDao;
import com.mb.persistance.Employee;
import com.mb.utilities.Utilities;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private Utilities utilities;

	@Override
	public <T> List<T> findAll(Class<T> class1) throws Exception {
		return commonDao.findAll(class1);
	}

	@Override
	public <T> List<T> findAllByIds(Class<T> class1, List<Integer> ids) throws Exception {
		return commonDao.findAllByIds(class1, ids);
	}

	@Override
	public <T> T findOne(Class<T> class1, Integer id) throws Exception {
		return commonDao.findOne(class1, id);
	}

	@Override
	public <T> List<T> findAllByProperties(Class<T> class1, Map<String, Object> map) throws Exception {
		return commonDao.findAllByProperties(class1, map);
	}

	@Override
	public <T> void save(T t) throws Exception {
		commonDao.save(t);

	}

	@Override
	public <T> void saveOrUpdate(T t) throws Exception {
		commonDao.saveOrUpdate(t);

	}

	@Override
	public <T> List<T> findAllWithDistinctRootEntity(Class<T> class1) throws Exception {
		return commonDao.findAllWithDistinctRootEntity(class1);
	}

	@Override
	public <T> T findOneWithDistinctRootEntity(Class<T> class1, Integer id) throws Exception {
		return commonDao.findOneWithDistinctRootEntity(class1, id);
	}

	@Override
	public <T, B> List<T> findAllByInCondition(Class<T> class1, Map<String, Collection<B>> map) throws Exception {
		return commonDao.findAllByInCondition(class1, map);
	}

	@Override
	public Employee findEmployeeByUsername(String email) throws Exception {
		return commonDao.findEmployeeByUsername(email);
	}

	@Override
	public <T> Boolean checkExistOrNotOnProperties(Class<T> class1, Map<String, Object> map) throws Exception {
		return commonDao.checkExistOrNotOnProperties(class1, map);
	}

	@Override
	public Map<String, String> saveFileOnHardrive(MultipartFile file, String filePath, String httpPath)
			throws Exception {
		return utilities.saveFileOnHardrive(file, filePath, httpPath);
	}

	@Override
	public String changeOrderStatus(Long orderid, Integer code) throws Exception {
		return commonDao.changeOrderStatus(orderid, code);
	}

	@Override
	public String assignBeauticianToOrder(Long orderid, Integer spid) throws Exception {

		return commonDao.assignBeauticianToOrder(orderid, spid);
	}

	@Override
	public <T> T findOne(Class<T> class1, Long id) throws Exception {
		return commonDao.findOne(class1, id);
	}

}
