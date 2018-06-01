package com.imooc.myo2o.service;

import com.imooc.myo2o.dto.ShopExecution;
import com.imooc.myo2o.entity.Shop;

import java.io.File;

public interface ShopService {
    ShopExecution addShop(Shop shop, File shopImg);
}
