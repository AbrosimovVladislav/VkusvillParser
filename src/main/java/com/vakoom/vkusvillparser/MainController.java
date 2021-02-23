package com.vakoom.vkusvillparser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {

    private final ExcelService excelService;
    private final ParsingService parsingService;

    @GetMapping("/load")
    public List<Product> loadProducts() throws IOException {
        List<Product> oldProducts = excelService.getProducts();
        log.info("Product links loaded from excel. Size: {}", oldProducts.size());
        List<Product> preparedProducts = parsingService.prepareNewProductsForLoad(oldProducts);
        log.info("Products parsed from https://spb.vkusvill.ru/. Size: {}", preparedProducts.size());
        List<Product> result = excelService.refreshProducts(preparedProducts);
        log.info("Refreshing completed");
        return result;
    }

}
