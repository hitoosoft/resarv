package com.hitoo.bas.repo.cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hitoo.bas.entity.Cell;
import com.hitoo.bas.repo.cell.service.CellService;
import com.hitoo.entity.arv.ArvBasInfo;
import com.hitoo.frame.base.BaseController;
import com.hitoo.frame.base.BusinessException;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.pub.model.TreeModel;

@Controller
@RequestMapping("/repo/cell")
public class CellContorller extends BaseController {
	
	@Autowired
	private CellService cellService;
	
	/**
	 * 根据设备ID查询单元格
	 */
	@RequestMapping("/queryCellByEqptID")
	@ResponseBody
	public Map<String, Object> queryCellByEqptID(HttpServletRequest request, String eqptID) throws Exception {
		if(StringUtils.isBlank(eqptID)){
			throw new BusinessException("设备ID不能为空！");
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("Cell", cellService.queryCellByEqptID(eqptID));
		return this.writeSuccMsg("", paraMap);
	}
	
	/**
	 * 根据设备ID查询排
	 */
	@RequestMapping("/queryCellPartNOByEqptID")
	@ResponseBody
	public List<TreeModel> queryCellPartNOByEqptID(HttpServletRequest request, String eqptID) throws Exception {
		if(StringUtils.isBlank(eqptID)){
			throw new BusinessException("设备ID不能为空！");
		}
		return cellService.queryCellPartNOByEqptID(eqptID);
	}
	
	/**
	 * 根据设备ID、排数查单元格
	 */
	@RequestMapping("/queryCellByEqptIDAndPartNO")
	@ResponseBody
	public Map<String, Object> queryCellByEqptIDAndPartNO(HttpServletRequest request, String eqptID, Integer partNO) throws Exception {
		if(StringUtils.isBlank(eqptID)){
			throw new BusinessException("设备ID不能为空！");
		}
		if(partNO==null || partNO<=0){
			throw new BusinessException("单元格排号不能为空，并且必须大于零！");
		}
		return this.writeSuccMsg("", cellService.queryCellByEqptIDAndPartNO(eqptID, partNO));
	}
	
	/**
	 * 更新格子实际容量
	 */
	@RequestMapping("/updateCellVolumn")
	@ResponseBody
	public Map<String, Object>  updateCellVolumn(HttpServletRequest request, String selectedCellIDs,Integer totalNum) throws Exception {
		if(StringUtils.isBlank(selectedCellIDs)){
			throw new BusinessException("格ID不能为空！");
		}
		cellService.updateCellVolumn(selectedCellIDs,totalNum);
		return this.writeSuccMsg("更新格子实际容量完成！");
	}
	
	
	
	/**
	 * 新增设备排
	 */
	@RequestMapping("/addEqptPartNO")
	@ResponseBody
	public void addEqptPartNO(HttpServletRequest request, String eqptID) throws Exception {
		if(StringUtils.isBlank(eqptID)){
			throw new BusinessException("设备ID不能为空！");
		}
		cellService.addEqptPartNO(eqptID);
	}
	
	/**
	 * 新增单元格
	 */
	@RequestMapping("/addCell")
	@ResponseBody
	public void addCell(HttpServletRequest request, @Valid Cell cell) throws Exception {
		if(StringUtils.isBlank(cell.getEqptID())){
			throw new BusinessException("设备ID不能为空！");
		}
		if(cell.getPartNO()==null||cell.getPartNO().intValue()<=0){
			throw new BusinessException("单元格排号不能为空，并且必须大于零！");
		}
		if(cell.getLevNO()==null||cell.getLevNO().intValue()<=0){
			throw new BusinessException("单元格层号不能为空，并且必须大于零！");
		}
		cellService.addCell(cell);
	}
	
	/**
	 * 删除单元格
	 */
	@RequestMapping("/deleteCell")
	@ResponseBody
	public Map<String, Object> deleteCell(HttpServletRequest request, String cellIDs) throws Exception {
		if(StringUtils.isBlank(cellIDs)){
			throw new BusinessException("要删除的单元格ID为空！");
		}
		cellService.deleteCell(cellIDs);
		return this.writeSuccMsg("删除单元格成功！");
	}
	
	/**
	 * 智能划分单元格
	 */
	@RequestMapping("/addAutoCell")
	@ResponseBody
	public Map<String, Object> addAutoCell(HttpServletRequest request, @Valid Cell cell) throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("ID", cellService.addAutoCell(cell));
		return this.writeSuccMsg("单元格划分成功！", paraMap);
	}
	
	/**
	 * 入库保存
	 */
	@RequestMapping("/saveInRepo")
	@ResponseBody
	public Map<String, Object> saveInRepo(HttpServletRequest request, String entityType ,String entityIDs, String cellIDs) throws Exception {
		if(StringUtils.isBlank(entityIDs) || StringUtils.isBlank(cellIDs)){
			throw new BusinessException("参数不合法");
		}
		cellService.saveInRepo(this.getLoginInfo(request), entityType, entityIDs, cellIDs);
		return this.writeSuccMsg("入库完成");
	}
	
	//---------------------- 跳转页面
	/**
	 * 新增单元格页面
	 */
	@RequestMapping("/forwardAddCell")
	public String forwardAddCell(HttpServletRequest request) throws Exception {
		return "repo/eqpt/addcell";	
	}
	
	/**
	 * 智能划分单元格页面
	 */
	@RequestMapping("/forwardCellAutoAdd")
	public String forwardCellAutoAdd(HttpServletRequest request) throws Exception {
		return "repo/eqpt/addautocell";
	}
	
	/**
	 * 根据单元格id，查询单元格内容
	 */
	@RequestMapping("/forwardShowCellContent")
	public String forwardShowCellContent(HttpServletRequest request,String cellID) throws Exception {
		PageInfo pi =cellService.queryCellContent(cellID);
		request.setAttribute("contentDg", JSON.toJSONString(pi));
		return "repo/eqpt/showcontent";
	}
	//---------------------- end of 跳转页面
}