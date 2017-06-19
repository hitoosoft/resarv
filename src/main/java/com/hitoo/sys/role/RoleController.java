package com.hitoo.sys.role;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitoo.frame.base.BaseController;
import com.hitoo.frame.pub.global.LoginInfo;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;
import com.hitoo.sys.entity.Role;
import com.hitoo.sys.role.service.RoleService;
/**
 * 角色
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController extends BaseController {
	
	@Autowired
	private RoleService roleService;
	
	/*
	 * 查询用户拥有的角色列表
	 * 直接在sys_usr_role 角色表中管理
	 */
	@RequestMapping("/queryOwnedRoleDgByUser")
	@ResponseBody
	public PageInfo queryOwnedRoleDgByUser(HttpServletRequest request,String usrID) throws Exception{
		return roleService.queryOwnedRoleDgByUser(usrID,RoletypeFuncDefine.FUNCAUT.getCode());
	}
	
	/*
	 * 查询用户没有拥有的角色列表，但是可以作为可选
	 * 直接在sys_usr_role 角色表中管理
	 */
	@RequestMapping("/queryNotOwnedRoleDgByUser")
	@ResponseBody
	public PageInfo queryNotOwnedRoleDgByUser(HttpServletRequest request,String usrID) throws Exception{
		LoginInfo user = this.getLoginInfo(request);
		return roleService.queryNotOwnedRoleDgByUser(user ,usrID,RoletypeFuncDefine.FUNCAUT.getCode());
	}
	
	
	/*
	 * 查询用户拥有的角色列表
	 * 直接在sys_usr_role 角色表中管理
	 */
	@RequestMapping("/queryOwnedRoleArvtypeDgByUser")
	@ResponseBody
	public PageInfo queryOwnedRoleArvtypeDgByUser(HttpServletRequest request,String usrID) throws Exception{
		return roleService.queryOwnedRoleDgByUser(usrID,RoletypeFuncDefine.DATAUT_ARVTYPE.getCode());
	}
	
	/*
	 * 查询用户没有拥有的角色列表，但是可以作为可选
	 * 直接在sys_usr_role 角色表中管理
	 */
	@RequestMapping("/queryNotOwnedRoleArvtypeDgByUser")
	@ResponseBody
	public PageInfo queryNotOwnedRoleArvtypeDgByUser(HttpServletRequest request,String usrID) throws Exception{
		LoginInfo user = this.getLoginInfo(request);
		return roleService.queryNotOwnedRoleDgByUser(user ,usrID,RoletypeFuncDefine.DATAUT_ARVTYPE.getCode());
	}
	
	
	
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
		return roleService.queryRoleDgOwnByUserWithPage(pi ,user);
	}
	
	/*
	 *  新增角色--功能权限类型
	 */ 
	@RequestMapping("/addRole")
	@ResponseBody
	public Map<String, Object> addRole(HttpServletRequest request,@Valid Role role) throws Exception{		
		roleService.addRole(role);				
		return this.writeSuccMsg("");		
	}
	
	/*
	 *  删除角色--功能权限类型
	 */ 
	@RequestMapping("/delRoleFnc")
	@ResponseBody
	public Map<String, Object> delRoleFnc(HttpServletRequest request,String roleID) throws Exception{
		roleService.delRoleFnc(roleID);
		return this.writeSuccMsg("删除角色成功！");
	}
	
	/*
	 *  修改角色--功能权限类型
	 */ 
	@RequestMapping("/editRole")
	@ResponseBody
	public Map<String, Object> editRole(HttpServletRequest request,@Valid Role role) throws Exception{
		roleService.editRole(role);				
		return this.writeSuccMsg("");		
	}
	
	/*
	 * 根据角色ID，获取该角色的功能权限树
	 * */
	@RequestMapping("/queryRoleFuncTree")
	@ResponseBody
	public List<TreeModel> queryRoleFuncTree(HttpServletRequest request,String roleID,String  frontPageCascade) throws Exception{
		boolean frontPageCascadeFlag=true;
		if("0".equals(frontPageCascade)){
			frontPageCascadeFlag=false;
		}
		LoginInfo user = this.getLoginInfo(request);
		List<TreeModel> tList = roleService.queryRoleFuncTree(roleID,user,frontPageCascadeFlag);
		return tList;		
	}
	
	/*
	 *对角色进行赋权保存
	 */	
	@RequestMapping("/saveRoleFunc")
	@ResponseBody
	public Map<String, Object> saveRoleFunc(HttpServletRequest request,String roleID ,String funcIDs) throws Exception{
		roleService.saveRoleFunc(roleID ,funcIDs);
		return this.writeSuccMsg("保存成功！");		
	}
	
	//---------------------- 跳转页面
	/**
	 * 主菜单：角色管理
	 */
	@RequestMapping("/forwardRoleindex")
	public String forwardRoleindex(HttpServletRequest request) throws Exception {
		return "sys/role/roleindex";	
	}
	
	/**
	 * 新增或者修改角色页面
	 */
	@RequestMapping("/forwardManagerole")
	public String forwardManagerole(HttpServletRequest request) throws Exception {
		return "sys/role/managerole";	
	}
	//----------------------end of 跳转页面
	
}
