/**
 * =================================================================================================================
 * Copyright (c) 2015 by avivacanada.com. 
 * Aviva Canada Insurance Limited. Registered Office (Head Office) Scarborough, 2200-2206 Eglinton Ave. East M1L 4S8.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of AVIVA ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with AVIVA.
 * 
 * Creation date : Jun 11, 2015
 * =================================================================================================================
 */
package ca.java.spring.cache.web.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.java.spring.cache.domain.ModelData;
import ca.java.spring.cache.domain.RequestDTO;
import ca.java.spring.cache.domain.YearMakeModel;
import ca.java.spring.cache.repository.impl.YearMakeModelRepositoryDBImpl;
import ca.java.spring.cache.service.YearMakeModelService;
import ca.java.spring.cache.web.utilities.FileUtility;

@Controller
public class CacheController {
	private static final Logger LOGGER = Logger.getLogger(CacheController.class);
	private static final String ALL_KEY = "ALL";

    @Resource(name="cacheManager")
    private EhCacheCacheManager cacheManager;

    @Resource(name="cacheService")
    private YearMakeModelService cacheService;
    
    @Resource(name="databaseRepository")
    private YearMakeModelRepositoryDBImpl databaseRepository;
    
    @RequestMapping("/index")
    public String index() {
	return "index";
    }
    
    @RequestMapping("/insert")
    public String insert() {
	List<ModelData> rawData = null;	
	
	rawData = FileUtility.getListFromCSV();
	LOGGER.info("Insert...");
	for(ModelData data : rawData){
	    LOGGER.info(data);
	    databaseRepository.createModelData(data);
	}
	
	return "insert";
    }

    /**
     * Front controller for vehicle page to filter Make based on selected Year option
     * @param RequestDTO
     * @return makeKey
     */
    @RequestMapping(value="/makeService", method = RequestMethod.POST)
    public @ResponseBody List<String> makeService(@RequestBody final  RequestDTO request) {    
	String yearFiler=null;
	List<String> makes = null;
	if ((request!=null)&&(request.getParams()!=null)&&(request.getParams().size()>0)) {
	    yearFiler= (String) request.getParams().get(0);
	    try {
		makes = cacheService.findAllMakes(yearFiler, cacheManager);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return makes;
    }

    /**
     * Front controller for vehicle page to filter Model based on selected Make option
     * @param RequestDTO
     * @return makeKey
     */
    @RequestMapping(value="/modelService", method = RequestMethod.POST)
    public @ResponseBody List<String> modelService(@RequestBody final  RequestDTO request) {    
	String yearFiler=null;
	String makeFilter = null;
	List<String> models = null;
	if ((request!=null)&&(request.getParams()!=null)&&(request.getParams().size()>1)) {
	    yearFiler = (String)request.getParams().get(0);
	    makeFilter = (String)request.getParams().get(1);
	    try {
		models = cacheService.findAllModels(yearFiler, makeFilter, cacheManager);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return models;
    }

    /**
     * To service to be called to clear YEAR/MAKE/MODEL cache
     * @return string
     */
    @RequestMapping(value="/clearCache", method = RequestMethod.GET)
    public @ResponseBody String clearCache() {  

	YearMakeModel yearMakeModelObj =null;
	LOGGER.info("Method executed to clear the cache. Current time is :: "+ new Date());
	Ehcache ehcache = (Ehcache)cacheManager.getCache("yearMakeModelCache").getNativeCache();
	try {
	    yearMakeModelObj  = cacheService.findAllYearMakeModel();
	    if(yearMakeModelObj!=null){
		//since operation successful clear the cache 
		cacheService.clearCache();
		LOGGER.warn("yearMakeModelCache cache cleared successful");
		//update with latest cache
		ehcache.put(new Element(ALL_KEY, yearMakeModelObj));
		LOGGER.warn("cache updated with the latest entries");

		YearMakeModel yearMakeModeInCache = (YearMakeModel)ehcache.get(ALL_KEY).getObjectValue();
		LOGGER.info("from cache year list" +yearMakeModeInCache.getEnglish().keySet()+ " was added in cache.");
		LOGGER.info("Task Year Make Model clear cache completed : operation successful");

	    }
	} catch (Exception excpetion) {
	    LOGGER.error("Task Year Make Model : operation failed "+excpetion.getMessage());
	    excpetion.printStackTrace();
	}
	return "true";
    }
}
