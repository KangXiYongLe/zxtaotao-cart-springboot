package cn.zxtaotao.cart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zxtaotao.sso.query.api.UserQueryService;
import cn.zxtaotao.sso.query.bean.User;
//事务管理的注解
//@Transactional//这是查询redis缓存，而不是查询mysql数据库,所以不能用这个注解来管理事务
@Service
public class UserService {

    @Autowired
    private UserQueryService userQueryService;
    
    public User queryByToken(String token){
        try {
            return this.userQueryService.queryUserByToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
        
    }
    
    
}
