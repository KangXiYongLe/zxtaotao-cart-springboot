package cn.zxtaotao.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.zxtaotao.cart.pojo.Cart;
import cn.zxtaotao.cart.service.CartCookieService;
import cn.zxtaotao.cart.service.CartService;
import cn.zxtaotao.cart.threadlocal.UserThreadLocal;
import cn.zxtaotao.sso.query.bean.User;

@Controller
@RequestMapping("cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private CartCookieService cartCookieService;
    
    /**
     * 加入商品到购物车
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") Long itemId, HttpServletRequest request,
            HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (user == null) {
            // 未登录状态
            this.cartCookieService.addItemToCart(request, response, itemId);
        } else {
            // 已登陆状态
            this.cartService.addItemToCart(itemId);
        }
        return "redirect:/cart/list.html";
    }
    
    /**
     * 查询购物车列表，并转到购物车列表页面显示
     * @return
     */
    @RequestMapping(value="list",method=RequestMethod.GET)
    public ModelAndView showCartList(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("cart");
        List<Cart> cartList = null;
        User user = UserThreadLocal.get();
        if(user==null){
            //未登录状态
            cartList = this.cartCookieService.queryCartList(request);
        }else{
            //已登陆状态
            cartList = this.cartService.queryCartList();
        }
        modelAndView.addObject("cartList", cartList);
        return modelAndView;         
    }
    
    
  
    /**
     * 修改商品的数量
     * @param itemId
     * @param num 最终购买的商品数量
     * @return
     */
    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateNum(@PathVariable("itemId") Long itemId,
            @PathVariable("num") Integer num, HttpServletRequest request, HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (user == null) {
            // 未登录状态
            this.cartCookieService.updateNum(request, response, num, itemId);
        } else {
            // 登陆状态
            this.cartService.updateNum(itemId, num);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    
    /**
     * 删除购物车中的商品
     * @param itemId
     * @return
     */
    @RequestMapping(value="delete/{itemId}",method=RequestMethod.GET)
    public String deleteItem(@PathVariable("itemId") Long itemId, HttpServletRequest request,
            HttpServletResponse response) {
        User user = UserThreadLocal.get();
        if (user == null) {
            // 未登录状态
            this.cartCookieService.deleteItem(request, response, itemId);
        } else {
            // 已登陆状态
            this.cartService.deleteItem(itemId);
        }
        return "redirect:/cart/show.html";
    }

}
