/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 11-Jun-2015
 */
package ca.java.spring.cache.repository;

import java.util.List;

import org.springframework.cache.CacheManager;

import ca.java.spring.cache.domain.YearMakeModel;

/**
 * The Interface YearMakeModelRepository.
 * This is a repository interface for Year/Make/Model cache
 *
 * @author Srinivas Rao
 */
public interface YearMakeModelRepository {
    
    /**
     * To make a call to backend to fetch all the records of Year/Make/Model.
     * This method will return YearMakeModel bean object.
     * This method should not be cached
     *
     * @return the year make model
     */
    YearMakeModel findAllYearMakeModel();
    
    /**
     * This method will return YearMakeModel bean from cache manager.
     * This method should not be cached.
     * 
     * @param cacheManager
     * 		the cache manager
     * @return YearMakeModel
     */
    YearMakeModel findYearMakeModel(CacheManager cacheManager);
    
    /**
     * Clear cache.
     * Remove all cache for Year/Make/Model.
     */
    void clearCache();

    /**
     * Find all years Year/Make/Model cache.
     *
     * @param name
     *            the name
     * @return the list
     */
    List<String> findAllYears(String name);

    /**
     * Find all makes from Year/Make/Model cache.
     *
     * @param year
     *            the year
     * @param cacheManager
     *            the cache manager
     * @return the list
     */
    List<String> findAllMakes(String year, CacheManager cacheManager);

    /**
     * Find all models from Year/Make/Model cache.
     *
     * @param year
     *            the year
     * @param make
     *            the make
     * @param cacheManager
     *            the cache manager
     * @return the list
     */
    List<String> findAllModels(String year, String make, CacheManager cacheManager);
}
