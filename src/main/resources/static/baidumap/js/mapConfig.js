/**
 * 打开百度地图
 * @param longitude  经度ID
 * @param latitude  纬度ID
 * @param address  地址ID
 * @returns
 */
function openBaiduMap(longitude, latitude, address) {
	
	var longitudeId = "#" + longitude;
	var latitudeId = "#" + latitude;
	var addressId = "#" + address;
	
	var longitudeVal = "";
	var latitudeVal = "";
	var addressVal = "";
	
	if (longitude != "") {
		longitudeVal = $(longitudeId).val();
	}
	if (latitude != "") {
		latitudeVal = $(latitudeId).val();
	}
	if (address != "") {
		addressVal = $(addressId).val();
	}
	
	top.layer.open({
		type : 2,
		title : "选择地址",
		area : [ '890px', '620px' ],
		fixed : false, // 不固定
		maxmin : true,
		content : '/baiduMap/open?longitude='+longitudeVal+"&latitude="+latitudeVal+"&address="+addressVal,// 打开子页面
		btn : [ '确定', '关闭' ],
		yes : function(index, layero) {
			// 得到iframe页的窗口对象
			var iframeWin = layero.find('iframe')[0]; 
			// 执行iframe页面的方法
			var result = iframeWin.contentWindow.confirm();
			if (longitude != "") {
				var id = "#" + longitude;
				var errorId = "#" + longitude + "-error";
				if ($(errorId) != undefined) {
					$(errorId).remove();
				}
				$(id).val(result[0]);
			}
			if (latitude != "") {
				var id = "#" + latitude;
				var errorId = "#" + latitude + "-error";
				if ($(errorId) != undefined) {
					$(errorId).remove();
				}
				$(id).val(result[1]);
			}
			if (address != "") {
				var id = "#" + address;
				var errorId = "#" + address + "-error";
				if ($(errorId) != undefined) {
					$(errorId).remove();
				}
				$(id).val(result[2]);
			}
			//关闭对话框。
        	top.layer.close(index);
		},
		cancel : function(index) {
		}
	});
}