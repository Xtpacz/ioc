package com.xtpacz.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xtpacz.bean.BeanDefinition;
import com.xtpacz.utils.JsonUtils;

import java.io.InputStream;
import java.util.List;

public class JsonApplicationContext extends BeanFactoryImpl{

    private String fileName;

    public JsonApplicationContext(String fileName) {
        this.fileName = fileName;
    }

    public void init(){
        loadFile();
    }

    private void loadFile(){

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

        List<BeanDefinition> beanDefinitions = JsonUtils.readValue(is,new TypeReference<List<BeanDefinition>>(){});

        if(beanDefinitions != null && !beanDefinitions.isEmpty()) {

            for (BeanDefinition beanDefinition : beanDefinitions) {
                // TODO 搞清楚如何注册一个bean
                registerBean(beanDefinition.getName(), beanDefinition);
            }
        }

    }
}
