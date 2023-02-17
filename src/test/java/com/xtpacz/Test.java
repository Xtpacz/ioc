package com.xtpacz;

import com.xtpacz.bean.President;
import com.xtpacz.core.JsonApplicationContext;

/**
 * @ClassName Test
 * @Description TODO
 * @Author Administrator
 * @Date 2023/2/15
 * @Version 1.0
 **/
public class Test {
    public static void main(String[] args) throws Exception{
        JsonApplicationContext applicationContext = new JsonApplicationContext("application.json");
        applicationContext.init();
        President president = (President) applicationContext.getBean("president");
//        System.out.println("I have got the president, and president is " + president);
        president.doIt();
    }
}
