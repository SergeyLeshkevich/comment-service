package ru.clevertec.comment.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.comment.entity.dto.CommentResponse;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Aspect class providing caching functionality for CommentServiceImpl methods.
 *
 * @author Sergey Leshkevich
 * @version 1.0
 */
@Aspect
@Component
@Profile("dev")
public class CommentAspect {

    private final CacheFactory<Long, CommentResponse> cacheFactory;
    private final Cache<Long, CommentResponse> cache;
    private final Lock lock;

    /**
     * Constructor for CommentAspect.
     *
     * @param cacheFactory Factory for creating the cache.
     */
    public CommentAspect(CacheFactory<Long, CommentResponse> cacheFactory) {
        this.cacheFactory = cacheFactory;
        this.cache = cacheFactory.createCache();
        this.lock = new ReentrantLock();
    }

    /**
     * Implements cache via AOP for the 'get' method in CommentServiceImpl, providing caching.
     */
    @Around("ru.clevertec.comment.aop.CommentPointcut.pointcutGetMethod()")
    public CommentResponse get(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            Long id = (Long) joinPoint.getArgs()[0];
            CommentResponse commentResponse = cache.get(id);
            if (commentResponse == null) {
                commentResponse = (CommentResponse) joinPoint.proceed();
            }
            cache.put(commentResponse.id(), commentResponse);
            return commentResponse;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Implements cache via for the 'create' method in CommentServiceImpl, providing caching.
     */
    @Around("ru.clevertec.comment.aop.CommentPointcut.pointcutCreateMethod()")
    public CommentResponse create(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            CommentResponse response = (CommentResponse) joinPoint.proceed();
            Long id = response.id();
            cache.put(id, response);
            return response;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Implements cache via for the 'archive' method in CommentServiceImpl, removing item from cache.
     */
    @Around("ru.clevertec.comment.aop.CommentPointcut.pointcutArchiveMethod()")
    public void archived(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            Long id = (Long) joinPoint.getArgs()[0];
            joinPoint.proceed();
            cache.removeByKey(id);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Implements cache via for the 'update' method in CommentServiceImpl, updating cache.
     */
    @Around("ru.clevertec.comment.aop.CommentPointcut.pointcutUpdateMethod()")
    public CommentResponse patch(ProceedingJoinPoint joinPoint) throws Throwable {
        lock.lock();
        try {
            Long id = (Long) joinPoint.getArgs()[0];
            CommentResponse response = (CommentResponse) joinPoint.proceed();
            cache.removeByKey(id);
            cache.put(id, response);
            return response;
        } finally {
            lock.unlock();
        }
    }
}
