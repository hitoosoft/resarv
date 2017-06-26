package com.hitoo.sys.dataut.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

import com.hitoo.bas.entity.Gnlarv;
import com.hitoo.frame.base.BaseDAO;
import com.hitoo.frame.enumdic.EnumSuperAdminType;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.sys.entity.Role;

@Component
public class DatautDao  extends BaseDAO{
	@SuppressWarnings("unchecked")
	public List<Gnlarv> findGnls(String funcType) throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("from Gnlarv");
		if(StringUtils.isNotBlank(funcType)){
			hql.append(" where funcTyp = :funcType");
		}
		Query query = getCurrentSession().createQuery(hql.toString());
		if(StringUtils.isNotBlank(funcType)){
			query.setString("funcType", funcType);
		}
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Gnlarv> findGnls(String funcType,String userId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct gnl.* ");
		sql.append("   from bas_gnlarv gnl, bas_gnl_dataut bgd, sys_usr_role ur ");
		sql.append("  where ur.usrid = :userId ");
		sql.append("    and ur.roleid = rg.roleid ");
		sql.append("    and bgd.gnlarvid = gnl.gnlarvid ");
		if(StringUtils.isNotBlank(funcType)){
			sql.append(" and f.functyp = :funcType ");
		}
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setString("userId", userId);
		if(StringUtils.isNotBlank(funcType)){
			query.setString("funcType", funcType);
		}
		query.addEntity("gnl", Gnlarv.class);
		return query.list();
	}
	
	/**
	 * 某个角色用户的数据权限ID列表
	 */
	@SuppressWarnings("unchecked")
	public List<String> queryDatautListByRoleID(String roleID) throws Exception {
		String sqlStr="select bgd.gnlarvid from  bas_gnl_dataut bgd where bgd.ROLEID=:roleID";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		return query.list();
	}
	
	/**
	 * 获取超级类型的用户拥有的角色列表，根据角色类型
	 */
	public PageInfo queryAllRoleExcludeSuperRoleWithPage(PageInfo pi ,String roleType ) throws Exception{
		String sqlStr = " select * from sys_role where roletype = :roleType and roleid != :superRole "
				+ " order by rolenam" ;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("roleType", roleType);
		parameters.put("superRole", EnumSuperAdminType.SUPERROLE.getCode());
		SQLEntity sqlEntity = new SQLEntity(sqlStr, parameters);
		return executePageQuery(pi, sqlEntity, Role.class);
	}
	
	/**
	 * 分页普通用户拥有的角色列表，根据角色类型
	 */
	public PageInfo queryAllRoleOwnByCommonUsrWithPage(PageInfo pi ,String roleType ,String usrID) throws Exception{
		String sqlStr = " select distinct r.* from sys_role r ,sys_usr_role ur"
				+ " where r.roleid = ur.roleid  "
				+ " and r.roletype = :roleType "
				+ " and r.roleid != :superRole "
				+ " and ur.usrid = :usrID "
				+ " order by rolenam" ;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("roleType", roleType);
		parameters.put("usrID", usrID);
		parameters.put("superRole", EnumSuperAdminType.SUPERROLE.getCode());
		SQLEntity sqlEntity = new SQLEntity(sqlStr, parameters);
	    return executePageQuery(pi, sqlEntity, Role.class);
	}
	
	/**
	 * 根据名字查询角色
	 * 检验是否已经存在
	 */
	@SuppressWarnings("unchecked")
	public Role queryRoleByNam(String roleNam)throws Exception{
		String sqlStr = " select * from sys_role where rolenam = :roleNam ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleNam", roleNam);
		query.addEntity(Role.class);
		List<Role> ls =query.list();
		if(ls!=null && ls.size() >0){
			return ls.get(0);
		}
		return null ;
	}
	
	/**
	 * 某个角色用户的功能权限ID列表
	 */
	@SuppressWarnings("unchecked")
	public List<String> queryGnlListByRoleID(String roleID) throws Exception {
		String sqlStr="select bgd.GNLARVID from  bas_gnl_dataut bgd where bgd.ROLEID=:roleID";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		return query.list();
	}
	
	/**
	 * 删除角色与数据权限关系
	 */
	public void deleteRoleGnlEntity(String roleID,String gnlID) throws Exception {
		String sqlStr="delete from  bas_gnl_dataut  where ROLEID=:roleID and GNLARVID = :gnlID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.setString("gnlID", gnlID);
		query.executeUpdate();
	}
	
	/**
	 * 删除数据权限类型的角色及角色相关的表：角色与用户 ，角色与数据权限
	 */
	public void delRoleDatautAndRelation(String roleID,String relativeTableName)throws Exception{
		String sqlStr = " delete from sys_role where roleid = :roleID " ;
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.executeUpdate();
		
		sqlStr = " delete from sys_usr_role where roleid = :roleID " ;
		query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.executeUpdate();
		//删除数据权限关系表
		sqlStr = " delete from "+relativeTableName+" where roleid = :roleID " ;
		query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.executeUpdate();
	}
}
