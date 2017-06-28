package com.hitoo.bas.repo.cell.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Component;

import com.hitoo.entity.arv.ArvBasInfo;
import com.hitoo.entity.arv.ArvVolInfo;
import com.hitoo.enumdic.EnumOrganizeModel;
import com.hitoo.bas.entity.Cell;
import com.hitoo.bas.entity.CellNo;
import com.hitoo.bas.entity.Eqpt;
import com.hitoo.bas.entity.Gnlarv;
import com.hitoo.bas.entity.Repo;
import com.hitoo.bas.entity.CellArvtype;
import com.hitoo.bas.repo.cell.EqptPart;
import com.hitoo.frame.base.CommonDao;
import com.hitoo.frame.common.util.BeanUtil;
import com.hitoo.frame.pub.model.PageInfo;
import com.hitoo.frame.base.BusinessException;

@Component
public class CellDao extends CommonDao {
	
	/**
	 * 查询某个分类未使用的单元格
	 */
	@SuppressWarnings("unchecked")
	public Cell getOneUnusedCellByArvTypeID(String arvTypeID)throws Exception {
		String sql=" select cell.*    from REPO_CELL_ARVTYPE  rel,REPO_CELL cell  "
				+ " where 1=1  "
				+ " and rel.cellid = cell.cellid"
				+ " and  cell.totalnum >0   "
				+ " and ( cell.usednum is null  or  (cell.totalnum - cell.usednum) >0 )"
				+ " and rel.arvtypeid = :arvTypeID"
				+ " order by cell.eqptid,cell.partno,cell.levno,cell.cellno ";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("arvTypeID", arvTypeID);
		List<Cell> unUsedCells = query.addEntity(Cell.class).list();
		if(unUsedCells==null||unUsedCells.size()<=0){
			throw new BusinessException("无可用库房单元格，请联系管理员分配！");
		}
		Cell oneCell = unUsedCells.get(0);
		return oneCell;
	}
	
	/**
	 * 查询某个分类未使用的单元格
	 */
	@SuppressWarnings({ "unchecked"})
	public CellNo getFirstUnusedCellNoBycellID(String cellID)throws Exception {
		String sql=" select REPO_CELL_NO.*   from REPO_CELL_NO"
				+ " where existflag = '0'  "
				+ " and cellid = :cellID"
				+ " order by seqno";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("cellID", cellID);
		List<CellNo> unUsedCellNos = query.addEntity(CellNo.class).list();
		if(CollectionUtils.isEmpty(unUsedCellNos)){
			return null;
		}else{
			return unUsedCellNos.get(0);
		}

	}
	/**
	 * 归还单元格
	 */
	public void returnBackCell(String cellID)throws Exception {
		Cell cell=this.findEntityByID(Cell.class, cellID);
		if(cell!=null){
			if(cell.getUsedNum()==null||cell.getUsedNum()<=0){
				cell.setUsedNum(0);
			}else{
				cell.setUsedNum(cell.getUsedNum()-1);
			}
			
		}
	}
	
	/**
	 * 查询某个分类已经分配的库房单元格
	 */
	@SuppressWarnings("unchecked")
	public List<String> queryCellByArvTypeID(String arvTypeID)throws Exception {
		String sql=" select distinct cellid from REPO_CELL_ARVTYPE where arvtypeid = :arvTypeID";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("arvTypeID", arvTypeID);
		query.addEntity(CellArvtype.class);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public String queryArvTypeIDRepoDescr(String arvTypeID)throws Exception {
		String sql=" select reponam||'---'||eqptnam||'---'||partno||'排'from ( "
				+ " select distinct  def.repoid,def.reponam ,eq.eqptid,eq.eqptnam,cell.partno"
				+ "  from REPO_CELL_ARVTYPE  rel,REPO_CELL cell ,REPO_EQPT eq ,REPO_DEF def"
				+ "  where 1=1 "
				+ " and rel.cellid = cell.cellid and cell.eqptid = eq.eqptid and eq.repoid = def.repoid"
				+ " and rel.arvtypeid = :arvTypeID "
				+ " order by def.repoid ,eq.eqptid,cell.partno"
				+ " ) t";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("arvTypeID", arvTypeID);
		List<String> descrList=query.list();
		String descr="";
		for(String str:descrList){
			if(descr.length()>0){
				descr += ",<br>";
			}
			descr += str;
		}
		return descr;
	}
	
	/**
	 * 构建可视化库房的单元格组成，通过checkbox或者radiobox
	 */
	public Map<String, Object> BuildCellByHtmlByEqptIDAndPartNO(String eqptID, Integer partNO, String arvTypeID) throws Exception {
		List<String> checkedCellIDList = null;
		if(StringUtils.isNotBlank(arvTypeID)){
			checkedCellIDList = queryCellByArvTypeID(arvTypeID);
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		List<Cell> cellList = this.queryCellListByEqptIDAndPartNO(eqptID, partNO);
		Map<Integer ,List<Cell>> floorCellMap = new HashMap<Integer ,List<Cell>>();
		Map<Integer ,List<Cell>> levCellMap = new HashMap<Integer ,List<Cell>>();
		for(Cell cell:cellList){
			if(levCellMap.keySet().contains(cell.getLevNO())){
				levCellMap.get(cell.getLevNO()).add(cell);
			}else{
				List<Cell> tmp=new ArrayList<Cell>();
				tmp.add(cell);
				levCellMap.put(cell.getLevNO(), tmp);
			}
		}
		
		int MAXCELL=0;//该设备的最大层数
		//该设备的每一列，格数量
		List<Map<String, Object>> levMaxCellMap = this.queryMaxCellNumerByEqptIDAndPartNO(eqptID, partNO);
		for(Map<String, Object> tmp : levMaxCellMap){
			int cellNOofthisLev = ((BigDecimal)tmp.get("MAXCELLCOUNTINLEV")).intValue();
			if(cellNOofthisLev > MAXCELL){
				MAXCELL = cellNOofthisLev;
			}
		}
		StringBuilder sb_html=new StringBuilder();
		StringBuilder sb_allcellid=new StringBuilder();//记录该设备的所有id集合
		for(int i = 1; i <= MAXCELL; i ++){
			StringBuilder sb_onetd_html = new StringBuilder();
			StringBuilder sb_onetdcellid = new StringBuilder();//记录一行的所有cellid集合
			for(Integer levNO : levCellMap.keySet()){
				int size = levCellMap.get(levNO).size();
				if(i <= size){
					Cell currCell=levCellMap.get(levNO).get(i-1);
					String cellID=currCell.getCellID();
					if(i == size && size < MAXCELL){
						String tmpStr = oneCellTDWithCheckbox(MAXCELL - size + 1, levNO, i, currCell, checkedCellIDList);
						sb_onetd_html.append(tmpStr);
					}else{
						String tmpStr = oneCellTDWithCheckbox(1, levNO, i, currCell, checkedCellIDList);
						sb_onetd_html.append(tmpStr);
					}
					
					List<Cell> floorCellList = floorCellMap.get(levNO);
					if(floorCellList == null){
						floorCellList = new ArrayList<Cell>();
						floorCellMap.put(levNO, floorCellList);
					}
					floorCellList.add(levCellMap.get(levNO).get(i-1));//第n列的数据
					
					if(sb_allcellid.length()>0){
						sb_allcellid.append(",");
					}
					sb_allcellid.append(cellID);
					
					if(sb_onetdcellid.length()>0){
						sb_onetdcellid.append(",");
					}
					sb_onetdcellid.append(cellID);
				}
				
			}
			sb_onetd_html.append("</tr>");
			sb_onetd_html.insert(0, "<td style='border:solid #66CDAA; border-width:0px 1px 1px 0px; padding:10px;'>"
					+ "<div style='margin-left: auto;margin-right: auto;'><input type='checkbox' onclick='cellrowspan_click(this);'  "+" value='"+sb_onetdcellid.toString()+"'/></div></td>");
			sb_onetd_html.insert(0,"<tr>");
			
			//把行数据添加
			sb_html.append(sb_onetd_html);
		}
		
		if(sb_html.length()>0){
			sb_html.append("</table>");
			//分配库房位置描述
			String checkboxFirstRow="<tr>";
			checkboxFirstRow += "<td style='border:solid #66CDAA; border-width:0px 1px 1px 0px;' nowrap='nowrap'>"
				+ "<div style='margin-left: auto;margin-right: auto;'><input type='checkbox' name='red_allpartcell'  onclick='cellrowspan_click(this);' " + " value='" + sb_allcellid.toString() + "'/>全选</td>";
			for(Integer levNO : levCellMap.keySet()){
				StringBuilder sb_onelevcellid = new StringBuilder();
				for(Cell cell : floorCellMap.get(levNO)){
					if(sb_onelevcellid.length()>0){
						sb_onelevcellid.append(",");
					}
					sb_onelevcellid.append(cell.getCellID());
				}
				checkboxFirstRow += "<td style='border:solid #66CDAA; border-width:0px 1px 1px 0px; padding:10px;'>"+
					"<div style='margin-left: auto;margin-right: auto;'><input type='checkbox'  onclick='cellrowspan_click(this);' "+" value='"+sb_onelevcellid.toString()+"'/></td>";
			}
			checkboxFirstRow += "</tr>";
			sb_html.insert(0, checkboxFirstRow);
			sb_html.insert(0, "<table style='border:solid #66CDAA; border-width:1px 0px 0px 1px;' >");
		}
		if(StringUtils.isNotBlank(arvTypeID)){
			String allocateDescr= this.queryArvTypeIDRepoDescr(arvTypeID);
			if(StringUtils.isNotBlank(allocateDescr)){
				sb_html.append("<br><span style='color:blue;size:10px;'>已分配的库房架位：<br>"+allocateDescr+"</span>");
			}
		}
		paraMap.put("celltableHtml", sb_html.toString());
		return paraMap;
	}
	
	private String oneCellTD(int rowspan, Integer levNO, int cellCount, String cellID, Cell cell){
		String name = levNO + "列" + cellCount + "层";
		String volumn = "<br>" + buildVolumn(cell);
		name += volumn;
		String cellHtmlContent = "<td nowrap='nowrap' style='border:solid #66CDAA; border-width:0px 1px 1px 0px; padding:10px;' valign='top' rowspan='" + rowspan + "'>";
		final String final_cont = "<div style='display: inline-block;text-align:center;margin: 0px auto;'>"
				+ "<div style='margin-left: auto;margin-right: auto;'><img src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAt1JREFUeNpsk91LFFEYxp/5np1182tTdxM/iVDTiMqoiygECSnIPyAICynosiAv6qIuDEPqNknBvAiC6DZUikRSCwONTPFjNdPU/VJXZ9fdmTm9M2lIeuC5OGfO8zvnfc47HPYZ114MfS88mF3OOAH6loGFUGTs1a3TFf/vY4ztNV9s6e16OxJjwyuM9cwx9maCsWcDJqtr6X23H4DbmTS0fx4tyvFWWnTq6uYWfociyPflQ9+MwK3w8GgeRGJxxOLxbx0Nx6t2AOKVpx+bk4Zx7/LJMhT40hCOA+ukQDAJtybDCw0l+ZmIJmidNB82K+ue9DJJFB8To4m71PqBVZeXYDmScoiKqiIej8Al8tBUDcF1HRvxJHK9OZAUFZrnAER9ASPT83h9+xwnprsEXChzIyPtAMaCEkJ0eqapodif5dwmSpoNJqDIPIq8Ek7k6VR3Jsbnfjmli2u6iZcDEfjSoqg5moEj2R509iyjbzwCWVEgqy5kp6uor2BIJWPomzARTkgIb6T+AngecLtcCCVS6BqMotibwNWaQnAcjx9hBad8G1SahU/TKSyuWpAlES5FAr8dvkhZ0oLikBRJxtK6gfb+EEpz01FfpeL9JI9QbIu+c3CpsrPPhuAfwA5OVp0TLcuCxCSYpomZlRglTk+qG1BlETwBBDLbkmV5F4AiUahOXhDIaMAyLVgCo7kF3RAIbhs5AvCOBEFwAuW2O4jfDC90BgJTTkdpLo2uqVJwChQqK25wkOi6kiQ5c7sEqhhDo5PQw4udNsD2HSLlnb/b0Xj42NlGv89PDeRGyrBQmsUhyQQCAGuUw0RgDsOD/W19rdfbyLNEfbPA7Wptf7q/tKT6RvP93PzS2oKCEpQVZmB1TcfXsQn8DEx1f2lverS+OD1Dexf3/Avbw36dQk9ekf/MzdYHclpG7VYs2j34/M7D2NKsbZojWfv+TPuAikiZpChpdrdxN+CPAAMAgHgn4lmxXosAAAAASUVORK5CYII='><br>";
		cellHtmlContent = cellHtmlContent + final_cont + "<input type='radio' name='red' value='" + cellID + "'/>" + name + "</div></div></td>";
		return cellHtmlContent;
	}
	
	private String buildVolumn(Cell cell) {
		if(null == cell.getTotalNum()){
			return "[容量未定义]";
		}
		if(0 == cell.getTotalNum()){
			return "[剩余容量：0]";
		}
		int usedNum = 0;
		if(null != cell.getUsedNum()){
			usedNum = cell.getUsedNum();
		}
		return "[剩余容量：" + String.valueOf(cell.getTotalNum() - usedNum) + "]";
	}
		
	private String oneCellTDWithCheckbox(int rowspan, Integer levNO, int cellCount, Cell cell, List<String> checkedCellIDList){
		String cellID=cell.getCellID();
		String cellChecked="";
		if(checkedCellIDList!=null&&checkedCellIDList.contains(cellID)){
			cellChecked = " checked ";
		}
		String name = levNO + "列" + cellCount + "层";
		String volumn =  "<br>[容量:" + (cell.getTotalNum()==null?0:cell.getTotalNum())+"-"+(cell.getUsedNum()==null?0:cell.getUsedNum())+"]";
		name= name + volumn;
		String cellHtmlContent = "<td nowrap='nowrap' style='border:solid #66CDAA; border-width:0px 1px 1px 0px; padding:10px;' valign='top' rowspan='" + rowspan + "'>";
		final String final_cont = "<div style='display: inline-block;text-align:center;margin: 0px auto;'>"
				+ "<div style='margin-left: auto;margin-right: auto;'><img src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAt1JREFUeNpsk91LFFEYxp/5np1182tTdxM/iVDTiMqoiygECSnIPyAICynosiAv6qIuDEPqNknBvAiC6DZUikRSCwONTPFjNdPU/VJXZ9fdmTm9M2lIeuC5OGfO8zvnfc47HPYZ114MfS88mF3OOAH6loGFUGTs1a3TFf/vY4ztNV9s6e16OxJjwyuM9cwx9maCsWcDJqtr6X23H4DbmTS0fx4tyvFWWnTq6uYWfociyPflQ9+MwK3w8GgeRGJxxOLxbx0Nx6t2AOKVpx+bk4Zx7/LJMhT40hCOA+ukQDAJtybDCw0l+ZmIJmidNB82K+ue9DJJFB8To4m71PqBVZeXYDmScoiKqiIej8Al8tBUDcF1HRvxJHK9OZAUFZrnAER9ASPT83h9+xwnprsEXChzIyPtAMaCEkJ0eqapodif5dwmSpoNJqDIPIq8Ek7k6VR3Jsbnfjmli2u6iZcDEfjSoqg5moEj2R509iyjbzwCWVEgqy5kp6uor2BIJWPomzARTkgIb6T+AngecLtcCCVS6BqMotibwNWaQnAcjx9hBad8G1SahU/TKSyuWpAlES5FAr8dvkhZ0oLikBRJxtK6gfb+EEpz01FfpeL9JI9QbIu+c3CpsrPPhuAfwA5OVp0TLcuCxCSYpomZlRglTk+qG1BlETwBBDLbkmV5F4AiUahOXhDIaMAyLVgCo7kF3RAIbhs5AvCOBEFwAuW2O4jfDC90BgJTTkdpLo2uqVJwChQqK25wkOi6kiQ5c7sEqhhDo5PQw4udNsD2HSLlnb/b0Xj42NlGv89PDeRGyrBQmsUhyQQCAGuUw0RgDsOD/W19rdfbyLNEfbPA7Wptf7q/tKT6RvP93PzS2oKCEpQVZmB1TcfXsQn8DEx1f2lverS+OD1Dexf3/Avbw36dQk9ekf/MzdYHclpG7VYs2j34/M7D2NKsbZojWfv+TPuAikiZpChpdrdxN+CPAAMAgHgn4lmxXosAAAAASUVORK5CYII='><br>";
		cellHtmlContent = cellHtmlContent + final_cont + "<input type='checkbox' name='red' " + cellChecked + "value='" + cellID + "'/>" + name + "</div></div></td>";
		return cellHtmlContent;
	}
		
	/**
	 * 获取库房位置描述
	 */
	public String queryRepoCellInfo(String cellID) throws Exception {
		String descr="";
		if(StringUtils.isNotBlank(cellID)){
			Cell cell= this.findEntityByID(Cell.class, cellID);
			if(cell!=null){
				//找到设备
				Eqpt eqpt = this.findEntityByID(Eqpt.class, cell.getEqptID());
				Repo repo = this.findEntityByID(Repo.class, eqpt.getRepoID());
				descr = repo.getRepoNam() + "， " +eqpt.getEqptNam()+"， 第"
				+cell.getPartNO()+"排 "+cell.getLevNO()+"列 "+cell.getCellNO()+"层";
			}
		}
		return descr;
	}
	
	/**
	 * 获取库房位置描述<br>
	 * 烟台开发区的需求，目前对库房的设备、排、列、层、节等概念没有达成共识，后续再行改进
	 */
	public String queryRepoCellInfo(Cell cell, ArvBasInfo arv) throws Exception {
		String descr = "";
		if(null != cell){
			Eqpt eqpt = this.findEntityByID(Eqpt.class, cell.getEqptID());
			Repo repo = this.findEntityByID(Repo.class, eqpt.getRepoID());
			//获得档案的所在单元格的序号
			if("烟台开发区".equals(arv.getGnlArvID())){
				descr = repo.getRepoNam() + " | " + cell.getPartNO() + " 架| " + cell.getCellNO() + " 层| " + cell.getLevNO() + " 节";
				if(StringUtils.isNotBlank(arv.getCellnoID())){
					CellNo cellNo = findEntityByID(CellNo.class, arv.getCellnoID());
					descr += "| " + cellNo.getSeqNO();
				}
			}else{
				descr = repo.getRepoNam() + " | " + cell.getPartNO() + " 排| " + cell.getLevNO() + " 列| " + cell.getCellNO() + " 层";
				if(StringUtils.isNotBlank(arv.getCellnoID())){
					CellNo cellNo = findEntityByID(CellNo.class, arv.getCellnoID());
					descr += "| " + cellNo.getSeqNO();
				}
			}
		}
		return descr;
	}
	
	/**
	 * 验证设备是否已经划分单元格
	 */
	@SuppressWarnings("unchecked")
	public Cell queryCountOfCellByEqptID(String eqptID) throws Exception {
		String sqlStr = " select * from REPO_CELL where eqptID =:eqptID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("eqptID", eqptID);
		query.addEntity(Cell.class);
		List<Cell> cellList=query.list();
		if(cellList!=null&&cellList.size()>0){
			return cellList.get(0);
		}
		return null;
	}
	
	/**
	 * 根据设备ID，设备的排号和列号，查看该排最大的层号
	 */
	@SuppressWarnings("unchecked")
	public Integer queryMaxCellNO(String eqptID, Integer partNO, Integer levNO) throws Exception {
		String sqlStr = " select max(cellNO) from REPO_CELL where EQPTID= :eqptID and PARTNO = :partNO and LEVNO = :levNO ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("eqptID", eqptID);
		sqlQuery.setInteger("levNO", levNO);
		List<BigDecimal> cellNOList = sqlQuery.list();
		if(cellNOList!=null&&cellNOList.size()>0&&cellNOList.get(0)!=null){
			return cellNOList.get(0).intValue();
		}
		return 0;
	}
	
	/**
	 * 根据设备ID，设备排号，查询设备最大列号
	 */
	@SuppressWarnings("unchecked")
	public Integer queryMaxLevNO(String eqptID, Integer partNO) throws Exception {
		String sqlStr = " select max(levNO) from REPO_CELL where eqptID= :eqptID and partNO = :partNO ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("eqptID", eqptID);
		sqlQuery.setInteger("partNO", partNO);
		List<BigDecimal> levNOList = sqlQuery.list();
		if(levNOList!=null&&levNOList.size()>0&&levNOList.get(0)!=null){
			return levNOList.get(0).intValue();
		}
		return 0;
	}
	
	/**
	 * 根据设备ID查询排
	 */
	@SuppressWarnings("unchecked")
	public List<EqptPart> queryCellPartNOByEqptID(String eqptID) throws Exception {
		String sqlStr = " select distinct partNO from REPO_CELL where eqptID = :eqptID ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("eqptID", eqptID);
		List<Map<String, Object>> list = sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return BeanUtil.getBeanListFromMap(list, EqptPart.class);
	}
	
	/**
	 * 根据设备ID、排数查单元格
	 */
	@SuppressWarnings("unchecked")
	private List<Cell> queryCellListByEqptIDAndPartNO(String eqptID, Integer partNO) throws Exception {
		String hql = "from Cell where eqptID = :eqptID and partNO = :partNO order by levNO,cellNO";
		Query query = getCurrentSession().createQuery(hql);
		query.setString("eqptID", eqptID);
		query.setInteger("partNO", partNO);
		return query.list();
	}
	
	/**
	 * 根据设备ID、排数，查单元格最大层数
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryMaxCellNumerByEqptIDAndPartNO(String eqptID, Integer partNO) throws Exception {
		String sqlStr = " select LEVNO,COUNT( DISTINCT CELLNO) AS MAXCELLCOUNTINLEV from REPO_CELL "
				+ " where eqptID = :eqptID and partNO = :partNO GROUP by LEVNO order by LEVNO ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("eqptID", eqptID);
		query.setInteger("partNO", partNO);
		List<Map<String, Object>> datalist = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return datalist;
	}
	
	/**
	 * 根据设备ID查询设备中最大排号
	 */
	@SuppressWarnings("unchecked")
	public Integer queryMaxPartNO(String eqptID) throws Exception {
		String sqlStr = " select max(partNO) from REPO_CELL where eqptID = :eqptID ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("eqptID", eqptID);
		List<BigDecimal> partNOList = sqlQuery.list();
		if(partNOList!=null&&partNOList.size()>0&&partNOList.get(0)!=null){ //查询数据为空时，返回是[null]
			return partNOList.get(0).intValue();
		}
		return 0;
	}
	
	/**
	 * 验证单元格中是否有档案
	 */
	@SuppressWarnings("unchecked")
	public ArvBasInfo queryFirstArvtypeUsingCellID(String cellID) throws Exception {
		String sqlStr = " select * from ARV_BASINFO where cellID = :cellID ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("cellID", cellID);
		List<ArvBasInfo> arvBasInfo = sqlQuery.list();
		if(arvBasInfo!=null&&arvBasInfo.size()>0){
			return arvBasInfo.get(0);
		}
		return null;
	}
	
	/**
	 * 验证单元格中是否有档案
	 */
	@SuppressWarnings("unchecked")
	public ArvVolInfo queryFirstVolUsingCellID(String cellID) throws Exception {
		String sqlStr = " select * from ARV_VOLINFO where cellID = :cellID ";
		SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.setString("cellID", cellID);
		List<ArvVolInfo> arvVolInfo = sqlQuery.list();
		if(arvVolInfo!=null&&arvVolInfo.size()>0){
			return arvVolInfo.get(0);
		}
		return null;
	}

	/**
	 * 删除库房单元格和档案分类的关系
	 */
	public void deleteCellArvType(String arvTypeID, String eqptID, String partNO) throws Exception {
		String sql = "delete from REPO_CELL_ARVTYPE where ARVTYPEID = :arvTypeID " +
				"and CELLID in(select CELLID from REPO_CELL where EQPTID = :eqptID and PARTNO = :partNO)";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("arvTypeID", arvTypeID);
		query.setString("eqptID", eqptID);
		query.setInteger("partNO", Integer.parseInt(partNO));
		query.executeUpdate();
	}
		
	@SuppressWarnings("unchecked")
	public List<Cell> queryCellByIDs(String cellIDs) throws Exception {
		String[] cellIDArr = cellIDs.split(",");
		List<String> cellIDList = new ArrayList<String>();
		for (int i = 0; i < cellIDArr.length; i++) {
			cellIDList.add(cellIDArr[i]);
		}
		String hql = "from Cell where cellID in (:cellID) order by partNO,levNO,cellNO";
		Query query = getCurrentSession().createQuery(hql);
		query.setParameterList("cellID", cellIDList);
		return query.list();
	}
	
	//根据传入的表格的ID查询可用的表格中的序号
	@SuppressWarnings("unchecked")
	public List<CellNo> queryCellNoByCells(List<Cell> cellList) throws Exception {
		List<CellNo> list = new ArrayList<CellNo>();
		for(int i = 0; i < cellList.size(); i ++){
			Cell cell = cellList.get(i);
			String hql = "from CellNo where cellID = :cellID order by seqno";
			Query query = getCurrentSession().createQuery(hql);
			query.setString("cellID", cell.getCellID());
			List<CellNo> noList = query.list();
			list.addAll(noList);
		}
		return list;
	}
	/**
	 * 根据单元格ID查询单元格顺序号
	 */
	public int queryCellNoByCellID(String cellID) throws Exception {
		String sql = " select count(*) num from REPO_CELL_NO where cellID = :cellID";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("cellID", cellID);
		int count = (Integer) query.addScalar("num", IntegerType.INSTANCE).uniqueResult();
		return count;
	}
	
	/**
	 * 删除库房单元格中的顺序号
	 */
	public void deleteCellNoByCellID(String cellID) throws Exception {
		String sql = "delete from REPO_CELL_NO where cellID = :cellID ";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("cellID", cellID);
		query.executeUpdate();
	}
	
	/**
	 * 清除库房单元格和档案分类的关系
	 */
	public void clearAllCellByArvTypeID(String arvTypeID) throws Exception {
		String sql=" delete from REPO_CELL_ARVTYPE where arvtypeid = :arvTypeID";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.setString("arvTypeID", arvTypeID);
		query.executeUpdate();
	}
	
	/**
	 * 查询单元格的内容
	 */
	@SuppressWarnings("unchecked")
	public PageInfo queryCellContent(String cellID) throws Exception {

		String sqlStr=" select VOLID AS PK, volmod as TYPE,volno AS TYPENO,volnam AS TYPENAM from arv_volinfo where cellid = :cellID "
				+ " union "
				+ " select  ARVID AS PK,:arv_type_flag AS TYPE,arvno AS TYPENO,arvnam AS TYPENAM from arv_basinfo where cellid = :cellID and volid is null ";
		SQLQuery query = getCurrentSession().createSQLQuery(sqlStr);
		query.setString("cellID", cellID);
		query.setString("arv_type_flag", EnumOrganizeModel.arv.getCode());
		List<Map<String, Object>> ls = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		PageInfo pi = new PageInfo();
		pi.setTotal(""+ls.size());
		pi.setRows(ls);
		return pi;
	}
}