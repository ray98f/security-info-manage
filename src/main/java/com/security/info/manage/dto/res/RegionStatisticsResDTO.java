package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description:
 *
 * @author zhangxin
 * @version 1.0
 * @date 2023/1/12 14:10
 */
@Data
@ApiModel
public class RegionStatisticsResDTO {

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
