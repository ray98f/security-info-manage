package com.security.info.manage.dto.req;

import com.security.info.manage.entity.PhysicalResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class PhysicalResultImportReqDTO {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "医院流水号")
    private String hospitalNo;

    @ApiModelProperty(value = "应检人数")
    private Integer estimateNum;

    @ApiModelProperty(value = "受检人数")
    private Integer actualNum;

    @ApiModelProperty(value = "体检时间")
    private String physicalTime;

    @ApiModelProperty(value = "创建人")
    private String userId;

    @ApiModelProperty(value = "选择人员")
    private List<PhysicalResult> users;

}
