package org.qingshan.utils.serDes;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;


/**
 * Hessian serDes
 */
@Slf4j
public class HessianSerDes implements SerDes {
    @Override
    public Object serialize(Object o) throws IOException {
        ByteArrayOutputStream os = null;
        Hessian2Output output = null;
        try {
            os = new ByteArrayOutputStream();
            output = new Hessian2Output(os);
            output.writeObject(o);
            output.flush();
            byte[] bytes = os.toByteArray();
            return bytes;
        } catch (Exception e) {
            log.error("HessianSerDes 序列化异常,errorMsg->{}", e.getLocalizedMessage());
            throw e;
        } finally {
            if (null != output) {
                try {
                    output.close();
                } catch (Exception e) {
                    log.error("关闭Hessian2Output失败,errorMsg->{}", e.getLocalizedMessage());
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (Exception e) {
                    log.error("关闭ByteArrayOutputStream失败,errorMsg->{}", e.getLocalizedMessage());
                }
            }
        }
    }

    @Override
    public Object deserialize(Object o) throws IOException {
        byte[] source;
        Object target;
        if (o == null) {
            return null;
        } else if ("[B".equals(o.getClass().getName())) {
            source = (byte[]) o;
        } else if ("[C".equals(o.getClass().getName()) || o instanceof String) {
            source = Base64.getDecoder().decode(o.toString());
        } else {
            return null;
        }
        ByteArrayInputStream bis = null;
        Hessian2Input input = null;
        try {
            bis = new ByteArrayInputStream(source);
            input = new Hessian2Input(bis);
            target = input.readObject();
            return target;
        } catch (Exception e) {
            log.error("HessianSerDes 反序列化异常,errorMsg->{}", e.getLocalizedMessage());
            throw e;
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (Exception e) {
                    log.error("关闭Hessian2Input失败,errorMsg->{}", e.getLocalizedMessage());
                }
            }
            if (null != bis) {
                try {
                    bis.close();
                } catch (Exception e) {
                    log.error("关闭ByteArrayInputStream失败,errorMsg->{}", e.getLocalizedMessage());
                }
            }
        }
    }
}
