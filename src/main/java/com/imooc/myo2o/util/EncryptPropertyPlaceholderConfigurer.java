package com.imooc.myo2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    //需要加密的字段数组
    private String[] encryptPropNames={"jdbc.username","jdbc.password"};

    /**
     * 对关键的属性进行转换
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if(isEncryptProp(propertyName)){
            //对已经加密的字段进行揭秘
            String decryptValue=DESUtil.getDecryptString(propertyValue);
            return decryptValue;
        }else{
            return propertyValue;
        }
    }

    private boolean isEncryptProp(String propertyName){
        //若等于需要加密的field,则进行加密
        for (String encryptpropertyName:encryptPropNames){
            if(encryptpropertyName.equals(propertyName))
                return true;
        }
        return false;
    }
}
