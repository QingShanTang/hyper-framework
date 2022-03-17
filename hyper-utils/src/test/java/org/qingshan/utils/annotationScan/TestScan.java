package org.qingshan.utils.annotationScan;

import org.junit.Test;
import org.qingshan.utils.json.JSONUtil;

import java.util.List;

public class TestScan {
    @Test
    public void testScan() {
        List<AnnotationTarget<TestAnno>> targets = AnnotationScanUtil.scan(TestAnno.class, null, "org.qingshan.utils");
        JSONUtil.printJSONStringWithFormat(targets);
    }
}
