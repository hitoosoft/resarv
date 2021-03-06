package com.hitoo.sys.role.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

import com.hitoo.frame.base.BaseDAO;
import com.hitoo.frame.enumdic.EnumSuperAdminType;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.sys.entity.Func;
import com.hitoo.sys.entity.Role;
import com.hitoo.sys.entity.UsrRole;

@Component
public class RoleDao  extends BaseDAO{
	@SuppressWarnings("unchecked")
	public List<Func> findFuncs(String funcType) throws Exception {
		StringBuffer hql = new StringBuffer();
		hql.append("from Func");
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
	public List<Func> findFuncs(String funcType, String userId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct f.* ");
		sql.append("   from sys_func f, sys_role_func rf, sys_usr_role ur ");
		sql.append("  where ur.usrid = :userId ");
		sql.append("    and ur.roleid = rf.roleid ");
		sql.append("    and rf.funcid = f.funcid ");
		if(StringUtils.isNotBlank(funcType)){
			sql.append(" and f.functyp = :funcType ");
		}
		SQLQuery query = getCurrentSession().createSQLQuery(sql.toString());
		query.setString("userId", userId);
		if(StringUtils.isNotBlank(funcType)){
			query.setString("funcType", funcType);
		}
		query.addEntity("f", Func.class);
		return query.list();
	}

	
	/**
	 * 判断某个用户是否拥有超级管理权限
	 */
	@SuppressWarnings("unchecked")
	public boolean ownSuperRole(String usrID)throws Exception{
		String sqlStr = " select * from sys_usr_role where usrid = :usrID and roleid = :superRole ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("usrID", usrID);
		query.setString("superRole", EnumSuperAdminType.SUPERROLE.getCode());
		query.addEntity(UsrRole.class);
		List<UsrRole> roles = query.list();
		if(!CollectionUtils.isEmpty(roles)){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据名字查询角色
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
	 * 获取超级类型的用户拥有的角色列表，根据角色类型
	 */
	@SuppressWarnings("unchecked")
	public List<Role> queryAllRoleExcludeSuperRole(String roleType ) throws Exception{
		String sqlStr = " select * from sys_role where roletype = :roleType and roleid != :superRole "
				+ " order by rolenam" ;
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleType", roleType);
		query.setString("superRole", EnumSuperAdminType.SUPERROLE.getCode());
		query.addEntity(Role.class);
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
	 * 普通用户拥有的角色列表，根据角色类型
	 */
	@SuppressWarnings("unchecked")
	public List<Role> queryAllRoleOwnByCommonUsr(String roleType ,String usrID) throws Exception{
		String sqlStr = " select distinct r.* from sys_role r ,sys_usr_role ur"
				+ " where r.roleid = ur.roleid  "
				+ " and r.roletype = :roleType "
				+ " and r.roleid != :superRole "
				+ " and ur.usrid = :usrID "
				+ " order by rolenam" ;
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleType", roleType);
		query.setString("usrID", usrID);
		query.setString("superRole", EnumSuperAdminType.SUPERROLE.getCode());
		query.addEntity(Role.class);
		return query.list();
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
	 * 删除功能权限类型的角色及角色相关的表：角色与用户 ，角色与功能权限
	 */
	public void delRoleFncAndRelation(String roleID,String relativeTableName)throws Exception{
		String sqlStr = " delete from sys_role where roleid = :roleID " ;
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.executeUpdate();
		
		sqlStr = " delete from sys_usr_role where roleid = :roleID " ;
		query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.executeUpdate();
		
		sqlStr = " delete from "+relativeTableName+" where roleid = :roleID " ;
		query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.executeUpdate();
		
	}
	
	/**
	 * 某个角色用户的功能权限ID列表
	 */
	@SuppressWarnings("unchecked")
	public List<String> queryFuncListByRoleID(String roleID) throws Exception {
		String sqlStr="select srf.FUNCID from  sys_role_func srf where srf.ROLEID=:roleID";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		return query.list();
	}
	
	/**
	 * 删除角色与功能权限关系
	 */
	public void deleteRoleFuncEntity(String roleID,String funcID) throws Exception {
		String sqlStr="delete from  sys_role_func  where ROLEID=:roleID and FUNCID = :funcID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.setString("funcID", funcID);
		query.executeUpdate();
	}
}
