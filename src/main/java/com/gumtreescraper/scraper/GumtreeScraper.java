/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gumtreescraper.scraper;

import com.gumtreescraper.model.Gumtree;
import com.sun.jna.platform.win32.WinBase;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author duonghung1269
 */
public class GumtreeScraper extends AbstractScraper {
    
    private String username;
    private String password;
    private String url;
    private String fileName;
    
    private static final String BASE_URL = "https://www.gumtree.com.au";
    private static final String LOGIN_URL = "https://www.gumtree.com.au/t-login-form.html";
    
    
    public GumtreeScraper(String userName, String password, String url, String fileName) {
        super();
        this.username = userName;
        this.password = password;
        this.url = url;
        this.fileName = fileName;
        this.webDriver = new ChromeDriver();
        
        // add 100 result per page cookie
//        Cookie ck = new Cookie("up", "%7B%22ln%22%3A%22532176319%22%2C%22sps%22%3A%22100%22%2C%22ls%22%3A%22l%3D0%26c%3D20031%26r%3D0%26sv%3DLIST%26sf%3Ddate%22%2C%22lbh%22%3A%22l%3D0%26c%3D20031%26r%3D0%26sv%3DLIST%26sf%3Ddate%22%7D");
//        webDriver.manage().addCookie(ck);
    }

    public boolean login() {
        openSite(LOGIN_URL);
        webDriver.findElement(By.id("login-email")).sendKeys(username);
        webDriver.findElement(By.id("login-password")).sendKeys(password);
        webDriver.findElement(By.className("login-form-submit")).findElement(By.tagName("button")).click();
        
        waitForSeconds(10);
        
        try {
            // if found then return true, otherwise return false
            webDriver.findElement(By.className("item-sign-out")); 
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
        
        return true;
    }
    
    public void scrapeWithClick(List<Gumtree> gumtrees, String url) {
        
        openSite(url);
            waitForPageToLoad();
                
        do {
            List<WebElement> gumtreeAds = (new WebDriverWait(this.webDriver, 15))
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//ul[@id='srchrslt-adtable']/li//h6[@class='rs-ad-title']/a")));
                      
            for (WebElement ad : gumtreeAds) {
//                String adUrl = (new WebDriverWait(this.webDriver, 15))
//            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h6[@class='rs-ad-title']/a"))).getAttribute("href");
//                String adUrl = ad.findElement(By.xpath("//h6[@class='rs-ad-title']/a")).getAttribute("href");
                String adUrl = ad.getAttribute("href");
                Gumtree gumtree = new Gumtree();
                gumtree.setUrl(adUrl);
                gumtrees.add(gumtree);
            }

            List<WebElement> nextElements = webDriver.findElements(By.xpath("//a[@class='rs-paginator-btn next']"));
            if (nextElements.isEmpty()) { //  no more next page
                break;
            }

            nextElements.get(0).click();
            try {
                Thread.sleep(5000);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(GumtreeScraper.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        } while(true);
    }
    
    public void scrape(List<Gumtree> gumtrees, String url) {
        
        
        // get total page
        int totalPage = getTotalPage(url);
        
        for (int i = 1; i <= totalPage; i++) {
            
                
            
            String newUrl = buildPageUrl(url, i);
//            openSiteWithoutTimeout(newUrl);
            openSite(newUrl);
            waitForPageToLoad();
            
            if (i == 1) {
                try {
                    Thread.sleep(20 * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GumtreeScraper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
//            List<WebElement> gumtreeAds = (new WebDriverWait(this.webDriver, 15))
//            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//ul[@id='srchrslt-adtable']/li")));
            
            List<WebElement> gumtreeAds = (new WebDriverWait(this.webDriver, 15))
            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//ul[@id='srchrslt-adtable']/li//h6[@class='rs-ad-title']/a")));

            for (WebElement ad : gumtreeAds) {
//               (new WebDriverWait(this.webDriver, 15))
//            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h6[@class='rs-ad-title']/a")));
                
                try {
//                    String adUrl = ad.findElement(By.xpath("//h6[@class='rs-ad-title']/a")).getAttribute("href");
                    String adUrl = ad.getAttribute("href");
                    Gumtree gumtree = new Gumtree();
                    gumtree.setUrl(adUrl);
                    gumtrees.add(gumtree);
                } catch (StaleElementReferenceException ex) {
                    ex.printStackTrace();
                    String adUrl = ad.findElement(By.xpath("//h6[@class='rs-ad-title']/a")).getAttribute("href");
                    Gumtree gumtree = new Gumtree();
                    gumtree.setUrl(adUrl);
                    gumtrees.add(gumtree);
                }
            }
            
//            List<WebElement> nextElements = webDriver.findElements(By.xpath("//a[@class='rs-paginator-btn next']"));
//            if (nextElements.isEmpty()) { //  no more next page
//                break;
//            }
//
//            nextElements.get(0).click();
        }
        
//        List<WebElement> lastPageElements = webDriver.findElements(By.xpath("//a[@class='rs-paginator-btn last']"));
//        if (lastPageElements.isEmpty()) { // 1 page only
//            return;
//        }
//                
//        do {
//            List<WebElement> gumtreeAds = (new WebDriverWait(this.webDriver, 15))
//            .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//ul[@id='srchrslt-adtable']/li")));
//                      
//            for (WebElement ad : gumtreeAds) {
//                String adUrl = (new WebDriverWait(this.webDriver, 15))
//            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h6[@class='rs-ad-title']/a"))).getAttribute("href");
////                String adUrl = ad.findElement(By.xpath("//h6[@class='rs-ad-title']/a")).getAttribute("href");
//                Gumtree gumtree = new Gumtree();
//                gumtree.setUrl(adUrl);
//                gumtrees.add(gumtree);
//            }
//
//            List<WebElement> nextElements = webDriver.findElements(By.xpath("//a[@class='rs-paginator-btn next']"));
//            if (nextElements.isEmpty()) { //  no more next page
//                break;
//            }
//
//            nextElements.get(0).click();
//
////        scrape(gumtrees, nextPageUrl);
//        
//        } while(true);
    }
    
    private String buildPageUrl(String url, int pageNo) {
//        http://www.gumtree.com.au/s-land-for-sale/c20031
        // http://www.gumtree.com.au/s-land-for-sale/page-109/c20031
        int lastIndexOfSlash = url.lastIndexOf("/");
        String newUrl = url.substring(0, lastIndexOfSlash) + "/page-" + pageNo + url.substring(lastIndexOfSlash);
        return newUrl;
//        String findString = "/page-";        
//        int fromIndex = url.indexOf(findString);
//        int toIndex = StringUtils.indexOf(url, "/", fromIndex + 1);
//        String newPage = findString + pageNo;
//        String newPageUrl = url.substring(0, fromIndex) + newPage + url.substring(toIndex);
//        return newPageUrl;
    }
    
    private int getTotalPage(String url) {        
        openSiteWithoutTimeout(url);
        String lastPageUrl = (new WebDriverWait(this.webDriver, 15))
            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='rs-paginator-btn last']"))).getAttribute("href");
        
        // http://www.gumtree.com.au/s-land-for-sale/page-109/c20031
        String findString = "/page-";
        int indexPage = lastPageUrl.indexOf(findString);
        int lastIndexOfSlash = lastPageUrl.lastIndexOf("/");
        String totalPageStr = lastPageUrl.substring(indexPage + findString.length(), lastIndexOfSlash);
        return Integer.parseInt(totalPageStr);
    }
    
    private void waitForSeconds(int seconds) {
        // sleep for 10 seconds
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected String getStartURL() {
        return url;
    }

    @Override
    protected String getDriverPath() {
        return "src/main/java/driver/chromedriver";
    }

}
