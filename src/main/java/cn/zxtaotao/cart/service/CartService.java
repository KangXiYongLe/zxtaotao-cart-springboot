package cn.zxtaotao.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.zxtaotao.cart.bean.Item;
import cn.zxtaotao.cart.mapper.CartMapper;
import cn.zxtaotao.cart.pojo.Cart;
import cn.zxtaotao.cart.threadlocal.UserThreadLocal;
import cn.zxtaotao.sso.query.bean.User;

import com.github.abel533.entity.Example;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private ItemService itemService;

    //事务管理的注解
    @Transactional
    public void addItemToCart(Long itemId) {
        //1，判断该商品再购物车表中是否存在
        User user = UserThreadLocal.get();
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(user.getId());
        Cart cart = this.cartMapper.selectOne(record);
                
        //2，如果不存在，封装商品数据，将该商品插入购物车表
        if(cart==null){
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setNum(1);//TODO 先默认为1
            cart.setItemId(itemId);
            cart.setUserId(user.getId());
            
            Item item = this.itemService.queryById(itemId);
            cart.setItemImage(item.getImage().split(",")[0]);
            cart.setItemPrice(item.getPrice());
            cart.setItemTitle(item.getTitle());
            
            this.cartMapper.insert(cart);//将商品存入购物车表
        }else{
          //3，如果存在，修改商品数量，更新商品表
            cart.setUpdated(new Date());
            cart.setNum(cart.getNum()+1);//TODO 先默认为1
            this.cartMapper.updateByPrimaryKeySelective(cart);
        }   
    }

    /**
     * 查询对应用户名下的购物车中的商品列表
     * @return
     */
    public List<Cart> queryCartList() {
        return this.queryCartList(UserThreadLocal.get().getId());
    }
    
    /**
     * 根据用户id查询购物车商品列表
     * @param userId
     * @return
     */
    //事务管理的注解
    @Transactional(readOnly=true)
    public List<Cart> queryCartList(Long userId) {
        Example example = new Example(Cart.class);
        example.setOrderByClause("created DESC");
  
        example.createCriteria().andEqualTo("userId", userId);
        
        return this.cartMapper.selectByExample(example);
    }

    //事务管理的注解
    @Transactional
    public void updateNum(Long itemId, Integer num) {
        // 1,更新条件
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("userId", UserThreadLocal.get().getId())
                .andEqualTo("itemId", itemId);
        // 2,更新内容
        Cart cart = new Cart();
        cart.setUpdated(new Date());
        cart.setNum(num);

        // 3,执行更新
        this.cartMapper.updateByExampleSelective(cart, example);
    }

    /**
     * 物理删除对应用户的购物车中的对应商品
     * @param itemId
     */
    //事务管理的注解
    @Transactional
    public void deleteItem(Long itemId) {
        Cart cart = new Cart();
        cart.setItemId(itemId);
        cart.setUserId(UserThreadLocal.get().getId());
        
        this.cartMapper.delete(cart);
        
    }
}
