/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 11-Jun-2015
 */
package ca.java.spring.cache.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Srinivas Rao
 *
 */
public class YearMakeModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Map<String, YearMakeModel> english = new TreeMap<String, YearMakeModel>();
	private Map<String, YearMakeModel> french = new TreeMap<String, YearMakeModel>();
	
	public Map<String, YearMakeModel> getEnglish() {
		return english;
	}
	public void setEnglish(Map<String, YearMakeModel> english) {
		this.english = english;
	}
	public Map<String, YearMakeModel> getFrench() {
		return french;
	}
	public void setFrench(Map<String, YearMakeModel> french) {
		this.french = french;
	}
}
