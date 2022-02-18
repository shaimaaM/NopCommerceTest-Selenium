package firstSelemium;

import java.text.ParseException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class DatePicker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void selectDate(String year, String monthName, String day, WebDriver driver,
			String cal, String calButton) throws ParseException, InterruptedException {
		
		WebElement calendar = driver
				.findElement(By.xpath("//input[@id='"+ calButton +"']/following-sibling::span/span[1]"));
		calendar.click();
		
		WebElement currentYear = driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-live='assertive']"));
		String curYear = currentYear.getAttribute("innerText");
		
		Thread.sleep(1000);
		
		if (!curYear.contains(year)) {
			do {
				driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-label=\"Next\"]")).click();
			} while (!driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-live='assertive']")).getAttribute("innerText")
					.contains(year));
		}
		
		Thread.sleep(1000);

		WebElement curMonth = driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-live='assertive']"));
		String currentMonth = curMonth.getAttribute("innerText");
		
		if (!currentMonth.contains(monthName)) {
			do {
				driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-label=\"Next\"]")).click();
			} while (!driver.findElement(By.xpath("//div[@id='" + cal + "']//a[@aria-live='assertive']")).getAttribute("innerText")
					.contains(monthName));
		}

		Thread.sleep(1000);

		List<WebElement> allDateOfDesiredMonth = driver
				.findElements(By.xpath("//div[@id='" + cal + "']//table/tbody//a"));
		for (WebElement d : allDateOfDesiredMonth) {
			if (d.getAttribute("innerText").equals(day) && !d.getAttribute("class").contains("k-other-month")) {
//				System.out.println(d.getAttribute("innerText"));
				d.click();

				JavascriptExecutor executor = (JavascriptExecutor)driver;
				WebElement parentElement = (WebElement)executor.executeScript("return arguments[0].parentNode;", d);

				boolean activeStyle = parentElement.getAttribute("class").contains("k-state-selected");
				Assert.assertTrue(activeStyle, "Verify table data cell has the orange background color");
				
				break;
			}
		}

	}
}
