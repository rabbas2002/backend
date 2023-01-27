package com.example.backend.dataloader;

import com.example.backend.model.Product;
import com.example.backend.model.Supplier;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.*;

public class DataLoader  {
    public static String TYPE =  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[]HEADERs = {"Id","code","name","batch","stock","deal","free","mrp","rate","exp","company","supplier"};
    static String SHEET = "sample_inventory";
    static HashMap<String,Supplier>map = new HashMap<>();
    static Logger logger = LoggerFactory.getLogger(DataLoader.class);

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    private static Date getDate(String dateString){
        HashMap<String,Integer> months = new HashMap<>();
        months.put("Jan", Calendar.JANUARY);
        months.put("Feb",Calendar.FEBRUARY);
        months.put("Mar",Calendar.MARCH);
        months.put("Apr",Calendar.APRIL);
        months.put("May",Calendar.MAY);
        months.put("Jun",Calendar.JUNE);
        months.put("Jul",Calendar.JULY);
        months.put("Aug",Calendar.AUGUST);
        months.put("Sept",Calendar.SEPTEMBER);
        months.put("Oct",Calendar.OCTOBER);
        months.put("Nov",Calendar.NOVEMBER);
        months.put("Dec",Calendar.DECEMBER);
        if(dateString.length()>10) {
            int date = Integer.parseInt(dateString.substring(0,2));
            String month = "";
            int i = 3;
            while (dateString.charAt(i)!='-'){
                month+=dateString.charAt(i);
                i++;
            }
            i++;
            int year  = Integer.parseInt(dateString.substring(i));
            return new Date(year-1900,months.get(month),date);
        }
        else{
            logger.info("Invalid date");
            return null;
        }
    }

    public static List[] excelToProducts(InputStream is) {
        List []arr = new List[2];
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Supplier> supplierList = new ArrayList<>();
            List<Product>productList = new ArrayList<>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                String currSupplier = currentRow.getCell(10)==null?"":currentRow.getCell(10).toString();
                Supplier s;
                Product p = new Product();
                p.setCode(currentRow.getCell(0).toString());
                p.setName(currentRow.getCell(1).toString());
                p.setBatch(currentRow.getCell(2)!=null?currentRow.getCell(2).toString():"");
                p.setStock(Math.round(Float.parseFloat(currentRow.getCell(3).toString())));
                p.setDeal(Math.round((Float.parseFloat(currentRow.getCell(4).toString()))));
                p.setFree(Math.round(Float.parseFloat(currentRow.getCell(5).toString())));
                p.setMrp(Float.parseFloat(currentRow.getCell(6).toString()));
                p.setRate(Float.parseFloat(currentRow.getCell(7).toString()));
                p.setCompany(currentRow.getCell(9).toString());
                String dateString = currentRow.getCell(8).toString();
                Date expDate = getDate(dateString);
                p.setExp(expDate);
                if(map.containsKey(currSupplier)){
                    s = map.get(currSupplier);
                }
                else{
                    s = new Supplier();
                    s.setSupplier(currSupplier);
                    map.put(currSupplier,s);
                }
                p.setSupplier(s);
                supplierList.add(s);
                productList.add(p);
                s.getProducts().add(p);
            }
            arr[0] = productList;
            arr[1] = supplierList;
            workbook.close();
            logger.info("Parsed the Excel File Successfully!");
            return arr;
        } catch (IOException e) {
            logger.error("failed to parse Excel file: " + e.getMessage());
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
