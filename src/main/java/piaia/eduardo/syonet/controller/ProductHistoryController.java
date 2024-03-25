package piaia.eduardo.syonet.controller;



import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import piaia.eduardo.syonet.model.Product;
import piaia.eduardo.syonet.model.ProductHistory;
import piaia.eduardo.syonet.model.User;
import piaia.eduardo.syonet.record.StockHistory;
import piaia.eduardo.syonet.record.StockMovement;
import piaia.eduardo.syonet.repository.ProductHistoryRepository;
import piaia.eduardo.syonet.repository.ProductRepository;
import piaia.eduardo.syonet.repository.UserRepository;
import piaia.eduardo.syonet.util.MovementType;

@RestController
@RequestMapping("/stocks")
public class ProductHistoryController {

    @Autowired
    private ProductHistoryRepository productHistoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/in")
    public ResponseEntity<ProductHistory> stockIn(@RequestBody @Valid StockMovement stockMovement) {
        return stockMovement(stockMovement, MovementType.STOCK_IN);
    }

    @PostMapping("/out")
    public ResponseEntity<ProductHistory> stockOut(@RequestBody @Valid StockMovement stockMovement) {
        return stockMovement(stockMovement, MovementType.STOCK_OUT);
    }

    private ResponseEntity<ProductHistory> stockMovement(StockMovement stockMovement, MovementType movement) {
        Product product = productRepository.findById(stockMovement.productId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findById(stockMovement.userId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        if (stockMovement.quantity() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        if (stockMovement.quantity().compareTo(BigDecimal.ZERO) < 1) {
            return ResponseEntity.badRequest().build();
        }
        
        if (MovementType.STOCK_OUT.equals(movement) && stockMovement.quantity().compareTo(product.getStock()) > 0) {
            return ResponseEntity.badRequest().build();
        }

        ProductHistory productHistory = new ProductHistory();
        productHistory.setProduct(product);
        productHistory.setQuantity(stockMovement.quantity());
        productHistory.setUser(user);
        productHistory.setHour(new Date());
        productHistory.setType(movement.getId());

        if (MovementType.STOCK_IN.equals(movement)) {
            product.setStock(product.getStock().add(stockMovement.quantity()));
        } else {
            product.setStock(product.getStock().subtract(stockMovement.quantity()));
        }
        product = productRepository.save(product);

        ProductHistory persistedHistory = productHistoryRepository.save(productHistory);
        return ResponseEntity.ok(persistedHistory);
    }

    @GetMapping
    public ResponseEntity<List<StockHistory>> getAllHistory() {
        List<ProductHistory> history = productHistoryRepository.findAllByOrderByHourDesc();
        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<StockHistory> stockHistories = new ArrayList<>();
        for (ProductHistory prodHistory : history) {
            Integer id = prodHistory.getId();
            Product product = prodHistory.getProduct();
            String productIdentification = product.getId() + " - " + product.getName();
            User user = prodHistory.getUser();
            String type = MovementType.getTypeById(prodHistory.getType()).getPtDescription();
            String dateHour = new SimpleDateFormat("dd/MM/yyyy").format(prodHistory.getHour());
            BigDecimal quantity = prodHistory.getQuantity();
            stockHistories.add(new StockHistory(id, productIdentification, type, user.getName(), dateHour, quantity));
        }

        return ResponseEntity.ok(stockHistories);    
    }

}
