# JavaSDK
JavaSDK for custome Java based framework
Can be use with nay java based project and allow testing artifiacts to be uploaded

Improvement is welcome

>https://mvnrepository.com/artifact/com.github.reportportal/JavaSDK  
```xml
<dependency>
  <groupId>com.github.reportportal</groupId>
  <artifactId>JavaSDK</artifactId>
  <version>0.0.2</version>
</dependency>


Reportimpl ReportPortalClient ;

ReportPortalClient = new Reportimpl(/*Launch name*/ "LaunchTest", /* Project Name */ "ReportLib",/* Sprint */ "1", /*uuid */ "a9cff9c8-5f4d-487b-9cb4-b1fb21d780fc", /* url */ "http://<ip:port>");


ReportPortalClient.StartLaunch(); // Start the Launch, once


ReportPortalClient.StartSuite(/* Suite name */ "REportportalLibSelfTest test1", /* Suite description*/ "Junit self test of report portal"); // Start suite , one per suite

ReportPortalClient.StartTest(/*name */ "sample", /* description */ "sample test"); // Start Test , once per test

ReportPortalClient.StartStep("Step Name", "Step name here"); // Start Step , once per step

ReportPortalClient.log("Test Message from self lib", ReportPortalClient.getLogStatus().LogStatus.Info); // Log info, optional, available after test suite is started. there is optional file parameter for attachment as third arguments


ReportPortalClient.endStep(ReportPortalClient.getStatus().PASSED); // report step as per set status, require for each step

ReportPortalClient.endTest(ReportPortalClient.getStatus().PASSED); // end test and report result, require for each test

ReportPortalClient.endSuite(ReportPortalClient.getStatus().PASSED); // end suite

ReportPortalClient.endLaunch(ReportPortalClient.getStatus().PASSED); // end launch
