/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 11-Jun-2015
 */
package ca.java.spring.cache.web.listener;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ca.java.spring.cache.domain.ModelData;
import ca.java.spring.cache.domain.YearMakeModel;
import ca.java.spring.cache.repository.YearMakeModelRepository;
import ca.java.spring.cache.service.YearMakeModelService;
import ca.java.spring.cache.web.utilities.FileUtility;

/**
 * Fill the cache at startup
 * @author Srinivas Rao
 *
 */
public class CacheListener implements ServletContextListener{

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheListener.class);
    private static final String CACHE_MANAGER = "cacheManager";
    private static final String YEAR_MAKE_MODLE_BEAN = "cacheRepository";
    private static final String YEAR_MAKE_MODLE_SERVICE_BEAN = "cacheService";
    private static final String ALL_KEY = "ALL";
    private static final String YEAR_MAKE_MODEL_KEY = "yearMakeModelCache";

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
	LOGGER.info("========> Cache Generation : BEGIN. ");
	ServletContext servletContext = sce.getServletContext();
	if (null == servletContext) {
	    LOGGER.warn("servlet context is null !");
	}
	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	if (null != ctx) {
	    List<ModelData> rawData = null;	
	    YearMakeModelService cacheService = (YearMakeModelService)WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(YEAR_MAKE_MODLE_SERVICE_BEAN);
	    YearMakeModelRepository cacheRepository = (YearMakeModelRepository) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(YEAR_MAKE_MODLE_BEAN);
	    
	    //To insert data into HSQL database while using Database as repository.
	    rawData = FileUtility.getListFromCSV();
	    for(ModelData data : rawData){
		cacheService.createModelData(data);
	    }
	    
	    EhCacheCacheManager cacheManager = (EhCacheCacheManager) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(CACHE_MANAGER);
	    Ehcache ehcache = (Ehcache)cacheManager.getCache(YEAR_MAKE_MODEL_KEY).getNativeCache();
	    YearMakeModel yearMakeModel  = cacheRepository.findAllYearMakeModel();
	    ehcache.put(new Element(ALL_KEY, yearMakeModel));
	    LOGGER.info("========> Cache Generation : YMM was added in cache." +yearMakeModel.getEnglish().keySet());
	    YearMakeModel yearMakeModeInCache = (YearMakeModel)ehcache.get(ALL_KEY).getObjectValue();
	    LOGGER.info("========> Cache Generation : YMM from cache." +yearMakeModeInCache.getEnglish().keySet());
	} else {
	    LOGGER.warn("ctx is null !");
	}
	LOGGER.info("========> Cache Generation : END");
    }

}
