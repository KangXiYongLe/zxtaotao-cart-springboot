package cn.zxtaotao.cart.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.zxtaotao.cart.pojo.Cart;
import cn.zxtaotao.cart.service.CartService;

@Controller
@RequestMapping("api/cart")
public class ApiCartController {
    
    @Autowired
    private CartService cartService;
    
    @RequestMapping(value="{userId}",method=RequestMethod.GET)
    public ResponseEntity<List<Cart>> queryCartList(@PathVariable("userId")Long userId){
        try {
            List<Cart> carts = this.cartService.queryCartList(userId);
            if(carts ==null||carts.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
