package com.avivacanada.gi.pl.quoteandbuy.service.vehicle.cache;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avivacanada.gi.pl.quoteandbuy.jsonrpc.EdgeJsonRpcC2Request;
import com.avivacanada.gi.pl.quoteandbuy.util.ConstantAndUtil;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.ConnectionConfigurator;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import edge.webapi.dto.YearMakeModelDTO_AC;

public class EdgeUtil {

	private static final Logger logger = LoggerFactory.getLogger(EdgeUtil.class);
	private final static String USERTOKEN = "TamID";
	private final static String GRANTED_AUTHORITIES = "{\"id\":1,\"username\":\"TamID\",\"securityRealm\":{\"id\":1,\"realm\":\"default\"},\"grantedAuthorities\":[{\"id\":1,\"target\":\"6954297329\",\"authorityType\":\"ACCOUNT\"}]}";
	private final static String PROPERTIES_FILE = "config.properties";
	private final static String URL = "SERVICE_URl";
	private final static String USER_NAME = "PORTAL_USER_NAME";
	private final static String USER_PASSWORD = "PORTAL_USER_PASSWORD";
	private final static String AUTHORIZATION_TYPE = "Basic ";
	private static String PORTAL_USER_NAME;
	private static String PORTAL_USER_PASSWORD;
	private static String AUTHORIZATION;
	private static String SERVICE_URL;
	
	public enum ServiceName {availability, autofill, quote;}

	public enum Method{
		isProductAvailableBasedOnPostalCode(ServiceName.availability),
		autofillBasedOnPostalCode(ServiceName.autofill),
		create(ServiceName.quote),
		save(ServiceName.quote),
		getPAPortalQuestionSet_AC(ServiceName.quote),
		autofillBasedOnVIN_AC(ServiceName.autofill),
		quote(ServiceName.quote),
		getYearMakeModelSet_AC(ServiceName.autofill);

		final ServiceName service;

		Method( ServiceName s){
			this.service = s;
		}
	}

	public static EdgeUtil launchEdgeAccessorForTest(){
		EdgeUtil edgeAccessor = new EdgeUtil();
		PropertiesConfiguration envConfigProp = null;
		try {
			envConfigProp = new PropertiesConfiguration(PROPERTIES_FILE);
			if(!envConfigProp.isEmpty()){
				Map<String, String> env = System.getenv();
				edgeAccessor.SERVICE_URL = env.get("PC_HOST") + "/pc/service/edge/";
				edgeAccessor.PORTAL_USER_NAME = env.get("PC_USER");
				edgeAccessor.PORTAL_USER_PASSWORD = env.get("PC_PW");
				edgeAccessor.AUTHORIZATION =  AUTHORIZATION_TYPE + new String(Base64.encodeBase64((edgeAccessor.PORTAL_USER_NAME + ":" + edgeAccessor.PORTAL_USER_PASSWORD).getBytes()));
			}
		} catch (ConfigurationException e) {
			logger.info("Cannot find properties file source");
			e.printStackTrace();
		}
		
		return edgeAccessor;
	}
	
	//TOTO: make it a common untility -  to be accessed by method - remove from EdgeAccessor
	private Object callEdgeApi(URL edgeServiceUrl, Method method, ArrayList<Object> requestDtoList, Integer requestId) throws Exception {
		List<Object> paramList = new ArrayList<Object>();
		if(requestDtoList != null){
			for(Object requestDto : requestDtoList){
				if(requestDto == null){

				}else if(requestDto instanceof String){
					paramList.add(requestDto);
				}else{
					HashMap<String, Object> reqDataMap = ConstantAndUtil.jacksonObjMapper.readValue(ConstantAndUtil.jacksonObjMapper.writeValueAsString(requestDto), HashMap.class);
					paramList.add(reqDataMap);
				}
			}
		}

		// call method isProductAvailableBasedOnPostalCode on service endpoint /pc/service/availability
		JSONRPC2Session mySession = new JSONRPC2Session(edgeServiceUrl);

		mySession.setConnectionConfigurator(new ConnectionConfigurator() {
			@Override
			public void configure(HttpURLConnection uc) {
				uc.setRequestProperty("usertoken", USERTOKEN);
				uc.setRequestProperty("Granted-Authorities", GRANTED_AUTHORITIES);
				uc.setRequestProperty("Authorization", AUTHORIZATION);
			}
		});

		// Once the client session object is created, you can use to send a series
		// of JSON-RPC 2.0 requests and notifications to it.

		// Construct new request
		//++ requestId; // increase request ID by 1 for each call
		EdgeJsonRpcC2Request request = new EdgeJsonRpcC2Request(method.name(), requestId.toString());
		request.setPositionalParams(paramList);

		//logger.info("request : " + request.toJSONString());

		// Send request
		JSONRPC2Response response = null;

		try {
			response = mySession.send(request);

		} catch (JSONRPC2SessionException e) {
			String errMsg = "Error!!! JSON-RPC session error!";
			logger.error(errMsg, e);
			throw new Exception(errMsg, e);
			// handle exception...
		}

		Object result = null;

		// Print response result / error
		if(response == null){
			String err = "Error! Null response from Guidewire system. Please see Guidewire server log for details.";
			throw new Exception(err);
		}else{
			//logger.info("response : " + response.toJSONString());
			if (response.indicatesSuccess()){
				result = response.getResult(); // result is a Map
			}
			else{
				throw new Exception(response.getError().getMessage());
			}

		}

		return result;
	}
	
	//TODO:// This class needs to be refactor to utilize Edge Accessor	
	/**
	 * This method call PC to get YEAR/MAKE/MODEL
	 * @return List<YearMakeModelDTO_AC>
	 * @throws Exception
	 */
	public static List<YearMakeModelDTO_AC> getYearMakeModelFromPC() throws Exception{
		List<YearMakeModelDTO_AC> result = null;
		EdgeUtil edgeAccessor = EdgeUtil.launchEdgeAccessorForTest();
		URL edgeServiceUrl = new URL(edgeAccessor.SERVICE_URL + Method.getYearMakeModelSet_AC.service);	
		result = (List<YearMakeModelDTO_AC>)edgeAccessor.callEdgeApi(edgeServiceUrl, Method.getYearMakeModelSet_AC, new ArrayList<Object>(), 1001);
		return result;
	}
}
