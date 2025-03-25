package com.trimblecars.controllers;

import com.trimblecars.services.LeaseReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LeaseReportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LeaseReportService leaseReportService;

    @InjectMocks
    private LeaseReportController leaseReportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(leaseReportController).build();
    }

    @Test
    void testExportLeaseHistory() throws Exception {
        byte[] pdfContent = "Mock PDF Data".getBytes();

        when(leaseReportService.generateLeaseHistoryReport()).thenReturn(pdfContent);

        mockMvc.perform(get("/lease-report/export"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=lease_history.pdf"))
                .andExpect(content().bytes(pdfContent));

        verify(leaseReportService, times(1)).generateLeaseHistoryReport();
    }
}
