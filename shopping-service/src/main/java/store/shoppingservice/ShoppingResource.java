package store.shoppingservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingResource {

    @GetMapping("/shopping")
    public String shopping() {
        return "shopping";
    }

}
