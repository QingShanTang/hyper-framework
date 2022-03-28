package org.qingshan.web.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class FileUploadParams {

    @JsonIgnore
    @NotEmpty(message = "文件列表不可为空")
    private List<MultipartFile> fileList;

    @NotBlank(message = "remark不可为空")
    private String remark;
}
