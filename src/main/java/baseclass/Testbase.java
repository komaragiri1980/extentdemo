package baseclass;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Testbase {
	
	public static WebDriver driver;
	public static Properties prop;
	public static FileReader fr;
	public ExtentSparkReporter htmlReporter;
	 public ExtentReports extent;
	 public ExtentTest test;

	public Testbase() throws IOException {

		fr = new FileReader(System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\config.properties");
		prop= new Properties();

		prop.load(fr);

	}
	 @BeforeTest
	 public void setExtent() {
	  // specify location of the report
	  htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/myReport.html");

	  htmlReporter.config().setDocumentTitle("Automation Report"); // Tile of report
	  htmlReporter.config().setReportName("Functional Testing"); // Name of the report
	  htmlReporter.config().setTheme(Theme.DARK);
	  
	  extent = new ExtentReports();
	  extent.attachReporter(htmlReporter);
	  
	  // Passing General information
	  extent.setSystemInfo("Host name", "localhost");
	  extent.setSystemInfo("Environemnt", "QA");
	  extent.setSystemInfo("user", "Madhu");
	 }

	 @AfterTest
	 public void endReport() {
	  extent.flush();
	 }
	 @BeforeMethod
	public static void initialization() {
		
		if(prop.getProperty("browser").equalsIgnoreCase("chrome")) {
			
			WebDriverManager.chromedriver().setup();
			driver=new ChromeDriver();
						
		}

	else if (prop .getProperty("browser").equalsIgnoreCase("firefox")) {
		
		WebDriverManager.edgedriver().setup();
		driver=new EdgeDriver();
							
		}
	else if (prop.getProperty("browser").equalsIgnoreCase("edge")) {
			
			WebDriverManager.edgedriver().setup();
			driver=new EdgeDriver();
		}
			driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		
		driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.MINUTES);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.MINUTES);
		}

	 @AfterMethod
	 public void tearDown(ITestResult result) throws IOException {
	  if (result.getStatus() == ITestResult.FAILURE) {
	   test.log(Status.FAIL, "TEST CASE FAILED IS " + result.getName()); // to add name in extent report
	   test.log(Status.FAIL, "TEST CASE FAILED IS " + result.getThrowable()); // to add error/exception in extent report
	   String screenshotPath = Testbase.getScreenshot(driver, result.getName());
	   test.addScreenCaptureFromPath(screenshotPath);// adding screen shot
	  } else if (result.getStatus() == ITestResult.SKIP) {
	   test.log(Status.SKIP, "Test Case SKIPPED IS " + result.getName());
	  }
	  else if (result.getStatus() == ITestResult.SUCCESS) {
	   test.log(Status.PASS, "Test Case PASSED IS " + result.getName());
	  }
	  driver.quit();
	 }
	 
	 public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
	  String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
	  TakesScreenshot ts = (TakesScreenshot) driver;
	  File source = ts.getScreenshotAs(OutputType.FILE);
	  
	  // after execution, you could see a folder "FailedTestsScreenshots" under source folder
	  String destination = Paths.get("").toAbsolutePath() + "/Screenshots/" + screenshotName + dateName + ".png";
	  File finalDestination = new File(destination);
	  FileUtils.copyFile(source, finalDestination);
	  return destination;
	 }
		
	//Test1
	 @Test(priority=1)
	 public void googletitleTest() {
	  test = extent.createTest("Google Title Test");
	  driver.get("https://www.google.com/");
	  String title = driver.getTitle();
	  System.out.println(title);
	  Assert.assertEquals(title, "Googe");
	  	 }
	 @Test(priority=2)
	 public void facebookTitleTest() {
		  test = extent.createTest("Facebook Title Test");
		  driver.get("https://www.facebook.com/");
		  String title = driver.getTitle();
		  System.out.println(title);
		  Assert.assertEquals(title, "Facebook - log in or sign up");
		  	 }
		 
	 
	}
	


