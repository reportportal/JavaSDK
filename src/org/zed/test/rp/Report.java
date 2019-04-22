	/**
	 *  
	 * @author Zeeshan Farooq email zeeshanfrq@gmail.com
	 * Java Extesion for custom framework using client-java
	 * 
	 */
package org.zed.test.rp;

import java.io.File;
import java.sql.Time;
import java.util.Set;

import org.zed.test.rp.Report.LogStatus;
import org.zed.test.rp.Report.Status;

import com.epam.reportportal.service.ReportPortal;

import io.reactivex.Maybe;

public interface Report {


    public static enum Status {
    	
    	
    	Failed,
    	STOPPED,
    	SKIPPED,
    	RESTED,
    	CANCELLED,
    	PASSED
    	
    }
	
  
 public static enum LogStatus {
    	
    	
    	Info,
    	Error,
    	Debug,
    	Skipped, Warn, Fatal
    	
    }
	
    
   public Status getStatus();
   
   public LogStatus getLogStatus();
   
 
   public Set<String> getTags();
   
   public void setTags(Set<String> s);
   

	
	/*
	 * Start luanch of report portal
	 */
	public Maybe<String> StartLaunch();
	
 
	
	public Maybe<String> ReStartLaunch(Maybe<String> lid);
 
	public Maybe<String> ReStartSuite(Maybe<String> sid,String Name, String Description);
	
	/*
	 * Log messages to report portal
	 */
	public void log(String message, LogStatus status);

	
	/*
	 * log message to report portal with attachement
	 */
	public void log(String message, LogStatus status, File f);


	/*
	 * Start test suite
	 */
	Maybe<String> StartSuite(String Name, String Description);


	Maybe<String> StartTest(String Name, String Desc);


	Maybe<String> StartStep(String Name, String Description);
	
	Maybe<String> startItem(String item, String Name, String Description);

    void endItem(Status Status);
    
    
	void endStep(Status Status);

    void endItem(String Description, Status Status);
    
    
	void endStep(String Description, Status Status);

	

	void endTest(Status Status);


	void endSuite(Status Status);


	void endLaunch(Status Status);
	
	void endSuite();


	void endLaunch();

	Maybe<String> startItem(boolean test, String item, String Name, String Description);

	
}



