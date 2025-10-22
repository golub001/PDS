package org.example.orderservice.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.Dto.ProductDto;
import org.example.orderservice.Dto.ProductPriceDto;
import org.example.orderservice.Entity.Product;
import org.example.orderservice.Repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> create(ProductDto dto) {
        Product product = modelMapper.map(dto, Product.class);
        Product saved = productRepository.save(product);

        ProductDto responseDto = modelMapper.map(saved, ProductDto.class);

        URI location = URI.create("/products/" + saved.getId());
        return ResponseEntity.created(location).body(responseDto);
    }

    public List<ProductDto> getAll() {
        List<Product> list = productRepository.findAll();
        return list.stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }
    public ResponseEntity<?> getbyName(String email) {
        Optional<?> opt = productRepository.getProductByName(email);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with name " + email + " not found.");
        }
        return  ResponseEntity.ok().body(opt.get());
    }
    public ResponseEntity<?> getOne(int id) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with ID " + id + " not found.");
        }
        ProductDto dto = modelMapper.map(opt.get(), ProductDto.class);
        return ResponseEntity.ok(dto);
    }

    @Transactional
    public ResponseEntity<?> deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with ID " + id + " not found.");
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok("Product with ID " + id + " deleted successfully.");
    }

    @Transactional
    public ResponseEntity<?> updateProductPrice(int id, ProductPriceDto priceUpdateDTO) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product with ID " + id + " not found.");
        }
        Product productToUpdate = productOptional.get();
        productToUpdate.setPrice(priceUpdateDTO.getNewPrice());
        Product updated = productRepository.save(productToUpdate);

        ProductDto dto = modelMapper.map(updated, ProductDto.class);
        return ResponseEntity.ok(dto);
    }


}
