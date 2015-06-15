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
package ca.java.spring.cache.web.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import ca.java.spring.cache.domain.ModelData;

/**
 * @author Srinivas Rao
 *
 */
public class FileUtility {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) throws Exception
    {
       CsvToBean csv = new CsvToBean();
       List<ModelData> yymList = new ArrayList<ModelData>(); 
       String csvFilename = "src/main/resources/ymm_2500.csv";
       CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
        
       //Set column mapping strategy
       List list = csv.parse(setColumMapping(), csvReader);
       yymList.addAll(list);
       
       /*for (Object object : list) {
	   ModelData yym = (ModelData) object;
           System.out.println(yym);
       }
       System.out.println(yymList);*/
    }
     
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static ColumnPositionMappingStrategy setColumMapping()
    {
       ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
       strategy.setType(ModelData.class);
       String[] columns = new String[] {"year", "make", "modelEn", "modelFr"};
       strategy.setColumnMapping(columns);
       return strategy;
    }

    public static List<ModelData> getListFromCSV(){
	CsvToBean csv = new CsvToBean();
	List<ModelData> yymList = new ArrayList<ModelData>(); 
	try {
	    String csvFilename = "src/main/resources/ymm_2500.csv";
	    CSVReader csvReader = new CSVReader(new FileReader(csvFilename));
	            
	    //Set column mapping strategy
	    List list = csv.parse(setColumMapping(), csvReader);
	    yymList.addAll(list);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	return yymList;
    }
}
