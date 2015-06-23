/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 11-Jun-2015
 */
package ca.java.spring.cache.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import ca.java.spring.cache.domain.ModelData;
import ca.java.spring.cache.domain.YearMakeModel;
import ca.java.spring.cache.repository.YearMakeModelRepository;
import ca.java.spring.cache.service.YearMakeModelService;

/** 
 * Implementation of service YearMakeModelCacheService.
 * @author Srinivas Rao
 *
 */
@Service
public class YearMakeModelServiceImpl implements YearMakeModelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(YearMakeModelServiceImpl.class);

    @Resource(name="cacheRepository")
    private YearMakeModelRepository cacheRepository; 

   /**
    * To get all the entries of Year/Make/Model.
    */
    @Override
    public YearMakeModel findAllYearMakeModel() {
	return null;
    }

    /**
     * Remove all data in cache.
     */
    @Override
    public String clearCache() {
	return cacheRepository.clearCache();
    }

    /**
     * Find years list from Year/Make/Model cache.
     * 
     * @param name     
     *             the name
     * @return List<String>
     */
    @Override
    public List<String> findAllYears(String name) {
	LOGGER.info("findAllYears("+name+")"+" is running Service...");
	return cacheRepository.findAllYears(name);
    }

    /**
     * Find makes list from Year/Make/Model cache.
     * 
     * @param year
     *            the year
     * @param cacheManager     
     *            the cache manager
     * @return List<String>
     */
    @Override
    public List<String> findAllMakes(String year, CacheManager cacheManager) {
	LOGGER.info("findAllMakes("+year+")"+" is running Service...");
	return cacheRepository.findAllMakes(year, cacheManager);
    }

    /**
     * Find models list from Year/Make/Model cache.
     *
     * @param year
     *            the year
     * @param make
     *            the make
     * @param cacheManager
     *            the cache manager
     * @return List<String>
     */
    @Override
    public List<String> findAllModels(String year, String make, CacheManager cacheManager) {
	LOGGER.info("findAllModels("+year+make+")"+" is running Service...");
	return cacheRepository.findAllModels(year, make, cacheManager);
    }

    /* 
     * @see ca.java.spring.cache.service.YearMakeModelService#createModelData(ca.java.spring.cache.domain.ModelData)
     */
    @Override
    public void createModelData(ModelData data) {
	cacheRepository.createModelData(data);
    }
}
