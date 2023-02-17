package com.xtpacz.core;

import com.xtpacz.bean.BeanDefinition;
import com.xtpacz.bean.ConstructorArg;
import com.xtpacz.utils.BeanUtils;
import com.xtpacz.utils.ClassUtils;
import com.xtpacz.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BeanFactoryImpl implements BeanFactory{
    
    // beanMap 用来保存beanName和实例化后的对象
    private static final ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>();
    // beanDefineMap 存储的是对象的名称和对象对应的数据结构的映射
    private static final ConcurrentHashMap<String, BeanDefinition> beanDefineMap= new ConcurrentHashMap<>();
    private static final Set<String> beanNameSet = Collections.synchronizedSet(new HashSet<>());

    /** 
    * @Description: get a bean by its name
    * @Param: [name]
    * @return: java.lang.Object
    * @Author: Xtpacz
    * @Date: 2023/2/15
    */
    @Override
    public Object getBean(String name) throws Exception {
        // 查找对象是否已经被实例化(先查找是否有实例化好的对象)
        Object bean = beanMap.get(name);
        if (bean != null){
            return bean;
        }
        // 如果没有被实例化,就去 beanDefineMap 里面查找这个对象 对应的数据结构(beanDefinition),
        // 然后再利用这个数据结构去实例化一个对象
//        System.out.println("beanDefineMap.get(name) = " + beanDefineMap.get(name));
        bean = createBean(beanDefineMap.get(name));
        if (bean != null) {
            populatebean(bean);
            beanMap.put(name, bean);
        }
//        System.out.println("還沒有得到bean");
        // 這裡是一處錯誤，原本寫成return null;
        return bean;
    }
    
    /** 
    * @Description: store the definition and name of the bean to the map
    * @Param: [name, beanDefinition]
    * @return: void
    * @Author: Xtpacz
    * @Date: 2023/2/15
    */
    protected void registerBean(String name, BeanDefinition beanDefinition) {
        beanDefineMap.put(name, beanDefinition);
        beanNameSet.add(name);
    }
    
    /** 
    * @Description: create a bean object by beanDefinition
    * @Param: [beanDefinition]
    * @return: java.lang.Object
    * @Author: Xtpacz
    * @Date: 2023/2/15
    */
    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        String beanName = beanDefinition.getClassName();
        Class clz = ClassUtils.loadClass(beanName);
        if(clz == null) {
            throw new Exception("could not find bean by beanName");
        }
        List<ConstructorArg> constructorArgs = beanDefinition.getConstructorArgs();
        if(constructorArgs != null && !constructorArgs.isEmpty()){
            List<Object> objects = new ArrayList<>();
            for (ConstructorArg constructorArg : constructorArgs) {
                if (constructorArg.getValue() != null) {
                    objects.add(constructorArg.getValue());
                } else {
                    objects.add(getBean(constructorArg.getRef()));
                }
            }
            Class[] constructorArgTypes = objects.stream().map(it -> it.getClass()).collect(Collectors.toList()).toArray(new Class[]{});
            Constructor constructor = clz.getConstructor(constructorArgTypes);
            return BeanUtils.instanceByCglib(clz, constructor, objects.toArray());
        } else {
            return BeanUtils.instanceByCglib(clz, null, null);
        }
    }

    /** 
    * @Description: 
    * @Param: [bean]
    * @return: void
    * @Author: Xtpacz
    * @Date: 2023/2/15
    */
    private void populatebean(Object bean) throws Exception {
        Field[] fields = bean.getClass().getSuperclass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                String beanName = field.getName();
                beanName = StringUtils.uncapitalize(beanName);
                if (beanNameSet.contains(field.getName())) {
                    Object fieldBean = getBean(beanName);
                    if (fieldBean != null) {
                        ReflectionUtils.injectField(field,bean,fieldBean);
                    }
                }
            }
        }
    }
}
