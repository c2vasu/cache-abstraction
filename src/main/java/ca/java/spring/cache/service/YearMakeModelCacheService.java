/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 11-Jun-2015
 */
package ca.java.spring.cache.service;

import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * The service interface for Year/Make/Model cache.
 *
 * @author Srinivas Rao
 */
public interface YearMakeModelCacheService {

    /**
     * Evict cache.
     * Remove all cache for Year/Make/Model.
     */
    @CacheEvict(value = "yearMakeModelFindCache", allEntries = true)
    void clearCache();
	
    /**
     * Find all years.
     * This method to be cached.
     * 
     * @param name
     *            the name
     * @return the list
     */
    @Cacheable(value = "yearsFindCache", key = "#name")
    List<String> findAllYears(String name);
	
    /**
     * Find all makes.
     * This method to be cached.
     *
     * @param year
     *            the year
     * @param cacheManager
     *            the cache manager
     * @return the list
     */
    @Cacheable(value = "makesFindCache", key = "#year")
    List<String> findAllMakes(String year, CacheManager cacheManager);
	
    /**
     * Find all models.
     * This method to be cached.
     *
     * @param year
     *            the year
     * @param make
     *            the make
     * @param cacheManager
     *            the cache manager
     * @return the list
     */
    @Cacheable(value = "modelsFindCache", key = "#year + #make")
    List<String> findAllModels(String year, String make, CacheManager cacheManager);
}