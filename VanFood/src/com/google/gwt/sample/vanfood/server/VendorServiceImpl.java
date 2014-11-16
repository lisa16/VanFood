package com.google.gwt.sample.vanfood.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.google.gwt.sample.vanfood.client.VendorService;
import com.google.gwt.sample.vanfood.shared.Vendor;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class VendorServiceImpl extends RemoteServiceServlet implements VendorService{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4612809426927175171L;

	public Vendor[] getVendors(){
	// ArrayList to accomodate a variabe number of vendors and build up the Vendor[]
    ArrayList<Vendor> vendors = new ArrayList<Vendor>();

    try
    {
    	// Location of the vendor xls sheet that needs to be parsed
        FileInputStream file = new FileInputStream(new File("new_food_vendor_locations.xls"));

        //Create Workbook instance holding reference to .xls file
        HSSFWorkbook workbook = new HSSFWorkbook(file);

        //Get first/desired sheet from the workbook
        HSSFSheet sheet = workbook.getSheetAt(0);

        //Iterate through each row to build vendor objects
        int numRows = sheet.getLastRowNum();            
        for(int i = 1; i < numRows; i++){
        	HSSFRow row = sheet.getRow(i);
        	String name;
        	String address;
        	String foodtype;
            //For each row, get the string values of Columns 3, 4, and 5 - corresponding to cells D, E, and F 
        	try{
                name = row.getCell(3).getStringCellValue();
                if (name == ""){
                	name = "Name not available";
                }
        	}catch(NullPointerException npen){
        		
             name = "Name not available";
            }
        	
        	try{
                address = row.getCell(4).getStringCellValue();
                if (address == ""){
                	address = "Address not available";
                }
        	}catch(NullPointerException npe){
        		
             address = "Address not available";
            }
        	
        	try{
                foodtype = row.getCell(5).getStringCellValue();
                if (foodtype == ""){
                	foodtype = "Food type not available";
                }
        	}catch(NullPointerException npe){
        		
             foodtype = "Food type not available";
            }
        	
        	
       // Create Vendor object from information in the row
        
             Vendor vendor = new Vendor(name, address, foodtype);
              vendors.add(vendor);
        	
        }
        
        file.close();
    } 
    catch (Exception e) 
    {
        e.printStackTrace();
    }
    
    //Transform the vendors ArrayList into an array to match return value
   return vendors.toArray(new Vendor[vendors.size()]);
	}


}