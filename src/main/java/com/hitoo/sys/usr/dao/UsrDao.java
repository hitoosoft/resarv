package com.hitoo.sys.usr.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Component;

import com.hitoo.frame.base.BaseDAO;
import com.hitoo.frame.common.util.Md5Util;
import com.hitoo.frame.enumdic.EnumRootNodeID;
import com.hitoo.frame.enumdic.EnumSuperAdminType;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.sys.entity.Func;
import com.hitoo.sys.entity.Usr;
import com.hitoo.sys.entity.UsrRole;

@Component
public class UsrDao extends BaseDAO {
	/**
	 * 某个用户与角色关系
	 */
	@SuppressWarnings("unchecked")
	public UsrRole queryUsrRoleEntity(String usrID ,String roleID) throws Exception {
		String sqlStr="select * from  sys_usr_role  where ROLEID=:roleID and USRID = :usrID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.setString("usrID", usrID);
		query.addEntity(UsrRole.class);
		List<UsrRole> ls  = query.list();
		if(ls!=null&&ls.size()>0){
			return ls.get(0);
		}
		return null;
	}
	
	/**
	 * 某个用户与角色关系
	 */
	public void delUsrRoleEntity(String usrID ,String roleID) throws Exception {
		String sqlStr="delete from  sys_usr_role  where ROLEID=:roleID and USRID = :usrID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("roleID", roleID);
		query.setString("usrID", usrID);
		query.executeUpdate();
	}
	
	/*
	 * 查找用户编码
	 */
	@SuppressWarnings("unchecked")
	public List<Usr> queryUsrByCod(String usrCod, String usrID) {
		Query query = getCurrentSession().createQuery(" from Usr where usrCod = :usrCod and usrID <> :usrID");
		query.setString("usrCod", usrCod);
		query.setString("usrID", usrID);
		return query.list();
	}
	
	/*
	 * 查找用户名称
	 */
	@SuppressWarnings("unchecked")
	public List<Usr> queryUsrByNam(String usrNam, String usrID) {
		Query query = getCurrentSession().createQuery(" from Usr where usrNam = :usrNam and usrID <> :usrID");
		query.setString("usrNam", usrNam);
		query.setString("usrID", usrID);
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
	
	//保证超级用户superadmin不被查询出来,但是超级用户也可以是正常的机构用户
	//保证orgID = eamsroot的用户不能查询出来，都不是用户创建的。
	public PageInfo queryUsrWithPage(PageInfo pi ,String orgID,String usrCodOrNam) throws Exception{
		String sqlStr  = " select a.* ,b.orgnam as orgNam from sys_usr a " +
				" left join sys_org b on b.orgid = a.orgid " +
				" where  a.orgid <> :excludeRootOrg and a.usrcod <> :excludeRootUsr  " ;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("excludeRootOrg", EnumRootNodeID.ROOTNODE.getCode());
		parameters.put("excludeRootUsr", EnumSuperAdminType.SUPERADMIN.getCode());
		if(!EnumRootNodeID.ROOTNODE.getCode().equals(orgID)&&StringUtils.isNotBlank(orgID)){
			sqlStr += " and a.orgid = :orgID " ;//机构不是EAMSROOT，添加过滤条件，否则就查询全部机构
			parameters.put("orgID", orgID);
		}
		if(StringUtils.isNotBlank(usrCodOrNam)){
			//机构不是EAMSROOT，添加过来条件，否则就查询全部机构
			sqlStr += " and (a.usrcod like :usrCod or a.usrnam like :usrNam ) " ;
			parameters.put("usrCod", "%"+usrCodOrNam+"%");
			parameters.put("usrNam", "%"+usrCodOrNam+"%");
		}
		sqlStr += " order by a.orgid ,a.usrcod  ";
		SQLEntity sqlEntity = new SQLEntity(sqlStr, parameters);
		return executePageQuery(pi, sqlEntity, Usr.class);
	}
	
	/**
	 * 检测登入用户
	 */
	@SuppressWarnings("unchecked")
	public List<Usr> getUserByNoPwd(String usrCod, String pwd) throws Exception {
		String sqlStr = " select * from sys_usr where usrCod =:usrCod and usrPwd =:pwd ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("usrCod", usrCod);
		query.setString("pwd", Md5Util.md5(pwd));
		query.addEntity(Usr.class);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> queryUsrByOrgs(List<String> orgIdList) throws Exception{
		String hql = "select distinct usrID from Usr where orgID in (:orgIdList)";
		Query query = getCurrentSession().createQuery(hql);
		query.setParameterList("orgIdList", orgIdList);
		return query.list();
	}
	
}