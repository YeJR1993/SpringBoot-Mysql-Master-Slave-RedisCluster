<!DOCTYPE html>
<html lang="zh">

	<head>
		<#include "../../include/head.ftl"/>
	</head>

	<body class="contentBody">
		<div class="animated fadeIn fullHeight">
			<div class="tpl-portlet-components contentMinHight">
				<div class="portlet-title">
					<div class="caption font-green bold">
						<span class="am-icon-code"></span> 商店列表
					</div>
				</div>
				<div class="tpl-block">
					<div class="am-g">
						<div class="am-u-sm-12 am-u-md-6">
							<div class="am-btn-toolbar">
								<div class="am-btn-group am-btn-group-xs">
									<@shiro.hasPermission name="shop:export:shopInfo">
										<button type="button" class="am-btn am-btn-default am-btn-secondary"  onclick="exportData('确认导出？','/shop/info/export')"><span class="am-icon-save"></span> 导出</button>
									</@shiro.hasPermission>
									<@shiro.hasPermission name="shop:delete:shopInfo">
										<button type="button" class="am-btn am-btn-default am-btn-danger" onclick="deleteMultIterm('确认要冻结吗？','/shop/info/deleteAll')"><span class="am-icon-trash-o"></span> 冻结</button>
									</@shiro.hasPermission>
								</div>
							</div>
						</div>
						
						<div class="am-u-sm-12 am-u-md-3">
							<form id="searchForm" action="/shop/info/list">
								<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
								<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
								<div class="am-input-group am-input-group-sm">
									<input autocomplete="off" type="text" class="am-form-field" placeholder="输入查询的商店名或者用户名" name="searchKey" value="${shopInfo.searchKey !''}">
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
										<th>商店名</th>
										<th>登录名</th>
										<th>地址</th>
										<th>电话</th>
										<th>状态</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<#list page.list as shopInfo>
										<tr>
											<td> <input type="checkbox" id="${shopInfo.id}" name="box" class="tpl-table-fz-check"></td>
											<td>${shopInfo.shopName}</td>
											<td>${shopInfo.userName}</td>
											<td>${shopInfo.address}</td>
											<td>${shopInfo.mobile}</td>
											<td><#if shopInfo.state == 0>正常<#elseif shopInfo.state == 1>冻结</#if></td>
											<td>
												<div class="am-btn-toolbar">
                                                    <div class="am-btn-group am-btn-group-xs">
	                                                    <@shiro.hasPermission name="shop:view:shopInfo">
	                                                    	<button class="am-btn am-btn-default am-btn-xs am-hide-sm-only" onclick="openDialogView('查看商户', '/shop/info/form?id=${shopInfo.id}&urlType=1','960px', '720px')"><span class="am-icon-copy"></span> 查看</button>
	                                                    </@shiro.hasPermission>
	                                                    <@shiro.hasPermission name="shop:edit:shopInfo">
															<button class="am-btn am-btn-default am-btn-xs am-text-secondary"  onclick="openDialogSave('编辑商户', '/shop/info/form?id=${shopInfo.id}&urlType=1','960px', '720px')"><span class="am-icon-pencil-square-o"></span> 编辑</button>
	                                                    </@shiro.hasPermission>
	                                                    <@shiro.hasPermission name="shop:add:shopGoods">
															<button type="button" class="am-btn am-btn-default am-btn-xs am-text-thirdly" onclick="openDialogSave('添加商品', '/shop/goods/form?shopId=${shopInfo.id}&urlType=0','960px', '720px')"><span class="am-icon-plus"></span> 商品</button>
														</@shiro.hasPermission>
	                                                    <@shiro.hasPermission name="shop:delete:shopInfo">
			                                            	<button class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only"  onclick="deleteItem('确认要冻结该商户吗？', '/shop/info/delete?id=${shopInfo.id}')"><span class="am-icon-trash-o"></span> 冻结</button>
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