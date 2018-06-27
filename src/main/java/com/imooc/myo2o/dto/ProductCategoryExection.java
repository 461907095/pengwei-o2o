package com.imooc.myo2o.dto;

import com.imooc.myo2o.entity.ProductCategory;
import com.imooc.myo2o.enums.ProductCategoryStateEnum;

import java.util.List;

public class ProductCategoryExection {
    private int state;
    //状态表示
    private String stateInfo;
    private List<ProductCategory> productCategoryList;

    public ProductCategoryExection(){

    }
    //操作失败的时候使用的构造器

    public ProductCategoryExection(ProductCategoryStateEnum stateEnuma) {
        this.state = stateEnuma.getState();
        this.stateInfo = stateEnuma.getStateInfo();
    }
    //操作成功时候的使用的构造器
    public ProductCategoryExection(ProductCategoryStateEnum stateEnum,List<ProductCategory> productCategoryList){
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
        this.productCategoryList=productCategoryList;
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

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }
}
