package com.example.employeemanagement.service;

import com.example.employeemanagement.entity.Employee;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);


    private JasperPrint generateEmployeeJasperPrint(Employee employee) throws Exception {
        InputStream stream = new ClassPathResource("reports/employee_report.jrxml").getInputStream();
        JasperReport report = JasperCompileManager.compileReport(stream);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("employeeCode", employee.getEmployeeCode());
        parameters.put("firstName", employee.getFirstName());
        parameters.put("lastName", employee.getLastName());
        parameters.put("nic", employee.getNic());
        parameters.put("gender", employee.getGender());
        parameters.put("dateOfBirth", employee.getDateOfBirth() != null ? employee.getDateOfBirth().toString() : "");
        parameters.put("email", employee.getEmail());
        parameters.put("mobileNo", employee.getMobileNo());
        parameters.put("address", employee.getAddress());
        parameters.put("designationName", employee.getDesignation() != null ? employee.getDesignation().getDesignationTitle() : "");
        parameters.put("status", employee.getStatus().name());

        return JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
    }

    public byte[] exportEmployeeToPdf(Employee employee) throws Exception {
        try {
            JasperPrint print = generateEmployeeJasperPrint(employee);
            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            log.error("Failed to generate PDF for employee {}: {}", employee.getId(), e.getMessage(), e);
            throw e;
        }
    }

    public byte[] exportEmployeeToHtml(Employee employee) throws Exception {
        try {
            JasperPrint print = generateEmployeeJasperPrint(employee);
            HtmlExporter exporter = new HtmlExporter();
            
            // Create temporary file for HTML output
            File tempFile = File.createTempFile("employee_report_", ".html");
            
            try {
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(tempFile));
                exporter.exportReport();
                
                // Read and return file content as bytes
                return Files.readAllBytes(tempFile.toPath());
            } finally {
                // Clean up temporary file
                Files.delete(tempFile.toPath());
            }
        } catch (Exception e) {
            log.error("Failed to generate HTML for employee {}: {}", employee.getId(), e.getMessage(), e);
            throw e;
        }
    }

    public byte[] exportEmployeeToExcel(Employee employee) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Employee Details");
            
            // Create headers
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Field", "Value"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Fill data
            Object[][] data = {
                {"Employee Code", employee.getEmployeeCode()},
                {"First Name", employee.getFirstName()},
                {"Last Name", employee.getLastName()},
                {"NIC", employee.getNic()},
                {"Gender", employee.getGender()},
                {"Date of Birth", employee.getDateOfBirth() != null ? employee.getDateOfBirth().toString() : ""},
                {"Email", employee.getEmail()},
                {"Mobile No", employee.getMobileNo()},
                {"Address", employee.getAddress()},
                {"Designation", employee.getDesignation() != null ? employee.getDesignation().getDesignationTitle() : ""},
                {"Status", employee.getStatus().name()}
            };

            for (int i = 0; i < data.length; i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(data[i][0].toString());
                row.createCell(1).setCellValue(data[i][1] != null ? data[i][1].toString() : "");
            }

            // Auto size columns
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
