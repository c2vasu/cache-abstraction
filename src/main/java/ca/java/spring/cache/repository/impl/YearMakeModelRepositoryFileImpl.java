/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 11-Jun-2015
 */
package ca.java.spring.cache.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import ca.java.spring.cache.domain.ModelData;
import ca.java.spring.cache.domain.YearMakeModel;
import ca.java.spring.cache.repository.YearMakeModelRepository;
import ca.java.spring.cache.web.utilities.FileUtility;


/**
 * This is an implementation of YearMakeModelRepository interface for
 * Year/Make/Model cache.
 * 
 * @author Srinivas Rao
 */
@Repository
public class YearMakeModelRepositoryFileImpl implements YearMakeModelRepository {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(YearMakeModelRepositoryFileImpl.class);
    private static final String ALL_KEY = "ALL";
    private static final String YEAR_MAKE_MODEL_KEY = "yearMakeModelCache";

    @Resource(name = "cacheManager")
    private CacheManager cacheManager;


    /**
     * To make a call to backend to fetch all the records of Year/Make/Model.
     * This method will return YearMakeModel bean object. This method should not
     * be cached
     *
     * @see YearMakeModelRepository#findAllYearMakeModel()
     * @return YearMakeModel
     */
    @Override
    public YearMakeModel findAllYearMakeModel() {
	LOGGER.info("findAllYearMakeModel is running from File System Repository...");
	List<ModelData> rawData = null;	
	List<ModelData> modelEnList = null;
	List<ModelData> modelFrList = null;
	List<ModelData> makeList = null;
	Map<String, Object> modelEnMap =null;
	Map<String, Object> modelFrMap =null;
	Map<String, Object> makeMap =null;
	YearMakeModel year = new YearMakeModel();


	try {
	    rawData = FileUtility.getListFromCSV();

	    //eliminate all duplicate entries
	    TreeMap<String, Object> yearMap = removeDuplicates(rawData);

	    Iterator it = yearMap.entrySet().iterator();
	    while (it.hasNext()) {

		Map.Entry pair = (Map.Entry)it.next();

		//get key
		String yearKey = (String)pair.getKey();

		try {
		    //list of all make's based on the year input
		    makeList = getMakeList(rawData, yearKey);
		    // Record encountered Strings in Map.
		    makeMap = new TreeMap<String, Object>();
		    // Loop over argument list.
		    YearMakeModel make = new YearMakeModel();

		    for (ModelData makeKey : makeList) {
			// If String is not in map, add it to the map.
			if (!makeMap.containsKey(makeKey.getMake())) {
			    makeMap.put(makeKey.getMake(), new Object());
			    make.getEnglish().put(makeKey.getMake(), new YearMakeModel());

			    //list of all make's based on the year input
			    modelEnList = getModelList(rawData, yearKey, makeKey.getMake());
			    // Record encountered Strings in Map.
			    modelEnMap = new TreeMap<String, Object>();
			    // Loop over argument list.
			    YearMakeModel modelEn = new YearMakeModel();

			    for (ModelData modelEnKey : modelEnList) {
				// If String is not in map, add it to the map.
				if (!modelEnMap.containsKey(modelEnKey.getModelEn())) {
				    modelEnMap.put(modelEnKey.getModelEn(),  new Object());
				    modelEn.getEnglish().put(modelEnKey.getModelEn(), new YearMakeModel());
				}
			    }

			    //list of all make's based on the year input
			    modelFrList = getModelList(rawData, yearKey, makeKey.getMake());
			    // Record encountered Strings in Map.
			    modelFrMap = new TreeMap<String, Object>();
			    // Loop over argument list.
			    YearMakeModel modelFr = new YearMakeModel();
			    for (ModelData modelFrKey : modelFrList) {
				// If String is not in map, add it to the map.
				if (!modelFrMap.containsKey(modelFrKey.getModelFr())) {
				    modelFrMap.put(modelFrKey.getModelFr(), new Object());
				    modelFr.getFrench().put(modelFrKey.getModelFr(), new YearMakeModel());
				}
			    }
			    //update make with models
			    make.getEnglish().put(makeKey.getMake(), modelEn);
			    make.getFrench().put(makeKey.getMake(), modelFr);
			}
		    }
		    //add this TreeMap to the parent TreeMap
		    year.getEnglish().put(yearKey, make);


		    //reverse order of the years
		    year = reverseYears(year);

		} catch (Exception e) {
		    e.printStackTrace();
		}finally{
		    it.remove(); // avoids a ConcurrentModificationException
		}

	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return year;
    }

    /**
     * This method will return YearMakeModel bean from cache manager.
     * 
     * @see YearMakeModelRepository#findYearMakeModel(CacheManager)
     * @param cacheManager
     *             the cache manager
     * @return YearMakeModel
     */
    @Override
    public YearMakeModel findYearMakeModel(CacheManager cacheManager) {
	YearMakeModel yearMakeModeBeanInCache = null;
	Ehcache ehcache = (Ehcache) cacheManager.getCache(YEAR_MAKE_MODEL_KEY)
		.getNativeCache();
	yearMakeModeBeanInCache = (YearMakeModel) ehcache.get(ALL_KEY)
		.getObjectValue();
	LOGGER.info("findYearMakeModel is running from EhCache...");
	return yearMakeModeBeanInCache;
    }

    /**
     * Clear cache.
     * 
     * @see YearMakeModelRepository#clearCache()
     */
    @Override
    public String clearCache() {
	YearMakeModel yearMakeModeBean =null;
	String flag = "Unsuccessful";
	LOGGER.info("Method executed to clear the cache. Current time is :: "+ new Date());
	Ehcache ehcache = (Ehcache)cacheManager.getCache("yearMakeModelCache").getNativeCache();
	try {
	    //Get from backend repository
	    LOGGER.info("Call to backend reposioty to fetch all the record from Year/Make/Model:" );
	    yearMakeModeBean = findAllYearMakeModel();
	    if(yearMakeModeBean!=null){
		LOGGER.info("Succesfully operation to fetching all the records of Year/Make/Model from backend");
		//since operation successful clear the cache 
		evictCache();
		LOGGER.warn("yearMakeModelCache cache cleared successful");
		//update with latest cache
		ehcache.put(new Element(ALL_KEY, yearMakeModeBean));
		LOGGER.warn("cache updated with the latest entries");

		YearMakeModel yearMakeModeInCache = (YearMakeModel)ehcache.get(ALL_KEY).getObjectValue();
		LOGGER.info("from cache year list" +yearMakeModeInCache.getEnglish().keySet()+ " was added in cache.");
		LOGGER.info("Task Year Make Model clear cache completed : operation successful");
		
		//successful
		flag = "Successful";
	    }else{
		LOGGER.info("Unsuccesfully operation to fetching all the records of Year/Make/Model from backend");
		flag = "Unsuccessful";
	    }
	} catch (Exception excpetion) {
	    LOGGER.error("Task Year Make Model : operation failed "+excpetion.getMessage());
	    excpetion.printStackTrace();
	}
	return flag;
    }

    /**
     * Find all years Year/Make/Model cache.
     *
     * @param name
     *            the name
     * @return the list
     * 
     * @see YearMakeModelRepository#findAllYears(java.lang.String)
     */
    @Override
    public List<String> findAllYears(String name) {
	List<String> items = new ArrayList<String>();
	LOGGER.info("findAllYears is running from File System Repository...");
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
     * @see YearMakeModelRepository#findAllMakes(String,CacheManager)
     */
    @Override
    public List<String> findAllMakes(String year, CacheManager cacheManager) {
	List<String> items = new ArrayList<String>();
	LOGGER.info("findAllMakes is running File System Repository...");
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
     * @see YearMakeModelRepository#findAllModels(String,
     *      String, CacheManager)
     */
    @Override
    public List<String> findAllModels(String year, String make,
	    CacheManager cacheManager) {
	List<String> items = new ArrayList<String>();
	LOGGER.info("findAllModels is running from File System Repository...");
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

    /**
     * To reverse all years from the given Year/Make/Model
     * @param YearMakeModel
     * @return YearMakeModel
     */
    private YearMakeModel reverseYears(YearMakeModel yearMakeModel) {
	YearMakeModel reverserYearMakeModel = new YearMakeModel();
	Map<String, YearMakeModel> originalYearMap = yearMakeModel.getEnglish();
	Map<String, YearMakeModel> reverserYearMap = new TreeMap<String, YearMakeModel>(Collections.reverseOrder());
	reverserYearMap.putAll(originalYearMap);
	reverserYearMakeModel.setEnglish(reverserYearMap);
	return reverserYearMakeModel;
    }

    /**
     * To remove duplicate entries form the complete list and make the collection of unique
     * @param list
     * @return TreeMap
     */
    public static TreeMap<String, Object> removeDuplicates(List<ModelData> list) {
	// Record encountered Strings in Map.
	TreeMap<String, Object> map = new TreeMap<String, Object>();

	// Loop over argument list.
	for (ModelData key : list) {
	    // If String is not in map, add it to the the list and map.
	    if (!map.containsKey(key.getYear())) {
		map.put(key.getYear(), new Object());
	    }
	}
	return map;
    }
    
    /**
     * Filtered list of ModelData based on year value from the complete list
     * @param originalList
     * @param year
     * @return List<ModelData>
     */
    public static List<ModelData> getMakeList(List<ModelData> originalList, String year){
	List<ModelData> list = new ArrayList<ModelData>();
	for (ModelData data : originalList) {
	    if(year.equals(data.getYear())){
		list.add(data);
	    }
	    
	}
	return list;
    }
    
    /**
     * Filtered list of ModelData based on year and make data value from the complete list
     * @param originalList
     * @param year
     * @return List<ModelData>
     */
    public static List<ModelData> getModelList(List<ModelData> originalList, String year, String make){
	List<ModelData> list = new ArrayList<ModelData>();
	for (ModelData data : originalList) {
	    if(year.equals(data.getYear()) && make.equals(data.getMake())){
		list.add(data);
	    }
	}
	return list;
    }

    /**
     * This method creates model data if data does not exits.
     */
    @Override
    public void createModelData(ModelData data) {
    }

    /**
     * To evict all the entries of cache.
     */
    @CacheEvict(value = "yearMakeModelFindCache", allEntries = true)
    private void evictCache(){
	
    }
}
