/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 22-Jun-2015
 */

package ca.java.spring.cache.web.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ca.java.spring.cache.domain.RequestDTO;
import ca.java.spring.cache.domain.ViewData;
import ca.java.spring.cache.service.YearMakeModelService;

@Controller
public class CacheController {
    private static final Logger LOGGER = Logger.getLogger(CacheController.class);
    private static final String ALL_KEY = "ALL";

    @Resource(name="cacheManager")
    private EhCacheCacheManager cacheManager;

    @Resource(name="cacheService")
    private YearMakeModelService cacheService;

    /*@Resource(name="databaseRepository")
    private YearMakeModelRepositoryDBImpl databaseRepository;*/

    @Autowired
    @Qualifier("viewData")
    private ViewData viewData;


    /*@RequestMapping("/insert")
    public String insert() {
	List<ModelData> rawData = null;	

	rawData = FileUtility.getListFromCSV();
	LOGGER.info("Insert...");
	for(ModelData data : rawData){
	    LOGGER.info(data);
	    databaseRepository.createModelData(data);
	}

	return "insert";
    }*/
    
    
    /**
     * The front controller for Year/Make/Model using HSQL Database as Repository.
     * @param viewData
     * @return ModelAndView
     */
    @ModelAttribute("viewData")
    @RequestMapping(value="/index", method=RequestMethod.GET)
    public ModelAndView yearMakeModelUsingDatabase(@ModelAttribute("viewData") ViewData viewData){

	List<String> years = null;
	ModelAndView mav = new ModelAndView("cache");
	//from cache service
	years = cacheService.findAllYears(ALL_KEY);
	mav.addObject("years", years);
	return mav;

    }

    /**
     * The front controller for Year/Make/Model using File System as Repository.
     * @param viewData
     * @return ModelAndView
     */
    /*@ModelAttribute("viewData")
    @RequestMapping(value="/index", method=RequestMethod.GET)
    public ModelAndView yearMakeModelUsingFileSystem(@ModelAttribute("viewData") ViewData viewData){
	List<String> years = null;
	ModelAndView mav = new ModelAndView("cache");
	//from cache service
	years = cacheService.findAllYears(ALL_KEY);
	mav.addObject("years", years);
	return mav;

    }*/

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
	return cacheService.clearCache();
    }
}
