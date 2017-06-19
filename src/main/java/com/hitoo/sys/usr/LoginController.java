package com.hitoo.sys.usr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitoo.frame.base.BaseController;
import com.hitoo.frame.base.BusinessException;
import com.hitoo.frame.pub.global.LoginInfo;
import com.hitoo.frame.pub.model.TreeModel;
import com.hitoo.sys.entity.Role;
import com.hitoo.sys.role.service.RoleService;
import com.hitoo.sys.usr.service.UserService;

@Controller
public class LoginController extends BaseController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	/**
	 * 已登录跳转到主页面，未登录跳转到登录界面
	 */
	@RequestMapping("/")
	public String home(ModelMap map, HttpServletRequest request) throws Exception {
		return index(map, request);
	}
	
	/**
	 * 已登录跳转到主页面，未登录跳转到登录界面
	 */
	@RequestMapping("/index")
	public String index(ModelMap map, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(true);
		LoginInfo loginInfo = (LoginInfo) session.getAttribute(LoginInfo.LOGIN_USER);
		if (loginInfo == null) {
			return "logon";
		}
		// 加载用户所有的菜单权限
		List<TreeModel> menuList = userService.getMenuAut(loginInfo);
		map.put("menuList", menuList);
		return "index";
	}
	
	/**
	 * 用户登入
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest request, String usrCod, String pwd) throws Exception {
		HttpSession session = request.getSession(true);
		if(StringUtils.isBlank(usrCod)){
			throw new BusinessException("用户名不能为空！");
		}
		if(StringUtils.isBlank(pwd)){
			throw new BusinessException("密码不能为空！");
		}
		//获取session中的用户
		LoginInfo loginInfo  = (LoginInfo) session.getAttribute(LoginInfo.LOGIN_USER);

		if (loginInfo != null) {
			if (!usrCod.equals(loginInfo.getUserCod())) {
				throw new BusinessException("已经登录了一个其他用户，请选退出再登录该用户！");
			}
		}
		loginInfo = userService.getUserByNoPwd(usrCod, pwd);
		if(loginInfo==null){
			throw new BusinessException("您输入的用户名和密码不匹配！");
		}
		loginInfo.setIp(request.getRemoteAddr());
		//将用户的角色保存在logininfo中,排除超级角色标志。。
		List<Role> roleList =  roleService.queryAllRoleOwnByCommonUsr(loginInfo.getUserId());
		List<String> roleIDs=new ArrayList<String>();
		for(Role role:roleList){
			roleIDs.add(role.getRoleID());
		}
		loginInfo.setRoleIDList(roleIDs);
		
		//检查用户是否超级用户
		loginInfo.setSuperAdmin(roleService.ownSuperRole(loginInfo.getUserId()));
		
		//将用户的权限列表保存在logininfo中供权限拦截器使用
		Set<String> urlList = userService.getUsrFuncUrls(loginInfo);
		loginInfo.setUrlList(urlList);
		
		//加载用户所有的按钮权限返回json串
		 Map<String,Set<String>> btnMap = userService.getBtnAut(loginInfo);
		loginInfo.setBtnMap(btnMap);
	
		// 验证通过，用户放入session
		session.removeAttribute(LoginInfo.LOGIN_USER);
		session.setAttribute(LoginInfo.LOGIN_USER, loginInfo);
		
		return this.writeSuccMsg("");
	}
	
	/**
	 * 再次登录页面
	 */
	@RequestMapping("/forwardRelogin")
	public String forwardRelogin(ModelMap map, HttpServletRequest request) throws Exception {
		return "relogon";
	}
	
	/**
	 * 首页欢迎页
	 */
	@RequestMapping("/welcome")
	public String welcome(ModelMap map, HttpServletRequest request) throws Exception {
		return "welcome";
	}
	
	/**
	 * 登录超时检测
	 */
	@RequestMapping("/checkLoginOverTime")
	@ResponseBody
	public Map<String, Object> checkLoginOverTime(HttpServletRequest request,HttpServletResponse response, String usrCod, String pwd) throws Exception {
		String normalsta="1";
		LoginInfo loginInfo = (LoginInfo) request.getSession(true).getAttribute(LoginInfo.LOGIN_USER);
		if(null == loginInfo){
			normalsta = "0";
		}
		return this.writeSuccMsg("", "normalsta", normalsta);
	}
	
	
	/**
	 * 登出
	 */
	@RequestMapping("/logout")
	public String logout(ModelMap map, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(true);
		session.removeAttribute(LoginInfo.LOGIN_USER);
		return "logon";
	}
	
	/**
	 *根据页面funcid，查询该页面用户具有的按钮权限
	 */
	@RequestMapping("/admin/queryBtnMapByPageFuncID")
	@ResponseBody
	public Map<String, Object> queryBtnMapByPageFuncID(ModelMap map, HttpServletRequest request,String pageFuncID) throws Exception {
		Set<String> pageBtns=null;
		LoginInfo loginInfo =(LoginInfo)request.getSession().getAttribute(LoginInfo.LOGIN_USER);
		Map<String, Set<String>> allFuncBtn=loginInfo.getBtnMap();
		if(allFuncBtn!=null){
			pageBtns = allFuncBtn.get(pageFuncID);
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("pageBtns", pageBtns);
		return this.writeSuccMsg("", paraMap);
	}

}