package com.hitoo.bas.repo.def;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitoo.bas.entity.Repo;
import com.hitoo.bas.repo.def.service.DefService;
import com.hitoo.frame.base.BaseController;
import com.hitoo.frame.base.BusinessException;
import com.hitoo.frame.pub.model.TreeModel;

@Controller
@RequestMapping("/repo/def")
public class DefContorller extends BaseController{
	
	@Autowired
	private DefService defService;
	
	/*
	 * 加载库房树
	 */
	@RequestMapping("/queryDefTree")
	@ResponseBody
	public List<TreeModel> queryDefTree(HttpServletRequest request) throws Exception {
		return defService.queryDefTree();
	}
	
	/*
	 * 根据ID查询库房信息
	 */
	@RequestMapping("/queryDefByID")
	@ResponseBody
	public Map<String, Object> queryDefByID(HttpServletRequest request, String repoID) throws Exception {
		if(StringUtils.isBlank(repoID)){
			throw new BusinessException("查询的ID为空！");
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("Def", defService.queryDefByID(repoID));
		return this.writeSuccMsg("", paraMap);
	}
	
	/*
	 * 新增库房
	 */
	@RequestMapping("/addDef")
	@ResponseBody
	public Map<String, Object> addDef(HttpServletRequest request, @Valid Repo def) throws Exception{
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("ID",defService.addDef(def));
		return this.writeSuccMsg("",paraMap);
	}
	
	/*
	 * 删除库房
	 */
	@RequestMapping("/deleteDef")
	@ResponseBody
	public Map<String, Object> deleteDef(HttpServletRequest request, String repoID) throws Exception {
		if(StringUtils.isBlank(repoID)){
			throw new BusinessException("要删除的库房ID为空！");
		}
		defService.deleteDef(repoID);
		return this.writeSuccMsg("删除成功！");
	}
	
	/*
	 * 修改库房
	 */
	@RequestMapping("/modifyDef")
	@ResponseBody
	public Map<String, Object> modifyDef(HttpServletRequest request, @Valid Repo def) throws Exception{
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("ID",defService.modifyDef(def));
		return this.writeSuccMsg("",paraMap);
	}
	
	//---------------------- 跳转页面
	/**
	 * 主菜单：库房管理页面
	 */
	@RequestMapping("/forwardDefindex")
	public String forwardDefindex(HttpServletRequest request) throws Exception {
		return "repo/def/defindex";
	}
	
	/**
	 * 新增库房页面
	 */
	@RequestMapping("/forwardAddDef")
	public String forwardAddDef(HttpServletRequest request) throws Exception {
		return "repo/def/adddef";
	}
	
	/**
	 * 修改库房页面
	 */
	@RequestMapping("/forwardModifyDef")
	public String forwardModifyDef(HttpServletRequest request) throws Exception {
		return "repo/def/modifydef";	
	}
	//----------------------end of 跳转页面
	
}