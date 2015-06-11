/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 11-Jun-2015
 */
package ca.java.spring.cache.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ca.java.spring.cache.domain.YearMakeModel;
import ca.java.spring.cache.repository.YearMakeModelRepository;

/**
 * Fill the cache at startup
 * @author Srinivas Rao
 *
 */
public class CacheListener implements ServletContextListener{

	private static final Logger logger = LoggerFactory.getLogger(CacheListener.class);
	private static final String CACHE_MANAGER = "cacheManager";
	private static final String YEAR_MAKE_MODLE_BEAN = "ymm";
	private static final String ALL_KEY = "ALL";
	private static final String YEAR_MAKE_MODEL_KEY = "yearMakeModelCache";
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
			logger.info("========> contextInitialized() : BEGIN. ");
			ServletContext servletContext = sce.getServletContext();
			if (null == servletContext) {
				logger.warn("servlet context is null !");
			}
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			if (null != ctx) {
				EhCacheCacheManager cacheManager = (EhCacheCacheManager) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(CACHE_MANAGER);
				YearMakeModelRepository yearMakeModelDAOObj = (YearMakeModelRepository) WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(YEAR_MAKE_MODLE_BEAN);
				Ehcache ehcache = (Ehcache)cacheManager.getCache(YEAR_MAKE_MODEL_KEY).getNativeCache();
				YearMakeModel yearMakeModelBeanObj  = yearMakeModelDAOObj.findAllYearMakeModel();
				ehcache.put(new Element(ALL_KEY, yearMakeModelBeanObj));
				YearMakeModel yearMakeModeBeanInCache = (YearMakeModel)ehcache.get(ALL_KEY).getObjectValue();
				logger.info("========> contextInitialized() : YMM" +yearMakeModeBeanInCache.getEnglish().keySet()+ " was added in cache.");
			} else {
				logger.warn("ctx is null !");
			}
			logger.info("========> contextInitialized() : END");
		}

}
