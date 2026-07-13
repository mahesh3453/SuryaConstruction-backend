package com.suryaconstruction.permit.service;

import com.suryaconstruction.permit.dto.PermitEntryDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] exportToExcel(LocalDate date, List<PermitEntryDto> entries) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Permit Details");

            // Define custom fonts
            Font titleFont = workbook.createFont();
            titleFont.setFontName("Calibri");
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);
            titleFont.setColor(IndexedColors.WHITE.getIndex());

            Font headerFont = workbook.createFont();
            headerFont.setFontName("Calibri");
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setBold(true);

            Font dataFont = workbook.createFont();
            dataFont.setFontName("Calibri");
            dataFont.setFontHeightInPoints((short) 11);

            // Title Style (Merged Row 0)
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setBorders(titleStyle, BorderStyle.MEDIUM);

            // Header Style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setBorders(headerStyle, BorderStyle.THIN);
            headerStyle.setWrapText(true);

            // Data Style - Regular
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setFont(dataFont);
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setVerticalAlignment(VerticalAlignment.TOP);
            setBorders(dataStyle, BorderStyle.THIN);
            dataStyle.setWrapText(true);

            // Data Style - Alternating Row Shading
            CellStyle altDataStyle = workbook.createCellStyle();
            altDataStyle.setFont(dataFont);
            altDataStyle.setAlignment(HorizontalAlignment.LEFT);
            altDataStyle.setVerticalAlignment(VerticalAlignment.TOP);
            altDataStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            altDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setBorders(altDataStyle, BorderStyle.THIN);
            altDataStyle.setWrapText(true);

            // 1. Create Title Row
            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(40);
            Cell titleCell = titleRow.createCell(0);
            String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            titleCell.setCellValue("SURYA CONSTRUCTION - DAILY PERMIT TRACKING (" + formattedDate + ")");
            titleCell.setCellStyle(titleStyle);
            
            // Merge A1:E1 for Title
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            // 2. Create Header Row
            Row headerRow = sheet.createRow(1);
            headerRow.setHeightInPoints(28);
            String[] headers = {
                    "Supervisor / Engineer",
                    "Hot work permit activity",
                    "Height work permit activity",
                    "General work permit activity",
                    "Man power / workers name"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 3. Fill Data Rows
            int rowIdx = 2;
            for (PermitEntryDto entry : entries) {
                Row row = sheet.createRow(rowIdx++);
                row.setHeightInPoints(24);

                CellStyle currentStyle = (rowIdx % 2 == 0) ? altDataStyle : dataStyle;

                createCell(row, 0, entry.getEmployeeName(), currentStyle);
                createCell(row, 1, entry.getHotWorkActivity(), currentStyle);
                createCell(row, 2, entry.getHeightWorkActivity(), currentStyle);
                createCell(row, 3, entry.getGeneralWorkActivity(), currentStyle);
                createCell(row, 4, entry.getManpowerNames(), currentStyle);
            }

            // 4. Auto-size columns to fit content nicely
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // Apply minimum width to prevent columns from being too narrow
                int currentWidth = sheet.getColumnWidth(i);
                if (currentWidth < 6000) {
                    sheet.setColumnWidth(i, 6000);
                }
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    private void setBorders(CellStyle style, BorderStyle border) {
        style.setBorderTop(border);
        style.setBorderBottom(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
    }
}
