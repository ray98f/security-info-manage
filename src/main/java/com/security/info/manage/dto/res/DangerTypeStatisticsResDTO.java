package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class DangerTypeStatisticsResDTO {

    @ApiModelProperty(value = "问题类型")
    private String type;

    @ApiModelProperty(value = "问题总数")
    private Integer total;

    @ApiModelProperty(value = "上月遗留问题数")
    private Integer lastLegacy;

    @ApiModelProperty(value = "本月新增问题数")
    private Integer nowAdd;

    @ApiModelProperty(value = "本月已整改问题数")
    private Integer nowSolve;

    @ApiModelProperty(value = "本月还存留问题数")
    private Integer nowLegacy;

    @ApiModelProperty(value = "周期新增问题数（广播）")
    private Integer cycleAddBroadcast;

    @ApiModelProperty(value = "周期解决问题数（广播）")
    private Integer cycleSolveBroadcast;
}
