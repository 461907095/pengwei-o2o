package com.imooc.myo2o.service;

import com.imooc.myo2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
