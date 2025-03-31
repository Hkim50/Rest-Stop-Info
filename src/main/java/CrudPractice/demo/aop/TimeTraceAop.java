package CrudPractice.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("dev")
public class TimeTraceAop {

    @Around("execution(* CrudPractice.demo..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        Long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());

        Object result = joinPoint.proceed();

        Long executionTime = System.currentTimeMillis() - start;
        System.out.println("End: " + joinPoint.toString() + " " + executionTime + "ms");
        return result;

    }
}
