package com.hitoo.bas.dataut.service;

import java.util.List;

import com.hitoo.frame.pub.global.LoginInfo;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;
import com.hitoo.sys.entity.Role;

public interface DatautService {
	/*
	 * 分页角色列表
	 */
	public PageInfo queryRoleDgOwnByUserWithPage(PageInfo pi ,LoginInfo user) throws Exception ;
	
	/**
	 * 获取角色具有的功能权限树：已有的打勾
	 */
	public List<TreeModel> queryRoleDatautTree(String roleID,LoginInfo user,boolean frontPageCascadeFlag) throws Exception ;
	
	/**
	 * 为角色赋权保存
	 */
	public void  saveRoleGnl(String roleID ,String gnlIDs) throws Exception;
	
	/**
	 * 功能权限类型角色-增删改
	 */
	public void addRole(Role role) throws Exception ;
	public void delRoleDataut(String roleID) throws Exception ;
	public void editRole(Role role) throws Exception ;
}
