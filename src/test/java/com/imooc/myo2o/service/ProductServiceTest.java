package com.imooc.myo2o.service;

import com.imooc.myo2o.BaseTest;
import com.imooc.myo2o.dto.ImageHolder;
import com.imooc.myo2o.dto.ProductExection;
import com.imooc.myo2o.dto.ShopExecution;
import com.imooc.myo2o.entity.*;
import com.imooc.myo2o.enums.ProductStateEnum;
import com.imooc.myo2o.enums.ShopStateEnum;
import com.imooc.myo2o.exceptions.ShopOperationException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends BaseTest {

    @Autowired
    private ProductService  productService;

    @Test
    public void testModifyProduct() throws ShopOperationException,FileNotFoundException{
        //创建shopId为1且productCategoryId为1的商品实力并且给其成员变量复制
        Product product=new Product();
        Shop shop=new Shop();
        shop.setShopId(1L);
        ProductCategory pc=new ProductCategory();
        pc.setProductCategoryId(1L);
        product.setProductId(1L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("正式的商品");
        product.setProductDesc("正式的商品");
        //创建缩略图文件流
        File thumbnailFile=new File("G:/modify.jpg");
        InputStream is=new FileInputStream(thumbnailFile);
        ImageHolder thumbnail=new ImageHolder(is,thumbnailFile.getName());
        //创建两个商品详情图文件流并将他们添加到详情图列表中
        File productImg1=new File("G:/modify.jpg");
        InputStream is1=new FileInputStream(productImg1);
        File productImg2=new File("G:/modify.jpg");
        InputStream is2=new FileInputStream(productImg2);
        List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
        productImgList.add(new ImageHolder(is1,productImg1.getName()));
        productImgList.add(new ImageHolder(is2,productImg2.getName()));
        //添加商品并验证
        ProductExection pe=productService.modifyProduct(product,thumbnail,productImgList);
        assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());
    }

    @Test
    @Ignore
    public void testAddProduct() throws ShopOperationException,FileNotFoundException{
        //创建shopId为1且productCategoryId为1的商品实力并且给其成员变量复制
        Product product=new Product();
        Shop shop=new Shop();
        shop.setShopId(1L);
        ProductCategory pc=new ProductCategory();
        pc.setProductCategoryId(1L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("测试商品1");
        product.setProductDesc("测试商品1");
        product.setPriority(20);
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
        //创建缩略图文件流
        File thumbnailFile=new File("G:/news.png");
        InputStream is=new FileInputStream(thumbnailFile);
        ImageHolder thumbnail=new ImageHolder(is,thumbnailFile.getName());
        //创建两个商品详情图文件流并将他们添加到详情图列表中
        File productImg1=new File("G:/news.png");
        InputStream is1=new FileInputStream(productImg1);
        File productImg2=new File("G:/modify.jpg");
        InputStream is2=new FileInputStream(productImg2);
        List<ImageHolder> productImgList=new ArrayList<ImageHolder>();
        productImgList.add(new ImageHolder(is1,productImg1.getName()));
        productImgList.add(new ImageHolder(is2,productImg2.getName()));
        //添加商品并验证
        ProductExection pe=productService.addProduct(product,thumbnail,productImgList);
        assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());
    }
}
