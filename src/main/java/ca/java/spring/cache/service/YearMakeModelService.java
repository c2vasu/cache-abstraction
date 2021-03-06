/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 11-Jun-2015
 */
package ca.java.spring.cache.service;

import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;

import ca.java.spring.cache.domain.ModelData;
import ca.java.spring.cache.domain.YearMakeModel;

/**
 * The service interface for Year/Make/Model cache.
 *
 * @author Srinivas Rao
 */
public interface YearMakeModelService {

    /**
     * To get the cached Year/Make/Model.
     * @return YearMakeModel
     */
    YearMakeModel findAllYearMakeModel();
    
    /**
     * Evict cache.
     * Remove all cache for Year/Make/Model.
     */
    String clearCache();
	
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
    
    /**
     * To insert model data into the repository.
     * @param data
     */
    void createModelData(ModelData data);
}