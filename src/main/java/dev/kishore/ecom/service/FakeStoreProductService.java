package dev.kishore.ecom.service;

import dev.kishore.ecom.dto.FakeStoreProductDto;
import dev.kishore.ecom.exception.ProductNotFoundException;
import dev.kishore.ecom.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {
    private RestTemplate restTemplate;
    private RedisTemplate redisTemplate;

    public FakeStoreProductService(RestTemplate restTemplate,
                                   RedisTemplate redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getSingleProduct(Long productId) throws ProductNotFoundException {

        Product productFromRedis = (Product) redisTemplate.opsForHash().get("PRODUCTS", "PRODUCTS_"+productId);
        if(productFromRedis != null) {
            return productFromRedis;
        }
        FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + productId,
                FakeStoreProductDto.class
        );

        if(fakeStoreProductDto == null) {
            throw new ProductNotFoundException("Product not found " +
                    "with id "+ productId);
        }


// You can use the below function to handle 3rd party api responses accordingly
//        ResponseEntity<FakeStoreProductDto> fakeStoreProductDto = restTemplate.getForEntity(
//                "https://fakestoreapi.com/products/" + productId,
//                FakeStoreProductDto.class
//        );
//
//
//
//        if(fakeStoreProductDto.getStatusCode() == HttpStatus.OK) {
//            // print Hello
//        } else if(fakeStoreProductDto.getStatusCode() == HttpStatus.NOT_FOUND) {
//            // handle things accordingly
//            throw new ProductNotFoundException("Product not found " +
//                    "with id "+ productId);
//        }
//
//        System.out.printf(fakeStoreProductDto.toString());

        redisTemplate.opsForHash().put("PRODUCTS", "PRODUCTS_"+productId, fakeStoreProductDto.toProduct());
        return fakeStoreProductDto.toProduct();
    }

    @Override
    public Page<Product> getAllProducts(int pageSize, int pageNumber, String fieldName) {
//        List<Product> products = new ArrayList<>();
//        FakeStoreProductDto[] res = restTemplate.getForObject(
//                "url",
//                FakeStoreProductDto[].class
//        );
//
//        for(FakeStoreProductDto fs: res) {
//            products.add(fs.toProduct());
//        }
//        return products;
        return null;
    }

    @Override
    public Product createProduct(Product product) {
        FakeStoreProductDto fs = new FakeStoreProductDto();
        fs.setId(product.getId());
        fs.setTitle(product.getTitle());
        fs.setCategory(product.getCategory().getTitle());
        fs.setImage(product.getImageUrl());
        fs.setDescription(product.getDescription());
        fs.setPrice(product.getPrice());

        FakeStoreProductDto response = restTemplate.postForObject(
                "https://fakestoreapi.com/products",
                fs,
                FakeStoreProductDto.class
        );

//        Product p = new Product();
//        p.setId(response.getId());
//        // set all the variables and return p;
        return response.toProduct();
    }

    @Override
    public Product updateProduct(Product product) throws ProductNotFoundException {
        return null;
    }

}
