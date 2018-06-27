package com.imooc.myo2o.dao;

import com.imooc.myo2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {
    /**
     * 分页查询店铺，可回溯如的条件有：店铺名（模糊）店铺状态，店铺类别，取余I的，owner
     * @param shopCondition
     * @param rowIndex 从第几行开始去数据
     * @param pageSize 返回的条数
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
                             @Param("rowIndex")int rowIndex, @Param("pageSize")int pageSize);

    /**
     * 返回queryShopList的总数
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition")Shop shopCondition);
    /**
     * 通过shop id查询店铺
     * @param shopId
     * @shopId Shop
     */
    Shop queryByShopId(long shopId);
    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @param shop
     * @return
     */
    int updateShop(Shop shop);
}
