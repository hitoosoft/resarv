package com.hitoo.sys.dataut;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hitoo.frame.base.BaseController;
import com.hitoo.frame.pub.global.LoginInfo;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;
import com.hitoo.sys.dataut.service.DatautService;
import com.hitoo.sys.entity.Role;

@Controller
@RequestMapping("/sys/dataut")
public class DatautControl extends BaseController{
	@Autowired
	private DatautService datautService;
	
	/*
	 * 分页角色列表，
	 * 其中： 当用户用户超级管理角色时，可以管理的角色为除了超级管理之外的所有角色；
	 * 当用户只是普通用户时，能管理的角色只是她自身拥有的角色，
	 *  总之，超级管理角色在角色管理页面是不可见的，即不可编辑。
	 */
	@RequestMapping("/queryRoleDgOwnByUserWithPage")
	@ResponseBody
	public PageInfo queryRoleDgOwnByUserWithPage(HttpServletRequest request) throws Exception{
		PageInfo pi = this.getPageInfo(request);
		LoginInfo user = this.getLoginInfo(request);
		return datautService.queryRoleDgOwnByUserWithPage(pi ,user);
	}
	
	/*
	 * 根据角色ID，获取该角色的数据权限树
	 * */
	@RequestMapping("/queryRoleDatautTree")
	@ResponseBody
	public List<TreeModel> queryRoleDatautTree(HttpServletRequest request,String roleID,String  frontPageCascade) throws Exception{
		boolean frontPageCascadeFlag=true;
		if("0".equals(frontPageCascade)){
			frontPageCascadeFlag=false;
		}
		LoginInfo user = this.getLoginInfo(request);
		List<TreeModel> tList = datautService.queryRoleDatautTree(roleID,user,frontPageCascadeFlag);
		return tList;		
	}
	
	/*
	 *  新增角色--数据权限类型
	 */ 
	@RequestMapping("/addRole")
	@ResponseBody
	public Map<String, Object> addRole(HttpServletRequest request,@Valid Role role) throws Exception{		
		datautService.addRole(role);				
		return this.writeSuccMsg("");		
	}
	
	/*
	 *  删除角色--功能权限类型
	 */ 
	@RequestMapping("/delRoleDataut")
	@ResponseBody
	public Map<String, Object> delRoleDataut(HttpServletRequest request,String roleID) throws Exception{
		datautService.delRoleDataut(roleID);
		return this.writeSuccMsg("删除角色成功！");
	}
	
	/*
	 *  修改角色--数据权限类型
	 */ 
	@RequestMapping("/editRole")
	@ResponseBody
	public Map<String, Object> editRole(HttpServletRequest request,@Valid Role role) throws Exception{
		datautService.editRole(role);				
		return this.writeSuccMsg("");		
	}
	//---------------------- 跳转页面
	/**
	 * 主菜单：数据权限管理
	 */
	@RequestMapping("/forwardOrgdatautindex")
	public String forwardRoleindex(HttpServletRequest request) throws Exception {
		return "sys/dataut/orgdatautindex";
	}
	
	/**
	 * 新增或者修改角色页面
	 */
	@RequestMapping("/forwardManagerole")
	public String forwardManagerole(HttpServletRequest request) throws Exception {
		return "sys/dataut/managerole";	
	}
}
