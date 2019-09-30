package com.shanghai.modules.shop.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shanghai.common.datasource.ReadDataSource;
import com.shanghai.common.persistence.BaseController;
import com.shanghai.common.utils.PageInfo;
import com.shanghai.modules.shop.entity.ShopOrder;
import com.shanghai.modules.shop.service.ShopOrderService;

/**
* @author：YeJR
* @version：2018年9月3日 下午4:25:13
* 商店订单
*/
@Controller
@RequestMapping(value = "shop/order")
public class ShopOrderController extends BaseController{

	@Autowired
	private ShopOrderService shopOrderService;
	
	/**
	 * list列表
	 * @param shopOrder
	 * @param model
	 * @return
	 */
	@ReadDataSource
	@RequestMapping(value = "list")
	@RequiresPermissions(value = "shop:list:shopOrder")
	public String list(ShopOrder shopOrder, Model model) {
		PageInfo<ShopOrder> page = shopOrderService.getShopOrderWithOutGoodsByPage(getPageNo(), getPageSize(), shopOrder);
		model.addAttribute("page", page);
		return "modules/shop/shopOrderList";
	}
	
	/**
	 * form表单
	 * @param shopOrder
	 * @param model
	 * @return
	 */
	@ReadDataSource
	@RequestMapping(value = "form")
	@RequiresPermissions(value = "shop:view:shopInfo")
	public String form(ShopOrder shopOrder, Model model) {
		shopOrder = shopOrderService.getShopOrderByIdOrNumber(shopOrder);
		model.addAttribute("shopOrder", shopOrder);
		return "modules/shop/shopOrderForm";
	}
}
