package com.vakoom.vkusvillparser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String name;
    private String link;
    private String price;
    private String ccal;
    private String ves;
    private String type;

}
