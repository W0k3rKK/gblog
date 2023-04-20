package com.gblog.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author: W0k3rKK
 * @description 日志
 * @date: 2023/4/10 15:49
 */
@Aspect
public class LogAop {
	@Pointcut("execution(* com.gblog.controller.*.*(..))")
	public void pointCut() {
	}

	@Before("pointCut()")
	public void logStart(JoinPoint joinPoint) {
		System.out.println(joinPoint.getSignature().getDeclaringTypeName() + "_"
				+ joinPoint.getSignature().getName() + " :运行。。。参数列表是：{" + joinPoint.getArgs() + "}");
	}

}
