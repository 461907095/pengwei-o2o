package com.imooc.myo2o.dao;

import com.imooc.myo2o.entity.ProductCategory;
import com.imooc.myo2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {
    /**
     * 通过shop id 查询店铺商品类别
     *
     * @param long
     *            shopId
     * @return List<ProductCategory>
     */
    List<ProductCategory> queryProductCategoryList(long shopId);


    /**
     * 批量新增商品类别
     * @param productCategoryList
     * @return
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);

    /**
     * 删除指定商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     */
    int deleteProductCategory(@Param("productCategoryId")long productCategoryId, @Param("shopId")long shopId);
}
