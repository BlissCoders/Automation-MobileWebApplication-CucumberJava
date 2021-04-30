package steps;
import configs.TestConfig;

import cucumber.api.java.en.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.apache.http.util.Asserts;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.touch.offset.PointOption.point;
import static java.lang.Thread.sleep;


public class HomeSteps  extends TestConfig {
    AppiumDriver<MobileElement> driver;

    public void closeSignUpPopUp(){
        //Close Sign-up pop-up
        FindByCssSelectorClick("div[class='close-button']");
    }

    public void clickMainMenu(){
        FindByCssSelectorClick("a[class='nav-toggle']");
    }

    public void selectLanguage(){
        waitForVisibilityOf(driver.findElementByCssSelector("div[class='nav-list-item'"));
        List<MobileElement> navList = driver.findElementsByCssSelector("div[class='nav-list-item'");
        //Click Language from navList
        navList.get(0).click();
        selectOption("Language","text","English");
    }

    public void selectCurrency(String currency){
        waitForVisibilityOf(driver.findElementByCssSelector("div[class='nav-list-item'"));
        List<MobileElement> navList = driver.findElementsByCssSelector("div[class='nav-list-item'");
        //Click Language from navList
        navList.get(1).click();
        //Select PHP value: object:64
        //Select SGD Value: object:70
        //selectOption("Currency","value","object:70");
        selectOption("Currency","text",currency);
    }

    public void closeHamburger() throws InterruptedException {
        //Close Menu options and go back to main page
        sleep(2000);
        new TouchAction<>(driver).tap(point(382, 320)).perform();
    }

    public void changeLoyalty(String loyalty) throws InterruptedException {

        WebElement el = driver.findElementByCssSelector(".label.capitalize");

        if (el.getText().toLowerCase().equals("change")) {
            System.out.println("Select Change...");
            el.click();
            waitForVisibilityOf(driver.findElementByCssSelector("div[class='partner-logo']"));


            List<MobileElement> logos = driver.findElements(By.cssSelector("div[class='partner-logo']"));

            for (MobileElement logo : logos) {
                if (logo.findElementByCssSelector("img").getAttribute("src").contains(loyalty)) {
                    System.out.println("Select Etihad Guest");
                    logo.click();
                    break;
                }
            }
        }
    }

    public void chooseDestination(String destination) throws InterruptedException {
        waitAndClickFor(driver.findElementById("s2id_destinationInput"));
        System.out.println("Select Destination...");

        //Type singapore in search destination
        int ok_size=driver.findElementsByCssSelector("input.select2-input").size();
        WebElement dtext = driver.findElementsByCssSelector("input.select2-input").get(ok_size-1);
        dtext.sendKeys(destination + Keys.ENTER);
        System.out.println("Set Destination to Singapore...");

        //Select first destination result
        sleep(2000);
        WebElement ulList =  driver.findElementById("select2-results-1");
        List<WebElement> liList = ulList.findElements(By.tagName("li"));
        liList.get(0).click();
        System.out.println("Select first item from destination result...");
    }

    public void setCheckInAndCheckOut() throws InterruptedException {
        //Click CheckIn
        waitAndClickFor(driver.findElementById("start"));
        System.out.println("Set Checkin Start");

        //Select from calendar to next month
        waitForVisibilityOf(driver.findElementByCssSelector("button.pika-next"));
        int okNext  = driver.findElementsByCssSelector("button.pika-next").size();
        WebElement clickNext = driver.findElementsByCssSelector("button.pika-next").get(okNext - 1);
        clickNext.click();

        //Set Date Values
        LocalDate todaysDate = LocalDate.now().plusMonths(1);
        LocalDate todayPlusTwo = todaysDate.plusDays(2);
        System.out.println("One Month: " + todaysDate.toString());
        System.out.println("Plus two: " + todayPlusTwo.toString());

        //Set Checkin Start Date
        selectDayFromCalendar(Integer.toString(todaysDate.getDayOfMonth()));
        System.out.println("Set Checkin Date = Todays Date plus 2 months from now...");

        //Set CheckOut End Date
        selectDayFromCalendar(Integer.toString(todayPlusTwo.getDayOfMonth()));
        System.out.println("Set Checkout Date Two Days from Checkin Date...");
    }

    public void searchHotels() throws InterruptedException {
        //Search Hotels
        waitAndClickFor(driver.findElementById("search-form-submit"));
        System.out.println("Click Search Hotels");

        //Wait for search result
        waitForSearchResult();
    }
    public void applyFilter(String filter) throws InterruptedException {
        //Click Filter
        driver.findElementByCssSelector("span[translate='Filter'").click();

        //Set Filter to Hilton
        int okFilter = driver.findElementsByCssSelector("input[title='Filter hotel name']").size();
        WebElement textFilterName = driver.findElementsByCssSelector("input[title='Filter hotel name']").get(okFilter - 1);
        waitAndClickFor(textFilterName);


        //Enter Hilton and apply filter
        sleep(500);
        textFilterName.sendKeys(filter + Keys.ENTER);

        //Close modal filter
        int okCloser = driver.findElementsByCssSelector("a.modal-closer").size();
        WebElement modalCloser = driver.findElementsByCssSelector("a.modal-closer").get(okCloser - 1);
        waitAndClickFor(modalCloser);
    }

    public void validateFilterResult(String filter){
        waitForVisibilityOf(driver.findElementByCssSelector("div[ng-bind='hotel.name']"));
        String filterResult = driver.findElementByCssSelector("div[ng-bind='hotel.name']").getText();
        System.out.println("Filter hotel result name:" + filterResult);
        Assert.assertTrue("Result should have " + filter, filterResult.contains(filter));
    }
    @Then ("^I see account picker screen with my email address$")
    public void goHere() throws InterruptedException {

        closeSignUpPopUp();

        //Click Menu
        clickMainMenu();

        //Select Language
        selectLanguage();

        //Select Currency
        selectCurrency("SGD");

        //Close Menu options and go back to main page
        closeHamburger();

        //Change LoyaltyProgram to Etihad
        changeLoyalty("logo-etihad");


        //Choose Destination
        chooseDestination("Singapore");

        //Set CheckIn and CheckOut
        setCheckInAndCheckOut();

        //Select Rooms=1
        selectRoomAdultsChildren("Rooms","1");

        //Select Adults=2
        selectRoomAdultsChildren("Adults","2");

        //Select Children=0
        selectRoomAdultsChildren("Children","0");


        //Search Hotels
       searchHotels();

        //Do Assertions
        doAssertions();

        //Apply Filter = Hilton
        applyFilter("Hilton");

        //Validate Result to have Hilton
        validateFilterResult("Hilton");


        //Click result to navigate to page
        waitAndClickFor(driver.findElementByCssSelector("div[ng-bind='hotel.name']"));

        //End of Test
        sleep(3000);

    }

    public void doAssertions(){
        //Perform Assertions:
        //1. The URL should begin with https://www.kaligo.com/results/
        //2. The destination name shown on the top of the page is "Singapore, Singapore"
        //3. Guest count and room counts shown on the page is accurate
        //4. There should be more than 100 available hotels
        //5. Hotel prices are shown in SGD

        System.out.println("Current URL: "+ driver.getCurrentUrl());
        Asserts.check(driver.getCurrentUrl().contains("https://www.kaligo.com/results"),"URL should have https://www.kaligo.com/results/");

        System.out.println("Destination: " + driver.findElementByCssSelector("div.destination").getText());
        Asserts.check(driver.findElementByCssSelector("div.destination").getText().contains("Singapore, Singapore"),"Destination should be Singapore, Singapore");

        String hotelCount = driver.findElementByCssSelector("span[translate='HOTEL_SEARCH_RESULTS_COUNT']").getText();
        //System.out.println(hotelCount);

        String[] hotelList  = hotelCount.split("of");
        String hotelFound = hotelList[0].replaceAll("\\D+","");
        System.out.println("Number of hotels found: " + hotelFound);
        Assert.assertTrue("Hotels found should be more than 100", Integer.parseInt(hotelFound) > 100);

        String currencyPrice = driver.findElementByCssSelector("span.currency[ng-bind='selectedCurrency.code']").getText();
        System.out.println("Price shown as: " + currencyPrice);

        List<MobileElement> summary =  driver.findElementByCssSelector("div.summary").findElementsByTagName("span");
        System.out.println("Show summary...");

        for (MobileElement detail: summary){
            System.out.println("Detail : " + detail.getText());
            if (detail.getText().contains("room(s)")) {
                Assert.assertTrue("Room should be 1",detail.getText().contains("1"));
            }
            if (detail.getText().contains("guest(s)")) {
                Assert.assertTrue("Guest should be 2",detail.getText().contains("2"));
            }
        }


    }

    public void waitAndClickFor(WebElement element){
        WebDriverWait w = new WebDriverWait(driver, 30);
        w.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public void waitForSearchResult() throws InterruptedException {
        sleep(1000);
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.invisibilityOf((WebElement) driver.findElementById("progress-bar")));
        System.out.println("Searching hotels done...");
    }

    public void selectRoomAdultsChildren(String criteria, String number) throws InterruptedException {
        sleep(2000);
        //Select rooms
        List<MobileElement> listSelect = driver.findElementsByCssSelector("div[class*='ui-select-container select2 select2-container'");

        if (criteria.toLowerCase().equals("rooms")){
            //Select rooms
            System.out.println("Select Rooms...");
            listSelect.get(0).click();
        }
        if (criteria.toLowerCase().equals("adults")){
            //Select adults
            System.out.println("Select Adults");
            listSelect.get(1).click();
        }
        if (criteria.toLowerCase().equals("children")){
            //Select children
            System.out.println("Select Children");
            listSelect.get(2).click();
        }

        sleep(2000);

        List<MobileElement> listItems = driver.findElementsByCssSelector("div[ng-bind-html='item']");
        for ( MobileElement li:listItems ) {
            //System.out.println("list item:" + li.getText());
            if(li.getText().equals(number)) {
                li.click();
                System.out.println("Select: " + number);
                sleep(1000);
                break;

            }
        }
    }

    public void selectDayFromCalendar(String day) throws InterruptedException {
        sleep(2000);
        List<MobileElement> dayList = driver.findElementsByCssSelector("button[class='pika-button pika-day']");

        for (MobileElement pickDay:dayList) {
            //System.out.println("Day value:" + pickDay.getText());
            if(pickDay.getText().equals(day))
            {
                pickDay.click();
                sleep(1000);
                break;
            }

        }
    }
   public void selectOption(String criteria, String selectBy, String text_value){
       List<MobileElement> dropdownLists = driver.findElementsByCssSelector("select");
       Select oSelect = null;
       WebElement drp = null;

       if (criteria.toLowerCase().equals("language")) {
           System.out.println("Select Language...");
            drp = dropdownLists.get(0);
            oSelect = new Select(dropdownLists.get(0));
       }else if (criteria.toLowerCase().equals("currency")) {
           System.out.println("Select Currency...");
            drp = dropdownLists.get(1);
            oSelect = new Select(dropdownLists.get(1));
       }

       List<WebElement> oList = oSelect.getOptions();
       int selectIndex = 0;

       for(WebElement l:oList)
       {
           //System.out.println("select value:" + l.getAttribute("value") + " Text: " + l.getText() + " Index: " + l.getAttribute("index"));
           if (l.getText().contains(text_value)){

               selectIndex = Integer.parseInt(l.getAttribute("index"));
               break;
           }

       }

       if (selectBy.toLowerCase().equals("text"))
       {
          oSelect.selectByIndex(selectIndex);
       }else if (selectBy.toLowerCase().equals("value"))
       {
           oSelect.selectByValue(text_value);
       }

       System.out.println(text_value + " is selected...");
       //Click dropdown object to close pop-up
       drp.click();
   }


    public void waitForVisibilityOf(WebElement element){
        WebDriverWait wait = new WebDriverWait(driver,30);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void FindByCssSelectorClick(String cssSelector){
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.elementToBeClickable(
                (WebElement)
                        driver.findElementByCssSelector(cssSelector)
                )
        );

        MobileElement el = driver.findElementByCssSelector(cssSelector);
        el.click();
    }

    public void clickElementByText(String text)
    {
        if (findByText(text) == true)
        {
            WebElement el = driver.findElement(By.xpath(String.format("//*[text()='%s']",text)));
        }
    }

    public Boolean findByText(String text){
        WebElement textDemo = driver.findElement(By.xpath(String.format("//*[text()='%s']",text)));

        if (textDemo.isDisplayed()) {
            return true;
        }

        return false;

    }



    @When("^app launches go to url \"([^\"]*)\"$")
    public void appLaunchesGoToUrl(String url) throws Throwable {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", PLATFORM_NAME);
        capabilities.setCapability("deviceName", DEVICE_NAME);
        capabilities.setCapability("platformVersion", PLATFORM_VERSION);
        capabilities.setCapability("browserName", BROWSER_NAME);
        System.setProperty("webdriver.chrome.driver",CHROME_DRIVER);

       try {
            driver = new AndroidDriver<MobileElement>(new URL(APPIUM_SERVER), capabilities);

            // Open the app.
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @And("^set Language to \"([^\"]*)\"$")
    public void setLanguageTo(String language) throws Throwable {
        closeSignUpPopUp();

        //Click Menu
        clickMainMenu();

        //Select Language
        selectLanguage();
    }

    @And("^set Currency to \"([^\"]*)\"$")
    public void setCurrencyTo(String currency) throws Throwable {
        selectCurrency(currency);

        //Close Menu options and go back to main page
        closeHamburger();
    }

    @Then("^change loyalty program to \"([^\"]*)\"$")
    public void changeLoyaltyProgramTo(String loyaltyProgram) throws Throwable {
        changeLoyalty("logo-etihad");
    }

    @And("^set destination to \"([^\"]*)\" and select first that shows up$")
    public void setDestinationToAndSelectFirstThatShowsUp(String destination) throws Throwable {
        chooseDestination(destination);
    }

    @And("^select checkin date of one month from today$")
    public void selectCheckinDateOfOneMonthFromToday() {
    }

    @And("^select checkout date of two days later$")
    public void selectCheckoutDateOfTwoDaysLater() {
    }

    @And("^select room as \"([^\"]*)\"$")
    public void selectRoomAs(String arg0) throws Throwable {
        selectRoomAdultsChildren("Rooms","1");
    }

    @And("^select adult as \"([^\"]*)\"$")
    public void selectAdultAs(String arg0) throws Throwable {
        //Select Adults=2
        selectRoomAdultsChildren("Adults","2");
    }

    @And("^select children as \"([^\"]*)\"$")
    public void selectChildrenAs(String arg0) throws Throwable {
        //Select Children=0
        selectRoomAdultsChildren("Children","0");
    }

    @Then("^search for hotels and result page should show up$")
    public void searchForHotelsAndResultPageShouldShowUp() throws InterruptedException {
        searchHotels();
    }

    @Given("^all the data above now perform assertions$")
    public void allTheDataAboveNowPerformAssertions() {
        doAssertions();
    }

    @Then("^filter result and look for \"([^\"]*)\"$")
    public void filterResultAndLookFor(String filter) throws Throwable {
        applyFilter(filter);
    }

    @And("^now make sure \"([^\"]*)\" appears in the result$")
    public void nowMakeSureAppearsInTheResult(String filter) throws Throwable {
       validateFilterResult(filter);
    }

    @Then("^click on the result to navigate to page$")
    public void clickOnTheResultToNavigateToPage() throws InterruptedException {
        //Click result to navigate to page
        waitAndClickFor(driver.findElementByCssSelector("div[ng-bind='hotel.name']"));

        //End of Test
        sleep(3000);
    }

    @And("^select checkin date of one month from today and two day later for check out$")
    public void selectCheckinDateOfOneMonthFromTodayAndTwoDayLaterForCheckOut() throws InterruptedException {
        setCheckInAndCheckOut();
    }
}
