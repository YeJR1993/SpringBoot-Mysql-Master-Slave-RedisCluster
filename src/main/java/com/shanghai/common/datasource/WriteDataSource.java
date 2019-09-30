package com.shanghai.common.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: YeJR
 * @version: 2018年8月1日 上午11:53:46
 * 写库注解配合切面
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface WriteDataSource {

	DataSourceType value() default DataSourceType.write;
}
