package com.shanghai.modules.shop.web;

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
import com.shanghai.modules.shop.entity.ShopGoods;
import com.shanghai.modules.shop.service.ShopGoodsService;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:22:43
* 商店商品
*/
@Controller
@RequestMapping(value = "shop/goods")
public class ShopGoodsController extends BaseController{
	
	@Autowired
	private ShopGoodsService shopGoodsService;

	/**
	 * list列表
	 * @param shopGoods
	 * @param model
	 * @return
	 */
	@ReadDataSource
	@RequestMapping(value = "list")
	@RequiresPermissions(value = "shop:list:shopGoods")
	public String list(ShopGoods shopGoods, Model model) {
		PageInfo<ShopGoods> page = shopGoodsService.getShopGoodsByPage(getPageNo(), getPageSize(), shopGoods);
		model.addAttribute("page", page);
		return "modules/shop/shopGoodsList";
	}
	
	/**
	 * form表单
	 * @param shopGoods
	 * @param model
	 * @param urlType
	 * @return
	 */
	@ReadDataSource
	@RequestMapping(value = "form")
	@RequiresPermissions(value = {"shop:add:shopGoods", "shop:view:shopGoods", "shop:edit:shopGoods"}, logical = Logical.OR)
	public String form(ShopGoods shopGoods, Model model, Integer urlType) {
		if (shopGoods.getId() != null) {
			shopGoods = shopGoodsService.getByShopGoodsId(shopGoods);
			model.addAttribute("shopGoods", shopGoods);
		}
		model.addAttribute("urlType", urlType);
		return "modules/shop/shopGoodsForm";
	}
	
	/**
	 * 保存商店商品
	 * @param shopGoods
	 * @param redirectAttributes
	 * @param urlType
	 * @return
	 */
	@WriteDataSource
	@RequestMapping(value = "save")
	@RequiresPermissions(value = {"shop:edit:shopGoods", "shop:add:shopGoods"}, logical = Logical.OR)
	public String save(ShopGoods shopGoods, RedirectAttributes redirectAttributes, Integer urlType) {
		if (shopGoods.getId() != null) {
			shopGoodsService.updateShopGoods(shopGoods);
		} else {
			shopGoodsService.saveShopGoods(shopGoods);
		}
		redirectAttributes.addFlashAttribute("msg", SysConstants.OPERATE_SUCCESS_PAGE_TIP);
		if (urlType == 0) {
			return "redirect:/shop/info/list";
		}
		if (urlType == 1) {
			return "redirect:/shop/goods/list";
		}
		return "";
	}
	
	/**
	 * 单个删除
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@WriteDataSource
	@RequestMapping(value = "delete")
	@RequiresPermissions(value = "shop:delete:shopGoods")
	public String delete(Integer id, RedirectAttributes redirectAttributes) {
		shopGoodsService.deleteShopGoodsById(id);
		redirectAttributes.addFlashAttribute("msg", SysConstants.OPERATE_SUCCESS_PAGE_TIP);
		return "redirect:/shop/goods/list";
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@WriteDataSource
	@RequestMapping(value = "deleteAll")
	@RequiresPermissions(value = "shop:delete:shopGoods")
	public String deleteAll(Integer[] ids, RedirectAttributes redirectAttributes) {
		shopGoodsService.deleteShopGoodsByIds(ids);
		redirectAttributes.addFlashAttribute("msg", SysConstants.OPERATE_SUCCESS_PAGE_TIP);
		return "redirect:/shop/goods/list";
	}
}
