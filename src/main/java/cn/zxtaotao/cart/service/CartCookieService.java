package cn.zxtaotao.cart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zxtaotao.cart.bean.Item;
import cn.zxtaotao.cart.pojo.Cart;
import cn.zxtaotao.common.utils.CookieUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

//事务管理的注解
//@Transactional不是对数据库的操作，不能用这个注解
@Service
public class CartCookieService {

    public static final String COOKIE_NAME = "ZXTT_CART";

    public static final Integer COOKIE_TIME = 60 * 60 * 24 * 30 * 12;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ItemService itemService;
    
    /**
     * 查询商品列表，TODO按照创建时间倒序排序
     * @param request
     * @return
     */
    public List<Cart> queryCartList(HttpServletRequest request) {
        String cookieValue = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        if (StringUtils.isEmpty(cookieValue)) {
            return new ArrayList<Cart>(0);
        }
        try {
            //将json字符串反序列化为List集合
            return MAPPER.readValue(cookieValue,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Cart>(0);
    }
    
    public void addItemToCart(HttpServletRequest request,HttpServletResponse response,Long itemId) {
        //1，判断该商品再购物车表中是否存在
        List<Cart> cartList = this.queryCartList(request);
        Cart cart = null;
        for (Cart c : cartList) {
            if(c.getItemId().longValue() == itemId.longValue()){
                cart = c;
                break;
            }
        }
                
        //2，如果不存在，封装商品数据，将该商品插入购物车表
        if(cart==null){
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setNum(1);//TODO 先默认为1
            cart.setItemId(itemId);
            
            Item item = this.itemService.queryById(itemId);
            cart.setItemImage(item.getImage().split(",")[0]);
            cart.setItemPrice(item.getPrice());
            cart.setItemTitle(item.getTitle());
            
            cartList.add(cart);
        }else{
          //3，如果存在，修改商品数量，更新商品表
            cart.setUpdated(new Date());
            cart.setNum(cart.getNum()+1);//TODO 先默认为1            
        } 
        
        //将购物车数据写回到Cookie中
        saveCartsToCookie(request,response,cartList);
    }
    
    /**
     * 将购物车数据写回到Cookie中
     * 
     * @param request
     * @param response
     * @param cartList
     */
    private void saveCartsToCookie(HttpServletRequest request, HttpServletResponse response,
            List<Cart> cartList) {
        try {
            CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(cartList),
                    COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 更新购物车中商品数量
     * @param request
     * @param response
     * @param num
     * @param itemId
     */
    public void updateNum(HttpServletRequest request, HttpServletResponse response, Integer num, Long itemId) {
        List<Cart> cartList = this.queryCartList(request);
        for (Cart cart : cartList) {
            if (cart.getItemId().longValue() == itemId.longValue()) {
                cart.setNum(num);
                cart.setUpdated(new Date());
                break;
            }
        }
        saveCartsToCookie(request, response, cartList);//将购物车数据写回Cookie
    }
    
    /**
     * 删除Cookie中的购物车数据
     * @param request
     * @param response
     * @param itemId
     */
    public void deleteItem(HttpServletRequest request, HttpServletResponse response, Long itemId) {
        List<Cart> cartList = this.queryCartList(request);
        for (Cart cart : cartList) {
            if (cart.getItemId().longValue() == itemId.longValue()) {
                cartList.remove(cart);
                break;
            }
        }
        saveCartsToCookie(request, response, cartList);//将购物车数据写回Cookie
    }

}
