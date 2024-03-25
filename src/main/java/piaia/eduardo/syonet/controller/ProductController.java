package piaia.eduardo.syonet.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import piaia.eduardo.syonet.model.Product;
import piaia.eduardo.syonet.model.ProductHistory;
import piaia.eduardo.syonet.repository.ProductHistoryRepository;
import piaia.eduardo.syonet.repository.ProductRepository;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAllByOrderByIdAsc();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        return productRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> save(@RequestBody @Valid Product product) {
        Product persistedProduct = productRepository.save(product);
        return ResponseEntity.ok(persistedProduct);
    }

    @PutMapping
    public ResponseEntity<Product> update(@RequestBody @Valid Product product) {
        Product existentProduct = productRepository.findById(product.getId()).orElse(null);
        if (existentProduct == null) {
            return ResponseEntity.notFound().build();
        }
        existentProduct.setName(product.getName());
        Product persistedProduct = productRepository.save(existentProduct);
        
        return ResponseEntity.ok(persistedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> delete(@PathVariable Integer id) {
        Product existentProduct = productRepository.findById(id).orElse(null);
        if (existentProduct == null) {
            return ResponseEntity.notFound().build();
        }

        List<ProductHistory> history = productHistoryRepository.findAllByProduct(existentProduct);
        if (!history.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        productRepository.delete(existentProduct);

        return ResponseEntity.ok().build();
    }

}
