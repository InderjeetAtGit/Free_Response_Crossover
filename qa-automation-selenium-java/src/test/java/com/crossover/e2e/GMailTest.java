package com.crossover.e2e;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GMailTest {
	private WebDriver driver;
	private Properties properties = new Properties();

	@BeforeMethod
	public void setUp() throws Exception {

		properties.load(new FileReader(new File("src/test/resources/test.properties")));
		// Dont Change below line. Set this value in test.properties file incase you
		// need to change it..
		System.setProperty("webdriver.gecko.driver", "C:\\Browserdrivers\\geckodriver.exe");
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		caps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@AfterMethod
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void testSendEmail() throws Exception {
		driver.get("https://gmail.com");
		WebDriverWait wait = new WebDriverWait(driver, 10);

		WebElement userElement = driver.findElement(By.id("identifierId"));
		userElement.sendKeys(properties.getProperty("username"));

		driver.findElement(By.id("identifierNext")).click();
		Thread.sleep(1000);
		WebElement passwordElement = driver
				.findElement(By.xpath("//input[@name='password' and @class='whsOnd zHQkBf']"));

		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//input[@name='password' and @class='whsOnd zHQkBf']")));

		wait.until(ExpectedConditions.elementToBeClickable(passwordElement));
		passwordElement.sendKeys(properties.getProperty("password"));
		driver.findElement(By.id("passwordNext")).click();

		String emailSubject = properties.getProperty("email.subject");
		String emailBody = properties.getProperty("email.body");

		WebElement composeElement = driver
				.findElement(By.xpath("//div[@class='T-I J-J5-Ji T-I-KE L3' and @role='button']"));
		wait.until(ExpectedConditions.elementToBeClickable(composeElement));
		composeElement.click();
		WebElement to = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//textarea[@name='to']")));
		Actions action = new Actions(driver);
		action.moveToElement(to);
		to.sendKeys(String.format("%s@gmail.com", properties.getProperty("toemail")));

		driver.findElement(By.xpath("//input[@name='subjectbox' and @id=':qu']")).sendKeys(emailSubject);

		driver.findElement(By.xpath("//div[@id=':rz' and @class='Am Al editable LW-avf tS-tW']")).sendKeys(emailBody);

		driver.findElement(By.xpath("//*[@role='button' and text()='Send']")).click();
		Thread.sleep(5000);


	}
}
