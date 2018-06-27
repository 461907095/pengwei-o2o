package com.imooc.myo2o.dto;

import com.imooc.myo2o.entity.Product;
import com.imooc.myo2o.enums.ProductStateEnum;

import java.util.List;

public class ProductExection {
    private int state;
    private  String stateInfo;
    private  int count;
    private Product product;
    private List<Product> productList;

    public ProductExection() {
    }

    //失败的构造器

    public ProductExection(ProductStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
    }

    //成功的构造器
    public ProductExection(ProductStateEnum stateEnum,Product product){
        this.state = stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.product=product;
    }

    //成功的构造器
    public ProductExection(ProductStateEnum stateEnum,List<Product> productList){
        this.state = stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.productList=productList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
