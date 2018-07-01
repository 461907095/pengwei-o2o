package com.imooc.myo2o.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolWriper {
    /**Redis连接池对象*/

    private JedisPool jedisPool;

    public JedisPoolWriper(JedisPoolConfig poolConfig,final String host,
                           final int port) {
        try{
            jedisPool=new JedisPool(poolConfig,host,port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取Redis连接池对象
     *
     * @return
     */
    public JedisPool getJedisPool(){
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool){
        this.jedisPool=jedisPool;
    }
}
