package zti_project.Aspect;

import zti_project.Model.Database.Database;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;


@Aspect
public class UseDatabaseAspect {
    @Around("(@within(zti_project.Aspect.UseDatabase) || @annotation(zti_project.Aspect.UseDatabase)) && (execution(* *(..)))")
    public Object useDatabase(ProceedingJoinPoint joinPoint) throws Throwable
    {
        Object result = null;
        try
        {
            Database.createConnection();
            result = joinPoint.proceed();
        }
        catch(Exception ex)
        {
            throw ex;
        }
        finally
        {
            Database.closeConnection();
        }

        return result;
    }
}