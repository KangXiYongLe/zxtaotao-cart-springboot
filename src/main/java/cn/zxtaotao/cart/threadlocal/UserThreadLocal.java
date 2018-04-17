package cn.zxtaotao.cart.threadlocal;

import cn.zxtaotao.sso.query.bean.User;

public class UserThreadLocal {

    private static final ThreadLocal<User> THREAD_LOCAL = new ThreadLocal<User>();
    
    public static User get(){
        return THREAD_LOCAL.get();
    }
    
    public static void set(User value){
        THREAD_LOCAL.set(value);
    }
}
