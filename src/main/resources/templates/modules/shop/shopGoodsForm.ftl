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
           		<form class="am-form am-form-horizontal" id="saveForm" action="/shop/goods/save">
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
	                       		<input autocomplete="off" type="text" name="id" id="id" value="${shopGoods.id !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group" hidden="hidden">
                 		<label  class="am-u-sm-3 am-form-label">商店ID</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name=shopId id="shopId" value="${shopGoods.shopId !''}">
	                       		<small></small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group">
                 		<label  class="am-u-sm-3 am-form-label"><span class="required">*</span>商品名</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="goodsName" id="goodsName" placeholder="商品名" maxlength="20"  value="${shopGoods.goodsName !''}">
	                       		<small>商品名称</small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group">
                 		<label  class="am-u-sm-3 am-form-label"><span class="required">*</span>图片</label>
                  		<div class="am-u-sm-9">
                  			<div class="am-input-group am-input-group-sm">
	                       		<input autocomplete="off" class="uploadFile" uploadType="image" type="text" id="picture" name="picture" placeholder="商品图片" value="${shopGoods.picture !''}" readonly="readonly">
                  				<span class="am-input-group-btn">
							    	<button class="am-btn input-btn am-btn-default iconfont icon-shangchuan" type="button" onclick="openUploadPage('image', 'picture', 5)"></button>
							    </span>
                  			</div>
                  			<small>商品图片最多上传5张</small>
                    	</div>
                	</div>
                	<div class="am-form-group">
                 		<label  class="am-u-sm-3 am-form-label"><span class="required">*</span>价格</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="salePrice" id="salePrice" maxlength="6" placeholder="销售价"  value="${shopGoods.salePrice !''}">
	                       		<small>商品销售价格</small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group">
                 		<label  class="am-u-sm-3 am-form-label"><span class="required">*</span>库存</label>
                  		<div class="am-u-sm-9">
                  			<div>
	                       		<input autocomplete="off" type="text" name="stock" id="stock" maxlength="5" placeholder="库存"  value="${shopGoods.stock !''}">
	                       		<small>商品库存</small>
                  			</div>
                    	</div>
                	</div>
                	<div class="am-form-group">
                 		<label  class="am-u-sm-3 am-form-label"><span class="required">*</span>描述</label>
                  		<div class="am-u-sm-9">
                  			<div>
                  				<textarea name="description" id="description">${shopGoods.description !''}</textarea>
	                       		<small>商品描述</small>
                  			</div>
                    	</div>
                	</div>
       			</form>
        	</div>
		</div>
		<script type="text/javascript">
			//页面加载完成时调用  
	        $(function(){
	        	//验证价格格式
				jQuery.validator.addMethod("priceValid", function(value, element) {					
					var reg = /^[0-9]+(.[0-9]{1,2})?$/;	   		   
					if(reg.test(value) || value == ""){
				   		return true;
				  	} else {
						return false;
					}   		   
				},"请输入正确格式");
	        	//要给对应的表单加入validate校验  
	            $("#saveForm").validate({  
	                rules:{  
	                	goodsName:{  
	                		required: true,
	                    },
	                    picture:{  
	                		required: true,
	                    },
	                    salePrice:{  
	                		required: true,
	                		priceValid: true,
	                    },
	                    stock:{  
	                		required: true,
	                		number: true,
							digits: true,
	                    },
	                },  
	                messages:{  
	                	goodsName:{  
	                		required: "请输入商品名称",
	                    },
	                    picture:{  
	                		required: "请上传商品图片",
	                    },
	                    salePrice:{  
	                		required: "请输入商品售价",
	                		priceValid: "请输入正确的价格",
	                    },
	                    stock:{  
	                		required: "请输入商品库存",
	                		number: "请输入正确的商量库存",
							digits: "请输入正确的商量库存",
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