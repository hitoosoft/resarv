package com.hitoo.sys.param.service.impl;

import org.hibernate.SQLQuery;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Service;

import com.hitoo.frame.base.BaseService;
import com.hitoo.sys.param.service.ParamService;

@Service
public class ParamServiceImpl extends BaseService implements ParamService{

	/**
	 * 根据编码查值
	 */
	public String queryValByCod(String code) throws Exception{
		String sqlStr=" select val from SYS_PARAM where COD = :code ";
		SQLQuery query = commonDao.getCurrentSession().createSQLQuery(sqlStr);
		query.setString("code", code);
		return (String) query.addScalar("val", StringType.INSTANCE).uniqueResult();
	}
}
