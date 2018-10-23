package TicketSwap.SampleTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.exec.ExecuteException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class BaseUtils {

	public static OutputStream os = null;
	public DesiredCapabilities capabilities;

	public AndroidDriver<WebElement> androidDriver;
	private int time = 5;
	public AppiumDriver<?> appium;

	public enum Direction {
		LEFTTORIGHT, RIGHTTOLEFT, TOPTOBOTTOM, BOTTOMTOTOP
	}
	String appPackage = "com.ticketswap.ticketswap";
	
	public enum Navigation {
		HOME, SELL, ACCOUNT;
	}
	
	public BaseUtils() {
	}

	public BaseUtils(AndroidDriver androidDriver) {
		this.androidDriver = androidDriver;
		appium = (AppiumDriver<?>) androidDriver;
	}

	public AndroidDriver getDriver() {
		return androidDriver;
	}
	
	public WebElement getElementWhenVisible(WebElement locator) {
		WebElement we = null;

		WebDriverWait wait = new WebDriverWait(androidDriver, time);
		we = wait.until(ExpectedConditions.elementToBeClickable(locator));

		return we;
	}
	
	public void backToPrevScreen() throws InterruptedException {
		Thread.sleep(2000);
		androidDriver.pressKeyCode(AndroidKeyCode.BACK);
		Thread.sleep(1000);

	}
	
	public void clickOnWebElement(AndroidDriver androidDriver, WebElement element) {
		this.androidDriver = androidDriver;
		WaitForElement(androidDriver, element);
		element.click();
	}
	
	
	public void scrollByPointer(AndroidDriver<WebElement> androidDriver, double value) {
		Dimension size = androidDriver.manage().window().getSize();
		int startx = size.width / 2;
		int starty = (int) (size.height * 0.80);
		int endy = (int) (size.height * value); // changed from 0.1 0.005
		TouchAction action = new TouchAction(androidDriver);
		action.press(startx, starty).waitAction().moveTo(startx, endy).release().perform();
	}
	
	
	public Set<String> getTitleOfEventsPresent(AndroidDriver<WebElement> androidDriver,List<WebElement> element, Direction direction, int noOfSwipes) {
		Set<String> titles = new HashSet<String>();
		for (int i = 0; i <= noOfSwipes; i++) {
			int listSize = element.size();
			for (int j = 0; j < listSize; j++) {
				if(!element.get(j).getText().trim().equals("EXCLUSIVE")) {
				titles.add(element.get(j).getText().trim());
				}
			}
			swipeRailsHorizontally(androidDriver,element, direction);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return titles;
	}
	
	public void swipeRailsHorizontally(AndroidDriver<WebElement> androidDriver, List<WebElement> element, Direction direction) {
		Dimension size = androidDriver.manage().window().getSize();
		int y1 = element.get(0).getLocation().getY();
		switch (direction) {
		case LEFTTORIGHT: {
			new TouchAction(androidDriver).press(100, (y1 + 10)).moveTo((size.getWidth() - 10), (y1 + 10)).release()
					.perform();
			break;
		}

		case RIGHTTOLEFT: {
			new TouchAction(androidDriver).press((size.getWidth() - 200), (y1 + 10)).waitAction()
					.moveTo(30, (y1 + 10)).release().perform();

			break;
		}
		}
	}
	
	protected WebDriver driver;

	public AndroidDriver<WebElement> startDriverParallel(String url) throws MalformedURLException{
		try{
            System.setProperty("ANDROID_HOME", System.getProperty("user.home")+"/Library/Android/sdk");
			String appLocation= "";
		//	startAppiumServer_v1(StringUtils.substringBetween(url, ":","/wd/hub"));
			init(appLocation);
			androidDriver= new AndroidDriver<WebElement>(new URL(url), capabilities);
			System.out.println("\nExecuting Test...\n");
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("App Launch failed");
			System.out.println("\nApp launch fail\n");
			System.out.println(e.getMessage());
			System.out.println("\nRetrying...\n");
			
			try{
				stopDriver();
				driver=  new AndroidDriver<WebElement>(new URL(url), capabilities);
				System.out.println("Retry Success...Starting execution...\n");
			}
			catch(Exception e1){
				System.out.println("\nApp launch fail\n");
				System.out.println(e.getMessage());
				System.out.println("\nRetrying...\n");
			}
		}
		return androidDriver;

	}
	
	public void stopDriver(){

		try{
			androidDriver.close();
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	
	public void type(AndroidDriver androidDriver, WebElement element, String value) {
		this.androidDriver = androidDriver;
		WaitForElement(androidDriver, element);
		WebElement we = getElementWhenVisible(element);
		if (we != null) {
			we.clear();
			we.sendKeys(value);
		}
	}
	
	public WebElement WaitForElement(AndroidDriver<WebElement> androidDriver, WebElement element) {
		this.androidDriver = androidDriver;
		try {
			WebDriverWait wait = new WebDriverWait(androidDriver, 15);
			wait.until(ExpectedConditions.visibilityOf(element));
			return element;
		} catch (NoSuchElementException e) {
			System.out.println("wait condition for element not met");
		}
		return null;
	}

	
	public boolean verifyIfElementIsPresent(AndroidDriver androidDriver, WebElement element) {
		this.androidDriver = androidDriver;
		boolean flag = false;
		if (element.isEnabled()) {
			flag = true;
		} else {
			flag = false;
			System.out.println(element + "is not present");
		}
		return flag;
	}
	
	public void init(String appLocation){
        AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();

		capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "ABC");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
		capabilities.setCapability("newCommandTimeout", 60 * 3);
		capabilities.setCapability("appPackage", "com.ticketswap.ticketswap");
		//capabilities.setCapability("appActivity", "com.google.android.apps.chrome.Main");
		capabilities.setCapability("appActivity", "com.ticketswap.android.ui.LauncherActivity");
		capabilities.setCapability("noReset", "true");
		
		androidDriver = new AndroidDriver(service, capabilities);

	}
	
	
	public void startAppiumServer() throws ExecuteException, IOException, InterruptedException{
		RuntimeExec appiumObj = new RuntimeExec();
		appiumObj.stopAppium("killall -9 node");
		appiumObj.startAppium("/usr/local/bin/node /usr/local/bin/appium --address 0.0.0.0  --port 4445 --no-reset --command-timeout 90 --backend-retries 2"); 

	}
	
	private class RuntimeExec {
		public StreamWrapper getStreamWrapper(InputStream is, String type){
			return new StreamWrapper(is, type);
		}
		private class StreamWrapper extends Thread {
			InputStream is = null;
			String type = null;
			String message = null;

			StreamWrapper(InputStream is, String type) {
				this.is = is;
				this.type = type;
			}

			public void run() {
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					StringBuffer buffer = new StringBuffer();
					String line = null;
					while ( (line = br.readLine()) != null) {
						buffer.append(line);//.append("\n");
					}
					message = buffer.toString();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}  
		}
	
		public void startAppium(String command) {
			Runtime rt = Runtime.getRuntime();
			RuntimeExec rte = new RuntimeExec();
			StreamWrapper error, output;

			try {
				Process proc = rt.exec(command);
				error = rte.getStreamWrapper(proc.getErrorStream(), "ERROR");
				output = rte.getStreamWrapper(proc.getInputStream(), "OUTPUT");
				//   int exitVal = 0;

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String s;
				while((s = stdInput.readLine()) != null){
					System.out.println(s);
					if(s.contains("Appium REST http")){
						System.out.println("Started Appium Server");
						break;
					}
				}
				error.start();
				output.start();
				error.join(3000);
				output.join(3000);
				// exitVal = proc.waitFor();
				System.out.println("Output: "+output.message+"\nError: "+error.message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void stopAppium(String command) {
			Runtime rt = Runtime.getRuntime();
			RuntimeExec rte = new RuntimeExec();
			StreamWrapper error, output;

			try {
				Process proc = rt.exec(command);
				error = rte.getStreamWrapper(proc.getErrorStream(), "ERROR");
				output = rte.getStreamWrapper(proc.getInputStream(), "OUTPUT");
				error.start();
				output.start();
				error.join(3000);
				output.join(3000);
				if(error.message.equals("") && output.message.equals(""))
					System.out.println("Closed Appium Server");
				else if(error.message.contains("No matching processes belonging to you were found")){
					//Display nothing as no instances of Appium Server were found running
				}
				else{
					System.out.println("Unable to Close Appium Server");
					System.out.println("Output: "+output.message+"\nError: "+error.message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getPropertyValue(String propertyFileName, String propertyName) {

		String directory = getUserDirectoryPath();
		String propFileName = directory + propertyFileName;
		File file = new File(propFileName);
		FileInputStream fileInput = null;

		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String propertyValue = prop.getProperty(propertyName);
		return propertyValue;
	}
	
	public String getUserDirectoryPath() {
		return System.getProperty("user.dir");
	}
	
}
