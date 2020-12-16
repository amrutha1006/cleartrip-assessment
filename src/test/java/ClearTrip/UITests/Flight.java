package ClearTrip.UITests;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import ClearTrip.Utilities.Conversion;

public class Flight {
	
    ExtentHtmlReporter htmlReporter;
    ExtentReports extent;
    ExtentTest test;
    WebDriver driver;
    
    @BeforeTest
    public void startReport() {
    	// initialize the HtmlReporter
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/testReport.html");
        
        //initialize ExtentReports and attach the HtmlReporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        
        //configuration items to change the look and feel
        //add content, manage tests etc
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Flight Search Details");
        htmlReporter.config().setReportName("Flight Search Report");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    }
	
	@Test
	public void FlightSearch() throws InterruptedException {
		test = extent.createTest("FlightSearch");
		 System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\Resources\\chromedriver.exe");
		 ChromeOptions options = new ChromeOptions();
		 options.addArguments("--disable-notifications");
	        driver = new ChromeDriver(options);
	        WebDriverWait wait=new WebDriverWait(driver, 20);
	        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	        driver.manage().window().maximize();
	        driver.get("https:\\cleartrip.com");
	        driver.findElement(By.linkText("Flights")).click();
	        driver.findElement(By.cssSelector("#RoundTrip")).click();
	        driver.findElement(By.id("FromTag")).sendKeys("New Delhi, IN - Indira Gandhi Airport (DEL)");
	        driver.findElement(By.id("ToTag")).sendKeys("Mumbai, IN - Chatrapati Shivaji Airport (BOM)");
	        driver.findElements(By.cssSelector("[data-datepicker=\"click\"]")).get(0).click();
	        driver.findElement(By.cssSelector("[class=\" ui-datepicker-days-cell-over  selected\"]")).click();
	        DateFormat dateFormat = new SimpleDateFormat("dd");
	        Date currentDate = new Date();
	        Calendar c = Calendar.getInstance();
	        c.setTime(currentDate);
	        c.add(Calendar.DATE, 10);
	        Date currentDatePlusTen = c.getTime();
	        
	        int toDate = Integer.parseInt(dateFormat.format(currentDatePlusTen));
	        String s=String.valueOf(toDate);
	        Thread.sleep(2000);
	        driver.findElements(By.xpath("//a[@class=\"ui-state-default \" and text()='"+s+"']")).get(0).click();
	        driver.findElement(By.id("SearchBtn")).click();
	        
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()=\"Stops\"]")));
	        int onwardCount=0;
	        int returnCount=0;
	        boolean t=true;
	        int e = driver.findElements(By.xpath("//div[@data-test-attrib=\"return-view\"]/div/div")).size();
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            while(t) {
            	Thread.sleep(2000);
            	js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            	int d = driver.findElements(By.xpath("//div[@data-test-attrib=\"return-view\"]/div/div")).size();
            	if(e==d) {
            		t=false;
            	}
            	e=d;
            }
            
	        int onwardFlights = driver.findElements(By.xpath("//div[@data-test-attrib=\"onward-view\"]/div/div")).size();
	        int returnFlights = driver.findElements(By.xpath("//div[@data-test-attrib=\"return-view\"]/div/div")).size();
	        System.out.println("onward flights "+onwardFlights);
	        System.out.println("return flights "+returnFlights);
	        for(int i=1;i<=onwardFlights;i++) {
	        	String priceStr = driver.findElements(By.xpath("//div[@data-test-attrib=\"onward-view\"]/div/div["+i+"]//p")).get(5).getText();
	        	int priceInt = Conversion.convertCurrencyStringToInt(priceStr);
	        	if(priceInt<5000) {
	        		onwardCount++;
	        	}
	        }
	        for(int i=1;i<=returnFlights;i++) {
	        	String priceStr = driver.findElements(By.xpath("//div[@data-test-attrib=\"return-view\"]/div/div["+i+"]//p")).get(5).getText();
	        	int priceInt = Conversion.convertCurrencyStringToInt(priceStr);
	        	if(priceInt<7000) {
	        		returnCount++;
	        	}
	        }
	        System.out.println("onward flights less than 5000 are "+onwardCount);
	        System.out.println("return flights less than 7000 are "+returnCount);
	        js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//label//p[text()=\"Non-stop\"]"))); 
	        driver.findElement(By.xpath("//label//p[text()=\"Non-stop\"]")).click();
	        driver.findElement(By.xpath("//label//p[text()=\"1 stop\"]")).click();
	        t=true;
	        e = driver.findElements(By.xpath("//div[@data-test-attrib=\"return-view\"]/div/div")).size();
	        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            while(t) {
            	Thread.sleep(2000);
            	js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            	int d = driver.findElements(By.xpath("//div[@data-test-attrib=\"return-view\"]/div/div")).size();
            	if(e==d) {
            		t=false;
            	}
            	e=d;
            }
	        int onwardFlightsOneStop = driver.findElements(By.xpath("//div[@data-test-attrib=\"onward-view\"]/div/div")).size();
	        int returnFlightsOneStop = driver.findElements(By.xpath("//div[@data-test-attrib=\"return-view\"]/div/div")).size();
	        System.out.println("onward flights with 1 stop and non-stop "+onwardFlightsOneStop);
	        System.out.println("return flights with 1 stop and non-stop "+returnFlightsOneStop);
	        onwardCount=0;returnCount=0;
	        for(int i=1;i<=onwardFlightsOneStop;i++) {
	        	String priceStr = driver.findElements(By.xpath("//div[@data-test-attrib=\"onward-view\"]/div/div["+i+"]//p")).get(5).getText();
	        	int priceInt = Conversion.convertCurrencyStringToInt(priceStr);
	        	if(priceInt<7000) {
	        		onwardCount++;
	        	}
	        }
	        for(int i=1;i<=returnFlightsOneStop;i++) {
	        	String priceStr = driver.findElements(By.xpath("//div[@data-test-attrib=\"return-view\"]/div/div["+i+"]//p")).get(5).getText();
	        	int priceInt = Conversion.convertCurrencyStringToInt(priceStr);
	        	if(priceInt<7000) {
	        		returnCount++;
	        	}
	        }
	        System.out.println("onward flights less than 7000 are "+onwardCount);
	        System.out.println("return flights less than 7000 are "+returnCount);
	        js.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.cssSelector("[data-test-attrib=\"cleartrip-logo\"]")));
	        driver.findElement(By.xpath("//div[@data-test-attrib=\"onward-view\"]/div/div[2]")).click();
	        driver.findElement(By.xpath("//div[@data-test-attrib=\"return-view\"]/div/div[5]")).click();
	        String firstFlight = driver.findElements(By.xpath("//div[@data-test-attrib=\"onward-view\"]/div/div[2]//p")).get(5).getText();
	        String secondFlight = driver.findElements(By.xpath("//div[@data-test-attrib=\"return-view\"]/div/div[5]//p")).get(5).getText();
	        int firstFlightRate = Conversion.convertCurrencyStringToInt(firstFlight);
	        int secondFlightRate = Conversion.convertCurrencyStringToInt(secondFlight);
	        int TwoFlightsRate = firstFlightRate+secondFlightRate;
	        String Amount = Conversion.convertIntToCurrencyString(TwoFlightsRate);
	        Thread.sleep(2000);
	        String AmountUI = driver.findElement(By.cssSelector("[class=\"flex flex-bottom flex-right\"]")).getText();
	        Assert.assertEquals(AmountUI, Amount);
	       
	}
	
	@AfterMethod
    public void getResult(ITestResult result) throws IOException {
        if(result.getStatus() == ITestResult.FAILURE) {
        	TakesScreenshot scrShot =((TakesScreenshot)driver);
        	File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        	File DestFile=new File(System.getProperty("user.dir")+"/"+result.getName()+".png");
        	FileUtils.copyFile(SrcFile, DestFile);
        	test.addScreenCaptureFromPath(System.getProperty("user.dir")+"/"+result.getName()+".png");
            test.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" FAILED ", ExtentColor.RED));
            test.fail(result.getThrowable());
        }
        else if(result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" PASSED ", ExtentColor.GREEN));
        }
        else {
            test.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+" SKIPPED ", ExtentColor.ORANGE));
            test.skip(result.getThrowable());
        }
    }
	
	@AfterTest
    public void tearDown() {
		driver.quit();
        extent.flush();
    }
}
