package com.dailystatus.dailyupdate.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SeleniumConfig {

    @Value("${download.folder.path}")
    private String downloadFolderPath;

    @Value("${webdriver.chrome.path}")
    private String chromeDriverPath;

    @Bean
    public WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadFolderPath);
        prefs.put("download.prompt_for_download", false);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--headless", "--disable-gpu");

        return new ChromeDriver(options);
    }
}
