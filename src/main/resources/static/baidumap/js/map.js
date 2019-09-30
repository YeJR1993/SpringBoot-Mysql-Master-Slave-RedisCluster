// 百度地图， 位置， 标记  对象
var geoc, map, point, marker, longitude, latitude;

$(function() {
	// 对默认的值进行处理
	$("#longitude").val($("#lng").val());
	$("#latitude").val($("#lat").val());
	
	// 创建点坐标
	if ($("#lng").val() != "" && $("#lat").val() != "") {
		longitude = parseFloat($("#lng").val());
		latitude = parseFloat($("#lat").val());
		point = new BMap.Point(longitude, latitude);
	} else {
		point = new BMap.Point(116.404, 39.915);
	}
	
	// 百度地图转换对象
	geoc = new BMap.Geocoder();
	// 创建地图实例
	map = new BMap.Map("map");
	

	// 初始化地图，设置中心点坐标和地图级别
	map.centerAndZoom(point, 15);
	// 开启鼠标滚轮缩放
	map.enableScrollWheelZoom(true);
	// 创建标注
	marker = new BMap.Marker(point);
	// 添加覆盖物
	map.addOverlay(marker);

	// 单击获取点击的经纬度
	map.addEventListener("click", function(e) {
		longitude = e.point.lng;
		latitude = e.point.lat;
		// 通过坐标获取地址，并放入对应位置
		pointToAddress(longitude, latitude)
	});


	// 监听input地址框
	// 建立一个自动完成的对象
	var ac = new BMap.Autocomplete({
		"input" : "address",
		"location" : map
	});
	// 放入默认地址
	ac.setInputValue($("#addr").val());
	// 鼠标放在下拉列表上的事件
	ac.addEventListener("onhighlight", function(e) {
		var str = "";
		var _value = e.fromitem.value;
		var value = "";
		if (e.fromitem.index > -1) {
			value = _value.province + _value.city + _value.district + _value.street + _value.business;
		}
		str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

		value = "";
		if (e.toitem.index > -1) {
			_value = e.toitem.value;
			value = _value.province + _value.city + _value.district + _value.street + _value.business;
		}
		str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
		$("#searchResultPanel").innerHTML = str;
	});

	// 鼠标点击下拉列表后的事件
	ac.addEventListener("onconfirm", function(e) {
		var _value = e.item.value;
		var myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
		$("#searchResultPanel").innerHTML = "onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
		// 地址转坐标
		addressToPoint(myValue);
	});

	// 经纬度input框监听
	$('#longitude').bind('input propertychange', function() {  
		var lng = $("#longitude").val();
		var lat = $("#latitude").val();
		if (lng != "" && lat != "") {
			pointToAddress(lng, lat);
		}
	});
	$('#latitude').bind('input propertychange', function() {  
		var lng = $("#longitude").val();
		var lat = $("#latitude").val();
		if (lng != "" && lat != "") {
			pointToAddress(lng, lat);
		}
	});
	
	// 当页面控件只有一个地址时
	if ($("#lng").val() == "" && $("#lat").val() == "") {
		if ($("#addr").val() != "") {
			addressToPoint($("#addr").val());
		} 
	}
	
	// 当页面控件只有经纬控件时
	if ($("#lng").val() != "" && $("#lat").val() != "") {
		if ($("#addr").val() == "") {
			pointToAddress(longitude, latitude);
		} 
	}
});

/**
 * 坐标获取具体地址
 * 
 * @param point
 * @returns
 */
function pointToAddress(longitude, latitude) {
	geoc.getLocation(new BMap.Point(longitude, latitude), function(result) {
		// addressComponents对象可以获取到详细的地址信息
		var addComp = result.addressComponents;
		var site = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
		// 移除覆盖物
		map.clearOverlays();
		// 重新创建并添加覆盖物
		point = new BMap.Point(longitude, latitude);
		marker = new BMap.Marker(point);
		map.addOverlay(marker);
		// 地图移动到点击的点
		map.panTo(point);
		// 将对应的HTML元素设置值
		$("#longitude").val(longitude);
		$("#latitude").val(latitude);
		$("#address").val(site);
	});
}

/**
 * 地址转坐标
 * @param address
 * @returns
 */
function addressToPoint(address) {
	// 回调方法
	function search(){
		//清除地图上所有覆盖物
		map.clearOverlays();   
		//获取第一个智能搜索的结果
		point = local.getResults().getPoi(0).point;
		longitude = point.lng;
		latitude = point.lat;
		marker = new BMap.Marker(point);
		map.addOverlay(marker);
		// 地图移动到点击的点
		map.panTo(point);
		// 放入对应的元素
		$("#longitude").val(longitude);
		$("#latitude").val(latitude);
	}
	//智能搜索
	var local = new BMap.LocalSearch(map, { 
		onSearchComplete: search
	});
	local.search(address);
}

/**
 * 确认选择的地址
 * @returns
 */
function confirm() {
	var longitude = $("#longitude").val();
	var latitude = $("#latitude").val();
	var address = $("#address").val();
	var result = [];
	result.push(longitude);
	result.push(latitude);
	result.push(address);
	return result;
}