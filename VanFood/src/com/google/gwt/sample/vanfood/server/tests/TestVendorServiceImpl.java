package com.google.gwt.sample.vanfood.server.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.sample.vanfood.shared.Vendor;

public class TestVendorServiceImpl {

	private ArrayList<Vendor> vendors;
	
	// Make sure this method stays updated to reflect actual parser used in application 
		// Find it at VendorServiceImpl.parseVendors()
		public ArrayList<Vendor> getVendorsTest(){
			ArrayList<Vendor> vendors1 = new ArrayList<Vendor>();
			try
			{

				// Location of the vendor xls sheet that needs to be parsed
				FileInputStream file = new FileInputStream(new File("test/new_food_vendor_locations.xls"));

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
					double lat;
					double lon;
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

						foodtype = "Foodtype not available";
					}
					
					try{
						lat = row.getCell(6).getNumericCellValue();
					}catch(NullPointerException npe){

						lat = 0;
					}
					
					try{
						lon = row.getCell(7).getNumericCellValue();
						
					}catch(NullPointerException npe){

						lon = 0;
					}


					// Create Vendor object from information in the row

					Vendor vendor = new Vendor(name, address, foodtype, lat, lon);
					vendors1.add(vendor);

				}

				file.close();

			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			return vendors1;

		}

	@Before
	public void beforeTests(){
		vendors = getVendorsTest();	
		
	}
	
	
	
	@Test
	public void testSize() {
		assertEquals(vendors.size(), 111);

	}
	
	@Test
	public void testLatLons(){
		for(Vendor v: vendors){
			assertTrue(v.getLat() < 51);
			assertTrue(v.getLat() > 49);
			assertTrue(v.getLon()< -123);
			assertTrue(v.getLon() > -124);

		}
		
	}
	
	@Test
	public void testName(){
		assertEquals(vendors.get(2).getName(), "Holy Perogy");
	}
	
	@Test
	public void printList(){
		for(Vendor v: vendors){
			System.out.println(v.toString());
		}
	}
	

}
