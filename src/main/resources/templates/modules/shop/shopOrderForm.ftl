<!DOCTYPE html>
<html lang="zh">

	<head>
		<#include "../../include/head.ftl"/>
		<style type="text/css">
			table {
				background-color: white;
			}
		</style>
	</head>

	<body>
		<div class="animated fadeIn">
			<div class="am-u-sm-12 am-u-md-11 form-top">
           		<form class="am-form am-form-horizontal" id="saveForm" action="">
           			<div class="am-form-group" hidden="hidden">
                 		<label  class="am-u-sm-3 am-form-label">主键ID</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="id" id="id" value="${shopOrder.id !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">订单编号</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="orderNumber" id="orderNumber" value="${shopOrder.orderNumber !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">商店名称</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="shopName" id="shopName" value="${shopOrder.shopName !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">收货人姓名</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="receivingName" id="receivingName" value="${shopOrder.receivingName !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">收货人电话</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="receivingPhone" id="receivingPhone" value="${shopOrder.receivingPhone !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">收货人地址</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="receivingAddress" id="receivingAddress" value="${shopOrder.receivingAddress !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">支付价</label>
                  		<div class="am-u-sm-9">
                  			<div class="am-input-group am-input-group-sm">
	                       		<input autocomplete="off" type="text" name="actualPrice" id="actualPrice" value="${shopOrder.actualPrice !''}">
                  				<span class="am-input-group-btn">
							    	<button class="am-btn input-btn am-btn-default " type="button">元</button>
							    </span>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">快递单号</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="expressNumber" id="expressNumber" value="${shopOrder.expressNumber !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">创建时间</label>
                  		<div class="am-u-sm-9">
                  			<div>
                  				<#if shopOrder.createTime??>
                  					<input autocomplete="off" type="text" id="createTime" name="createTime"  value="${shopOrder.createTime?string('yyyy-MM-dd HH:mm:ss')}">
                  				<#else>
	                       			<input autocomplete="off" type="text" id="createTime" name="createTime"  value="" >
                  				</#if>
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">支付时间</label>
                  		<div class="am-u-sm-9">
                  			<div>
                  				<#if shopOrder.payTime??>
                  					<input autocomplete="off" type="text" id="payTime" name="payTime"  value="${shopOrder.payTime?string('yyyy-MM-dd HH:mm:ss')}">
                  				<#else>
	                       			<input autocomplete="off" type="text" id="payTime" name="payTime"  value="" >
                  				</#if>
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">配送时间</label>
                  		<div class="am-u-sm-9">
                  			<div>
                  				<#if shopOrder.deliveryTime??>
                  					<input autocomplete="off" type="text" id="deliveryTime" name="deliveryTime"  value="${shopOrder.deliveryTime?string('yyyy-MM-dd HH:mm:ss')}">
                  				<#else>
	                       			<input autocomplete="off" type="text" id="deliveryTime" name="deliveryTime"  value="" >
                  				</#if>
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">订单状态</label>
                  		<div class="am-u-sm-9">
                  			<div>
                  				<#if shopOrder.state == 0>
                  					<input autocomplete="off" type="text" id="state" name="state"  value="未支付" >
								<#elseif shopOrder.state == 1>
									<input autocomplete="off" type="text" id="state" name="state"  value="待配送" >
								<#elseif shopOrder.state == 2>
									<input autocomplete="off" type="text" id="state" name="state"  value="已完成" >
								</#if>
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" >
                 		<label  class="am-u-sm-3 am-form-label">订单商品</label>
                  		<div class="am-u-sm-9">
                  			<div>
                  				<table class="am-table am-table-hover table-main">
									<thead>
										<tr>
											<th>商品名</th>
											<th>单价</th>
											<th>数量</th>
										</tr>
									</thead>
									<tbody>
										<#list shopOrder.orderGoodsList as orderGoods>
											<tr>
												<td>${orderGoods.goodsName}</td>
												<td>${orderGoods.salePrice}</td>
												<td>${orderGoods.buyNum}</td>
											</tr>
										</#list>
									</tbody>
								</table>
                  			</div>
                    	</div>
                	</div>
       			</form>
        	</div>
		</div>
	</body>
</html>