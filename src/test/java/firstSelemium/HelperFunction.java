package firstSelemium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class HelperFunction {
	public static void validatePageOnLoad(WebDriver driver, String title, String url, String pageTitle) {
		Assert.assertTrue(driver.getCurrentUrl().contains(url));
		Assert.assertEquals(driver.getTitle(), pageTitle);
		String selector = url == "login" ? "//h1" : "//div[contains(@class, 'content-header')]//h1";
		System.out.println(selector);
		WebElement headingTag = driver.findElement(By.xpath(selector));
		Assert.assertTrue(headingTag.getText().contains(title));
	}

	public static void checkToolTip(WebDriver driver, Actions action, String ele, String title) {
		WebElement element = driver.findElement(By.xpath("//label[@for='" + ele + "']/following-sibling::div"));
		action.moveToElement(element).build().perform();
		Assert.assertTrue(element.getAttribute("data-original-title").contains(title), "Tooltip for " + ele);
	}

	// Check of loader appears
	public static void isLoading(WebDriver driver) {
		WebElement loader = driver.findElement(By.id("ajaxBusy"));
		Assert.assertTrue(loader.getCssValue("display").equals("block"));
	}

	// Check if active style effected on links
	public static void isActive(WebElement ele, Actions action, String eq) throws InterruptedException {
		action.clickAndHold(ele).build().perform();
		Thread.sleep(1500);
		System.out.println(ele.getCssValue("background-color"));
		Assert.assertTrue(ele.getCssValue("background-color").equals(eq));

	}

	public static void isHover(WebElement ele, Actions action, String eq) throws InterruptedException {
		action.moveToElement(ele).build().perform();
		Thread.sleep(1500);
		System.out.println(ele.getCssValue("background-color"));
		Assert.assertTrue(ele.getCssValue("background-color").equals(eq));

	}

	public static void checkNestedList(WebDriver driver, String listName) {
		WebElement ele = driver.findElement(By.xpath(
				"//aside//nav/ul/li/a/*[contains(text(),'" + listName + "')]/ancestor::a/following-sibling::ul"));
		Assert.assertTrue(ele.getCssValue("display").equals("block"));
		
	}

	// Check collapsed div
	public static void cardCollapse(WebDriver driver, String divName) {
		WebElement ele = driver.findElement(By.id(divName));
		if (ele.getAttribute("class").contains("collapsed-card")) {
			ele.click();
			WebElement collapseBtn = driver
					.findElement(By.xpath("//div[@id='" + divName + "']//div[contains(@class, \"card-tools\")]//i"));
			Assert.assertTrue(collapseBtn.getAttribute("class").contains("fa-minus"));

			WebElement cardBody = driver
					.findElement(By.xpath("//div[@id='" + divName + "']//div[contains(@class, \"card-body\")]"));
			Assert.assertTrue(cardBody.getCssValue("display").contains("block"));
		}
	}

	// Handle search box expanding
	public static void handleSearchBox(WebDriver driver) {
		WebElement ele = driver.findElement(By.xpath("//div[contains(@class, 'card-search')]/div/div[1]"));
		if (!ele.getAttribute("class").contains("opened")) {
			ele.click();
		}
	}
	
	public static void alertMessageChecker(WebDriver driver, String messageContent) {
		WebElement successAlert = driver.findElement(By.className("alert-success"));
		System.out.println(successAlert.getCssValue("background-color"));
		boolean isSuccess = (successAlert.getCssValue("background-color").equals("rgba(23, 183, 109, 1)"));
		Assert.assertTrue(isSuccess, "Check the alert back color");

		boolean isAlertContainText = successAlert.getText().contains(messageContent);
		System.out.println(successAlert.getText());
		Assert.assertTrue(isAlertContainText, "Check the alert content");
	}
	
	public static void checkActiveNavItem(WebDriver driver, String itemText) {
		WebElement ele = driver.findElement(By.linkText(itemText));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.attributeContains(ele, "class", "active"));
		Assert.assertTrue(ele.getAttribute("class").contains("active"));
	}
	
	public static void checkLeftBarMenu(WebDriver driver, String listName) {
		WebElement eleLeftBar = driver.findElement(By.xpath(
				"//aside//nav/ul/li/a/*[contains(text(),'" + listName + "')]/ancestor::a"));
		Assert.assertEquals(eleLeftBar.getCssValue("border-color"), "rgb(0, 123, 255)");

	}
	
	public static void checkParentMenuItemOnHover(WebDriver driver, Actions action, String listName) {
		WebElement el = driver.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'"+listName+"')]/ancestor::li"));
		action.moveToElement(el).build().perform();
		
		String anchorEl = driver.findElement(By.xpath("//aside//nav/ul/li/a/*[contains(text(),'"+listName+"')]/ancestor::a")).getCssValue("background-color");
		Assert.assertEquals(anchorEl, "rgba(255, 255, 255, 0.1)");
	}
	
	public static void isAdvance(WebDriver driver, boolean advance) {
		WebElement pseudoEle = driver.findElement(By.xpath("//span[@data-locale-advanced=\"Advanced\"]"));
		if(advance) {
			String colorRGB = ((JavascriptExecutor)driver).executeScript("return window.getComputedStyle(arguments[0], ':before').getPropertyValue('background-color');",pseudoEle).toString();
			Assert.assertEquals(colorRGB, "rgb(60, 141, 188)");
			Assert.assertEquals(pseudoEle.getCssValue("margin-left"), "0px");
		} else {
			String colorRGB = ((JavascriptExecutor)driver).executeScript("return window.getComputedStyle(arguments[0], ':after').getPropertyValue('background-color');",pseudoEle).toString();
			Assert.assertEquals(colorRGB, "rgb(239, 239, 239)");
			Assert.assertEquals(pseudoEle.getCssValue("margin-left"), "-128px");
		}
	}
	
	public static void advanceMode(WebDriver driver, boolean mode) throws InterruptedException {
		WebElement bodyElement = driver.findElement(By.xpath("//body"));
		boolean bodyClass = bodyElement.getAttribute("class").contains("basic-settings-mode");
		if (mode) {
			if(bodyClass) {
				WebElement advance = driver.findElement(By.xpath("//label[@for=\"advanced-settings-mode\"]"));
				advance.click();				
			}
		} else {
			if(!bodyClass) {
				WebElement advance = driver.findElement(By.xpath("//label[@for=\"advanced-settings-mode\"]"));
				advance.click();				
			}
		}
		Thread.sleep(1000);
	}
	
	public static void fillAndAssertField(WebDriver driver, Actions action, String element, String content) throws InterruptedException {
		WebElement el = driver.findElement(By.id(element));
		action.click(el).build().perform();
//		System.out.println(el.getCssValue("box-shadow"));
		Assert.assertEquals(el.getCssValue("box-shadow"), "rgba(0, 123, 255, 0.25) 0px 0px 0px 3.2px");
		Thread.sleep(500);
		el.sendKeys(content);
		Assert.assertEquals(el.getAttribute("value"), content);

	}
	
	public static void selectAssertList(WebDriver driver, Actions action, String element, String content) throws InterruptedException {		
		WebElement category = driver.findElement(By.xpath("//select[@id='"+element+"']/parent::div"));
		category.click();
		Assert.assertEquals(category.getCssValue("box-shadow"), "rgba(0, 123, 255, 0.25) 0px 0px 0px 3.2px");
		Thread.sleep(500);

		List<WebElement> selectCategoryListItems = driver
				.findElements(By.xpath("//ul[@id='"+element+"_listbox\']/li"));
		for (WebElement ele : selectCategoryListItems) {
			System.out.println(ele.getText());
			if (ele.getText().equals(content)) {
				ele.click();
			}
		}

		List<WebElement> selectedCategoryList = driver
				.findElements(By.xpath("//ul[@id='"+element+"_taglist']/li"));

		boolean isContainComputer = false;
		for (WebElement ele : selectedCategoryList) {
			if (ele.getText().trim().equals(content)) {
				isContainComputer = true;
				System.out.println(ele.getText());
			}
		}
		Assert.assertTrue(isContainComputer, "Check if the list contain Computer");

	}
	
	public static int tableCounter(WebDriver driver, WebDriverWait wait, String tableSelector) {
		WebElement discountTableCounter = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(tableSelector))); 

		String[] discountSplited = discountTableCounter.getText().split("\\s+");
		System.out.println(discountSplited[2]);
		return Integer.parseInt(discountSplited[2]);
	}; 
}
