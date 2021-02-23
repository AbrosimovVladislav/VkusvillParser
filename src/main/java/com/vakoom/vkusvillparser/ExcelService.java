package com.vakoom.vkusvillparser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class ExcelService {

    String fileLocation = "/Users/vladislavabrosimov/IdeaProjects/VkusvillParser/src/main/resources/res.xlsx";

    public List<Product> getProducts() throws IOException {
        FileInputStream file = new FileInputStream(fileLocation);
        Workbook workbook = new XSSFWorkbook(file);

        List<Product> result = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (isEmpty(row.getCell(0).getStringCellValue())) {
                break;
            }
            Product product = new Product();
            product.setLink(row.getCell(0).getStringCellValue());
            product.setName(row.getCell(1).getStringCellValue());
            product.setPrice(row.getCell(2).getStringCellValue());
            product.setCcal(row.getCell(3).getStringCellValue());
            product.setType(row.getCell(4).getStringCellValue());
            result.add(product);
        }

        return result;
    }

    public List<Product> refreshProducts(List<Product> products) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("List");

        int i = 0;
        for (Product product : products) {
            Row row = sheet.createRow(i);
            Cell linkCell = row.createCell(0);
            Cell nameCell = row.createCell(1);
            Cell priceCell = row.createCell(2);
            Cell ccalCell = row.createCell(3);
            Cell typeCell = row.createCell(4);
            linkCell.setCellValue(product.getLink());
            nameCell.setCellValue(product.getName());
            priceCell.setCellValue(product.getPrice());
            ccalCell.setCellValue(product.getCcal());
            typeCell.setCellValue(product.getType());
            i++;
        }

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();

        return products;
    }

}
