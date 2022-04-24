package org.qingshan.utils.feign;

import com.netflix.client.ClientFactory;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignUtil {

    /**
     * 根据类型获取服务
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getService(Class<T> clazz) throws Exception {
        FeignServicePOJO<T> servicePOJO = FeignConstant.serviceContainer.get(clazz);
        if (null != servicePOJO) {
            return servicePOJO.getService();
        } else {
            log.error("该类型未初始化,serviceCalzz:{}", clazz.getTypeName());
            throw new Exception("该服务未初始化!");
        }
    }

    /**
     * 刷新服务列表
     *
     * @param clientName
     */
    public static void updateListOfServers(String clientName) {
        log.info("刷新服务列表,clientName:{}", clientName);
        try {
            ILoadBalancer loadBalancer = ClientFactory.getNamedLoadBalancer(clientName);
            if (loadBalancer instanceof DynamicServerListLoadBalancer) {
                ((DynamicServerListLoadBalancer) loadBalancer).updateListOfServers();
            }
            log.info("刷新服务列表成功");
        } catch (Exception e) {
            log.error("刷新listOfServers失败,errorMsg->", e);
        }
    }
}
