/*
 * Copyright (c) 2015 Srinivas Rao. All rights reserved.
 * Creation Date : 22-Jun-2015
 */

package ca.java.spring.cache.web.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import ca.java.spring.cache.domain.ModelData;

/**
 * @author Srinivas Rao
 *
 */
public class FileUtility {
    private static final Logger LOGGER = LoggerFactory
	    .getLogger(FileUtility.class);
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
       
       for (Object object : list) {
	   ModelData yym = (ModelData) object;
           System.out.println(yym);
       }
       System.out.println(yymList);
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
	    String path = FileUtility.class.getClassLoader().getResource("ymm_2500.csv").getPath();
	    
	    CSVReader csvReader = new CSVReader(new FileReader(path));
	            
	    //Set column mapping strategy
	    List list = csv.parse(setColumMapping(), csvReader);
	    yymList.addAll(list);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	return yymList;
    }
}
