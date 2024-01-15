package store.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//import store.contracts.generated.main.java.store.contracts.StoreContracts;

import store.contracts.shopping.GetProductsRequest;
import store.contracts.shopping.ShoppingProto;

import java.util.Arrays;
import java.util.List;

@RestController()
public class ProductResource {


    @GetMapping("/products")
    public List<Product> products() {


//        var request = GetProductsRequest.newBuilder().build();
//        ShoppingProto.getDescriptor()

//        StoreContracts storeContracts = new StoreContracts();
//        StoreContracts storeContracts = null;
//        store.contracts..ShoppingProto;
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().isVirtual());
        return Arrays.asList(new Product("1", "Product 1"), new Product("2", "Product 2"));
    }

}
