package com.dailystatus.dailyupdate.service;

import com.dailystatus.dailyupdate.config.SeleniumConfig;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SeleniumDownloadService {

    private final WebDriver driver;

    @Value("${google.sheet.url}")
    private String sheetUrl;

    @Value("${download.folder.path}")
    private String downloadPath;

    public SeleniumDownloadService(SeleniumConfig seleniumConfig) {
        this.driver = seleniumConfig.getDriver();
    }


    public String downloadReport() throws Exception {
        driver.get(sheetUrl);
        Thread.sleep(5000);

        System.out.println("✅ Opened Google Sheet: " + sheetUrl);

        Actions actions = new Actions(driver);

        // Simulate keyboard shortcut: Alt + F → D → X (Excel download)
        actions.keyDown(Keys.ALT).sendKeys("f").keyUp(Keys.ALT).perform();
        Thread.sleep(2000);
        actions.sendKeys("d").perform();
        Thread.sleep(2000);
        actions.sendKeys("x").perform();

        System.out.println("📥 Download command triggered... Waiting for file...");

        // Wait for download to complete (polling loop)
        Path downloadDir = Paths.get(downloadPath);
        File downloadedFile = waitForDownload(downloadDir, ".xlsx", 30);

        driver.quit();

        if (downloadedFile != null) {
            System.out.println("✅ Download completed: " + downloadedFile.getAbsolutePath());
            return downloadedFile.getAbsolutePath();
        } else {
            throw new RuntimeException("❌ Download failed or timed out.");
        }
    }

    private File waitForDownload(Path folder, String extension, int timeoutSeconds) throws InterruptedException {
        int waited = 0;
        while (waited < timeoutSeconds) {
            File[] files = folder.toFile().listFiles((dir, name) -> name.endsWith(extension));
            if (files != null && files.length > 0) {
                // return the most recently downloaded file
                File latest = files[0];
                for (File f : files) {
                    if (f.lastModified() > latest.lastModified()) {
                        latest = f;
                    }
                }
                return latest;
            }
            Thread.sleep(1000);
            waited++;
        }
        return null;
    }
}
