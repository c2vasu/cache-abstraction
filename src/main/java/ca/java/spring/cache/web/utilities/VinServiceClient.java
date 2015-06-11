package com.avivacanada.gi.pl.quoteandbuy.service.vehicle;

import com.aviva.global.gi.modules.pl.modules.quote.auto.domain.YearMakeModel;
import com.avivacanada.gi.pl.quoteandbuy.dto.VinResponseDTO;

public interface VinServiceClient {

	/**
	 * To call the VIN Service Client
	 * @param vinValue
	 * @return VinResponseDTO
	 */
	public abstract VinResponseDTO callVINService(String vinValue);

	/**
	 * To set the customized error message based on the error code.
	 * 
	 * @param vinResponse
	 * @param errorCode
	 * @return VinResponseDTO
	 */
	public abstract VinResponseDTO customErrorMessage(
			VinResponseDTO vinResponse, String errorCode);

	/**
	 * To pre-process edge response to filter out the unique records required for Year Make Model drop-down list
	 * The filtered object is stored in form of a YearMakeModelBean.
	 * @param edgeResponse
	 * @return edgeResponse
	 * @author tcs_1979453
	 */
	public abstract YearMakeModel preProcessEdgeResponseForYearMakeModel(
			Object edgeResponse);

}