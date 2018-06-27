package com.imooc.myo2o.service;

import com.imooc.myo2o.dto.ImageHolder;
import com.imooc.myo2o.dto.ShopExecution;
import com.imooc.myo2o.entity.Shop;
import com.imooc.myo2o.exceptions.ShopOperationException;

import java.io.File;
import java.io.InputStream;

public interface ShopService {

    /**
     * 根据shopCondition分页返回响应店铺列表
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ShopExecution getShopList(Shop shopCondition,int pageIndex ,int pageSize);

    /**
     * 通过店铺id获取店铺信息
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     * 更新店铺信息，包括对图片的处理
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     */
    ShopExecution modifyShop(Shop shop,ImageHolder thumbnail);

    /**
     * 注册店铺信息，包括图片处理
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     */
    ShopExecution addShop(Shop shop, ImageHolder thumbnail);
}
