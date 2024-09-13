package org.java_lab3;

import org.java_lab3.entities.Product;
import org.java_lab3.entities.ProductType;
import org.java_lab3.service.Warehouse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WarehouseTest {

    @Test
    void shouldThrowExceptionWhenNameIsEmptyOrNull() {
        Warehouse warehouse = new Warehouse();

        assertThatThrownBy(() -> warehouse.newProduct(1, "", ProductType.ARMOR, 10, LocalDateTime.now(), LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product name cannot be null or empty");
    }


    @Test
    void shouldAddNewProductSuccessfully(){
        Warehouse warehouse = new Warehouse();

        LocalDateTime now = LocalDateTime.now();

        warehouse.newProduct(1, "Broad sword", ProductType.WEAPON, 2, now, now);

        List<Product> products = warehouse.getProducts();
        assertThat(products.size()).isEqualTo(1);

        Product product = products.getFirst();
        assertThat(product.id()).isEqualTo(1);
        assertThat(product.name()).isEqualTo("Broad sword");
        assertThat(product.type()).isEqualTo(ProductType.WEAPON);
        assertThat(product.rating()).isEqualTo(2);
        assertThat(product.created()).isEqualTo(now);
        assertThat(product.modified()).isEqualTo(now);
    }

    @Test
    void shouldThrowExceptionWhenTryingToFindProductThatDoesNotExist(){
        Warehouse warehouse = new Warehouse();

        LocalDateTime now = LocalDateTime.now();

        warehouse.newProduct(1, "Broad sword", ProductType.WEAPON, 2, now, now);

        assertThatThrownBy(() -> warehouse.getProductById(2))
        .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product with id: 2, does not exist");
    }

    @Test
    void shouldGetProductById1ThenReturnProductNamedBroadsword(){
        Warehouse warehouse = new Warehouse();
        LocalDateTime now = LocalDateTime.now();

        warehouse.newProduct(1, "Broad sword", ProductType.WEAPON, 2, now, now);

        assertThat(warehouse.getProductById(1))
                .extracting(Product::name)
                .isEqualTo("Broad sword");
    }

}