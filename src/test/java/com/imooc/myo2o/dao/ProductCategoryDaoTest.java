package com.imooc.myo2o.dao;

import com.imooc.myo2o.BaseTest;
import com.imooc.myo2o.entity.ProductCategory;
import com.imooc.myo2o.entity.ShopCategory;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;

import javax.rmi.PortableRemoteObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Test
    @Ignore
    public void testBQueryProductCategory(){
        long shopId=1;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        System.out.println("该店铺自定义类别数为：" + productCategoryList.size());
    }

    @Test
    public void testAInsertProductCategory(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryName("商品类别1");
        productCategory.setPriority(1);
        productCategory.setCreateTime(new Date());
        productCategory.setShopId(1L);
        ProductCategory productCategory2 = new ProductCategory();
        productCategory2.setProductCategoryName("商品类别2");
        productCategory2.setPriority(2);
        productCategory2.setCreateTime(new Date());
        productCategory2.setShopId(1L);
        List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
        productCategoryList.add(productCategory);
        productCategoryList.add(productCategory2);
        int effectedNum = productCategoryDao
                .batchInsertProductCategory(productCategoryList);
        assertEquals(2, effectedNum);

    }

    @Test
    public void testCDeleteProductCategory() throws Exception{
        long shopId=1;
        List<ProductCategory> productCategoryList =productCategoryDao.queryProductCategoryList(shopId);
        for(ProductCategory pc:productCategoryList){
            if("商品类别1".equals(pc.getProductCategoryName())||"商品类别2".equals(pc.getProductCategoryName())){
                int effectedNum=productCategoryDao.deleteProductCategory(pc.getProductCategoryId(),
                        shopId);
                assertEquals(1,effectedNum);
            }
        }
    }
}
