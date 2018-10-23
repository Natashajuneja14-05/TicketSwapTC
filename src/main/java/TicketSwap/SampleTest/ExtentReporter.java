package TicketSwap.SampleTest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
import org.zeroturnaround.zip.ZipUtil;


import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;

public class ExtentReporter implements IReporter {
	private ExtentReports extent;
	private AndroidDriver androidDriver;
	BaseUtils util = new BaseUtils();
	DateFormat df = new SimpleDateFormat("dd_MM_yyyy");
	String data = df.format(new Date());
	public static int testcasesCount;
	public static int passedTestcasesCount;
	public static int failedTestcasesCount;
	public static int skippedTestcasesCount;

	static boolean exceptionPresent =  false;
	

	public static void main(String args[]) throws Exception {
		ExtentReporter e = new ExtentReporter();
	}

	public static String getPropertyFromConfig(String property) {
		BaseUtils util = new BaseUtils();
		return util.getPropertyValue("/config.properties", property);
	}

	@SuppressWarnings("unchecked")
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		extent = new ExtentReports("./finalReport_" + data + "/TestExecutionReport.html", true);
		Map sysInfo = new HashMap();
		
		extent.addSystemInfo(sysInfo);

		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();

			for (ISuiteResult r : result.values()) {

				ITestContext context = r.getTestContext();
				System.out.println("EXTENT REPORT : " + context.getName());
				System.out.println("EXTENT REPORT tc name : " + context.getName() + " context : " + context.toString());
				context.getAllTestMethods()[0].getDescription();
				try {
					buildTestNodes(context.getPassedTests(), LogStatus.PASS);
					buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
					buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		System.out.println("Extent End time" + sdf.format(cal.getTime()));
		extent.flush();
		extent.close();
	}
	
	
	public void setExceptionFlag(boolean flag) {
		System.out.println("Setting Exception flag "+flag);
		exceptionPresent= flag;
	}
	

	private void buildTestNodes(IResultMap tests, LogStatus status)
			throws MalformedURLException {
		ExtentTest test;
		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {

				String packageName = result.getMethod().getTestClass().getName();
				String methodName = result.getMethod().getMethodName();
				String className = result.getMethod().getTestClass().getName();

				//ExcelExecutionManager xlmanager = new ExcelExecutionManager();
				Date date = new Date();
				Timestamp ts_now = new Timestamp(date.getTime());

				//String finalDesc = xlmanager.addDescription(packageName + "." + className);
				String testClassName = result.getMethod().getTestClass().getName();
				String testMethodName = result.getMethod().getMethodName();

				try {
					System.out.println("1." + testMethodName);
					int arrlen = testClassName.split("\\.").length;
					System.out.println("2." + testClassName.split("\\.")[arrlen - 1]);
					String testProject = this.getPropertyFromConfig("testProject");
					System.out.println("testProject = " + testProject);
					System.out.println("3." + testProject);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				System.out.println(result.getMethod().getTestClass().getName());
				System.out.println(result.getMethod().getMethodName());

				test = extent.startTest(testMethodName);
				System.out.println("classname is:" + testClassName.split("\\.").length);
				int len = testClassName.split("\\.").length;
				// test.assignCategory(result.getMethod().getTestClass().toString());

				if (len > 0)
					test.assignCategory(testClassName.split("\\.")[len - 1]);
				else
					test.assignCategory(testClassName);

				test.setStartedTime(getTime(result.getStartMillis()));
				test.setEndedTime(getTime(result.getEndMillis()));

				for (String group : result.getMethod().getGroups())
					test.assignCategory(group);

				Object currentClass = result.getMethod().getInstance();
				androidDriver = ((BaseUtils) currentClass).getDriver();
				// test.log(LogStatus.INFO, util.deviceName(testClassName));

				if (result.getMethod().getDescription() == null) {
				} else {
					String testcase[] = result.getMethod().getDescription().split(",");
					for (int i = 0; i < testcase.length; i++) {
						testcasesCount++;
					}
					extent.addSystemInfo("Test Cases Count", String.valueOf(testcasesCount));

					if (status.equals(LogStatus.PASS)) {
						for (int i = 0; i < testcase.length; i++) {
							passedTestcasesCount++;
						}
						extent.addSystemInfo("Passed Test Cases Count", String.valueOf(passedTestcasesCount));
					}

					if (status.equals(LogStatus.FAIL)) {
						for (int i = 0; i < testcase.length; i++) {
							failedTestcasesCount++;
						}
						extent.addSystemInfo("Failed Test Cases Count", String.valueOf(failedTestcasesCount));
					}

					if (status.equals(LogStatus.SKIP)) {
						for (int i = 0; i < testcase.length; i++) {
							skippedTestcasesCount++;
						}
						extent.addSystemInfo("Skipped Test Cases Count", String.valueOf(skippedTestcasesCount));
					}
					test.log(LogStatus.INFO, "Manual Test Cases", result.getMethod().getDescription());
					File directory1 = new File("./finalReport_"+data);
					DateFormat dateFormat = new SimpleDateFormat("MMM_dd_yyyy");

					String dateName = dateFormat.format(date);
					
					if(exceptionPresent) {
					
						String NewLogFileNamePath = null;
						String NewLogFilePathName = null;
						try {
							NewLogFileNamePath = directory1.getCanonicalPath() + "/logs/" + result.getMethod().getMethodName()+"___" + dateName + "_"+ ".txt";
							NewLogFilePathName = "/logs/" +  result.getMethod().getMethodName()+"___" + dateName + "_"+ ".txt";
							System.out.println("COPYING FILE to "+ NewLogFileNamePath);
							File f = new File(System.getProperty("user.dir")+"/logs/crashLogs.txt");
							FileUtils.copyFile(f, new File(NewLogFileNamePath));
							
							
//							NewLogFileNamePath = directory1.getCanonicalPath() + "/logs/" + result.getMethod().getMethodName()+"___" + dateName + "_"+ ".txt";
//							NewLogFilePathName = "<a href=" + NewLogFileNamePath + " target='_blank'>Click Here</a>";
							
							//NewLogFileNamePath = "<a href=." + NewLogFilePathName + " target='_blank'>Click Here</a>";
						} catch (IOException e) {
							e.printStackTrace();
						}
						test.log(LogStatus.INFO, "LOGS",NewLogFileNamePath);
					}

				}

				if (result.getThrowable() != null) {

					if (result.getThrowable().toString().contains("java.lang.AssertionError")) {
						test.log(LogStatus.INFO, result.toString().split("output=")[1]);
						//test.log(LogStatus.INFO, finalDesc);
						test.log(status, result.getThrowable());

					} else {
						test.log(LogStatus.INFO, result.toString().split("output=")[1]);
						//test.log(LogStatus.INFO, finalDesc);
						test.log(status, result.getThrowable());
					}
				} else {
					test.log(status, "Test " + status.toString().toLowerCase() + "ed");
					//test.log(LogStatus.INFO, finalDesc);
					// test.log(LogStatus.INFO, result.toString());

				}
				extent.endTest(test);
			}

		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	

}
