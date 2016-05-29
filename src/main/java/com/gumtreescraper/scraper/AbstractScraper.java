package com.gumtreescraper.scraper;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractScraper {
	
	protected WebDriver webDriver;
	
	public AbstractScraper() {
		System.setProperty(getDriverName(), getDriverPath());
	}
	
	public void openSite(String url) {
		webDriver.get(url);
//		webDriver.navigate().to(url);
		
		try {
                    webDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
//			webDriver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new IllegalStateException("Can't start Web Driver", e);
		}
	}

        public void openSiteWithoutTimeout(String url) {
		webDriver.get(url);
	}
        
//        public void waitForLoad(WebDriver driver) {
//            new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd ->
//                    ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
//        }
        
        public void waitForPageToLoad() {
            (new WebDriverWait(webDriver, 30)).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver d) {
                    return (((org.openqa.selenium.JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
                }
            });
        }
        
	public void closeBrowser() {
		//close the browser
	    webDriver.close();
	    webDriver.quit();
	}
	
	protected int getTimeout() {
            return 10;
        }
	
	protected abstract String getStartURL();
	
	protected String getDriverPath() {
		return "src/driver/chromedriver.exe";
	}
	
	protected String getDriverName() {
		return "webdriver.chrome.driver";
	}
	
	public void start() {
		openSite(getStartURL());
	}
}
