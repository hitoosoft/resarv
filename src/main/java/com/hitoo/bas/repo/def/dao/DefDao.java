package com.hitoo.bas.repo.def.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hitoo.bas.entity.Repo;
import com.hitoo.frame.base.BaseDAO;
import org.hibernate.SQLQuery;

@Component
public class DefDao extends BaseDAO {
	
	/**
	 * 查询所有库房，以seqNO排序
	 */
	@SuppressWarnings("unchecked")
	public List<Repo> queryDefTree() throws Exception {
		String sqlStr = " select * from REPO_DEF order by seqNO ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.addEntity(Repo.class);
		List<Repo> repolist = query.list();
		return repolist;
	}
	
	/**
	 * 验证库房名称重复
	 */
	@SuppressWarnings("unchecked")
	public String queryDefByNam(String repoID, String repoNam) throws Exception {
		String sqlStr = " select repoNam from REPO_DEF where repoNam = :repoNam and repoID <> :repoID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("repoNam", repoNam);
		query.setString("repoID", repoID);
		query.addEntity(Repo.class);
		List<String>  listNam = query.list();
		if(listNam!=null&&listNam.size()>0){
			return listNam.get(0);
		}
		return null;
	}
	
}
