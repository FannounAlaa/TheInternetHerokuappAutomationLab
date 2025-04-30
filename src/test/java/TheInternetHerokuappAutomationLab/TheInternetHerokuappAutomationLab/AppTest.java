package TheInternetHerokuappAutomationLab.TheInternetHerokuappAutomationLab;

import java.net.HttpURLConnection;
import java.net.URI;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class AppTest {
	WebDriver driver = new ChromeDriver();
	JavascriptExecutor js = (JavascriptExecutor) driver; 
	WebDriverWait wait;
	String URL = "https://the-internet.herokuapp.com/";

	
	@BeforeTest
	public void Setup() {
		driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get(URL);
	}
	
	@Test (priority = 1)
	public void AddRemoveTest() {
		WebElement AddRemoveButton = driver.findElement(By.linkText("Add/Remove Elements"));
		AddRemoveButton.click();
		
		WebElement AddElements = driver.findElement(By.xpath("//button[@onclick='addElement()']"));
		
		int numberOfElementsToAdd = 7;
		for (int i = 0 ; i < numberOfElementsToAdd ; i++) {
			AddElements.click();
		}
		
		while(true) {
			List<WebElement> RemoveElementButtons = driver.findElements(By.className("added-manually"));
			
			if (RemoveElementButtons.isEmpty()) {
				break;
			}
	        WebElement button = RemoveElementButtons.get(0);
	        button.click();
	        wait.until(ExpectedConditions.stalenessOf(button));
		}
		
		if (driver.findElements(By.className("added-manually")).size() == 0) {
			System.out.println("Elements Remove Successfully");
		}
		else {
			System.out.println("Some Elements were not Removed");
		}
		
		
	}
	
	@Test (priority = 2)
	public void BasicAuth()	{
	    String urlWithAuth = "https://admin:admin@the-internet.herokuapp.com/basic_auth";
	    driver.get(urlWithAuth);
	    
	    WebElement message = driver.findElement(By.tagName("p"));
	    String result = message.getText();
	    
	    if (result.contains("Congratulations")) {
	    	System.out.println("Basic auth succeeded");
	    }
	    else {
	    	System.out.println("Basic Auth failed");
	    }
	}
	
	@Test (priority = 3)
	public void BrokenImgTest () {
		driver.get(URL);
		
		WebElement BrokenImgBtn = driver.findElement(By.linkText("Broken Images"));
		BrokenImgBtn.click();

		
		List<WebElement> Images = driver.findElements(By.xpath("//div[@class='example']//img"));
		
		String baseUrl = "https://the-internet.herokuapp.com";
		
        System.out.println("Total images found: " + Images.size());
		for (int i = 0; i < Images.size(); i++) {
			String src = Images.get(i).getDomAttribute("src");
			
		    String fullUrl = src.startsWith("http") ? src : baseUrl + "/" + src;
	        System.out.println("Checking: " + fullUrl);
			
	        try {
	        	HttpURLConnection connection = (HttpURLConnection) URI.create(fullUrl).toURL().openConnection();
	        	connection.setRequestMethod("GET");
	            connection.connect();
	            int statusCode = connection.getResponseCode();

	            if (statusCode >= 400) {
	                System.out.println("Broken Image: " + fullUrl + " | Status Code: " + statusCode);
	            } else {
	                System.out.println("Valid Image: " + fullUrl + " | Status Code: " + statusCode);
	            }

	        } catch (Exception e) {
	            System.out.println("Error checking image: " + fullUrl);
	            e.printStackTrace();
	        }
			
		}
	}
	@Test (priority = 4)
	public void CheckBoxTest () {
		driver.get(URL);
		WebElement CheckboxesBtn = driver.findElement(By.linkText("Checkboxes"));
		CheckboxesBtn.click();

		List<WebElement> checkBoxes = driver.findElements(By.xpath("//form[@id='checkboxes']//input"));
		for (int i = 0; i < checkBoxes.size(); i++) {
			checkBoxes.get(i).click();
		}
	}
	@Test (priority = 5)
	public void ContextMenuTest() {
		driver.get(URL);
		WebElement ContextMenuBtn = driver.findElement(By.linkText("Context Menu"));
		ContextMenuBtn.click();
		
		WebElement Box = driver.findElement(By.id("hot-spot"));
		
		Actions actions = new Actions(driver);
		actions.contextClick(Box).perform();
		
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}
	@Test (priority = 6)
	public void DisappearingElementsTest () {
		driver.get(URL);
		WebElement DisappearingElementsBtn = driver.findElement(By.linkText("Disappearing Elements"));
		DisappearingElementsBtn.click();
		
	    boolean elementDisappeared = false;
		
		int NumberOfRefresh = 10;
		for (int i = 0; i < NumberOfRefresh ; i++) {
			driver.navigate().refresh();
			
			List<WebElement> MenuItems = driver.findElements(By.tagName("li"));
			
			int count = MenuItems.size();
			
			if(count < 5) {
				elementDisappeared = true;
				break;
			}
		}
		
		if(elementDisappeared) {
			System.out.println("Missing Items in the menu");
		}
		else {
			System.out.println("All menu items present");
		}
	}
	@Test(priority = 7)
	public void DragAndDropTest() {
		driver.get(URL);
		WebElement DisappearingElementsBtn = driver.findElement(By.linkText("Drag and Drop"));
		DisappearingElementsBtn.click();
		
		WebElement ColumnA = driver.findElement(By.id("column-a"));
		WebElement ColumnB = driver.findElement(By.id("column-b"));
		
		Actions actions = new Actions(driver);
		
		actions.dragAndDrop(ColumnA, ColumnB).perform();
	}
	
	

}
