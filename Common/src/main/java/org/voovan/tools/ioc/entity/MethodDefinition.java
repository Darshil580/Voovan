package org.voovan.tools.ioc.entity;

import java.lang.reflect.Method;

/**
 * 方法描述信息
 *
 * @author helyho
 * voovan Framework.
 * WebSite: https://github.com/helyho/voovan
 * Licence: Apache v2 License
 */
public class MethodDefinition extends BeanDefinition {
    private String owner;
    /**
     * 方法对象
     */
    private Method method;

    private BeanDefinition beanDefinition;

    public MethodDefinition(String name, String owner, Method method, boolean singleton, boolean lazy) {
        super(name, method.getDeclaringClass(), singleton, lazy);
        this.owner = owner;
        this.method = method;
    }


    public String getOwner() {
        return owner;
    }

    public Method getMethod() {
        return method;
    }
}
