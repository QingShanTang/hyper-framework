package org.qingshan.utils.serDes;

import com.carrotsearch.sizeof.RamUsageEstimator;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.qingshan.pojo.web.MockCallContext;
import org.qingshan.utils.json.JSONUtil;

@Slf4j
public class TestSerDes {
    @Test
    @SneakyThrows
    public void testSerDesCompare() {
        for (int i = 0; i < 5; i++) {
            JsonSerDes jsonSerDes = new JsonSerDes();
            HessianSerDes hessianSerDes = new HessianSerDes();

            MockCallContext context = new MockCallContext();
            context.setData("xixi");
            String jsonData = JSONUtil.toJSONString(context);


            long jsonSerStart = System.currentTimeMillis();
            String jsonSer = SerDesUtil.serResult2Str(jsonSerDes.serialize(context));
            log.info("json序列化耗时:{}ms", System.currentTimeMillis() - jsonSerStart);

            long jsonDesStart = System.currentTimeMillis();
            MockCallContext jsonContext = (MockCallContext) jsonSerDes.deserialize(jsonData, new TypeReference<MockCallContext>() {
            });
            log.info("json反序列化耗时:{}ms", System.currentTimeMillis() - jsonDesStart);

            long hessianSerStart = System.currentTimeMillis();
            String hessianStr = SerDesUtil.serResult2Str(new HessianSerDes().serialize(context));
            log.info("hessian序列化耗时:{}ms", System.currentTimeMillis() - hessianSerStart);

            long hessianDesStart = System.currentTimeMillis();
            MockCallContext hessianContext = (MockCallContext) hessianSerDes.deserialize(hessianStr);
            log.info("hessian反序列化耗时:{}ms", System.currentTimeMillis() - hessianDesStart);

            log.info("json数据量:{}B", RamUsageEstimator.sizeOf(jsonSer));
            log.info("hessian数据量:{}B", RamUsageEstimator.sizeOf(hessianStr));
            System.out.println("\n\n");
            Thread.sleep(2000);
        }
    }

    @SneakyThrows
    @Test
    public void testSerDes() {
        MockCallContext context = new MockCallContext();
        context.setData("xixi");

        SerDes serDes = SerDesUtil.getSerDesFromClazz(MockCallContext.class);
        String serData = SerDesUtil.serResult2Str(serDes.serialize(context));
        log.info("{}", serData);
    }
}
