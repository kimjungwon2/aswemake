package com.example.order.controller.product;

import com.example.order.controller.product.dto.ProductChangePriceRequest;
import com.example.order.controller.product.dto.ProductCreateRequest;
import com.example.order.controller.product.dto.ProductUpdateRequest;
import com.example.order.service.product.ProductService;
import com.example.order.service.product.response.ProductHistoryResponse;
import com.example.order.service.product.response.ProductResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product/new")
    public ProductResponse createProduct(@Validated @RequestBody ProductCreateRequest request){
        LocalDateTime registeredDateTime = LocalDateTime.now();

        return productService.createProduct(request, registeredDateTime);
    }

    @PutMapping("/product/{productId}/update")
    public ProductResponse updateProduct(
            @PathVariable("productId") Long productId,
            @Validated @RequestBody ProductUpdateRequest request
    ){
        LocalDateTime registeredDateTime = LocalDateTime.now();

        return productService.changeProduct(productId, request, registeredDateTime);
    }

    @PutMapping("/product/{productId}/price/update")
    public ProductResponse updateProductPrice(
            @PathVariable("productId") Long productId,
            @Validated @RequestBody ProductChangePriceRequest request
    ){
        LocalDateTime registeredDateTime = LocalDateTime.now();

        return productService.changePrice(productId, request, registeredDateTime);
    }

    @PutMapping("/product/{productId}/remove")
    public void removeProduct(
            @PathVariable("productId") Long productId,
            @RequestParam(value = "email") String email
    ){
        LocalDateTime registeredDateTime = LocalDateTime.now();

        productService.deleteProduct(productId, email, registeredDateTime);
    }

    @GetMapping("/product/history")
    public List<ProductHistoryResponse> getProductHistories(
            @RequestParam(value = "time") LocalDateTime createdDate
    ){
        return productService.getHistoryBasedOnTime(createdDate);
    }

}
