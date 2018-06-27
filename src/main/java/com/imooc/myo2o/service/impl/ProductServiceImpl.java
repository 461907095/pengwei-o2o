package com.imooc.myo2o.service.impl;

import com.imooc.myo2o.dao.ProductDao;
import com.imooc.myo2o.dao.ProductImgDao;
import com.imooc.myo2o.dao.ShopDao;
import com.imooc.myo2o.dto.ImageHolder;
import com.imooc.myo2o.dto.ProductExection;
import com.imooc.myo2o.dto.ShopExecution;
import com.imooc.myo2o.entity.Product;
import com.imooc.myo2o.entity.ProductImg;
import com.imooc.myo2o.entity.Shop;
import com.imooc.myo2o.enums.ProductStateEnum;
import com.imooc.myo2o.enums.ShopStateEnum;
import com.imooc.myo2o.exceptions.ProductCategoryOperationException;
import com.imooc.myo2o.exceptions.ProductOperationException;
import com.imooc.myo2o.exceptions.ShopOperationException;
import com.imooc.myo2o.service.ProductService;
import com.imooc.myo2o.service.ShopService;
import com.imooc.myo2o.util.ImageUtil;
import com.imooc.myo2o.util.PageCalculator;
import com.imooc.myo2o.util.PathUtil;
import javafx.scene.shape.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;


    @Override
    public ProductExection getProductList(Product productCondition, int pageIndex, int pageSize) {
        //页码转换成数据库的行，并调用dao层去会指定一饿吗的商品劣币哦啊
        int rowIndex=PageCalculator.calculateRowIndex(pageIndex,pageSize);
        List<Product> productList=productDao.queryProductList(productCondition,rowIndex,pageSize);
        //基于同样的查询条件返回该查询调价下的商品总数
        int count=productDao.queryProductCount(productCondition);
        ProductExection pe=new ProductExection();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }

    /**
     * 处理缩略图，获取缩略图相对路径并复制给Product
     * 往tb_product写入商品信息，获取productId
     * 结合productId批量处理商品详情图
     * 将闪屏详情图列表批量插入tb_product_img中
     * @param product
     * @param thumbnail
     * @param productImgHolderList
     * @return
     * @throws ProductOperationException
     */
    @Override
    @Transactional
    public ProductExection addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) throws ProductOperationException {
        //空值判断
        if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
            //给商品设置默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //默认为上架的状态
            product.setEnableStatus(1);
            //弱商铺缩略图不为空 则添加
            if(thumbnail!=null){
                addThumbnail(product,thumbnail);
            }
            try{
                //创建商品信息
                int effectedNum=productDao.insertProduct(product);
                if(effectedNum<=0){
                    throw new ProductOperationException("创建商品失败");
                }
            }catch (Exception e){
                throw new ProductOperationException("创建商品失败:"+e.getMessage());
            }
            //若商品详情图片不为空则添加
            if(productImgHolderList!=null&&productImgHolderList.size()>0){
                addProductImgList(product,productImgHolderList);
            }
            return new ProductExection(ProductStateEnum.SUCCESS,product);
        }else{
            //传参数为空，则返回空值错误信息
            return new ProductExection(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public ProductExection modifyProduct(Product product, ImageHolder thumbnail,
                                         List<ImageHolder> productImgHolderList) throws ProductOperationException {
        //空值判断
        if(product!=null&&product.getShop()!=null&&product.getShop().getShopId()!=null){
            //给商品设置默认属性
            product.setLastEditTime(new Date());
            //弱商铺缩略图不为空 则添加
            if(thumbnail!=null){
                //现货去一遍原有信息，因为原来的信息里有原图片的地址
                Product tempProduct=productDao.queryProductById(product.getProductId());
                if(tempProduct.getImgAddr()!=null){
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addThumbnail(product,thumbnail);
            }
            //如果有新存入的商品吸纳感情图，则将原来的删除，并添加新的图片
            if(productImgHolderList!=null&&productImgHolderList.size()>0){
                deleteProductImgList(product.getProductId());
                addProductImgList(product,productImgHolderList);
            }
            try{
                //更新商品信息
                int effectedNum=productDao.updateProduct(product);
                if(effectedNum<=0){
                    throw new ProductOperationException("跟通信商品失败");
                }
                return new ProductExection(ProductStateEnum.SUCCESS,product);
            }catch (Exception e){
                throw new ProductOperationException("更新商品失败:"+e.getMessage());
            }
        }else{
            //传参数为空，则返回空值错误信息
            return new ProductExection(ProductStateEnum.EMPTY);
        }
    }

    private void deleteProductImgList(Long productId) {
        //根据productId获取原来的图片
        List<ProductImg> productImgList=productImgDao.queryProductImgList(productId);
        //干掉原来的图片
        for (ProductImg productImg:productImgList){
            ImageUtil.deleteFileOrPath(productImg.getImgAddr());
        }
        //删除数据库里原有图片的信息
        productImgDao.deleteProductImgByProductId(productId);
    }

    /**
     * 批量处理添加商品详情图
     * @param product
     * @param productImgHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {

        //获取图片存储路径，这里直接存放到响应店铺的文件夹地下
        String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList=new ArrayList<ProductImg>();
        //便利图片一次去处理，并添加进productimg实体类里面
        for (ImageHolder productImgHolder:productImgHolderList){
            String imgAddr=ImageUtil.generateThumbnail(productImgHolder,dest);
            ProductImg productImg=new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        //如果确实有图片需要添加的，就执行批量添加操作
        if(productImgList.size()>0){
            try{
                int effectedNum=productImgDao.batchInsertProductImg(productImgList);
                if(effectedNum<=0){
                    throw  new ProductCategoryOperationException("创建商品详情图片失败");
                }
            }catch (Exception e){
                throw new ProductOperationException("创建商品详情图片失败"+e.toString());
            }
        }
    }

    /**
     * 天津爱缩略图
     * @param product
     * @param thumbnail
     */
    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String dest=PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr=ImageUtil.generateThumbnail(thumbnail,dest);
        product.setImgAddr(thumbnailAddr);
    }
}


