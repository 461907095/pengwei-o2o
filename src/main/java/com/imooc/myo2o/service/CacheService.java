package com.imooc.myo2o.service;

public interface CacheService {
    /**
     * 一句key前缀删除匹配该模式下的所有key-value
     * @param keyPrefix
     */
    void removeFromCache(String keyPrefix);
}
