package com.shanghai.modules.shop.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shanghai.common.datasource.ReadDataSource;
import com.shanghai.common.datasource.WriteDataSource;
import com.shanghai.common.persistence.BaseController;
import com.shanghai.common.utils.PageInfo;
import com.shanghai.common.utils.constant.SysConstants;
import com.shanghai.common.utils.excel.ExportExcel;
import com.shanghai.modules.shop.entity.ShopInfo;
import com.shanghai.modules.shop.service.ShopInfoService;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:24:23
* 商店基础信息
*/
@Controller
@RequestMapping(value = "shop/info")
public class ShopInfoController extends BaseController{
	
	@Autowired
	private ShopInfoService shopInfoService;
	
	/**
	 * list列表
	 * @param shopInfo
	 * @param model
	 * @return
	 */
	@ReadDataSource
	@RequestMapping(value = "list")
	@RequiresPermissions(value = "shop:list:shopInfo")
	public String list(ShopInfo shopInfo, Model model) {
		PageInfo<ShopInfo> page = shopInfoService.getShopInfoByPage(getPageNo(), getPageSize(), shopInfo);
		model.addAttribute("page", page);
		return "modules/shop/shopInfoList";
	}
	
	/**
	 * form表单
	 * @param shopInfo
	 * @param model
	 * @param urlType
	 * @return
	 */
	@ReadDataSource
	@RequestMapping(value = "form")
	@RequiresPermissions(value = {"shop:add:shopInfo", "shop:view:shopInfo", "shop:edit:shopInfo"}, logical = Logical.OR)
	public String form(ShopInfo shopInfo, Model model, Integer urlType) {
		if (shopInfo.getId() != null) {
			shopInfo = shopInfoService.getShopInfoById(shopInfo);
			model.addAttribute("shopInfo", shopInfo);
		}
		if (shopInfo.getUserId() != null) {
			shopInfo = shopInfoService.getShopInfoByUserId(shopInfo);
			if (shopInfo != null) {
				model.addAttribute("shopInfo", shopInfo);
			}
		}
		model.addAttribute("urlType", urlType);
		return "modules/shop/shopInfoForm";
	}
	
	/**
	 * 添加或者更新商家信息
	 * @param shopInfo
	 * @param redirectAttributes
	 * @param urlType 0:跳转至用户列表， 1：跳转至商店基础信息列表
	 * @return
	 */
	@WriteDataSource
	@RequestMapping(value = "save")
	@RequiresPermissions(value = {"shop:edit:shopInfo", "shop:add:shopInfo"}, logical = Logical.OR)
	public String save(ShopInfo shopInfo, RedirectAttributes redirectAttributes, Integer urlType) {
		if (shopInfo.getId() == null) {
			shopInfoService.saveShopInfo(shopInfo);
		} else {
			shopInfoService.updateShopInfo(shopInfo);
		}
		redirectAttributes.addFlashAttribute("msg", SysConstants.OPERATE_SUCCESS_PAGE_TIP);
		if (urlType == 0) {
			return "redirect:/sys/user/list";
		} 
		if (urlType == 1) {
			return "redirect:/shop/info/list";
		}
		return "";
	}
	
	/**
	 * 逻辑删除商家信息
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@WriteDataSource
	@RequestMapping(value = "delete")
	@RequiresPermissions(value = "shop:delete:shopInfo")
	public String delete(Integer id, RedirectAttributes redirectAttributes) {
		shopInfoService.deleteLogicById(id);
		redirectAttributes.addFlashAttribute("msg", SysConstants.OPERATE_SUCCESS_PAGE_TIP);
		return "redirect:/shop/info/list";
	}
	
	/**
	 * 批量删除商家信息
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@WriteDataSource
	@RequestMapping(value = "deleteAll")
	@RequiresPermissions(value = "shop:delete:shopInfo")
	public String deleteAll(Integer[] ids, RedirectAttributes redirectAttributes) {
		shopInfoService.deleteLogicByIds(ids);
		redirectAttributes.addFlashAttribute("msg", SysConstants.OPERATE_SUCCESS_PAGE_TIP);
		return "redirect:/shop/info/list";
	}
	
	/**
	 * 导出商家信息
	 * @param shopInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ReadDataSource
	@RequestMapping(value = "export")
	@RequiresPermissions(value="shop:export:shopInfo")
	public String export(ShopInfo shopInfo, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName = "商家数据" + System.currentTimeMillis() + ".xlsx";
        List<ShopInfo> list = shopInfoService.getAllShops(shopInfo);
 		new ExportExcel("商家数据", ShopInfo.class).setDataList(list).write(response, fileName).dispose();
		return null;
	}

}
