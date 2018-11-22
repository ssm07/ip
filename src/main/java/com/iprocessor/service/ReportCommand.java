package com.iprocessor.service;

import com.iprocessor.DTO.ReportDTO;
import com.iprocessor.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *    This class provides an API for  generating reports
 *    It is one of the command of {@link ProductionCycle}
 *   <p/>
 *
 *   @author  Saurabh Moghe, Abhijeet sathe
 * */
@Service
public class ReportCommand implements Command {

    private Date startDate;
    private Date endDate;
    private String reportName;
    @Value("${report_path}")
    private String reportPath;


    @Autowired
    UserRepository userRepository;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ReportCommand(Date startDate, Date endDate,String reportName) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportName=reportName;
    }

    public ReportCommand() {
    }

    @Override
    public void execute() {

        createReport();

    }

    public List<ReportDTO> getStandardUserDataForReport() {
        List<ReportDTO> standardUserReportDTOList = null;
        if (startDate != null && endDate != null) {
            standardUserReportDTOList = userRepository.getStandardUserForReport(startDate, endDate);
        } else if (startDate == null && endDate != null) {
            standardUserReportDTOList = userRepository.getStandardUserForReport(endDate, false);
        } else if (startDate != null && endDate == null) {
            standardUserReportDTOList = userRepository.getStandardUserForReport(startDate, true);
        } else {
            standardUserReportDTOList = userRepository.getStandardUserForReport();
        }

        return standardUserReportDTOList;

    }

    public List<ReportDTO> getPremiumUserDataForReport() {
        List<ReportDTO> premiumUserReportDTOList = null;
        if (startDate != null && endDate != null) {
            premiumUserReportDTOList = userRepository.getPremiumUserDataForReport(startDate, endDate);
        } else if (startDate == null && endDate != null) {
            premiumUserReportDTOList = userRepository.getPremiumUserDataForReport(endDate, false);
        } else if (startDate != null && endDate == null) {
            premiumUserReportDTOList = userRepository.getPremiumUserDataForReport(startDate, true);
        } else {
            premiumUserReportDTOList = userRepository.getPremiumUserDataForReport();
        }
        return premiumUserReportDTOList;
    }


    public void createReport() {
        String fileName;
        List<ReportDTO> standardUserReportDTOList = getStandardUserDataForReport();
        List<ReportDTO> premiumUserReportDTOList = getPremiumUserDataForReport();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet1 = workbook.createSheet(" Premium User");
        printHeaders(getHeaderForPremiumUserSheet(), sheet1);
        printData(premiumUserReportDTOList, sheet1, true,workbook);

        XSSFSheet sheet2 = workbook.createSheet(" Standard User");
        printHeaders(getHeadersForStandardUser(), sheet2);
        printData(standardUserReportDTOList, sheet2, false,workbook);
        if (this.reportName != null && !this.reportName.isEmpty()) {
            fileName = reportName + ".xlsx";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date now = new Date();
            String strDate = sdf.format(now);
            fileName = "userReport" + strDate + ".xlsx";
        }
        File file = new File(reportPath + File.separator + fileName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHeadersForStandardUser() {

        List<String> headers = new ArrayList<>();
        headers.add("First Name");
        headers.add("Last Name ");
        headers.add("Created Date");
        headers.add("Available Space(bytes)");
        return headers;
    }

    public List<String> getHeaderForPremiumUserSheet() {
        List<String> headers = getHeadersForStandardUser();
        headers.add("Premium Account Expiration Date");
        headers.add("Payment Amount");
        return headers;
    }

    public void printHeaders(List<String> headers, XSSFSheet sheet) {
        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);
        int colNum = 0;
        for (String header : headers) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(header);
        }

    }


    public CellStyle getDateStyle(Workbook wb){
        CellStyle cellStyle = wb.createCellStyle();
        CreationHelper createHelper = wb.getCreationHelper();
        cellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("mm/dd/yyyy"));

        return cellStyle;



    }
    public void printData(List<ReportDTO> premiumUserReportDTOList, XSSFSheet sheet, boolean isPremiumUser, Workbook wb) {
        int rowNum = 1;
        for (ReportDTO reportDTO : premiumUserReportDTOList) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            Cell cell1 = row.createCell(colNum++);
            cell1.setCellValue(reportDTO.getFirstName());

            Cell cell2 = row.createCell(colNum++);
            cell2.setCellValue(reportDTO.getLastName());

            Cell cell3 = row.createCell(colNum++);
            cell3.setCellValue(reportDTO.getCreatedDate());
            cell3.setCellStyle(this.getDateStyle(wb));

            Cell cell4 = row.createCell(colNum++);
            cell4.setCellValue(reportDTO.getResourceUsage());

            if (isPremiumUser) {
                Cell cell5 = row.createCell(colNum++);
                cell5.setCellValue(reportDTO.getExpirationDate());
                cell5.setCellStyle(this.getDateStyle(wb));
                Cell cell6 = row.createCell(colNum++);
                cell6.setCellValue("$"+reportDTO.getPaymentAmount());
            }


        }

    }

}
