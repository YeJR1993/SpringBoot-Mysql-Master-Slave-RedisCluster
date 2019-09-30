package com.shanghai.modules.sys.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: YeJR
 * @version: 2018年9月11日
 * 百度地图
 */
@Controller
@RequestMapping(value = "baiduMap")
public class BaiduMapController {
	
	/**
	 * 打开百度地图页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "open")
	public String openBaiduMap(Model model, String longitude, String latitude, String address) {
		model.addAttribute("longitude", longitude);
		model.addAttribute("latitude", latitude);
		model.addAttribute("address", address);
		return "modules/global/baiduMap";
	}
	
	
}
