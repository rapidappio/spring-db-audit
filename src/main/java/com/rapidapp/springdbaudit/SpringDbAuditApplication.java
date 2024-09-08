package com.rapidapp.springdbaudit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.config.Customizer.withDefaults;

@SpringBootApplication
public class SpringDbAuditApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDbAuditApplication.class, args);
    }

}

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
class ProductController {

    private final ProductRepository productRepository;

    @PostMapping
    public Product createProduct() {
        Product product = new Product();
        product.setName("Product " + System.currentTimeMillis());
        return productRepository.save(product);
    }

    @PatchMapping
    public void updateProduct() {
        productRepository.findById(1L).ifPresent(product -> {
            product.setName("Product " + System.currentTimeMillis());
            productRepository.save(product);
        });
    }
}

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}

@JaversSpringDataAuditable
interface ProductRepository extends CrudRepository<Product, Long> {}

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}