package com.trimblecars.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trimblecars.repositories.LeaseRepository;
import com.trimblecars.entities.Lease;

@Service
public class LeaseReportService {

    @Autowired
    private LeaseRepository leaseRepository;

    public byte[] generateLeaseHistoryReport() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Lease History Report\n\n"));

            List<Lease> leaseList = leaseRepository.findAll();

            for (Lease lease : leaseList) {
                document.add(new Paragraph("Lease ID: " + lease.getId()));
                document.add(new Paragraph("Pickup Venue: " + lease.getPickupVenue()));
                document.add(new Paragraph("Return Venue: " + lease.getReturnVenue()));
                document.add(new Paragraph("Pickup Date: " + lease.getPickupDate()));
                document.add(new Paragraph("Return Date: " + lease.getReturnDate()));

                // Add Customer Details
                if (lease.getCustomer() != null) {
                    document.add(new Paragraph("Customer Name: " + lease.getCustomer().getUserName()));
                    document.add(new Paragraph("Customer Phone: " + lease.getCustomer().getContactNo()));
                }

                // Add Owner Details
                if (lease.getCar().getOwner() != null) {
                    document.add(new Paragraph("Owner Name: " + lease.getCar().getOwner().getOwnerName()));
                    document.add(new Paragraph("Owner Phone: " + lease.getCar().getOwner().getContactNo()));
                }

                document.add(new Paragraph("------------------------------"));
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
