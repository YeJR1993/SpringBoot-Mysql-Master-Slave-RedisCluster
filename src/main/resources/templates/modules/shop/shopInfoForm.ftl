<!DOCTYPE html>
<html lang="zh">

	<head>
		<#include "../../include/head.ftl"/>
		<script>
	    	$(function(){
				 CKEDITOR.replace('description');
			}); 
	    </script>
	</head>

	<body>
		<div class="animated fadeIn">
			<div class="am-u-sm-12 am-u-md-11 form-top">
           		<form class="am-form am-form-horizontal" id="saveForm" action="/shop/info/save">
           			<div class="am-form-group" hidden="hidden">
                 		<label  class="am-u-sm-3 am-form-label">跳转方向</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="urlType" id="urlType" value="${urlType}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
           			<div class="am-form-group" hidden="hidden">
                 		<label  class="am-u-sm-3 am-form-label">主键ID</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="id" id="id" value="${shopInfo.id !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" hidden="hidden">
                 		<label  class="am-u-sm-3 am-form-label">用户ID</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="userId" id="userId" value="${shopInfo.userId !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group">
                 		<label  class="am-u-sm-3 am-form-label"><span class="required">*</span>商店名</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="shopName" id="shopName" placeholder="商店名" maxlength="20"  value="${shopInfo.shopName !''}">
	                       		<small>商店名称</small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group">
                 		<label  class="am-u-sm-3 am-form-label"><span class="required">*</span>logo</label>
                  		<div class="am-u-sm-9">
                  			<div class="am-input-group am-input-group-sm">
	                       		<input autocomplete="off" class="uploadFile" uploadType="image" type="text" id="shopLog" name="shopLog" placeholder="商店logo" value="${shopInfo.shopLog !''}" readonly="readonly">
                  				<span class="am-input-group-btn">
							    	<button class="am-btn input-btn am-btn-default iconfont icon-shangchuan" type="button" onclick="openUploadPage('image', 'shopLog', 1)"></button>
							    </span>
                  			</div>
                  			<small>上传商店logo</small>
                    	</div>
                	</div>
                	<div class="am-form-group">
                 		<label  class="am-u-sm-3 am-form-label"><span class="required">*</span>地址</label>
                  		<div class="am-u-sm-9">
                  			<div class="am-input-group am-input-group-sm">
	                       		<input type="hidden" id="longitude" name="longitude"  value="${shopInfo.longitude !''}" >
	                       		<input type="hidden" id="latitude" name="latitude"  value="${shopInfo.latitude !''}" >
	                       		<input autocomplete="off"  type="text" id="address" name="address" placeholder="商店地址" value="${shopInfo.address !''}" readonly="readonly">
                  				<span class="am-input-group-btn">
							    	<button class="am-btn input-btn am-btn-default iconfont icon-ditufuwu" type="button" onclick="openBaiduMap('longitude','latitude','address')"></button>
							    </span>
                  			</div>
                  			<small>选择商店地址</small>
                    	</div>
                	</div>
                	<div class="am-form-group">
                 		<label  class="am-u-sm-3 am-form-label"><span class="required">*</span>电话</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="mobile" id="mobile" placeholder="商店电话"  value="${shopInfo.mobile !''}">
	                       		<small>固话或者手机号</small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group">
                 		<label  class="am-u-sm-3 am-form-label"><span class="required">*</span>描述</label>
                  		<div class="am-u-sm-9">
                  			<div>
                  				<textarea name="description" id="description">${shopInfo.description !''}</textarea>
	                       		<small>商店描述</small>
                  			</div>
                    	</div>
                	</div>
       			</form>
        	</div>
		</div>
		<script type="text/javascript">
			//页面加载完成时调用  
	        $(function(){
	        	// 手机号的校验规则
	        	jQuery.validator.addMethod("phoneRule", function(value, element) {
					var reg = /^((0\d{2,3}-\d{7,8})|(1[3567984]\d{9})|(^400-?[0-9]{3}-?[0-9]{4})|(^800-?[0-9]{3}-?[0-9]{4}))$/; 
					return(reg.test(value));
				}, "");
	        	//要给对应的表单加入validate校验  
	            $("#saveForm").validate({  
	                rules:{  
	                	shopName:{  
	                		required: true,
	                    },
	                    shopLog:{  
	                		required: true,
	                    },
	                    address:{  
	                		required: true,
	                    },
	                    mobile:{  
	                		required: true,
	                		phoneRule: true,
	                    },
	                },  
	                messages:{  
	                	shopName:{  
	                		required: "请输入商店名称",
	                    },
	                    shopLog:{  
	                		required: "请输入商店Logo",
	                    },
	                    address:{  
	                		required: "请选择地址",
	                    },
	                    mobile:{  
	                		required: "请输入固话或者手机号",
	                		phoneRule: "请输入正确的固话或者手机号",
	                    },
	                },
	                errorPlacement : function(error, element) {
						error.insertAfter(element.parent());
					}
	            }); 
	            
	        });  
		
			function doSubmit () {
				if ($("#saveForm").valid()) {
					$("#saveForm").submit();
					return true;
				}
				return false;
			}
		</script>
	</body>
</html>