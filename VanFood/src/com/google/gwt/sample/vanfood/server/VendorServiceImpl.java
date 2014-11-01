package com.google.gwt.sample.vanfood.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.sample.vanfood.client.VendorService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class VendorServiceImpl extends RemoteServiceServlet implements VendorService{
	
	public Vendor[] getVendors(String[] args)
    {
		// ArrayList to accomodate a variabe number of vendors
        ArrayList<Vendor> vendors = new ArrayList<Vendor>();

        try
        {
        	// Location of the vendor xls sheet that needs to be parsed
            FileInputStream file = new FileInputStream(new File("c:/temp/vendors.xls"));
 
            //Create Workbook instance holding reference to .xls file
            HSSFWorkbook workbook = new HSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                //For each row, get the string values of Columns 3, 4, and 5 - corresponding to D, E, and F 
                 String name = row.getCell(3).getStringCellValue();
                 String address = row.getCell(4).getStringCellValue();
                 String foodtype = row.getCell(5).getStringCellValue();
                 
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


