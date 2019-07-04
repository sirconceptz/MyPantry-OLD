package models;

import com.hermanowicz.pantry.db.Product;

public class ProductModelTest {

    public static Product getTestProduct(){
        Product product = new Product();
        product.setName("Raspberry juice");
        product.setTypeOfProduct("Fruits");
        product.setProductFeatures("Juice");
        product.setExpirationDate("2020-01-01");
        product.setProductionDate("2019-12-31");
        product.setComposition("Raspberries");
        product.setHealingProperties("");
        product.setDosage("Much");
        product.setVolume(500);
        product.setWeight(1000);
        product.setHasSugar(true);
        product.setHasSalt(false);
        product.setTaste("Sweet");
        product.setHashCode(String.valueOf(product.hashCode()));
        return product;
    }

}