package com.shanghai.common.Intercept;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.shanghai.common.persistence.BaseEntity;

/** 
* @author: YeJR 
* @version: 2018年10月22日 下午4:31:38
* 对从DB获取的数据加上当前时间
*/
@Aspect
@Component
public class AcquireDataTimeIntercept {
	
	/**
	 * 定义Pointcut
	 */
	@Pointcut("execution(* com.shanghai.*.*.dao.*Dao.*(..))")
	public void addTime() {
	}
	
	/**
	 * 对每个切点进行加时间操作
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("addTime()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object obj = proceedingJoinPoint.proceed();
		if (obj != null && obj.getClass() != null) {
			if (BaseEntity.class.isAssignableFrom(obj.getClass())) {
				BaseEntity baseEntity = (BaseEntity) obj;
				baseEntity.setAcquireDataTime(new Date());
				return baseEntity;
			}
		}
        return obj;
    }
}
