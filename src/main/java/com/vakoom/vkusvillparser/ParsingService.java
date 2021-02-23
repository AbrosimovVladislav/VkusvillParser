package com.vakoom.vkusvillparser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ParsingService {

    public List<Product> prepareNewProductsForLoad(List<Product> oldProducts) {
        return oldProducts.parallelStream()
                          .map(this::parseProduct)
                          .filter(Objects::nonNull)
                          .collect(Collectors.toList());
    }

    public Product parseProduct(Product productOld) {
        Optional<Document> productPageDoc = getDocByUrl(productOld.getLink());

        if (!productPageDoc.isPresent()) {
            log.error("Problems with getting page: " + productOld.getLink());
            return null;
        }

        Product product = new Product();

        try {
            String name = productPageDoc.get().getElementsByClass("Product__title js-datalayer-catalog-list-name").get(0).text();
            product.setName(name);
            product.setLink(productOld.getLink());
            product.setType(productOld.getType());
            product.setPrice(productPageDoc.get().getElementsByClass("Price__value").get(0).text());
            String ccalOn100 = productPageDoc.get().getElementsByClass("DetailsList__value").get(0).text();
            String ves = productPageDoc.get().getElementsByClass("Product__listItem").get(1).text();
            ves = ves.replaceAll("Вес:","");
            ves = ves.replaceAll("г","");
            ves = ves.replaceAll(" ","");
            ccalOn100 = ccalOn100.replaceAll(",",".");
            product.setCcal(String.valueOf(Double.parseDouble(ccalOn100)*Integer.parseInt(ves)/100));
            log.info("Product {} successfully parsed", name);
        } catch (Exception e) {
            log.error("Problems with parsing page: " + productOld.getLink());
        }

        return product;
    }

    private Optional<Document> getDocByUrl(String url) {
        try {
            return Optional.of(Jsoup.connect(url).get());
        } catch (IOException e) {
            log.error("{} url: {}", e.getMessage(), url);
            return Optional.empty();
        }
    }

}
