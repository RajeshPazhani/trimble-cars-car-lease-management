package com.trimblecars.controllers;

import com.trimblecars.services.LeaseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lease-report")
public class LeaseReportController {

    @Autowired
    private LeaseReportService leaseReportService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportLeaseHistory() {
        byte[] pdfBytes = leaseReportService.generateLeaseHistoryReport();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lease_history.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
