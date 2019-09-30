<!DOCTYPE html>
<html lang="zh">

	<head>
		<#include "../../include/head.ftl"/>
		<style type="text/css">
			.search {
				float: right;
			}
		</style>
	</head>

	<body class="contentBody">
		<div class="animated fadeIn fullHeight">
			<div class="tpl-portlet-components contentMinHight">
				<div class="portlet-title">
					<div class="caption font-green bold">
						<span class="am-icon-code"></span> 订单列表
					</div>
				</div>
				<div class="tpl-block">
					<div class="am-g">
						<div class="am-u-sm-12 am-u-md-3 search">
							<form id="searchForm" action="/shop/order/list">
								<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
								<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
								<div class="am-input-group am-input-group-sm">
									<input autocomplete="off" type="text" class="am-form-field" placeholder="输入查询的商店名或者订单编号" name="searchKey" value="${shopOrder.searchKey !''}">
									<span class="am-input-group-btn">
							            <button class="am-btn  am-btn-default am-btn-success tpl-am-btn-success am-icon-search" type="submit"></button>
							        </span>
								</div>
							</form>
						</div>
					</div>
					<div class="am-g">
						<div class="am-u-sm-12">
							<table class="am-table am-table-hover table-main">
								<thead>
									<tr>
										<th class="table-check"><input type="checkbox" class="tpl-table-fz-check checkboxAll"></th>
										<th>订单编号</th>
										<th>商店名称</th>
										<th>收货人</th>
										<th>支付价</th>
										<th>状态</th>
										<th>创建时间</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<#list page.list as shopOrder>
										<tr>
											<td> <input type="checkbox" id="${shopOrder.id}" name="box" class="tpl-table-fz-check"></td>
											<td>${shopOrder.orderNumber}</td>
											<td>${shopOrder.shopName}</td>
											<td>${shopOrder.receivingName}</td>
											<td>${shopOrder.actualPrice}</td>
											<td><#if shopOrder.state == 0>未支付<#elseif shopOrder.state == 1>待配送<#elseif shopOrder.state == 2>已完成</#if></td>
											<td>${shopOrder.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
											<td>
												<div class="am-btn-toolbar">
                                                    <div class="am-btn-group am-btn-group-xs">
	                                                    <@shiro.hasPermission name="shop:view:shopOrder">
	                                                    	<button class="am-btn am-btn-default am-btn-xs am-hide-sm-only" onclick="openDialogView('查看订单', '/shop/order/form?id=${shopOrder.id}','960px', '720px')"><span class="am-icon-copy"></span> 查看</button>
	                                                    </@shiro.hasPermission>
												    </div>
                                                </div>
											</td>
										</tr>
									</#list>
								</tbody>
							</table>
							${page.html}
						</div>
					</div>
				</div>
			</div>
		</div>
		<input id="message" type="hidden" value="${msg!''}" icon=1>
	</body>
</html>