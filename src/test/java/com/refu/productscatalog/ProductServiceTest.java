package com.refu.productscatalog;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.refu.productscatalog.dto.ProductUpdateDTO;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.out.println("\n--- new test ---\n");
    }

    @Test
    void testCreate() {
        System.out.println("- create a new product");
        Product product = new Product();
        product.setName("Produto testa");
        product.setPrice(BigDecimal.TEN);
        
        // simulate repository saving and returningthe product
        when(repository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertNotNull(createdProduct.getCreatedAt(), "creation timestamp should be set");
        assertNotNull(createdProduct.getUpdatedAt(), "update timestamp should be set");
        assertEquals(product.getName(), createdProduct.getName(), "product name should match");
        assertEquals(product.getPrice(), createdProduct.getPrice(), "product price should match");

        verify(repository).save(product);

        System.out.println("- product created: " + createdProduct);
    }

    @Test
    void testGetAll() {
        System.out.println("- retrieve all products");
        List<Product> expectedProducts = Arrays.asList(new Product(), new Product());

        // simulate a repository returning products
        when(repository.findAll()).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.getAllProducts();

        assertEquals(expectedProducts, actualProducts, "the returned product list should match the expected one");

        verify(repository).findAll();

        System.out.println("- products retrieved: " + actualProducts);
    }

    @Test
    void testUpdate_ValidFields() {
        System.out.println("- update product with valid data");
        String id = "1";
        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setName("Old testa");
        existingProduct.setPrice(BigDecimal.ONE);
        existingProduct.setQuantity(1);

        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setName("New testa");
        updateDTO.setPrice(BigDecimal.TEN);

        // simulate a repository findingthe product
        when(repository.findById(id)).thenReturn(Optional.of(existingProduct));

        // simulate a repository saving the updated product
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = productService.updateProduct(id, updateDTO);

        assertEquals("New testa", updatedProduct.getName(), "product name should be updated");
        assertEquals(BigDecimal.TEN, updatedProduct.getPrice(), "product price should be updated");
        assertEquals(1, updatedProduct.getQuantity(), "product quantity should remain the same");
        assertNotNull(updatedProduct.getUpdatedAt(), "update timestamp should be set");

        verify(repository).findById(id);
        verify(repository).save(existingProduct);

        System.out.println("Product updated: " + updatedProduct);
    }

    @Test
    void testUpdate_InvalidFields() {
        System.out.println("- trying to update a product without fields");
        String id = "1";
        ProductUpdateDTO emptyUpdateDTO = new ProductUpdateDTO(); // DTO with no fields

        // expect IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(id, emptyUpdateDTO);
        }, "[here IllegalArgumentException should be thrown once no fields are provided]");

        System.out.println("- no fields provided for update, exception thrown as expected");
    }
}
