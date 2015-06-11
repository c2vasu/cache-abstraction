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
 * Creation date : May 20, 2015
 * =================================================================================================================
 */
package com.avivacanada.gi.pl.quoteandbuy.service.vehicle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aviva.global.gi.modules.pl.modules.quote.auto.domain.YearMakeModel;
import com.avivacanada.gi.pl.quoteandbuy.dto.VinResponseDTO;
import com.avivacanada.gi.pl.quoteandbuy.service.vehicle.YearMakeModeBean;
import com.avivacanada.services.ClientIDType;
import com.avivacanada.services.ClientInfo;
import com.avivacanada.services.LanguageType;
import com.avivacanada.services.vin.ValidateVINRequest;
import com.avivacanada.services.vin.ValidateVINRequestType;
import com.avivacanada.services.vin.ValidateVINResponse;
import com.avivacanada.services.vin.ValidateVINResponseType;
import com.avivacanada.services.vin.ValidateVINResponseType.VINInfo;
import com.avivacanada.services.vin.ValidateVINResponseType.VINInfo.DecodeErrors;
import com.avivacanada.services.vin.ValidateVINResponseType.VINInfo.DecodeErrors.DecodeError;
import com.avivacanada.services.vin.ValidateVINResponseType.VINInfo.MarketingMakes;
import com.avivacanada.services.vin.ValidateVINResponseType.VINInfo.MarketingMakes.MarketingMake;
import com.avivacanada.services.vin.ValidateVINResponseType.VINInfo.MarketingMakes.MarketingMake.Models;
import com.avivacanada.services.vin.ValidateVINResponseType.VINInfo.MarketingMakes.MarketingMake.Models.Model;
import com.avivacanada.services.vin.Vin;
import com.avivacanada.services.vin.VinService;
import com.jayway.jsonpath.JsonPath;


public class VinServiceClientImpl implements VinServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(VinServiceClientImpl.class);
	private static final String ERROR_CODE_8 = "8";
	private static final String ERROR_CODE_8_MESSAGE = "Sorry We are unable to retrieve you Car Information. Please call our Customer Care Centre at XXX-XXX-XXXX to discuss how we can help.";
	private static final int YEAR_LIMIT = 21;
	private static final String CLASSIC_YEAR_CODE = "C";
	private static final String CLASSIC_YEAR_CODE_ERROR_MESSAGE = "Sorry but no match could be found.  You can check the VIN and try again or select the Year, Make and Model below.";

	/* (non-Javadoc)
	 * @see com.avivacanada.gi.pl.quoteandbuy.service.vehicle.QuoteAndBuyVinService#callVINService(java.lang.String)
	 */
	@Override
	public VinResponseDTO callVINService(String vinValue){
		VinResponseDTO vinResponse = null;

		try {
			vinResponse = new VinResponseDTO();
			final ClientIDType clientIDValue = ClientIDType.MP;
			final String clientNamevalue = "MP";
			LanguageType langValue = LanguageType.EN_US;
			String clientGUIDValue = UUID.randomUUID().toString();
			XMLGregorianCalendar transactionTimeValue = null;
			try {
				Date date = new Date();
				GregorianCalendar gregory = new GregorianCalendar();
				gregory.setTime(date);
				transactionTimeValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
				logger.info("Default Values initialized " + "Language :"+langValue +"UUID :"+clientGUIDValue+ "ClientID Type :"+clientNamevalue+ "Transaction Time :"+transactionTimeValue);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}

			Vin port =	new VinService().getVinSOAP();

			ValidateVINRequest parameters = new ValidateVINRequest();

			ValidateVINRequestType vinRequestTypeValue = new ValidateVINRequestType();
			vinRequestTypeValue.setVIN(vinValue);
			ClientInfo clientInfoValue = new ClientInfo();
			clientInfoValue.setTransactionTime(transactionTimeValue);
			clientInfoValue.setClientName(clientNamevalue);
			clientInfoValue.setClientID(clientIDValue);
			clientInfoValue.setClientGUID(clientGUIDValue);

			parameters.setClientInfo(clientInfoValue);
			parameters.setVINInfo(vinRequestTypeValue);
			parameters.setLanguage(langValue);
			logger.info("Input " +"VIN : "+vinValue);
			logger.info("Input " +"Port : " +port);
			ValidateVINResponse response = port.validateVin(parameters);

			if (response!=null) {
				String flag = response.getIsSuccessful().value();
				String yearCode=null;
				String yearValue=null;
				ValidateVINResponseType responseType = response.getVINResponse();
				vinResponse.setSuccessful(flag);
				if("Y".equalsIgnoreCase(flag)){
					if (responseType!=null) {
						VINInfo vinInfo = responseType.getVINInfo();
						if (vinInfo!=null) {
							yearCode = vinInfo.getYearCode();
							yearValue= vinInfo.getYearValue();
							logger.info("yearValue " + yearValue);
							vinResponse.setYearCode(vinInfo.getYearCode());
							vinResponse.setYear(vinInfo.getYearValue());
							vinResponse.setVin(vinInfo.getVIN());
							vinResponse.setVehicleType(vinInfo.getVehicleType());
							MarketingMakes marketingMakes =  vinInfo.getMarketingMakes();
							List<MarketingMake> marketingMakeList = marketingMakes.getMarketingMake();
							for (MarketingMake make : marketingMakeList) {
								logger.info("Output " + "Make :"+make.getDescription());
								vinResponse.setMake(make.getDescription());
								Models modelObj = make.getModels();
								List<Model> modelList = modelObj.getModel();
								List<String> modelListValues = new ArrayList<String>();
								for(Model model : modelList){
									logger.info("Output " + "Model :"+model.getDescription());
									modelListValues.add(model.getDescription());
								}
								//setting model array list
								if(!modelListValues.isEmpty()){
									vinResponse.setModel(modelListValues);
								}
							}
							//for classic VINs and also for valid VINs year less then the 21 year
							if (yearValue!=null && !"".equals(yearValue)) {
								int currentYear = getCurrentYear();
								int yearLimitValue = currentYear - YEAR_LIMIT;
								int year = Integer.parseInt(yearValue);
								//VIN response YEAR value should be greater than 21 year from current year.
								if (year < yearLimitValue) {
									logger.info(year + " <  " + yearLimitValue);
									vinResponse = customErrorMessage(
											vinResponse, CLASSIC_YEAR_CODE);
								}
							}
							//for classic VINs
							if(yearCode!=null && CLASSIC_YEAR_CODE.equalsIgnoreCase(yearCode)){
								logger.info("Output " + "Classic VIN : "+ CLASSIC_YEAR_CODE +" :"+CLASSIC_YEAR_CODE_ERROR_MESSAGE);
								vinResponse = customErrorMessage(vinResponse, CLASSIC_YEAR_CODE);
							}
						}
					}
				}else{
					if (responseType!=null) {
						VINInfo vinInfo = responseType.getVINInfo();
						if (vinInfo!=null) {
							DecodeErrors objDecodeErrors = vinInfo.getDecodeErrors();
							// for valid errors messages
							if(objDecodeErrors!=null){
								List<DecodeError> decodeErrorList = objDecodeErrors.getDecodeError();
								for (DecodeError decodeError : decodeErrorList) {
									String errorMessage = decodeError.getMessage();
									String errorCode = decodeError.getCode();
									logger.info("Output " + "Error Code :"+errorCode+ " - Error Messge:"+errorMessage);
									vinResponse.setErrorMessage(errorMessage);
									vinResponse.setErrorCode(errorCode);
									// set the custom error message
									vinResponse = customErrorMessage(vinResponse, errorCode);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return vinResponse;
	}

	/* (non-Javadoc)
	 * @see com.avivacanada.gi.pl.quoteandbuy.service.vehicle.QuoteAndBuyVinService#customErrorMessage(com.avivacanada.gi.pl.quoteandbuy.dto.VinResponseDTO, java.lang.String)
	 */
	@Override
	public VinResponseDTO customErrorMessage(VinResponseDTO vinResponse, String errorCode){
		if(vinResponse!=null && (!"".equals(errorCode) || errorCode!=null)){
			if(errorCode.equals(ERROR_CODE_8)){ //custom error message based on error code
				vinResponse.setCustomErrorMessage(ERROR_CODE_8_MESSAGE);
				logger.info("Output " + "Error Code :"+ERROR_CODE_8+ " - Error Messge customized:"+ERROR_CODE_8_MESSAGE);
			}else if(errorCode.equals(CLASSIC_YEAR_CODE)){ //for classic VINs custom error message
				vinResponse.setCustomErrorMessage(CLASSIC_YEAR_CODE_ERROR_MESSAGE);
				//set year code as error code for front end validation.
				vinResponse.setErrorCode(CLASSIC_YEAR_CODE);
				logger.info("Output " + "Error Code : "+CLASSIC_YEAR_CODE +" - Error Messge customized:"+CLASSIC_YEAR_CODE_ERROR_MESSAGE);
			}else{ //custom error message defaulted WMB error message
				vinResponse.setCustomErrorMessage(vinResponse.getErrorMessage());
			}
		}
		return vinResponse;
	}

	/* (non-Javadoc)
	 * @see com.avivacanada.gi.pl.quoteandbuy.service.vehicle.QuoteAndBuyVinService#preProcessEdgeResponseForYearMakeModel(java.lang.Object)
	 */
	@Override
	public YearMakeModel preProcessEdgeResponseForYearMakeModel(Object edgeResponse) {

		final String jsonPathYear = "$[*].year";
		List<String> modelEnList = null;
		List<String> modelFrList = null;
		List<String> makeList = null;
		Map<String, Object> modelEnMap =null;
		Map<String, Object> modelFrMap =null;
		Map<String, Object> makeMap =null;
		List<String> yearList = new ArrayList<String>();
		YearMakeModel year = new YearMakeModel();

		String jsonString = edgeResponse.toString();	

		try {
			yearList = getfilteredList(jsonString, jsonPathYear);

			//eliminate all duplicate entries
			TreeMap<String, Object> yearMap = removeDuplicates(yearList);

			Iterator it = yearMap.entrySet().iterator();
			while (it.hasNext()) {

				Map.Entry pair = (Map.Entry)it.next();

				//get key
				String yearKey = (String)pair.getKey();

				//json path script expression
				String jsonPathMake = "$[?(@.year=='"+ yearKey +"')].make";

				try {
					//list of all make's based on the year json path input
					makeList = getfilteredList(jsonString, jsonPathMake);
					// Record encountered Strings in Map.
					makeMap = new TreeMap<String, Object>();
					// Loop over argument list.
					YearMakeModel make = new YearMakeModel();

					for (String makeKey : makeList) {
						// If String is not in map, add it to the map.
						if (!makeMap.containsKey(makeKey)) {
							makeMap.put(makeKey, new Object());
							make.getEnglish().put(makeKey, new YearMakeModel());

							//json path script expression
							String jsonPathModelEn = "$[?(@.year=='"+ yearKey +"' &&  @.make=='"+makeKey+"')].model_EN";

							//list of all make's based on the year json path input
							modelEnList = getfilteredList(jsonString, jsonPathModelEn);
							// Record encountered Strings in Map.
							modelEnMap = new TreeMap<String, Object>();
							// Loop over argument list.
							YearMakeModel modelEn = new YearMakeModel();

							for (String modelEnKey : modelEnList) {
								// If String is not in map, add it to the map.
								if (!modelEnMap.containsKey(modelEnKey)) {
									modelEnMap.put(modelEnKey,  new Object());
									modelEn.getEnglish().put(modelEnKey, new YearMakeModel());
								}
							}

							//json path script expression
							String jsonPathModelFr = "$[?(@.year=='"+ yearKey +"' &&  @.make=='"+makeKey+"')].model_FR";
							//list of all make's based on the year json path input
							modelFrList = getfilteredList(jsonString, jsonPathModelFr);
							// Record encountered Strings in Map.
							modelFrMap = new TreeMap<String, Object>();
							// Loop over argument list.
							YearMakeModel modelFr = new YearMakeModel();
							for (String modelFrKey : modelFrList) {
								// If String is not in map, add it to the map.
								if (!modelFrMap.containsKey(modelFrKey)) {
									modelFrMap.put(modelFrKey, new Object());
									modelFr.getFrench().put(modelFrKey, new YearMakeModel());
								}
							}
							//update make with models
							make.getEnglish().put(makeKey, modelEn);
							make.getFrench().put(makeKey, modelFr);
						}
					}
					//add this TreeMap to the parent TreeMap
					year.getEnglish().put(yearKey, make);

					//to restrict year to 21 year
					year = yearRestriction(year, YEAR_LIMIT);

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
	 * To filter all sorted recodes based on the year limit.
	 * @param processedYearMakeModel
	 * @param yearLimit
	 * @return VinResponseDTO
	 */
	public static YearMakeModel yearRestriction(YearMakeModel processedYearMakeModel, int yearLimit){
		YearMakeModel filteredYear = new YearMakeModel();
		if (yearLimit > 0) {
			int currentYear = getCurrentYear();
			int yearLimitValue = currentYear - yearLimit;
			if(processedYearMakeModel!=null){
				Iterator it = processedYearMakeModel.getEnglish().entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					logger.info(pair.getKey() + " = " + pair.getValue());
					int year = Integer.parseInt(pair.getKey().toString());
					if(year > yearLimitValue){
						logger.info(year + " >  " + yearLimitValue);
						filteredYear.getEnglish().put(pair.getKey().toString(), (YearMakeModel)pair.getValue());
					}
					it.remove(); // avoids a ConcurrentModificationException
				}
			}
		}
		return filteredYear;
	}

	/**
	 * Get current system year
	 * @return year
	 */
	public static int getCurrentYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * To remove duplicate entries form the complete list and make the collection of unique
	 * @param list
	 * @return TreeMap
	 */
	public static TreeMap<String, Object> removeDuplicates(List<String> list) {
		// Record encountered Strings in Map.
		TreeMap<String, Object> map = new TreeMap<String, Object>();

		// Loop over argument list.
		for (String key : list) {
			// If String is not in map, add it to the the list and map.
			if (!map.containsKey(key)) {
				map.put(key, new Object());
			}
		}
		return map;
	}

	public static List<String> getfilteredList(final String json, String jsonPathInput){
		List<String> list = new ArrayList<String>();
		if (!"".equals(json) && json!=null) {
			list = JsonPath.read(json, jsonPathInput);
		}
		return list;
	}

	/**
	 * To print the the YearMakeModeBean
	 * @param year
	 */
	public static void printYearMakeModel(YearMakeModeBean year){
		Iterator it = year.getEnglish().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			logger.info("=Year Key "+ pair.getKey());
			YearMakeModeBean make = (YearMakeModeBean)pair.getValue();
			Iterator itMake = make.getEnglish().entrySet().iterator();
			while (itMake.hasNext()) {
				Map.Entry pairMake = (Map.Entry)itMake.next();
				logger.info("====Make Key " + pairMake.getKey());
				YearMakeModeBean model = (YearMakeModeBean)pairMake.getValue();
				Iterator itModel = model.getEnglish().entrySet().iterator();
				while (itModel.hasNext()) {
					Map.Entry pairModel = (Map.Entry)itModel.next();
					logger.info("===========Model Key " + pairModel.getKey());
				}
			}
		}
	}

}
