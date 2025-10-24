package com.dailystatus.dailyupdate.service;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Service
public class SharePointDownloadService {

    @Value("${download.file.url}")
    private String fileUrl;

    @Value("${download.folder.path}")
    private String downloadFolderPath;

    public String downloadReport() throws Exception {
        File folder = new File(downloadFolderPath);
        if (!folder.exists()) folder.mkdirs();

        File downloadedFile = new File(folder, "report.xlsx");
        if (downloadedFile.exists()) downloadedFile.delete();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(fileUrl);
            request.setHeader("User-Agent", "Mozilla/5.0");

            try (CloseableHttpResponse response = client.execute(request)) {
                int status = response.getCode();
                System.out.println("📡 HTTP Response Code: " + status);

                if (status == 200) {
                    try (InputStream in = response.getEntity().getContent();
                         FileOutputStream out = new FileOutputStream(downloadedFile)) {

                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }
                    System.out.println("✅ File downloaded: " + downloadedFile.getAbsolutePath());
                    return downloadedFile.getAbsolutePath();
                } else {
                    throw new RuntimeException("Failed to download file. HTTP code: " + status);
                }
            }
        }
    }
}