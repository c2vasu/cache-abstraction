/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 22-Jun-2015
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
