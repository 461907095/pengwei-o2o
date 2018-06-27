package com.imooc.myo2o.service;

import com.imooc.myo2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    /**
     * g根据查询条件获取ShopCategory列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
