package com.woxiangbo.projects.mycompare.utils;

import java.io.File;
import java.io.FileOutputStream;

import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.woxiangbo.projects.mycompare.config.ReportConfig;

public class ExcelTool {
    public static WritableWorkbook wb;

    public static WritableSheet sheet;

    public static void prepareStatisticsExcel() {
        try {
            String filePath = ReportConfig.getStatisticsFileName();
            String[] temp = filePath.split("\\\\");
            File fileDir = new File(filePath.replace(temp[temp.length - 1], ""));
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(filePath);

            if (!file.exists()) {
                file.createNewFile();
            }
            wb = Workbook.createWorkbook(new FileOutputStream(file));
            if (wb.getSheets().length == 0) {
                wb.createSheet("TestCases", 0);
            }
            sheet = wb.getSheet(0);
            Label Idlabel = new Label(0, 0, "TestCaseID");
            Label RunLabel = new Label(1, 0, "Run");
            Label statusLabel = new Label(2, 0, "Status");
            sheet.addCell(Idlabel);
            sheet.addCell(RunLabel);
            sheet.addCell(statusLabel);
//            wb.write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCaseStatus(String caseId, String status) {
        try {
            int totalRow = sheet.getRows();

            jxl.write.Number Idlabel = null;
            Label RunLabel = null;
            Label statusLabel = null;

            Cell cell = sheet.findCell(caseId);
            if (cell == null) {
                Idlabel = new Number(0, totalRow, Integer.valueOf(caseId));
                RunLabel = new Label(1, totalRow, "Run");
                statusLabel = new Label(2, totalRow, status);
            } else {
                int curRow = cell.getRow();
                Idlabel = new Number(0, curRow, Integer.valueOf(caseId));
                RunLabel = new Label(1, curRow, "Run");
                statusLabel = new Label(2, curRow, status);
            }
            sheet.addCell(Idlabel);
            sheet.addCell(RunLabel);
            sheet.addCell(statusLabel);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            wb.write();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        prepareStatisticsExcel();
        setCaseStatus("123", "failed");
        close();
    }

}
