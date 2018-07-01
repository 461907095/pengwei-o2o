package com.imooc.myo2o.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.myo2o.cache.JedisUtil;
import com.imooc.myo2o.dao.ShopCategoryDao;
import com.imooc.myo2o.entity.ShopCategory;
import com.imooc.myo2o.exceptions.HeadLineOperationException;
import com.imooc.myo2o.exceptions.ShopCategoryOperationException;
import com.imooc.myo2o.service.ShopCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
//    private static String SCLISTKEY="shopcategorylist";
    private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);


    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        String key=SCLISTKEY;
        List<ShopCategory> shopCategoryList=null;
        ObjectMapper mapper=new ObjectMapper();
        if(shopCategoryCondition==null){
            key=key+"_allfirstlevel";
        }else if(shopCategoryCondition!=null&&shopCategoryCondition.getParent()!=null
                &&shopCategoryCondition.getParent().getShopCategoryId()!=null){
            key=key+"_parent"+shopCategoryCondition.getParent().getShopCategoryId();
        }else if(shopCategoryCondition!=null){
            key=key+"_allsecondlevel";
        }
        //判断key是否粗壮乃，
        if(!jedisKeys.exists(key)){
            //若不存在，则从数据库里取出响应数据
            shopCategoryList=shopCategoryDao.queryShopCategory(shopCategoryCondition);
            String jsonString;
            try{
                jsonString=mapper.writeValueAsString(shopCategoryList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        }else{
            String jsonString=jedisStrings.get(key);
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,ShopCategory.class);
            try{
                shopCategoryList=mapper.readValue(jsonString,javaType);
            } catch (JsonParseException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            } catch (JsonMappingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
        }

        return shopCategoryList;
    }
}
