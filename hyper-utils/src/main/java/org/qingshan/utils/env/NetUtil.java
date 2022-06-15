package org.qingshan.utils.env;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 网络工具
 */
@Slf4j
public class NetUtil {

    public static List<String> macs;

    static {
        try {
            macs = getMacList();
        } catch (Exception e) {
            log.error("获取mac地址失败,errorMsg->", e);
        }
    }

    /**
     * 获取mac地址
     * 一台机器可能有多个网卡
     *
     * @return
     * @throws Exception
     */
    private static List<String> getMacList() throws Exception {
        log.info("获取mac地址。。。");
        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        ArrayList<String> tmpMacList = new ArrayList<>();
        while (en.hasMoreElements()) {
            NetworkInterface iface = en.nextElement();
            List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
            for (InterfaceAddress addr : addrs) {
                InetAddress ip = addr.getAddress();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                if (network == null) {
                    continue;
                }
                byte[] mac = network.getHardwareAddress();
                if (mac == null) {
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                tmpMacList.add(sb.toString());
            }
        }
        if (tmpMacList.size() <= 0) {
            return tmpMacList;
        }
        /***去重，别忘了同一个网卡的ipv4,ipv6得到的mac都是一样的，肯定有重复，下面这段代码是。。流式处理***/
        List<String> unique = tmpMacList.stream().distinct().collect(Collectors.toList());
        log.info("mac:{}", unique);
        return unique;
    }
}


