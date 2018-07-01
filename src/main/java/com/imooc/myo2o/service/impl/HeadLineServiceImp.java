package com.imooc.myo2o.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.myo2o.cache.JedisUtil;
import com.imooc.myo2o.dao.HeadLineDao;
import com.imooc.myo2o.entity.HeadLine;
import com.imooc.myo2o.exceptions.AreaOperationException;
import com.imooc.myo2o.exceptions.HeadLineOperationException;
import com.imooc.myo2o.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImp implements HeadLineService {
    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private JedisUtil.Keys jediskeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;
//    private static String HLLISTKEY="headlinelist";
    private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImp.class);
    @Override
    @Transactional
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
        //定义redis的key的前缀
        String key=HLLISTKEY;
        //定义接受对象
        List<HeadLine> headLineList=null;
        //定义jackson数据转换操作
        ObjectMapper mapper=new ObjectMapper();
        //拼接出redis的key
        if(headLineCondition!=null&&headLineCondition.getEnableStatus()!=null){
            key=key+"_"+headLineCondition.getEnableStatus();
        }
        //判断key是否存在
        if(!jediskeys.exists(key)){
            //若不存在，则从数据库里取出响应数据
            headLineList=headLineDao.queryHeadLine(headLineCondition);
            //将相关的实体类集合转哈un成String，存入redis里面对应key中
            String jsonString;
            try{
                jsonString=mapper.writeValueAsString(headLineList);
            }catch (JsonProcessingException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        }else{
            //若存在，则直接从redis里面取出响应数据
            String jsonString=jedisStrings.get(key);
            //指定要将string转换成的结合类型
            JavaType javaType=mapper.getTypeFactory().constructParametricType(ArrayList.class,HeadLine.class);
            try{
                headLineList=mapper.readValue(jsonString,javaType);
            }catch (JsonParseException e){
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            } catch (JsonMappingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new HeadLineOperationException(e.getMessage());
            }
        }

        return headLineList;
    }
}
