package org.qingshan.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.qingshan.web.utils.serviceQuery.ServiceQuerier;
import org.qingshan.web.utils.serviceQuery.myService.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestServiceQuery {

    @Autowired
    private ServiceQuerier serviceQuerier;

    @Test
    public void getAllService() {
        Map<String, MyService> services = serviceQuerier.getAllService(MyService.class);
        System.out.println("1");
    }


    @Test
    public void getService() {
        MyService service = serviceQuerier.getService(MyService.class, "myServiceImpl1");
        System.out.println("1");
    }
}
