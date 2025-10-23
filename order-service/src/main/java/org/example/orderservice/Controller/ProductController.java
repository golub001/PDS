package org.example.orderservice.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.Dto.ProductDto;
import org.example.orderservice.Dto.ProductPriceDto;
import org.example.orderservice.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> create(@Valid @RequestBody ProductDto dto) {
        return productService.create(dto);
    }

    @GetMapping
    public List<ProductDto> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable int id) {
        return productService.getOne(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<?> updateProductPrice(@PathVariable int id,
                                                @Valid @RequestBody ProductPriceDto priceUpdateDTO) {
        return productService.updateProductPrice(id, priceUpdateDTO);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getIdFromName(@PathVariable String name) {
        return productService.getbyName(name);
    }

}
