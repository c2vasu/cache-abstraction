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
 * Creation date : May 29, 2015
 * =================================================================================================================
 */
package com.avivacanada.gi.pl.quoteandbuy.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;

import edge.webapi.dto.quote.QuestionSetDTO;


public final class ConstantAndUtil {
    private static final Logger LOGGER = Logger.getLogger(ConstantAndUtil.class);
    private final static Integer DAY = 15;
    public static final String ENV_CONFIG_FILE = "env.config.file";

    @Autowired
    private org.apache.commons.configuration.PropertiesConfiguration envConfigProp;


    @PostConstruct
    public void init(){
	LOGGER.info("In @PostConstruct of " + ConstantAndUtil.class.getName());
    }

    public ConstantAndUtil() throws ConfigurationException {
	try {
	    envConfigProp = new PropertiesConfiguration(System.getProperty(ENV_CONFIG_FILE));
	} catch (ConfigurationException configEx) {
	    LOGGER.error("Error when loading Environment Configuration File.", configEx);
	    throw configEx;
	}
    }


    public final static ObjectMapper jacksonObjMapper 
    = new ObjectMapper(){{ 
	setDateFormat(new org.codehaus.jackson.map.util.ISO8601DateFormat());  // ISO-8601 combined date and time string. Valid to Guidewire system.
	setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }};


    public static QuestionSetDTO pAPersonalAutoPreQual;
    public static QuestionSetDTO pAPortal;

    static {
	try{
	    InputStream questionSetStream = ConstantAndUtil.class.getResourceAsStream("/quoteandbuy/webapi/questionset/QuestionSets.json");
	    ObjectReader questionSetMapReader = jacksonObjMapper.reader(Map.class);

	    Map<String, Object> questionSetMap = questionSetMapReader.readValue(questionSetStream);
	    Set<Entry<String, Object>> questionSetEntrySet = questionSetMap.entrySet();
	    for(Entry<String, Object> entry : questionSetEntrySet){
		ObjectReader questionSetReader = jacksonObjMapper.reader(QuestionSetDTO.class);
		ObjectWriter questionSetWriter = jacksonObjMapper.writer();
		if(entry.getKey().equals("PAPersonalAutoPreQual")){
		    pAPersonalAutoPreQual = questionSetReader.readValue(questionSetWriter.writeValueAsString(entry.getValue()));
		}else if(entry.getKey().equals("PAPortal")){
		    pAPortal = questionSetReader.readValue(questionSetWriter.writeValueAsString(entry.getValue()));
		}
	    }

	    questionSetStream.close();

	}catch(IOException ioEx){
	    ioEx.printStackTrace();
	}
    }


    public static enum ProductAvailabilityPerPostalcode {
	AVAILABLE,
	NOT_AVAILABLE,
	NON_EXISTING_POSTALCODE,
	NON_CANADAIN_POSTALCODE;
    }

    public static enum ChevronClass {
	navdone,
	navon,
	navtodo;
    }


    public static final String CANADIAN_POSTALCODE_FORMAT = "[abceghjklmnprstvxyABCEGHJKLMNPRSTVXY][0-9][a-zA-Z]\\s?[0-9][a-zA-Z][0-9]";

    public static List<LabelValuePair<String, Integer>> getDayList(){
	List<LabelValuePair<String, Integer>> dayList = new ArrayList<LabelValuePair<String, Integer>>();

	for(int i = 1; i <= 31; i++){
	    dayList.add(new LabelValuePair<String, Integer>(String.format("%02d", i), i));
	}

	return dayList;
    }

    public static String ChevronVehicle = "ChevronVehicle";
    public static String ChevronDriver = "ChevronDriver";
    public static String ChevronSummary = "ChevronSummary";


    /**
     * <p>Append hashed content of manifest.json from http server to STATIC FILES URL on JSP in order to force client side to download static resource files for change static resources. 
     * 	If manifest.json stays the same that means no change of static resource. </p>
     * @author cnt_1927072 Wilson Chen
     * @since Sprint 4, Release 1
     * @return the hashed/encoded content of manifest.json from http server referenced by getStaticFilesUrl method of this class
     * @throws Exception Exception will be thrown if there is error when accessing the file manifest.json or when hashing it.
     */
    public String getVer() {

	Scanner s = null;
	try{
	    URL url = new URL(getStaticFilesUrl() + "manifest.json");
	    s = new Scanner(url.openStream());
	    if (s.hasNext()) {
		return new String(Base64.encodeBase64(MessageDigest.getInstance("SHA-256").digest(s.next().getBytes())));
	    } else {
		return "";
	    }

	}catch(Exception ex){
	    // log the exception in log file and return empty string
	    LOGGER.error("Error " + ex.toString() + " when hashing manifest.json on " + getStaticFilesUrl() + ". Returning empty String \"\".");
	    return ""; // return empty string if url access has exception
	}finally{
	    if(s != null){
		s.close();
	    }
	}

    }


    /**
     * @author cnt_1927072 Wilson Chen
     * @since Sprint 4, Release 1
     * @return STATIC FILES URL on JSP to for client side to download static resource files.
     * @throws ConfigurationException 
     */
    public String getStaticFilesUrl() {
	return envConfigProp.getString(EnvConfig.STATIC_FILES_URL);
    }
    /**
     * To return Message broker url for web service.
     * @return WMB_DOMAIN_PORT
     */
    public final String getVinServiceUrl(){
	LOGGER.info("VIN_SERVICE " + envConfigProp.getString(EnvConfig.VIN_SERVICE));
	return envConfigProp.getString(EnvConfig.VIN_SERVICE);
    }

    /**
     * To fudge date based on input number of months to be fudge from input date.
     * @param currentLicense
     * @param numberOfMonths
     * @return YearMonthDay
     */
    public static YearMonthDay furdgeDate(YearMonthDay currentLicense, int numberOfMonths){

	YearMonthDay fudgeDate = null;
	Integer year = currentLicense.getYear();
	Integer month = currentLicense.getMonth();
	Integer numberOfMonthsToFurdge = new Integer(numberOfMonths);
	LOGGER.info("furdgeDate - method : "+ " year "+year + " month "+month);
	//Integer day; //the day is defaulted to 15th of every month.
	if(numberOfMonthsToFurdge < month){ //number of months is less then current license months
	    Integer fudgeMonth = new Integer(month - numberOfMonthsToFurdge);
	    fudgeDate = new YearMonthDay(year, fudgeMonth, DAY);
	    LOGGER.info("furdgeDate - numberOfMonthsToFurdge < month : "+ " year "+fudgeDate.getYear() + " month "+fudgeDate.getMonth());
	}else{ //number of months is greater then current license months
	    Integer fudgeMonth = new Integer(12 + (month - numberOfMonthsToFurdge)); // 12 add to negative value to get the final month
	    year = year -1; //substract one year
	    fudgeDate = new YearMonthDay(year, fudgeMonth, DAY);
	    LOGGER.info("furdgeDate - numberOfMonthsToFurdge > month : "+ " year "+fudgeDate.getYear() + " month "+fudgeDate.getMonth());
	}

	return fudgeDate;
    }
}
