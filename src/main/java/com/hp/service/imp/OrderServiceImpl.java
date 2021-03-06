package com.hp.service.imp;

import com.hp.dao.ClientUserDao;
import com.hp.dao.OrderDao;
import com.hp.pojo.ClientUser;
import com.hp.pojo.ClientUserWithOrder;
import com.hp.pojo.Order;
import com.hp.service.OrderService;
import com.hp.vo.PageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ClientUserDao userDao;

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public PageObject<ClientUserWithOrder> getAllOrders(String sort, Integer currentPage, Integer pageSize,
                                                        String content,String type) {
        int startIndex = (currentPage - 1) * pageSize;//计算获得startIndex用于sql查询
        List<ClientUserWithOrder> entityList = orderDao.selAllOrders(sort,startIndex,pageSize,content,type);//获取数据
        int rowCount = orderDao.selCount();//获取总记录数
        int pageCount = rowCount / pageSize; //计算获得总页数

        /**总记录数不能除尽单页显示条目数，则总页数增加一页，用于显示零头信息*/
        if(rowCount % pageSize != 0){
            pageCount++;
        }


        PageObject<ClientUserWithOrder> obj = new PageObject<>();//创建PageObject对象用于封装信息
        /**封装信息*/
        obj.setPageCount(pageCount);
        obj.setPageCurrent(currentPage);
        obj.setItems(entityList);
        obj.setTotal(rowCount);
        return obj;//返回PageObject对象(到控制层)
    }

    @Override
    public int saveOrder(Order order,String realname) {
        ClientUser user = userDao.selectClientUserByRealName(realname);
        order.setOppen_id(user.getOppen_id());
        return orderDao.saveOrder(order);
    }

    @Override
    public int updateStatusByOrderId(Order order) {
        return orderDao.updateStatusByOpenId(order);
    }

    @Override
    public List<Order> selectAddr_name() {
        return orderDao.selectAddr_name();
    }
}
