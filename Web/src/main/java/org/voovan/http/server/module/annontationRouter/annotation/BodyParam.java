package org.voovan.http.server.module.annontationRouter.annotation;

import java.lang.annotation.*;

/**
 * 注解路由 BodyParam
 *
 * @author: helyho
 * Voovan Framework.
 * WebSite: https://github.com/helyho/Voovan
 * Licence: Apache v2 License
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BodyParam {

    String value() default "";
    boolean isRequire() default true;
    String defaultVal() default "";
    Class keyType() default Object.class;
    Class valueType() default Object.class;


    //-------------- API 文档相关属性 --------------
    /**
     * 参数说明
     * @return  参数说明
     */
    String description() default "";

    /**
     * 样例数据
     * @return 样例数据
     */
    String example() default "";
}
