package zti_project.Aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ExceptionHandlerAspect {
    @Around("execution(* zti_project.*.*(..))")
    public Object errorLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        try
        {
            return joinPoint.proceed();
        }
        catch (Throwable throwable)
        {
            System.err.println("Error note: " + throwable);
            System.err.println("Error occurred in method: " + joinPoint.getSignature());
        }
        return null;
    }
}