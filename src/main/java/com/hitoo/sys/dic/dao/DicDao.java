package com.hitoo.sys.dic.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

import com.hitoo.frame.base.BaseDAO;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.sys.entity.Dic;
import com.hitoo.sys.entity.DicDtl;

@Component
public class DicDao extends BaseDAO {
	
	/**
	 * 加载所有的字典
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<Map<String, String>>> queryAllDicList() throws Exception {
		Map<String, List<Map<String, String>>> resultMap = new HashMap<String, List<Map<String, String>>>();
		String sqlStr= " select * from sys_dic_dtl order by diccod, seqno ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.addEntity(DicDtl.class);
		List<DicDtl> dtlList = query.list();
		for(DicDtl dicDtl :dtlList){
			String dicCod= dicDtl.getDicCod();
			String dicdtlcod = dicDtl.getDicDtlCod();
			String dicdtlnam = dicDtl.getDicDtlNam();
			Map<String, String> oneDicDtlmap = new HashMap<String, String>();
			oneDicDtlmap.put("value", dicdtlcod);
			oneDicDtlmap.put("text", dicdtlnam);
			List<Map<String, String>> oneDicList = null;
			if (!resultMap.containsKey(dicCod)) {
				oneDicList = new ArrayList<Map<String, String>>();
				resultMap.put(dicCod, oneDicList);
			}
			oneDicList= (List<Map<String, String>>) resultMap.get(dicCod);
			oneDicList.add(oneDicDtlmap);
		}
		return resultMap;
	}

	/**
	 * 分页获取字典明细，按照seqno排序
	 */
	@SuppressWarnings("unchecked")
	public List<DicDtl> queryDicDtlByDicCod(String dicCod) throws Exception {
		if (StringUtils.isNotBlank(dicCod)) {
			String sqlStr = " select * from sys_dic_dtl where diccod = :dicCod order by seqno ";
			SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sqlStr);
			sqlQuery.setString("dicCod", dicCod);
			sqlQuery.addEntity(DicDtl.class);
			return sqlQuery.list();
		}
		return null;
	}
	
	/**
	 * 分页获取字典
	 */
	public PageInfo queryDicByPage(PageInfo pi, String dicNamOrCod) throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" select * from sys_dic where 1 = 1 ");
		Map<String, Object> parameters = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(dicNamOrCod)) {
			sqlBuilder.append(" and (diccod like :dicCod or dicnam like :dicNam) ");
			parameters.put("dicCod", "%" + dicNamOrCod + "%");
			parameters.put("dicNam", "%" + dicNamOrCod + "%");
		}
		sqlBuilder.append(" order by diccod ");
		SQLEntity sqlEntity = new SQLEntity(sqlBuilder.toString(), parameters);
		return executePageQuery(pi, sqlEntity, Dic.class);
	}

	
	/**
	 * 分页获取字典明细，按照seqno排序
	 */
	public PageInfo queryDicDtlByPage(PageInfo pi, String dicCod) throws Exception {
		if (StringUtils.isNotBlank(dicCod)) {
			String sqlStr = " select * from sys_dic_dtl where diccod = :dicCod order by seqno ";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("dicCod", dicCod);
			SQLEntity sqlEntity = new SQLEntity(sqlStr, parameters);
			return executePageQuery(pi, sqlEntity, DicDtl.class);
		}
		return null;
	}
	
	/**
	 * 根据名字查询字典
	 */
	@SuppressWarnings("unchecked")
	public Dic queryDicByNam(String dicNam) throws Exception {
		String sqlStr = " select * from sys_dic where dicnam = :dicNam  ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("dicNam", dicNam);
		sqlQuery.addEntity(Dic.class);
		List<Dic> ls= sqlQuery.list();
		if(ls!=null && ls.size()>0){
			return ls.get(0);
		}
		return null;
	}
	
	/**
	 * 根据code查询字典明细
	 */
	@SuppressWarnings("unchecked")
	public DicDtl queryDicDtlByDtlCod(String dicCod,String dicDtlCod) throws Exception {
		String sqlStr = " select * from sys_dic_dtl where diccod = :dicCod and dicdtlcod = :dicDtlCod ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("dicCod", dicCod);
		sqlQuery.setString("dicDtlCod", dicDtlCod);
		sqlQuery.addEntity(DicDtl.class);
		List<DicDtl> ls= sqlQuery.list();
		if(ls!=null && ls.size()>0){
			return ls.get(0);
		}
		return null ;
	}
	
	/**
	 * 根据名字查询字典明细
	 */
	@SuppressWarnings("unchecked")
	public DicDtl queryDicDtlByDtlNam(String dicCod,String dicDtlNam) throws Exception {
		String sqlStr = " select * from sys_dic_dtl where diccod = :dicCod and dicdtlnam = :dicDtlNam ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("dicCod", dicCod);
		sqlQuery.setString("dicDtlNam", dicDtlNam);
		sqlQuery.addEntity(DicDtl.class);
		List<DicDtl> ls= sqlQuery.list();
		if(ls!=null && ls.size()>0){
			return ls.get(0);
		}
		return null ;
	}
	
	/**
	 * 删除字典及明細
	 */
	public void delDicAndDtl(String dicCod) throws Exception {
		String sqlStr = " delete from sys_dic where diccod = :dicCod  ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("dicCod", dicCod);
		sqlQuery.executeUpdate();
		
		sqlStr = " delete from sys_dic_dtl where diccod = :dicCod  ";
		sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("dicCod", dicCod);
		sqlQuery.executeUpdate();
	}
	
	/**
	 * 删除字典的所有明細
	 */
	public void delDicDtl(String dicCod,String dicDtlCod) throws Exception {
		String sqlStr = " delete from sys_dic_dtl where diccod = :dicCod and dicdtlcod = :dicDtlCod  ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("dicCod", dicCod);
		sqlQuery.setString("dicDtlCod", dicDtlCod);
		sqlQuery.executeUpdate();
	}
}