package com.hitoo.bas.repo.eqpt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.hitoo.bas.entity.Eqpt;
import com.hitoo.bas.repo.eqpt.service.EqptService;
import com.hitoo.frame.base.BaseController;
import com.hitoo.frame.base.BusinessException;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;
import com.hitoo.enumdic.EnumOrganizeModel;

@Controller
@RequestMapping("/repo/eqpt")
public class EqptContorller extends BaseController{
	
	@Autowired
	private EqptService eqptService;
	
	/**
	 * 查询设备树(带库房)
	 */
	@RequestMapping("/queryRepoDefAndEqptTree")
	@ResponseBody
	public List<TreeModel> queryRepoDefAndEqptTree(HttpServletRequest request) throws Exception {
		String gnlArvID = request.getParameter("gnlArvID");
		return eqptService.queryRepoDefAndEqptTree(gnlArvID);
	}
	
	/**
	 * 查询设备列表，不分页
	 */
	@RequestMapping("/queryEqptDgWithoutPage")
	@ResponseBody
	public PageInfo queryEqptDgWithoutPage(HttpServletRequest request,String repoID) throws Exception {
		return eqptService.queryEqptDgWithoutPage(repoID);
	}
	
	/**
	 * 根据ID查询设备信息
	 */
	@RequestMapping("/queryEqptByID")
	@ResponseBody
	public Map<String, Object> queryEqptByID(HttpServletRequest request, String eqptID) throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("Eqpt", eqptService.queryEqptByID(eqptID));
		return this.writeSuccMsg("",paraMap);
	}
	
	/**
	 * 根据设备ID，查找该设备，构造设备节数目的combobox
	 */
	@RequestMapping("/queryEqptPartCombobox")
	@ResponseBody
	public  JSONArray queryEqptPartCombobox(HttpServletRequest request,String eqptID) throws Exception {
		if(StringUtils.isBlank(eqptID)){
			throw new BusinessException("设备ID不能为空！");
		}
		return eqptService.queryEqptPartCombobox(eqptID);
	}
	
	/**
	 * 根据设备ID和设备的某一个节，构造设备层数目的combobox
	 */
	@RequestMapping("/queryEqptLevCombobox")
	@ResponseBody
	public  JSONArray queryEqptLevCombobox(HttpServletRequest request, String eqptID, String partNO)
			throws Exception{
		if(StringUtils.isBlank(eqptID)){
			throw new BusinessException("设备ID不能为空！");
		}
		if(StringUtils.isBlank(partNO)){
			throw new BusinessException("设备排号不能为空！");
		}
		return eqptService.queryEqptLevCombobox(eqptID, partNO);
	}
	
	/**
	 * 新增设备
	 */
	@RequestMapping("/addEqpt")
	@ResponseBody
	public Map<String, Object> addEqpt(HttpServletRequest request, @Valid Eqpt eqpt) throws Exception {
		if(eqpt.getPartNum() == null){
			throw new BusinessException("设备排数不能为空！");
		}
		if(eqpt.getLevNum() == null){
			throw new BusinessException("设备层数不能为空！");
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("ID", eqptService.addEqpt(eqpt));
		return this.writeSuccMsg("",paraMap);
	}
	
	/**
	 * 删除设备
	 */
	@RequestMapping("/deleteEqpt")
	@ResponseBody
	public Map<String, Object> deleteEqpt(String eqptID) throws Exception {
		if(StringUtils.isBlank(eqptID)){
			throw new BusinessException("设备ID不能为空！");
		}
		eqptService.deleteEqpt(eqptID);
		return this.writeSuccMsg("删除设备成功！");
	}
	
	/**
	 * 修改设备
	 */
	@RequestMapping("/modifyEqpt")
	@ResponseBody
	public Map<String, Object> modifyEqpt(HttpServletRequest request, @Valid Eqpt eqpt) throws Exception {
		if(eqpt.getPartNum() == null){
			throw new BusinessException("设备排数不能为空！");
		}
		if(eqpt.getLevNum() == null){
			throw new BusinessException("设备层数不能为空！");
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("ID", eqptService.modifyEqpt(eqpt));
		return this.writeSuccMsg("",paraMap);
	}
	
	
	//---------------------- 跳转页面
	/**
	 * 主菜单：设备管理页面
	 */
	@RequestMapping("/forwardEqptindex")
	public String forwardEqptindex(HttpServletRequest request, ModelMap map) throws Exception {
		map.put("allocateCell", "1");
		map.put("checkboxFlag", 1);
		return "repo/eqpt/eqptindex";
	}
	
	/**
	 * 入库--案卷
	 */
	@RequestMapping("/forwardEqptindex4Vol")
	public String forwardEqptindex4Vol(HttpServletRequest request, ModelMap map) throws Exception {
		map.put("entityType", EnumOrganizeModel.vol.getCode());
		return "repo/eqpt/eqptindex";	
	}
	
	
	
	/**
	 * 入库--档案--传人的参数的清点交接单id
	 */
	@RequestMapping("/forwardEqptindex4ArvWithArvIDs")
	public String forwardEqptindex4ArvWithArvIDs(HttpServletRequest request, ModelMap map,String arvIDs) throws Exception {
		map.put("entityIDs", arvIDs);
		map.put("entityType", EnumOrganizeModel.arv.getCode());
		return "repo/eqpt/eqptindex";	
	}
	
	/**
	 * 入库--档案
	 */
	@RequestMapping("/forwardEqptindex4Arv")
	public String forwardEqptindex4Arv(HttpServletRequest request, ModelMap map) throws Exception {
		map.put("entityType", EnumOrganizeModel.arv.getCode());
		return "repo/eqpt/eqptindex";	
	}
	
	/**
	 * 入库--件
	 */
	@RequestMapping("/forwardEqptindex4Piece")
	public String forwardEqptindex4Piece(HttpServletRequest request, ModelMap map) throws Exception {
		map.put("entityType", EnumOrganizeModel.piece.getCode());
		return "repo/eqpt/eqptindex";	
	}
	
	/**
	 * 新增设备页面
	 */
	@RequestMapping("/forwardAddEqpt")
	public String forwardAddEqpt(HttpServletRequest request) throws Exception {
		return "repo/eqpt/addeqpt";
	}
	
	/**
	 * 修改设备页面
	 */
	@RequestMapping("/forwardModifyEqpt")
	public String forwardModifyEqpt(HttpServletRequest request) throws Exception {
		return "repo/eqpt/modifyeqpt";
	}
	//----------------------end of跳转页面
	
}
