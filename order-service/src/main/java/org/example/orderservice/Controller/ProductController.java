package org.example.orderservice.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.Dto.ProductDto;
import org.example.orderservice.Dto.ProductPriceDto;
import org.example.orderservice.Entity.Product;
import org.example.orderservice.Repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody ProductDto dto) {
        Product product = modelMapper.map(dto, Product.class);
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping
    public List<ProductDto> getAll() {
        List<Product> list= productRepository.findAll();
        return list.stream().map(x->modelMapper.map(x,ProductDto.class)).toList();
    }

    @GetMapping("/{id}")
    public ProductDto getOne(@PathVariable int id) {
        return modelMapper.map(productRepository.getProductById(id),ProductDto.class);
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID " + id + " not found.");
        }
    }

    @Transactional
    @PatchMapping("/{id}/price")
    public ResponseEntity<?> updateProductPrice(@PathVariable("id") int id,
                                                @RequestBody ProductPriceDto priceUpdateDTO) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            //vraca 404 ako ga nema
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with ID " + id + " not found.");
        }
        Product productToUpdate = productOptional.get();
        productToUpdate.setPrice(priceUpdateDTO.getNewPrice());
        Product updatedProduct = productRepository.save(productToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }
}
