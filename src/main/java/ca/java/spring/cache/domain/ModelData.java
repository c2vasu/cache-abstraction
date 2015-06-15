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
package ca.java.spring.cache.domain;

/**
 * @author Srinivas Rao
 *
 */
public class ModelData {
    private int id; 
    private String year;
    private String make;
    private String modelEn;
    private String modelFr;
    
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public String getModelEn() {
        return modelEn;
    }
    public void setModelEn(String modelEn) {
        this.modelEn = modelEn;
    }
    public String getModelFr() {
        return modelFr;
    }
    public void setModelFr(String modelFr) {
        this.modelFr = modelFr;
    }
    
}
