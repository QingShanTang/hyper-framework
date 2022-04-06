package org.qingshan.utils.cmd;


import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.qingshan.common.CommonConstant;
import org.qingshan.common.CommonEnum;
import org.qingshan.utils.json.JSONUtil;

import java.nio.charset.Charset;

/**
 * 执行系统命令工具类
 */
@Slf4j
public class CmdUtil {

    /**
     * 执行指定命令
     *
     * @param desc
     * @param cmds
     * @return
     */
    public static CmdExecRecord exeCmdForStr(String desc, String... cmds) {
        log.info("执行命令,params->desc:{},cmds:{}", desc, JSONUtil.toJSONString(cmds));
        CmdExecRecord dto = CmdExecRecord.builder().desc(desc).cmds(cmds).build();
        try {
            dto.setOutput(RuntimeUtil.execForStr(Charset.forName(CommonConstant.DEFAULT_CHARSET), cmds));
            dto.setStatus(CommonEnum.Status.SUCCESS);
        } catch (Exception e) {
            dto.setOutput(e.getLocalizedMessage());
            dto.setStatus(CommonEnum.Status.FAIL);
        } finally {
            log.info("命令执行记录:{}", JSONUtil.toJSONString(dto));
            return dto;
        }
    }
}


