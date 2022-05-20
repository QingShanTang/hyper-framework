package org.qingshan.utils.loadJar;

import org.junit.Test;

public class TestLoadJar {
    @Test
    public void testLoadJar() {
        JarLoaderImpl loader = new JarLoaderImpl();
        loader.load("xixi","${jarPath}");
        loader.unload("xixi");
    }
}
