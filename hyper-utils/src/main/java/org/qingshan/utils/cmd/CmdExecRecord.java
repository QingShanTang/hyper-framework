package org.qingshan.utils.cmd;

import lombok.Builder;
import lombok.Data;
import org.qingshan.common.CommonEnum;

@Data
@Builder
public class CmdExecRecord {
    /**
     * 操作描述
     */
    private String desc;

    /**
     * 工作目录
     */
    private String workDir;

    /**
     * 执行命令
     */
    private String[] cmds;

    /**
     * 状态
     */
    private CommonEnum.Status status;

    /**
     * 命令输出
     */
    private String output;
}
