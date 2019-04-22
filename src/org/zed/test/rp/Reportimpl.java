/**
 * 
 * @author Zeeshan Farooq email zeeshanfrq@gmail.com
 *
 */

package org.zed.test.rp;

import java.io.File;
import java.sql.Time;
import java.util.Calendar;
import java.util.Set;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
 

import io.reactivex.Maybe;



public class Reportimpl implements Report {

 
 
  	public String ProjectName = "";
	public String SprintName = "";
	 
	private String UID;
	private String RURL;
	private ReportPortal rpt = null;
	private String fst = "PASSED";
	private Maybe<String> lid;
	private Maybe<String> sid;
	private Maybe<String> tid;
	private Maybe<String> stid;
	private Maybe<String> itemid;
	String LuanchName;
	public Set<String> Tags;
	public Set<String> stepTags;
	public Set<String> testTags;
	public Set<String> suiteTags;
	Launch launch;

	private static Report.Status oStatus;
	private static Report.LogStatus oLogStatus;
	
	public Maybe<String> getLaucnId()
	{
		return lid;
		
		
	}
	
	public Reportimpl(final String luanchName, final String projectName, final String sprintName, final String uID,
			final String uRL) {
		ProjectName = projectName;
		SprintName = sprintName;
		UID = uID;
		RURL = uRL;
		LuanchName = luanchName;
		 

	}
	
 

	@Override
	public Maybe<String> StartLaunch() {

		ListenerParameters ll = new ListenerParameters();
		ll.setBaseUrl(RURL.trim());
		ll.setUuid(UID.trim());

		ll.setLaunchName(LuanchName);
		ll.setProjectName(ProjectName);
		ll.setDescription(LuanchName + " FOR " + ProjectName);
		ll.setEnable(true);

		if (Tags!=null)
			ll.setTags(Tags);

		StartLaunchRQ rq = new StartLaunchRQ();
		rq.setName(ll.getLaunchName());

		rq.setStartTime(Calendar.getInstance().getTime());
		rq.setMode(ll.getLaunchRunningMode());

		if (Tags!=null) {
			rq.setTags(ll.getTags());
		}

		rq.setDescription(ll.getDescription());

		try {

			rpt = ReportPortal.builder().withParameters(ll).build();

		} catch (Exception x) {
			x.printStackTrace();

		}

	      launch = rpt.newLaunch(rq);
         
		  lid = launch.start();
		  
		  

		return lid;

	}
	
	

	@Override
	public Maybe<String> StartSuite(String Name, String Description) {

	    
 
			StartTestItemRQ st = new StartTestItemRQ();
			
			st.setDescription(Description);
			st.setName(Name);
			st.setStartTime(Calendar.getInstance().getTime());
			st.setRetry(false);
			st.setType("SUITE");
			
			
			if (suiteTags!=null) {
				st.setTags(suiteTags);
			}
			
			
		    sid = launch.startTestItem(st);
		 
 
			  
		return sid;
	}

	@Override
	public Maybe<String> StartTest(String Name,String Desc) {
		 
		 StartTestItemRQ testcase = new StartTestItemRQ();
		
		testcase.setDescription(Desc);
		testcase.setName(Name);
		testcase.setStartTime(Calendar.getInstance().getTime());
		testcase.setRetry(false);
		testcase.setType("TEST");
		
		if (testTags!=null) {
			testcase.setTags(testTags);
		}
		
		tid = launch.startTestItem(sid, testcase);		 
		return tid; 
	}

	@Override
	public Maybe<String> StartStep(String Name, String Description) {
		 
		 StartTestItemRQ step = new StartTestItemRQ();
		
		step.setDescription(Description);
		step.setName(Name);
		step.setStartTime(Calendar.getInstance().getTime());
		step.setRetry(false);
		step.setType("STEP");
		
		if (stepTags!=null) {
			step.setTags(stepTags);
		}
		
		 stid = launch.startTestItem(tid, step);
		 return stid;
		 
	}

	@Override
	public void endLaunch(Status Status) {
		 
        FinishExecutionRQ fq = new  FinishExecutionRQ();
       
       fq.setEndTime(Calendar.getInstance().getTime());
   
       
       fq.setStatus(ConvertSStatus(Status));
       
       launch.finish(fq);

	}

	@Override
	public void endSuite(Status Status)  {
		FinishTestItemRQ ft = new FinishTestItemRQ();
		ft.setEndTime(Calendar.getInstance().getTime()); 
		ft.setStatus(ConvertSStatus(Status));
 
	    launch.finishTestItem(sid, ft);


	}
	
	@Override
	public void endLaunch() {
		 
        FinishExecutionRQ fq = new  FinishExecutionRQ();
       
       fq.setEndTime(Calendar.getInstance().getTime());
   
       
   //    fq.setStatus(ConvertSStatus(Status));
       
       launch.finish(fq);

	}

	@Override
	public void endSuite() {
		FinishTestItemRQ ft = new FinishTestItemRQ();
		ft.setEndTime(Calendar.getInstance().getTime()); 
	//	ft.setStatus(ConvertSStatus(Status));
 
	    launch.finishTestItem(sid, ft);


	}

	@Override
	public void endTest(Status Status) {
	    
		FinishTestItemRQ ftc = new FinishTestItemRQ();
		ftc.setEndTime(Calendar.getInstance().getTime()); 
		ftc.setStatus(ConvertSStatus(Status));
 
	    launch.finishTestItem(tid, ftc);

	}

	@Override
	public void endStep(Status Status) {
		FinishTestItemRQ ftep = new FinishTestItemRQ();
		ftep.setEndTime(Calendar.getInstance().getTime()); 
		ftep.setStatus(ConvertSStatus(Status));
 
	    launch.finishTestItem(stid, ftep);

	}

	@Override
	public void log(String message, LogStatus status) {
		
		 rpt.emitLog(message, ConvertStatus(status), Calendar.getInstance().getTime());

	}

	@Override
	public void log(String message, LogStatus status, File f) {
		
		rpt.emitLog(message, ConvertStatus(status), Calendar.getInstance().getTime(), f);

	}

	private String ConvertStatus(LogStatus S) {
		 switch (S) {
		 case Info : return "INFO"; 
		 case Error : return "ERROR"; 
	 
		 case Warn : return "WARN"; 
		 case Fatal : return "FATAL"; 
		 case Debug : return "DEBUG"; 
		 default : return "INFO";
		 }
		  
		
	}
	

	
	private String ConvertSStatus(Status S) {
		 switch (S) {
		 case PASSED : return "PASSED"; 
		 case Failed : return "FAILED"; 
		 case SKIPPED : return "SKIPPED"; 
 
		 default : return "PASSED";
		 }
		  
		
	}

	@Override
	public Maybe<String> ReStartLaunch(Maybe<String> elid) {
	 
		
	      launch = rpt.withLaunch(elid);

		  lid = launch.start();

		return lid;
	}

	@Override
	public Maybe<String> ReStartSuite(Maybe<String> esid,String Name, String Description) {

		StartTestItemRQ st = new StartTestItemRQ();
		
	    st.setName(Name);
	    st.setDescription(Description);
		st.setStartTime(Calendar.getInstance().getTime());
		//st.setRetry(true);
		 
		st.setType("SUITE");
		
	    sid = launch.startTestItem(esid, st);
	 

		  
	return sid;
	}



	@Override
	public Status getStatus() {
	    return oStatus;
	   
	}



	@Override
	public LogStatus getLogStatus() {
	 
		return oLogStatus;
	}



	@Override
	public Set<String> getTags() {
		
		return this.Tags;
	}



	@Override
	public void setTags(Set<String> s) {
		this.Tags = s;
		
	}



	@Override
	public Maybe<String> startItem(String item, String Name, String Description) {
		 StartTestItemRQ step = new StartTestItemRQ();
			
			step.setDescription(Description);
			step.setName(Name);
			step.setStartTime(Calendar.getInstance().getTime());
			step.setRetry(false);
			step.setType(item);
			
			 itemid = launch.startTestItem(tid, step); // under test
			 return itemid;
	}

	@Override
	public Maybe<String> startItem(boolean test,String item, String Name, String Description) {
		 StartTestItemRQ step = new StartTestItemRQ();
			
			step.setDescription(Description);
			step.setName(Name);
			step.setStartTime(Calendar.getInstance().getTime());
			step.setRetry(false);
			step.setType(item);
			
			if (test)
			 itemid = launch.startTestItem(lid, step); // suite level under launch
			else
				 itemid = launch.startTestItem(sid, step);	// under suite
			 return itemid;
	}


	@Override
	public void endItem(Status Status) {
		FinishTestItemRQ ftep = new FinishTestItemRQ();
		ftep.setEndTime(Calendar.getInstance().getTime()); 
		if (Status!=null)
			ftep.setStatus(ConvertSStatus(Status));
	 
 
	    launch.finishTestItem(itemid, ftep);
		
	}



	@Override
	public void endItem(String Description, Status Status) {
		FinishTestItemRQ ftep = new FinishTestItemRQ();
		ftep.setEndTime(Calendar.getInstance().getTime()); 
	    ftep.setDescription(Description);
		
		if (Status!=null)
			ftep.setStatus(ConvertSStatus(Status));
	 
 
	    launch.finishTestItem(itemid, ftep);
		
	}



	@Override
	public void endStep(String Description, Status Status) {
		FinishTestItemRQ ftep = new FinishTestItemRQ();
		ftep.setEndTime(Calendar.getInstance().getTime()); 
		ftep.setStatus(ConvertSStatus(Status));
		 ftep.setDescription(Description);
	    launch.finishTestItem(stid, ftep);
		
	}
	

}
