package com.github.util;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneralMethod {
	
	public String ConvertDateFormat(String inputFormat,String date,String outputFormat) throws ParseException
	{
		DateFormat srcFormat = new SimpleDateFormat(inputFormat);
		Date inputDate = srcFormat.parse(date);
	
		DateFormat destForamt = new SimpleDateFormat(outputFormat);
		String outputDate = destForamt.format(inputDate);
		
		return outputDate;
	}
	
	public int DigitCount(int value)
	{

		int count=0; 

		while(value !=0) 
		{
			value /=10; 
			count++; 
		}
		return count;
	}
	
	
	  public String convertValue(int value, int noOfDigit)
	  { 
		  DecimalFormat df = new DecimalFormat("#");
		  df.setRoundingMode(RoundingMode.HALF_UP);
		  
		  Double valueInDouble = Double.valueOf(value);
		  
		  String returnValue = "";
	  
		  if(noOfDigit>3 && noOfDigit<7)
		  {
			  valueInDouble = valueInDouble/10E2;
			  System.out.println(valueInDouble);
			  returnValue = df.format(valueInDouble)+"k";
		  }
		  
		  else if(noOfDigit>6 && noOfDigit<10)
		  {	
			  valueInDouble = valueInDouble/10E5;
			  System.out.println(valueInDouble);
			  returnValue = df.format(valueInDouble)+"m";
		  }
		  
		  else if(noOfDigit>9 && noOfDigit<13)
		  {	
			  valueInDouble = valueInDouble/10E8;
			  System.out.println(valueInDouble);
			  returnValue = df.format(valueInDouble)+"b";
		  }
		  return returnValue;
	  }
}

