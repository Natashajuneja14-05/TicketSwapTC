package TicketSwap.SampleTest;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import io.appium.java_client.android.AndroidDriver;


public class TakeScreenshot extends TestListenerAdapter {
	AndroidDriver driver;
	String QualifiedMethodName = "";
	String NewFileNamePath;
	String NewFilePathName;
	String NewLogFileNamePath;
	String NewLogFilePathName;
//	public static boolean logged=false;
	public boolean logged=false;
	BaseUtils util = new BaseUtils();
	DateFormat df = new SimpleDateFormat("dd_MM_yyyy");
	String data = df.format(new Date());
	static boolean exceptionPresent;
	
	public static String sMessages="";

	
	@Override
	public void onTestFailure(ITestResult tr) {

		try{
			Object currentClass = tr.getInstance();
			driver = ((BaseUtils) currentClass).getDriver();
		} catch (Exception e){
			System.out.println("exception in setting driver :"+driver);
			driver = null;
		}

		QualifiedMethodName = tr.getInstanceName();

		//SetStatusinTestCaseCreationSheet("Fail");
		
		xScreenShot();
		
		if(exceptionPresent) {
		logs(tr.getMethod().getMethodName());
		}
		
		
		//addDescription();
	}


	@Override
	public void onTestSkipped(ITestResult tr) {
		QualifiedMethodName = tr.getInstanceName();
		try{
			Object currentClass = tr.getInstance();
			driver = ((BaseUtils) currentClass).getDriver();
		} catch (Exception e){
			System.out.println("exception in setting driver :"+driver);
			driver = null;
		}
		if(exceptionPresent) {
			logs(tr.getMethod().getMethodName());
			}
		
		//SetStatusinTestCaseCreationSheet("Skipped");
		//addDescription();
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		QualifiedMethodName = tr.getInstanceName();
		try{
			Object currentClass = tr.getInstance();
			driver = ((BaseUtils) currentClass).getDriver();
		} catch (Exception e){
			System.out.println("exception in setting driver :"+driver);
			driver = null;
		}
		
		//SetStatusinTestCaseCreationSheet("Pass");

		if(exceptionPresent) {
		logs(tr.getMethod().getMethodName());
		}
		//addDescription();
	}
	
	public void logs(String method){
		try {
			File directory = new File("./finalReport_"+data);
			DateFormat dateFormat = new SimpleDateFormat("MMM_dd_yyyy");
			Date date = new Date();

			String dateName = dateFormat.format(date);
			NewLogFileNamePath = directory.getCanonicalPath() + "/logs/" + method+"___" + dateName + "_"+ ".txt";
			NewLogFilePathName = "/logs/" +  method+"___" + dateName + "_"+ ".txt";

			try{
				System.out.println("COPYING FILE to "+ NewLogFileNamePath);
				File f = new File(System.getProperty("user.dir")+"/logs/crashLogs.txt");
				System.out.println("COPYING FILE "+ f + " to "+ NewLogFileNamePath);
				FileUtils.copyFile(f, new File(NewLogFileNamePath));
			}
			catch(Exception e){
				System.out.println("EXCEPTION e  : "+e);
				java.awt.Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
				Rectangle rectangle = new Rectangle(resolution);
				Robot robot = new Robot();
				BufferedImage bi = robot.createScreenCapture(new Rectangle(rectangle));
				ImageIO.write(bi, "png", new File(NewLogFileNamePath));
			}

			NewLogFileNamePath = "<a href=." + NewLogFilePathName + " target='_blank'>Click Here</a>";
			Print("[LOGS]:" + NewLogFileNamePath);	
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	public void xScreenShot(){
		try {
			File directory = new File("./finalReport_"+data);
			DateFormat dateFormat = new SimpleDateFormat("MMM_dd_yyyy_hh_mm_ssaa");
			Date date = new Date();

			String dateName = dateFormat.format(date);
			NewFileNamePath = directory.getCanonicalPath() + "/screenshots/" + "___" + dateName + "_"+ ".png";
			NewFilePathName = "/screenshots/" + "___" + dateName + "_"+ ".png";

			
			try{
				System.out.println("COPYING FILE to "+ NewFileNamePath);
				System.out.println("driver "+driver);
				File f = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				System.out.println("COPYING FILE "+ f + " to "+ NewFileNamePath);
				FileUtils.copyFile(f, new File(NewFileNamePath));
			}
			catch(Exception e){
				System.out.println("EXCEPTION e  : "+e);
				java.awt.Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
				Rectangle rectangle = new Rectangle(resolution);
				Robot robot = new Robot();
				BufferedImage bi = robot.createScreenCapture(new Rectangle(rectangle));
				ImageIO.write(bi, "png", new File(NewFileNamePath));
			}

			NewFileNamePath = "<a href=." + NewFilePathName + " target='_blank'>Click Here</a>";
			Print1("[Snapshot]:" + NewFileNamePath);	
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	public void Print(String Text) {
		System.out.println(Text+"Logs should be printed");
		Reporter.log(Text);
		String Temp = Text;
		sMessages = sMessages + Temp.replaceAll(" ", "_") + "#";
	}
	
	public void Print1(String Text) {
		System.out.println(Text+"Logs should be printed");
		Reporter.log(Text);
		String Temp = Text;
		sMessages = sMessages + Temp.replaceAll(" ", "_") + "#";
	}

	
	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);

		// List of test results which we will delete later
		List<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();

		// collect all id's from passed test
		Set <Integer> passedTestIds = new HashSet<Integer>();

		for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
			passedTestIds.add(TestUtil.getId(passedTest));
		}

		Set <Integer> failedTestIds = new HashSet<Integer>();

		for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {

			// id = class + method + dataprovider
			int failedTestId = TestUtil.getId(failedTest);

			// if we saw this test as a failed test before we mark as to be deleted
			// or delete this failed test if there is at least one passed version
			if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {
				testsToBeRemoved.add(failedTest);				
			} else {
				failedTestIds.add(failedTestId);
			}
		}

		// finally delete all tests that are marked
		for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator.hasNext(); ) {
			ITestResult testResult = iterator.next();
			if (testsToBeRemoved.contains(testResult)) {
				iterator.remove();
			}
		}

	}
}

final class TestUtil {

	public static int getId(ITestResult result) {
		int id = result.getTestClass().getName().hashCode();
		id = 31 * id + result.getMethod().getMethodName().hashCode();
		id = 31 * id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
		return id;
	}	
}