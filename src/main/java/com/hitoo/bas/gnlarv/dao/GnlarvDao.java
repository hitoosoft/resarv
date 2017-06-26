package com.hitoo.bas.gnlarv.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.hitoo.bas.entity.Gnlarv;
import com.hitoo.frame.base.BaseDAO;
import com.hitoo.frame.common.util.BeanUtil;
import com.hitoo.sys.entity.Org;

@Component
public class GnlarvDao extends BaseDAO {
	
	/**
	 * 当前用户所归属的全宗
	 * 当前用户所属机构，如果查询到返回
	 * 查询不到则查询上级机构。
	 */
	public Gnlarv queryGnlBelongOrgOrParentOrg(String orgID) throws Exception{
		if(StringUtils.isBlank(orgID)){
			return null;
		}
		
		Org org = (Org) getCurrentSession().get(Org.class, orgID);
		if(org==null){
			return null;
		}
		Gnlarv gnlArv = queryGnlarvByOrg(orgID, "");
		if(gnlArv!=null){
			return gnlArv;
		}else{
			return queryGnlBelongOrgOrParentOrg(org.getParentID());
		}
		
	}
	
	/**
	 * 根据立档单位查询全宗
	 * 一个立档单位只能所属一个全宗
	 */
	public Gnlarv queryGnlarvByOrg(String orgID,String excludeGnlarvID) throws Exception{
		String sqlStr=" select a.*,b.orgnam from BAS_GNLARV a,SYS_ORG b where a.orgid = b.orgid and a.ORGID = :orgID ";
		if(StringUtils.isNotBlank(excludeGnlarvID)){
			sqlStr += " and a.GNLARVID <> :excludeGnlarvID ";
		}
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr.toString());
		query.setString("orgID", orgID);
		if(StringUtils.isNotBlank(excludeGnlarvID)){
			query.setString("excludeGnlarvID", excludeGnlarvID);
		}
		query.addEntity(Gnlarv.class);
		if(query.list()!=null && query.list().size() >0){
			return (Gnlarv) query.list().get(0);
		}
		return null ;
	}
	
	/**
	 * 根据ID查询全宗信息
	 */
	public Gnlarv queryGnlarvByID(String gnlarvID) throws Exception{
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select a.*,b.orgNam from BAS_GNLARV a "
				+ " left join SYS_ORG b on a.orgID = b.orgID ");
		if(StringUtils.isNotBlank(gnlarvID)){
			sqlStr.append(" where a.gnlarvID = :gnlarvID ");
		}
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr.toString());
		query.setString("gnlarvID", gnlarvID);
		query.addEntity(Gnlarv.class);
		if(query.list()!=null && query.list().size() >0){
			return (Gnlarv) query.list().get(0);
		}
		return null;
	}
	
	/*
	 * 查找全宗编码
	 */
	@SuppressWarnings("unchecked")
	public List<Gnlarv> queryGnlarvByCod(String gnlarvCod, String gnlarvID) {
		Query query = getCurrentSession().createQuery(" from Gnlarv where gnlarvCod = :gnlarvCod and gnlarvID <> :gnlarvID");
		query.setString("gnlarvCod", gnlarvCod);
		query.setString("gnlarvID", gnlarvID);
		return query.list();
	}
	
	/*
	 * 查找全宗名称
	 */
	@SuppressWarnings("unchecked")
	public List<Gnlarv> queryGnlarvByNam(String gnlarvNam, String gnlarvID) {
		Query query = getCurrentSession().createQuery(" from Gnlarv where gnlarvNam = :gnlarvNam and gnlarvID <> :gnlarvID");
		query.setString("gnlarvNam", gnlarvNam);
		query.setString("gnlarvID", gnlarvID);
		return query.list();
	}
	
	/*
	 * 暂时查找全部全宗
	 */
	@SuppressWarnings("unchecked")
	public List<Gnlarv> queryAllGnlarv() throws Exception {
		String sql = "select a.*,b.orgnam as orgnam from BAS_GNLARV a left join SYS_ORG b on a.orgID = b.orgID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		List<Map<String, Object>> list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return BeanUtil.getBeanListFromMap(list, Gnlarv.class);
	}
	
}