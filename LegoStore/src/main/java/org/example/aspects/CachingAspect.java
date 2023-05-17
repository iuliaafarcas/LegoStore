package org.example.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.model.LegoStore;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Aspect
@Component
@Order(3)
public class CachingAspect {
    private static final Logger logger = Logger.getLogger(CachingAspect.class.getName());
    private List<LegoStore> cache = new ArrayList<>();

    @Pointcut("execution(public * org.example.service.LegoStoreService.getList()) && !within(CachingAspect)")
    private void getListOperation() {
    }


    @Pointcut("execution(public * org.example.repository.LegoStoreRepository.findById(..)) && !within(CachingAspect) && args(id)")
    private void getObjectByIdOperation(Long id) {
    }


    @Pointcut("execution(public * org.example.repository.LegoStoreRepository.save(..)) && !within(CachingAspect)")
    private void saveOperation() {
    }

    ;

    @Pointcut("execution(public * org.example.repository.LegoStoreRepository.deleteById(..)) && !within(CachingAspect) && args(id)")
    private void deleteByIdOperation(Long id) {
    }

    ;

    @Around("getListOperation()")
    public Object getListOperationCache(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!cache.isEmpty()) {
            logger.info("Cache retrieved");
            return cache;
        }
        Object returnObject = joinPoint.proceed();
        cache = new ArrayList<>((List<LegoStore>) returnObject);
        logger.info("Objects cached");
        return returnObject;
    }


    @Around("getObjectByIdOperation(id)")
    public Object getObjectByIdOperationCache(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        Optional<LegoStore> legoOptional = findLegoInCache(id);
        if (legoOptional.isPresent()) {
            logger.info("Object returned from cache");
            return legoOptional;
        } else {
            Object returnObject = joinPoint.proceed();
            Optional<LegoStore> returnOptional = (Optional<LegoStore>) returnObject;
            if (returnOptional.isPresent()) {
                cache.add(returnOptional.get());
                logger.info("Object cached");
            }
            return returnObject;
        }
    }

    @Around("saveOperation()")
    public Object saveOperationCache(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnObject = joinPoint.proceed();
        LegoStore legoReturnObject = (LegoStore) returnObject;
        Optional<LegoStore> legoWithIdInCache = findLegoInCache(legoReturnObject.getId());
        if (legoWithIdInCache.isPresent()) {
            logger.info("Object cashed saved");
            cache.remove(legoWithIdInCache.get());
        } else {
            logger.info("Object cached");
        }
        cache.add(legoReturnObject);
        return returnObject;

    }

    @Around("deleteByIdOperation(id)")
    public Object deleteByIdOperationCache(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        Object returnObject = joinPoint.proceed();
        findLegoInCache(id).ifPresent(lego -> cache.remove(lego));
        logger.info("Object removed from cache");
        return returnObject;
    }


    private Optional<LegoStore> findLegoInCache(Long id) {
        return cache.stream().filter(lego -> id.equals(lego.getId())).findFirst();
    }


}
