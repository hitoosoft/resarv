package com.hitoo.sys.usr.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hitoo.frame.pub.global.LoginInfo;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;
import com.hitoo.sys.entity.Usr;

public interface UsrService {
	

	/**
	 * 获取用户的功能权限树
	 */
	public List<TreeModel> queryUsrAuthorityTree(String usrID)throws Exception ;
	//
	public void changeRoleForUsr(String usrID,String roleID,boolean addFlag)throws Exception ;
	//注销用户
	public void writeOffUsr(String writeOffUsrID) throws Exception;
	
	//校验登入用户
	public LoginInfo getUserByNoPwd(String usrCod, String pwd) throws Exception;
	
	//恢复用户
	public void recoverUsr(String recoverUsrID) throws Exception;

	//重置用户密码
	public void resetUsrPwd(String resetUsrID) throws Exception;

	/**
	 * 获取运维人员的所有功能菜单
	 */
	public List<TreeModel> getMenuAut(LoginInfo loginInfo) throws Exception;

	/**
	 * 获取运维人员的所有功能按钮，格式为：key=（菜单权限code）value=（set， 按钮权限code）
	 */
	public Map<String, Set<String>> getBtnAut(LoginInfo loginInfo) throws Exception;

	/**
	 * 获取用户所有的权限
	 */
	public Set<String> getUsrFuncUrls(LoginInfo loginInfo) throws Exception;
	
	/**
	 * 根据前台传过来的机构ID，查询获取该机构下的所有用户列表,根据orgID、usrCod、usrNam检索
	 * 以hibernate分页的方式去查询
	 */
	public PageInfo queryUsrWithPage(PageInfo pi ,String orgID,String usrCodOrNam) throws Exception;
	
	/*
	 * 根据ID获取用户信息
	 */
	public Usr queryUsrByID(String usrID) throws Exception;
	
	/**
	 * 修改密码
	 */
	public void modifyPwd(String usrCod, String oldPwd, String newPwd, String newPwdSure) throws Exception ;
	
	/*
	 * 新增用户
	 */
	public String addUsr(Usr usr) throws Exception;
	
	/*
	 * 修改用户
	 */
	public String modifyUsr(Usr usr) throws Exception;
	
	/*
	 * 保存参照授权
	 */
	public void saveRefer(String referUsrID, String usrIDs) throws Exception;
	
}