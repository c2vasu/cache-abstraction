/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 11-Jun-2015
 */
package ca.java.spring.cache.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.ehcache.Ehcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

import ca.java.spring.cache.domain.YearMakeModel;
import ca.java.spring.cache.repository.YearMakeModelRepository;
import ca.java.spring.cache.service.impl.YearMakeModelCacheServiceImpl;
import com.avivacanada.gi.pl.quoteandbuy.service.vehicle.VinServiceClient;
import com.avivacanada.gi.pl.quoteandbuy.service.vehicle.cache.EdgeUtil;
import com.avivacanada.gi.pl.quoteandbuy.util.ConstantAndUtil;


/**
 * This is an implementation of YearMakeModelRepository interface for
 * Year/Make/Model cache.
 * 
 * @author Srinivas Rao
 */
@Repository
public class YearMakeModelRepositoryImpl implements YearMakeModelRepository {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(YearMakeModelCacheServiceImpl.class);
    private static final String ALL_KEY = "ALL";
    private static final String YEAR_MAKE_MODEL_KEY = "yearMakeModelCache";

    @Resource(name = "cacheManager")
    private CacheManager cacheManager;
    
    @Autowired
	private VinServiceClient qnbVinService;

    /**
     * To make a call to backend to fetch all the records of Year/Make/Model.
     * This method will return YearMakeModel bean object. This method should not
     * be cached
     *
     * @see com.aviva.global.gi.modules.pl.modules.quote.auto.repository.YearMakeModelRepository
     * #findAllYearMakeModel()
     * @return YearMakeModel
     */
    @Override
    public YearMakeModel findAllYearMakeModel() {
	LOGGER.info("findAllYearMakeModel is running...");
	YearMakeModel yearMakeModelObject = null;
	String yearMakeModelJson = null;
	List<YearMakeModelDTO_AC> yearMakeModelDTOList = null;
	try {

	    yearMakeModelDTOList = EdgeUtil.getYearMakeModelFromPC();
	    if (yearMakeModelDTOList != null) {
		yearMakeModelJson = ConstantAndUtil.jacksonObjMapper
			.writeValueAsString(yearMakeModelDTOList);
		if (yearMakeModelJson != null) {
		    yearMakeModelObject = this.qnbVinService.preProcessEdgeResponseForYearMakeModel(yearMakeModelJson);
		}
	    }
	} 
	catch (Exception e) {  
	    e.printStackTrace(); 
	}
	return yearMakeModelObject;
    }

    /**
     * This method will return YearMakeModel bean from cache manager.
     * 
     * @see com.aviva.global.gi.modules.pl.modules.quote.auto.repository.YearMakeModelRepository
     * #findYearMakeModel(org.springframework.cache.CacheManager)
     * @param cacheManager
     *             the cache manager
     * @return YearMakeModel
     */
    @Override
    public YearMakeModel findYearMakeModel(CacheManager cacheManager) {
	YearMakeModel yearMakeModeBeanInCache = null;
	Ehcache ehcache = (Ehcache) cacheManager.getCache(YEAR_MAKE_MODEL_KEY)
		.getNativeCache();
	LOGGER.warn("ehcache !" + ehcache);
	yearMakeModeBeanInCache = (YearMakeModel) ehcache.get(ALL_KEY)
		.getObjectValue();
	return yearMakeModeBeanInCache;
    }

    /**
     * Clear cache.
     * 
     * @see com.aviva.global.gi.modules.pl.modules.quote.auto.repository.YearMakeModelRepository
     * #clearCache()
     */
    @Override
    public void clearCache() {
    }

    /**
     * Find all years Year/Make/Model cache.
     *
     * @param name
     *            the name
     * @return the list
     * 
     * @see com.aviva.global.gi.modules.pl.modules.quote.auto.repository.YearMakeModelRepository
     * #findAllYears(java.lang.String)
     */
    @Override
    public List<String> findAllYears(String name) {
	List<String> items = new ArrayList<String>();
	LOGGER.info("findAllYears is running...");
	YearMakeModel yearMakeModeBean = null;
	Set years = null;

	yearMakeModeBean = findYearMakeModel(cacheManager);
	if (yearMakeModeBean != null) {
	    years = yearMakeModeBean.getEnglish().keySet();
	    if (!years.isEmpty()) {
		items.addAll(years);
	    }
	}
	return items;
    }

    /**
     * Find all makes from Year/Make/Model cache.
     *
     * @param year
     *            the year
     * @param cacheManager
     *            the cache manager
     * @return the list
     * 
     * @see com.aviva.global.gi.modules.pl.modules.quote.auto.repository.YearMakeModelRepository
     * #findAllMakes(java.lang.String,
     *      org.springframework.cache.CacheManager)
     */
    @Override
    public List<String> findAllMakes(String year, CacheManager cacheManager) {
	List<String> items = new ArrayList<String>();
	LOGGER.info("findAllMakes is running...");
	YearMakeModel yearMakeModeBean = null;
	Set makeKeySet = null;
	YearMakeModel make = null;

	yearMakeModeBean = findYearMakeModel(cacheManager);
	if (yearMakeModeBean != null) {
	    make = yearMakeModeBean.getEnglish().get(year);
	    Map<String, YearMakeModel> english = make.getEnglish();
	    if (!english.isEmpty()) {
		makeKeySet = english.keySet();
		if (!makeKeySet.isEmpty()) {
		    items.addAll(makeKeySet);
		}
	    }
	}
	return items;
    }

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
     * 
     * @see com.aviva.global.gi.modules.pl.modules.quote.auto.repository.YearMakeModelRepository
     * #findAllModels(java.lang.String,
     *      java.lang.String, org.springframework.cache.CacheManager)
     */
    @Override
    public List<String> findAllModels(String year, String make,
	    CacheManager cacheManager) {
	List<String> items = new ArrayList<String>();
	LOGGER.info("findAllModels is running...");
	YearMakeModel yearMakeModeBean = null;
	YearMakeModel makeObject = null;
	YearMakeModel modelObject = null;
	Set modelKeySet = null;
	yearMakeModeBean = findYearMakeModel(cacheManager);
	if (yearMakeModeBean != null) {
	    makeObject = yearMakeModeBean.getEnglish().get(year);
	    Map<String, YearMakeModel> english = makeObject.getEnglish();
	    if (!english.isEmpty()) {
		modelObject = english.get(make);
		if (modelObject != null) {
		    modelKeySet = modelObject.getEnglish().keySet();
		    if (!modelKeySet.isEmpty()) {
			items.addAll(modelKeySet);
		    }
		}
	    }
	}
	return items;
    }

}
