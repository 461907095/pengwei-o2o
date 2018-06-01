package com.imooc.myo2o.service.impl;

import com.imooc.myo2o.dao.ShopDao;
import com.imooc.myo2o.dto.ShopExecution;
import com.imooc.myo2o.entity.Shop;
import com.imooc.myo2o.enums.ShopStateEnum;
import com.imooc.myo2o.exceptions.ShopOperationException;
import com.imooc.myo2o.service.ShopService;
import com.imooc.myo2o.util.ImageUtil;
import com.imooc.myo2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, File shopImg) {
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }

        try{
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effectedNum=shopDao.insertShop(shop);
            if(effectedNum<=0){
                throw  new ShopOperationException("店铺创建失败");
            }else{
                if(shopImg!=null){
                    //存储图片
                    try{
                        addShopImg(shop,shopImg);
                    }catch (Exception e){
                        throw new ShopOperationException("addShopImg"+e.getMessage());
                    }
                    //更新店铺的图片地址
                    effectedNum=shopDao.updateShop(shop);
                    if(effectedNum<=0){
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        }catch (Exception e){
            throw  new ShopOperationException("addShop error"+e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    private void addShopImg(Shop shop, File shopImg) {
        //获取shop图片目录的相对值路径
        // 获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImg,dest);
        shop.setShopImg(shopImgAddr);
    }
}
