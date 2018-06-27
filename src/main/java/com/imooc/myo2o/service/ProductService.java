package com.imooc.myo2o.service;

import com.imooc.myo2o.dto.ImageHolder;
import com.imooc.myo2o.dto.ProductExection;
import com.imooc.myo2o.entity.Product;
import com.imooc.myo2o.exceptions.ProductOperationException;

import java.io.InputStream;
import java.util.List;

public interface ProductService {

    /**
     * 查询商品列表并分页，可输入的条件有:商品名，商品状态，店铺id商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExection getProductList(Product productCondition,int pageIndex,int pageSize);

    /**
     * 通过商品Id查询唯一的商品信息
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    /**
     * 添加商品信息以及图片处理
     * @param product
     * @param thumbnail
     * @param productImgListName
     * @return
     * @throws ProductOperationException
     */
    ProductExection addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgListName) throws ProductOperationException;

    /**
     * 修改商品洗以及图片处理
     * @param product
     * @param thumbnail
     * @param productImgListName
     * @return
     * @throws ProductOperationException
     */
    ProductExection modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgListName)
            throws ProductOperationException;
}
