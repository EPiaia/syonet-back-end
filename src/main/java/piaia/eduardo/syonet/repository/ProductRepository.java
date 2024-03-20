package piaia.eduardo.syonet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import piaia.eduardo.syonet.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
