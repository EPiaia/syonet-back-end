package piaia.eduardo.syonet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import piaia.eduardo.syonet.model.Product;
import piaia.eduardo.syonet.model.ProductHistory;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Integer> {

    public List<ProductHistory> findAllByOrderByHourDesc();
    
    public List<ProductHistory> findAllByProduct(Product product);

}
