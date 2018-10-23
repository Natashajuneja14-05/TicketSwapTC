package TicketSwap.SampleTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import io.appium.java_client.android.AndroidDriver;

public class TicketSwapPage extends BaseUtils{

	private AndroidDriver<WebElement> androidDriver;
	
	
	

//	public TicketSwapPage(AndroidDriver<WebElement> androidDriver) {
//		this.androidDriver = androidDriver;
//	}

	@FindBy(id = "home")
	public WebElement homeTab;
	
	@FindBy(id = "sell")
	public WebElement sellTab;
	
	@FindBy(id = "searchBarHolder")
	public WebElement searchBar;
	
	@FindBy(id = "itemRoot")
	public List<WebElement> tileTitle;
	
	@FindBy(id = "sellButton")
	public WebElement sellButton;
	
	@FindBy(id = "toolbarTitle")
	public WebElement toolBar;
	
	@FindBy(id = "loginWithEmail")
	public WebElement signInWithEmail;
	
	@FindBy(id = "searchInput")
	public WebElement search;
	
	@FindBy(xpath= ("//*[android.widget.Button[contains(@text,'questionmark Select a file')]]"))
	public WebElement select;
	
	@FindBy(id = "itemRoot")
	public List<WebElement> venue;
	
	@FindBy(xpath=("//*[android.widget.LinearLayout//android.widget.TextView]"))
	public WebElement event;
	

	public TicketSwapPage(AndroidDriver<WebElement> androidDriver){
		this.androidDriver = androidDriver;
		PageFactory.initElements(androidDriver, this);
	}
	
	
	public void clickHomeTab() {
		clickOnWebElement(androidDriver,homeTab);
	}
	
	public void clickSellTab() {
		clickOnWebElement(androidDriver,sellTab);
	}

	public void clickSearchBar() {
		clickOnWebElement(androidDriver,searchBar);

	}
	
	public void clickVenueFromRailByIndex(int index) {
		clickOnWebElement(androidDriver,venue.get(index));
	}

	public String getVenueName(int index) {
		String name=venue.get(index).getText();
		return name;
	}
	
	public void selectSectionFromBottomNavigation(Navigation navigation) throws Exception{
		try {
		} catch(NoSuchElementException e) {
			backToPrevScreen();			
		}
		WebElement bottomNavigation = androidDriver.findElement(By.id("bottomNavigation"));
		WebElement bottomNavigationLayout = bottomNavigation.findElement(By.className("android.view.ViewGroup"));
		List<WebElement> bottomTabs = bottomNavigationLayout.findElements(By.className("android.widget.FrameLayout"));

		switch (navigation) {
		case HOME: {
			bottomTabs.get(0).click();
			break;
		}
		case SELL: {
			bottomTabs.get(1).click();
			break;
		}
		case ACCOUNT: {
			bottomTabs.get(2).click();
			break;
		}
		}

	}

	
	public Set<String> getTitleOfItemsPresentInRail(AndroidDriver<WebElement> androidDriver,List<WebElement> element, Direction direction, int noOfSwipes) {

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


	public void goToRailOnScreen(String railName) {
		List<WebElement> rail=androidDriver.findElements(By.xpath("//*[@text='"+railName+"']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView"));
		while(rail.isEmpty()) {
			scrollByPointer(androidDriver, 0.0001);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			rail=androidDriver.findElements(By.xpath("//*[@text='"+railName+"']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView"));
		}
	}


	public void clickEventFromRailByIndex(int index) {
		tileTitle.get(index).click();
	}
	
	public String getEventName(int index) {
		String name=tileTitle.get(index).getText();
		return name;
	}
	
	public String getEventTitle() {
		//WebElement event = androidDriver.findElement(By.xpath("//*[android.widget.LinearLayout//android.widget.TextView]"));
		return event.getText();
	}

	public void getEventsFromList(int index) throws Exception{
		List<WebElement> elements = androidDriver.findElements(By.xpath("//*[android.support.v7.widget.RecyclerView//android.widget.LinearLayout//android.widget.TextView]"));
		clickOnWebElement(androidDriver,elements.get(index));
	}
	
	public String getNameOfEvent(int index) {
		List<WebElement> elements = androidDriver.findElements(By.xpath("//*[android.support.v7.widget.RecyclerView//android.widget.LinearLayout//android.widget.TextView]"));
		String eventName = elements.get(index).getText();
		return eventName;
	}

	public void clickSellButton() {
		clickOnWebElement(androidDriver,sellButton);
	}

	public String eventNameOnDetailPage() {
		WebElement name= androidDriver.findElement(By.xpath("//*[android.widget.TextView[contains(@resource-id,'title')]]"));
		return name.getText();
	}

	public void selectRegular() throws Exception {
		Thread.sleep(5000);
		WebElement regular = androidDriver.findElement(By.xpath("//*[android.view.View[contains(@text,'Regular')]]"));
		getElementWhenVisible(regular);
		clickOnWebElement(androidDriver,regular);
	}

	public void clickNextStepButton() {
		WebElement next = androidDriver.findElement(By.xpath("//*[android.widget.Button[contains(@text,'Go to next step')]]"));
		clickOnWebElement(androidDriver,next);
	}


	public void selectFileButton() {
		//WebElement select = androidDriver.findElement(By.xpath("//*[android.widget.Button[contains(@text,'questionmark Select a file')]]"));
		clickOnWebElement(androidDriver,select);
	}
	

	public boolean verifyIfToolBarExist() {
		return verifyIfElementIsPresent(androidDriver, toolBar);
	}

	public void clickEmailButton() {
		clickOnWebElement(androidDriver,signInWithEmail);
	}

	public void enterEmailAddress() {
		WebElement signInWithEmail = androidDriver.findElement(By.id("input"));
		clickOnWebElement(androidDriver,signInWithEmail);
		type(androidDriver,signInWithEmail,"natashajuneja05@gmail.com");
		WebElement loginLinkButton = androidDriver.findElement(By.id("bottomButton"));
		clickOnWebElement(androidDriver,loginLinkButton);

	}

	public boolean verifyLogInSuccess() {
		WebElement success = androidDriver.findElement(By.id("positiveButton"));
		return verifyIfElementIsPresent(androidDriver, success);
	}
	
	String keyword = "gadjhgfjhsgf";

	public void typeKeyword() {
		type(androidDriver,search,keyword);
	}

	public boolean verifyEmptyState() {
		WebElement noSearchResult = androidDriver.findElement(By.id("positiveButton"));
		return verifyIfElementIsPresent(androidDriver, noSearchResult);

	}
	
	public Set<String> getItemsTitleFromRail(String railName){
		List<WebElement> rail=androidDriver.findElements(By.xpath("//*[@text='"+railName+"']//following-sibling::android.support.v7.widget.RecyclerView//android.widget.TextView"));
		return getTitleOfItemsPresentInRail(androidDriver,rail, Direction.RIGHTTOLEFT, 3);
	}
}
