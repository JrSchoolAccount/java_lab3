package org.java_lab3;

import org.java_lab3.entities.Product;
import org.java_lab3.entities.ProductType;
import org.java_lab3.service.Warehouse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WarehouseTest {

    @Test
    void shouldThrowExceptionWhenNameIsEmptyOrNull() {
        Warehouse warehouse = new Warehouse();

        LocalDate now = LocalDate.now();

        assertThatThrownBy(() -> warehouse.newProduct(1, "", ProductType.ARMOR, 10, now, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product name cannot be null or empty");
    }


    @Test
    void shouldAddNewProductSuccessfully(){
        Warehouse warehouse = new Warehouse();

        LocalDate now = LocalDate.now();

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

        LocalDate now = LocalDate.now();

        warehouse.newProduct(1, "Broad sword", ProductType.WEAPON, 2, now, now);

        assertThatThrownBy(() -> warehouse.getProductById(2))
        .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product with id: 2, does not exist");
    }

    @Test
    void shouldGetProductById1ThenReturnProductNamedBroadsword(){
        Warehouse warehouse = new Warehouse();
        LocalDate now = LocalDate.now();

        warehouse.newProduct(1, "Broad sword", ProductType.WEAPON, 2, now, now);

        assertThat(warehouse.getProductById(1))
                .extracting(Product::name)
                .isEqualTo("Broad sword");
    }

    @Test
    void shouldReturnAllProducts(){
        Warehouse warehouse = new Warehouse();
        LocalDate now = LocalDate.now();

        warehouse.newProduct(1, "Broad sword", ProductType.ARMOR, 2, now, now);
        warehouse.newProduct(2, "Pendulum of doom", ProductType.ARTIFACT, 3, now, now);
        warehouse.newProduct(3, "Chain mail", ProductType.WEAPON, 4, now, now);

        List<Product> products = warehouse.getProducts();
        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    void shouldThrowExceptionWhenListIsEmpty(){
        Warehouse warehouse = new Warehouse();

        assertThatThrownBy(() -> warehouse.getProductsByTypeSortedAtoZ(ProductType.ARMOR))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No products available!");
    }


    @Test
    void shouldReturnSortedAToZ(){
        Warehouse warehouse = new Warehouse();
        LocalDate now = LocalDate.now();

        warehouse.newProduct(1, "Morning star", ProductType.WEAPON, 2, now, now);
        warehouse.newProduct(2, "Shiv", ProductType.WEAPON, 3, now, now);
        warehouse.newProduct(3, "Broad sword", ProductType.WEAPON, 4, now, now);

        List<Product> sortedProducts = warehouse.getProductsByTypeSortedAtoZ(ProductType.WEAPON);


        List<Product> expectedSortedProducts = List.of(
                new Product(3, "Broad sword", ProductType.WEAPON, 4, now, now),
                new Product(1, "Morning star", ProductType.WEAPON, 2, now, now),
                new Product(2, "Shiv", ProductType.WEAPON, 3, now, now)
        );

        assertThat(sortedProducts)
                .containsExactlyElementsOf(expectedSortedProducts);
    }

    @Test
    void shouldThrowExceptionWhenTypeNotFound(){
        Warehouse warehouse = new Warehouse();

        LocalDate now = LocalDate.now();

        warehouse.newProduct(1, "Morning star", ProductType.WEAPON, 2, now, now);
        warehouse.newProduct(2, "Shiv", ProductType.WEAPON, 3, now, now);
        warehouse.newProduct(3, "Broad sword", ProductType.WEAPON, 4, now, now);

        assertThatThrownBy(() -> warehouse.getProductsByTypeSortedAtoZ(ProductType.ARMOR))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No products with type: ARMOR found!");
    }

   @Test
   void shouldThrowExceptionWrongDateFormat(){
       Warehouse warehouse = new Warehouse();
       LocalDate now = LocalDate.now();

       warehouse.newProduct(1, "Morning star", ProductType.WEAPON, 2, now, now);
       warehouse.newProduct(2, "Shiv", ProductType.WEAPON, 3, now, now);
       warehouse.newProduct(3, "Broad sword", ProductType.WEAPON, 4, now, now);

       assertThatThrownBy(() -> warehouse.getProductsCreatedAfter(2024, 12, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong date format!");

   }

    @Test
    void shouldReturnAllProductsCreatedAfter1ofAugust(){
        Warehouse warehouse = new Warehouse();

        LocalDate now = LocalDate.now();
        LocalDate june = LocalDate.of(2024, 7, 31);
        LocalDate august = LocalDate.of(2024, 8, 30);

        warehouse.newProduct(1, "Morning star", ProductType.WEAPON, 2, june, june);
        warehouse.newProduct(2, "Shiv", ProductType.WEAPON, 3, august, august);
        warehouse.newProduct(3, "Broad sword", ProductType.WEAPON, 4, now, now);

        List<Product> sortedProducts = warehouse.getProductsCreatedAfter(2024, 8, 1);

        List<Product> expectedSortedProducts = List.of(
                new Product(2, "Shiv", ProductType.WEAPON, 3, august, august),
                new Product(3, "Broad sword", ProductType.WEAPON, 4, now, now)
        );

        assertThat(sortedProducts).isEqualTo(expectedSortedProducts);
    }

    @Test
    void shouldThrowExceptionNoProductsCreatedAfter1ofAugust(){
        Warehouse warehouse = new Warehouse();

        LocalDate june = LocalDate.of(2024, 7, 31);

        warehouse.newProduct(1, "Morning star", ProductType.WEAPON, 2, june, june);
        warehouse.newProduct(2, "Shiv", ProductType.WEAPON, 3, june, june);
        warehouse.newProduct(3, "Broad sword", ProductType.WEAPON, 4, june, june);

        assertThatThrownBy(() -> warehouse.getProductsCreatedAfter(2024, 8, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No products created after: 2024-08-01 found!");
    }
}