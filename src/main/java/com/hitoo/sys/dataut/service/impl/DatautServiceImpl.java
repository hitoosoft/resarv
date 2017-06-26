package com.hitoo.sys.dataut.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitoo.bas.entity.Gnlarv;
import com.hitoo.bas.entity.RoleGnlDataut;
import com.hitoo.frame.base.BaseService;
import com.hitoo.frame.base.BusinessException;
import com.hitoo.frame.common.util.TreeUtil;
import com.hitoo.frame.pub.global.LoginInfo;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;
import com.hitoo.sys.entity.Role;
import com.hitoo.sys.role.RoletypeFuncDefine;
import com.hitoo.sys.dataut.dao.DatautDao;
import com.hitoo.sys.dataut.service.DatautService;

@Service
public class DatautServiceImpl extends BaseService implements DatautService {

	@Autowired
	private DatautDao datautDao ;

	/*
	 * 分页角色列表
	 * 查看用户是否拥有超级角色，如果拥有超级角色则返回，除了超级角色之外的所有角色，
	 * 如果没拥有超级角色，返回该用户拥有的角色
	 */
	@Override
	public PageInfo queryRoleDgOwnByUserWithPage(PageInfo pi,LoginInfo user) throws Exception {
		if(user.isSuperAdmin()){
			return datautDao.queryAllRoleExcludeSuperRoleWithPage(pi, RoletypeFuncDefine.DATAUT.getCode());
		}else{
			return datautDao.queryAllRoleOwnByCommonUsrWithPage(pi, RoletypeFuncDefine.DATAUT.getCode(), user.getUserId());
		}
	}
	
	/**
	 * 获取角色具有的数据权限树：已有的打勾
	 * 根据赋权用户的ID，查询该用户是否拥有超级角色，
	 * 如果用户超级角色，返回所有的数据权限组成的树
	 * 否则，返回该用户用户的数据权限组成的树。
	 */
	@Override
	public List<TreeModel> queryRoleDatautTree(String roleID,LoginInfo user,boolean frontPageCascadeFlag) throws Exception {
		//先获取全部权限树
		List<Gnlarv> gnlList = null ;
		if(user.isSuperAdmin()){
			gnlList = datautDao.findGnls("");
		}else{
			gnlList = datautDao.findGnls("",user.getUserId());
		}
		
		//再决定哪些需要打勾
 		List<String> checkList = datautDao.queryDatautListByRoleID(roleID);
		List<TreeModel> tList = TreeUtil.setTree(gnlList, checkList,frontPageCascadeFlag);
		return TreeUtil.setTreeOpenLevel(tList, 2);
	}
	
	/**
	 * 为角色赋权保存,前台传过来的，把由gnlIDs组成的串分割成数组
	 */
	@Override
	public void  saveRoleGnl(String roleID ,String gnlIDs) throws Exception{
		String[] frontgnlIDArray = gnlIDs.split(",");
		List<String> frontGnlIDList = Arrays.asList(frontgnlIDArray);
		List<String> oldgnlIDList = datautDao.queryGnlListByRoleID(roleID);
		for (String gnlID : frontGnlIDList) {
			if(StringUtils.isNotBlank(gnlID)){
				if(!oldgnlIDList.contains(gnlID)){
					RoleGnlDataut rolegnl = new RoleGnlDataut();
					rolegnl.setPk(dBUtil.getCommonId());
					rolegnl.setRoleID(roleID);
					rolegnl.setGnlArvID(gnlID);
					commonDao.saveEntity(rolegnl);
				}
			}
		}
		//找出要删除的
		for (String gnlID : oldgnlIDList) {
			if(!frontGnlIDList.contains(gnlID)){
				datautDao.deleteRoleGnlEntity(roleID, gnlID);
			}
		}	
	}
	
	/**
	 * 数据权限类型角色-增删改
	 */
	@Override
	public void addRole(Role role) throws Exception {
		if(null!=datautDao.queryRoleByNam(role.getRoleNam())){
			throw new BusinessException("角色名字已被占用！");
		}
		role.setRoleID(dBUtil.getCommonId());
		commonDao.saveEntity(role);
	}
	
	@Override
	public void delRoleDataut(String roleID) throws Exception {
		//删除角色需要删除角色本身 和角色与用户的管理，角色与功能权限的管理
		datautDao.delRoleDatautAndRelation(roleID, RoletypeFuncDefine.DATAUT.getRelativeTable());
	}
	
	@Override
	public void editRole(Role role) throws Exception {
		Role dBRole = commonDao.findEntityByID(Role.class, role.getRoleID());
		Role otherRole = datautDao.queryRoleByNam(role.getRoleNam()) ;
		if(otherRole!=null && !otherRole.getRoleID().equals(dBRole.getRoleID())){
			throw new BusinessException("角色名字已被占用！");
		}
		dBRole.setRoleNam(role.getRoleNam());
		dBRole.setDescr(role.getDescr());
		commonDao.updateEntity(dBRole);
	}
}	

