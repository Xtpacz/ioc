package com.xtpacz.utils;

public class ClassUtils {
    
    /** 
    * @Description: get the classLoader in the context
    * @Param: []
    * @return: java.lang.ClassLoader
    * @Author: Xtpacz
    * @Date: 2023/2/15
    */
    private static ClassLoader getDefaultClassLoader(){
        // 返回该线程的 类加载器(ClassLoader)的 上下文,以便运行在该线程的代码在加载类和资源时使用
        return Thread.currentThread().getContextClassLoader();
    }
    
    /** 
    * @Description: load class by className
    * @Param: [className]
    * @return: java.lang.Class
    * @Author: Xtpacz
    * @Date: 2023/2/15
    */
    public static Class loadClass(String className){
        try {
            // 在类加载器的上下文中，通过className获取这个类
            return getDefaultClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
//            System.out.println("发生类找不到的异常咯");
            // 打印出 类找不到 的异常
            e.printStackTrace();
        }
        // 类找不到，所以说就返回空
        return null;
    }
}
