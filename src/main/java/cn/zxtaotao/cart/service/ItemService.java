package cn.zxtaotao.cart.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.zxtaotao.cart.bean.Item;
import cn.zxtaotao.common.service.ApiService;

import com.fasterxml.jackson.databind.ObjectMapper;
//事务管理的注解
//@Transactional，不是对数据库的操作，所以不能用这个注解
@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Item queryById(Long itemId) {
        try {
            String url = this.TAOTAO_MANAGE_URL + "/rest/api/item/" + itemId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                return (Item) MAPPER.readValue(jsonData, Item.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
