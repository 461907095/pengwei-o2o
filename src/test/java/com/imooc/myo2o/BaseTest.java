package com.imooc.myo2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置spring和junit整合，junit启动是加载springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junitspring配置文件的文职
@ContextConfiguration({"classpath:spring/spring-dao.xml",
"classpath:spring/spring-service.xml"})
public class BaseTest {

}
