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

import ca.java.spring.cache.domain.YearMakeModel;
import ca.java.spring.cache.repository.YearMakeModelRepository;
import ca.java.spring.cache.service.YearMakeModelService;

/** 
 * Implementation of service YearMakeModelCacheService.
 * @author Srinivas Rao
 *
 */
@Service
public class YearMakeModelServiceFileImpl implements YearMakeModelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(YearMakeModelServiceFileImpl.class);

    @Resource(name="cacheRepository")
    private YearMakeModelRepository yearMakeModelRepository; 

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
    public void clearCache() {
	yearMakeModelRepository.clearCache();
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
	LOGGER.info("service findAllYears is running...");
	return yearMakeModelRepository.findAllYears(name);
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
	LOGGER.info("service findAllMakes is running...");
	return yearMakeModelRepository.findAllMakes(year, cacheManager);
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
	LOGGER.info("service findAllModels is running...");
	return yearMakeModelRepository.findAllModels(year, make, cacheManager);
    }
}
