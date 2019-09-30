<!DOCTYPE html>
<html lang="zh">

	<head>
		<#include "../../include/map.ftl"/>
	</head>

	<body>
		<div class="input-group tip">
			<span class="input-group-addon">经度</span>
			<input type="text" autocomplete="off" class="form-control" placeholder="经度" id="longitude">
			<span class="input-group-addon">纬度</span>
			<input type="text" autocomplete="off" class="form-control" placeholder="纬度" id="latitude">
			<span class="input-group-addon">地址</span>
			<input type="text" autocomplete="off" class="form-control" placeholder="地址" id="address">
		</div>
		<div id="searchResultPanel"></div>
		<div id="map"></div>
		<input type="hidden" value="${longitude}" id="lng">	
		<input type="hidden" value="${latitude}" id="lat">	
		<input type="hidden" value="${address}" id="addr">	
	</body>

</html>