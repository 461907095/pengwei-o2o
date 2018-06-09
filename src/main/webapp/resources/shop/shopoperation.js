/**
 *
 */
$(function(){
    var initUrl = '/imooc/shopadmin/getshopinitinfo';
    var registerShopUrl = '/imooc/shopadmin/registershop';

    /*此方法是获取店铺分类和区域信息，进行前端的店铺分类和所属区域的下拉菜单的初始化*/
    getShopInitInfo();
    function getShopInitInfo(){
        $.getJSON(initUrl, function(data){
            if(data.success){
                alert(data.toString())
                var tempHtml = '';
                var tempAreaHtml = '';
                data.shopCategoryList.map(function(item, index){
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">' + item.shopCategoryName + '</option>';
                });
                data.areaList.map(function(item, index){
                    tempAreaHtml += '<option data-id="' + item.areaId + '">' + item.areaName + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
                alert(tempHtml);
            }
        });
        $('#submit').click(function(){
            var shop = {};
            shop.shopName = $('#shop_name').val();
            shop.shopAddr = $('#shop_addr').val();
            shop.phone = $('#shop_phone').val();
            shop.shopDesc = $('#shop_desc').val();

            /*使用的下拉菜单来进行选择，获取值的方法*/
            shop.shopCategory = {
                shopCategoryId : $('#shop-category').find('option').not(function(){
                return !this.selected;
            }).data('id')
        };
            shop.area = {
                areaId : $('#area').find('option').not(function(){
                return !this.selected;
            }).data('id')
        };

            /*获取的是上传图片的输入流*/
            var shopImg = $('#shop_img')[0].files[0];
            //在ajax中传递的参数
            var formData = new FormData();
            //参数的内容，分别是上面的shop和shop图片
            formData.append('shopImg', shopImg);
            formData.append('shopStr', JSON.stringify(shop));
            /*使用ajax提交到后台*/
            $.ajax({
                url:registerShopUrl,
                type:'POST',
            data:formData,
                contentType:false,
                processData:false,
                cache:false,
                success:function(data){
                if(data.success){
                    $.toast('提交成功！');
                } else{
                    $.toast('提交失败！' + data.errMsg);
                }
            }
        })
        });
    }
})