package TicketSwap.SampleTest;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.net.URL;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import TicketSwap.SampleTest.TicketSwapPage;
import io.appium.java_client.android.AndroidDriver;


public class NewTest extends BaseUtils{

	public static String deviceName;

	SoftAssert softAssert; 
	WebDriverWait wait;
	TicketSwapPage page;

	@BeforeClass
	public void launchApp() throws Exception{
		init("http://0.0.0.0:4445/wd/hub");
		androidDriver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		page= new TicketSwapPage(androidDriver); 
		wait = new WebDriverWait(androidDriver, 5);
	}


	@AfterMethod
	public void afterMethod() {
		androidDriver.launchApp();
	}

	@AfterTest
	public void afterTest() {
		androidDriver.closeApp(); // Close the app which was provided in the capabilities at session creation   
		androidDriver.quit(); 
	}


	//TC-1: When user taps on any venue from Home tab, he should be redirected to detail page of that venue only
	//TC-2: When user taps on any event from Home tab, he should be redirected to detail page of that event only
	//TC-3: USer should be redirected to File select screen when tappe don 'Select File' button
	//TC-4: User should be redirected back to the app if he cancels the operation of uploading the file.

	//User should be able to sell tickets for any event. Prerequisite: User should be logged in
	@Test(description="Prerequisite: User should be logged in"
			+ "//TC-1: When user taps on any venue from Home tab, he should be redirected to detail page of that venue only\n" + 
			"	//TC-2: When user taps on any event from Home tab, he should be redirected to detail page of that event only\n" + 
			"	//TC-3: USer should be redirected to File select screen when tappe don 'Select File' button\n" + 
			"	//TC-4: User should be redirected back to the app if he cancels the operation of uploading the file.")
	public void sellTicket() throws Exception{

		softAssert = new SoftAssert();

		page.clickHomeTab();
		page.goToRailOnScreen("Popular venues");
		String venueName= page.getVenueName(1);
		page.clickVenueFromRailByIndex(1);
		WaitForElement(androidDriver,page.event);
		softAssert.assertEquals(page.getEventTitle(), venueName);
		String eventName= page.getNameOfEvent(0);
		page.getEventsFromList(0);
		softAssert.assertEquals(page.eventNameOnDetailPage(), eventName);
		page.clickSellButton();

		page.selectRegular();
		page.clickNextStepButton();
		page.selectFileButton();
		page.backToPrevScreen();
		softAssert.assertTrue(verifyIfElementIsPresent(androidDriver, page.select));
		softAssert.assertAll();
	}

	//TC-5: For unregistered user, when he taps on 'I want to Sell ticket' button, for him login flow should be initiated
	//TC-6: User should be able to enter his email address. And a link should be sent to his email address.

	//On the event description screen, while selling the ticket Login without signUp. User should be able to send the login link successfully to his email.

	@Test(description = "//TC-5: For unregistered user, when he taps on 'I want to Sell ticket' button, for him login flow should be initiated\n" + 
			"	//TC-6: User should be able to enter his email address. And a link should be sent to his email address.")
	public void loginAtTimeOfSellTickets() throws Exception {
		androidDriver.resetApp();
		androidDriver.launchApp();
		softAssert = new SoftAssert();

		page.clickHomeTab();
		page.goToRailOnScreen("Popular venues");
		page.getItemsTitleFromRail("Popular venues");
		String venueName= page.getVenueName(1);
		page.clickVenueFromRailByIndex(1);
		WaitForElement(androidDriver,page.event);
		softAssert.assertEquals(page.getEventTitle(), venueName);
		String eventName= page.getNameOfEvent(0);
		page.getEventsFromList(0);
		softAssert.assertEquals(page.eventNameOnDetailPage(), eventName);
		page.clickSellButton();

		if(page.verifyIfToolBarExist()==true)
		{
			page.clickEmailButton();
			if(page.verifyIfToolBarExist()==true) {
				page.enterEmailAddress();
				softAssert.assertTrue(page.verifyLogInSuccess());
			}
		}
		page.backToPrevScreen();
		softAssert.assertAll();
	}

	//TC-7: When user searches for any garbage value in search bar, and no matching results are found. 
	//		He should be shown the empty state of the screen; stating that 'No search results found'
	@Test(description = "TC-7: When user searches for any garbage value in search bar, and no matching results are found. He should be shown the empty state of the screen; stating that 'No search results found'")
	public void searchKeyword() throws Exception {

		softAssert = new SoftAssert();

		page.clickHomeTab();
		page.clickSearchBar();
		page.typeKeyword();
		Thread.sleep(2000);
		softAssert.assertTrue(page.verifyEmptyState());
		page.backToPrevScreen();

		softAssert.assertAll();
	}



}
