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
public class SafeExpectCollectionUnionResDTO {
    
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "预想会id")
    private String safeExpectId;

    @ApiModelProperty(value = "任务完成情况")
    private String taskCompletion;

    @ApiModelProperty(value = "存在问题及整改措施")
    private String problemMeasure;

    @ApiModelProperty(value = "收工会时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date collectionUnionTime;

    @ApiModelProperty(value = "收工会时间字符串")
    private String collectionUnionTimeStr;

    @ApiModelProperty(value = "创建人")
    private String createBy;
}
