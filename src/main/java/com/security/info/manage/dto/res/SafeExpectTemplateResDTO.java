package com.security.info.manage.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author frp
 */
@Data
public class SafeExpectTemplateResDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "风险id")
    private String riskId;

    @ApiModelProperty(value = "作业内容")
    private String workContent;

    @ApiModelProperty(value = "作业环境")
    private String workEnvironment;

    @ApiModelProperty(value = "作业注意事项")
    private String workAttentionMatter;

    @ApiModelProperty(value = "安全技术交底")
    private String safeTechnicalDisclosure;
}
