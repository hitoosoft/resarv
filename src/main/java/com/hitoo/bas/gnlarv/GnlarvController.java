package com.hitoo.bas.gnlarv;

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

import com.alibaba.fastjson.JSONArray;
import com.hitoo.bas.gnlarv.service.GnlarvService;
import com.hitoo.bas.entity.Gnlarv;
import com.hitoo.frame.base.BaseController;
import com.hitoo.frame.base.BusinessException;
import com.hitoo.frame.pub.model.TreeModel;

@Controller
@RequestMapping("/bas/gnlarv")
public class GnlarvController extends BaseController {
	
	@Autowired
	private GnlarvService gnlarvService ;
	
	/*
	 * 加载全宗树
	 */
	@RequestMapping("/queryGnlArvTree")
	@ResponseBody
	public List<TreeModel> queryGnlArvTree(HttpServletRequest request, String gnlarvID,String dataAutFlag) throws Exception {
		return gnlarvService.queryGnlArvTree(this.getLoginInfo(request), gnlarvID);
	}
	
	/*
	 * 根据全宗ID查询全宗信息
	 */
	@RequestMapping("/queryGnlarvByID")
	@ResponseBody
	public Map<String, Object> queryGnlarvByID(HttpServletRequest request, String gnlarvID) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Gnlarv", gnlarvService.queryGnlarvByID(gnlarvID));
		return this.writeSuccMsg("", paramMap);
	}
	
	/*
	 * 新增全宗
	 */
	@RequestMapping("/addGnlArv")
	@ResponseBody
	public Map<String, Object> addGnlArv(HttpServletRequest request, @Valid Gnlarv gnlarv) throws Exception {
		gnlarvService.addGnlArv(gnlarv);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID", gnlarv.getGnlArvID());
		return this.writeSuccMsg("", paramMap);
	}
	
	/*
	 * 修改全宗
	 */
	@RequestMapping("/modifyGnlarv")
	@ResponseBody
	public Map<String, Object> modifyGnlarv(HttpServletRequest request, @Valid Gnlarv gnlarv) throws Exception { 
		gnlarvService.modifyGnlarv(gnlarv);
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ID", gnlarv.getGnlArvID());
		return this.writeSuccMsg("", paramMap);
	}
	
	/*
	 * 删除全宗
	 */
	@RequestMapping("/deleteGnlArv")
	@ResponseBody
	public Map<String, Object> deleteGnlArv(HttpServletRequest request, String gnlarvID) throws Exception {
		if(StringUtils.isBlank(gnlarvID)){
			throw new BusinessException("要删除的全宗ID不能为空！");
		}
		gnlarvService.deleteGnlArv(gnlarvID);
		return this.writeSuccMsg("删除全宗成功！");
	}
	
	/*
	 * 查询所有全宗，组装下拉列表
	 */
	@RequestMapping("/queryGnlArv")
	@ResponseBody
	public JSONArray queryGnlArv(HttpServletRequest request) throws Exception {
		return gnlarvService.queryGnlArv();
	}
	
	//---------------------- 跳转页面
	/**
	 * 主菜单：全宗管理
	 */
	@RequestMapping("/forwardGnlarvindex")
	public String forwardGnlarvindex(HttpServletRequest request) throws Exception {
		return "bas/gnlarv/gnlarvindex";	
	}
	
	/**
	 * 新增全宗页面
	 */
	@RequestMapping("/forwardAddGnlarv")
	public String forwardAddGnlarv(HttpServletRequest request) throws Exception {
		return "bas/gnlarv/addgnlarv";	
	}
	
	/**
	 * 修改全宗页面
	 */
	@RequestMapping("/forwardModifygnlarv")
	public String forwardModifygnlarv(HttpServletRequest request) throws Exception {
		return "bas/gnlarv/modifygnlarv";	
	}
	//----------------------end of 跳转页面
	
}
