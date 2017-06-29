package com.hitoo.bas.repo.eqpt.dao;

import java.math.BigDecimal;
import java.util.List;

import com.hitoo.bas.entity.Eqpt;
import com.hitoo.entity.arv.ArvBasInfo;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;
import com.hitoo.frame.base.BaseDAO;

@Component
public class EqptDao extends BaseDAO {
	
	/**
	 * 查询库房设备名称是否重复
	 */
	@SuppressWarnings("unchecked")
	public String queryEqptIDByNam(String repoID, String eqptNam) throws Exception {
		String sqlStr = " select eqptNam from REPO_EQPT where repoID = :repoID and eqptNam = :eqptNam ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("repoID", repoID);
		query.setString("eqptNam", eqptNam);
		List<String>  listNam = query.list();
		if(listNam!=null&&listNam.size()>0){
			return listNam.get(0);
		}
		return null;
	}
	
	/**
	 * 查询库房设备名称是否重复(修改)
	 */
	@SuppressWarnings("unchecked")
	public String queryEqptIDModifyByNam(String repoID, String eqptID, String eqptNam) throws Exception {
		String sqlStr = " select eqptNam from REPO_EQPT where repoID = :repoID and "
				+ " eqptNam = :eqptNam and eqptID <> :eqptID ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("repoID", repoID);
		sqlQuery.setString("eqptID", eqptID);
		sqlQuery.setString("eqptNam", eqptNam);
		List<String> listNam = sqlQuery.list();
		if(listNam!=null&&listNam.size()>0){
			return listNam.get(0);
		}
		return null;
	}
	
	/**
	 * 查询库房下设备数量
	 */
	@SuppressWarnings("unchecked")
	public Integer queryEqptCountByRepoID(String repoID) throws Exception {
		String sqlStr = " select count(repoID) from REPO_EQPT where repoID = :repoID ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("repoID", repoID);
		List<BigDecimal> countList = sqlQuery.list();
		if(countList!=null&&countList.size()>0&&countList.get(0)!=null){
			return countList.get(0).intValue();
		}
		return 0;
	}
	
	/**
	 * 根据设备ID删除单元格
	 */
	public void deleteCellByEqptID(String eqptID) throws Exception {
		String sqlStr = " delete from REPO_CELL where eqptID = :eqptID ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("eqptID", eqptID);
		sqlQuery.executeUpdate();
	}
	
	/**
	 * 根据设备ID删除单序号
	 */
	public void deleteCellNoByEqptID(String eqptID) throws Exception {
		String sqlStr = " delete from REPO_CELL_NO where eqptID = :eqptID ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("eqptID", eqptID);
		sqlQuery.executeUpdate();
	}
	
	/**
	 * 验证设备下单元格是否存有档案
	 */
	@SuppressWarnings("unchecked")
	public ArvBasInfo queryFirstArvtypeUsingCellByEqptID(String eqptID) throws Exception {
		String sqlStr = " select c.* from REPO_EQPT a, REPO_CELL b, ARV_BASINFO c where  "
				+ "a.eqptID = b.eqptID and b.cellID = c.cellID and a.eqptID = :eqptID ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("eqptID", eqptID);
		sqlQuery.addEntity(ArvBasInfo.class);
		List<ArvBasInfo> arvBasInfo = sqlQuery.list();
		if(arvBasInfo!=null&&arvBasInfo.size()>0){
			return arvBasInfo.get(0);
		}
		return null;
	}
	
	/**
	 * 设备排、层同步到单元格
	 */
	public void delPartNOAndByID(String eqptID, String aftPartNO, String aftLevNO) throws Exception {
		String sqlStr = " delete from REPO_CELL where eqptID = :eqptID and partNO > :aftPartNO or levNO > :aftLevNO ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("eqptID", eqptID);
		sqlQuery.setString("aftPartNO", aftPartNO);
		sqlQuery.setString("aftLevNO", aftLevNO);
		sqlQuery.executeUpdate();
	}
	
	/**
	 * 根据设备ID和排号，查看该设备的该排有多少层
	 */
	@SuppressWarnings("unchecked")
	public Integer queryMaxLevNO(String eqptID, String partNO) throws Exception {
		
		String sqlStr = " select max(levNO) from REPO_CELL where eqptID = :eqptID and partNO = :partNO ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("eqptID", eqptID);
		sqlQuery.setString("partNO", partNO);
		List<BigDecimal> levNOList = sqlQuery.list();
		if(levNOList!=null&&levNOList.size()>0&&levNOList.get(0)!=null){
			return levNOList.get(0).intValue();
		}
		return 0;
	}
	
}
