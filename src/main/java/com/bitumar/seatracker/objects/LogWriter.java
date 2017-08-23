package com.bitumar.seatracker.objects;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class LogWriter
{
	private ArrayList<ArrayList<String>> lines;
	
	public LogWriter()
	{
		lines = new ArrayList<>();
	}
	
	/**
	 * Adds a line to the printer's print job
	 * @param params as many strings as necessary
	 */
	public void addLine(String... params)
	{
		ArrayList<String> temp = new ArrayList<>();
		temp.addAll(Arrays.asList(params));
		lines.add(temp);
	}
	
	/**
	 * Finally prints out the accumulated lines
	 * @param file to print to
	 * @param title
	 */
	public void print(String file, String... title)
	{
		CSVPrinter csvFilePrinter = null;
				
		try 
		{						
			//initialize CSVPrinter object 
	        csvFilePrinter = new CSVPrinter(new FileWriter(file, true), CSVFormat.DEFAULT);
	        
	        //Create CSV file header if empty
			if (Files.size(Paths.get(file)) == 0)
				csvFilePrinter.printRecord((Object[])title);
			
			for (ArrayList<String> line : lines)
			{
				csvFilePrinter.printRecord(line);
			}

			System.out.println("CSV file was created successfully !!!");
			
		} 
		catch (Exception e) 
		{
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace(System.out);
		}
		finally 
		{
			try 
			{
				lines.clear();
				csvFilePrinter.close();
			} 
			catch (IOException e) 
			{
				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace(System.out);
			}
		}	
	}
}
