package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class DangerRegionStatisticsResDTO {

    @ApiModelProperty(value = "区域id")
    private String regionId;

    @ApiModelProperty(value = "区域名称")
    private String regionName;

    @ApiModelProperty(value = "区域统计")
    private RegionStatisticsResDTO regionStatistics;

//    @Data
//    public static class RegionStatistics {
//        @ApiModelProperty(value = "问题总数")
//        private Integer total;
//
//        @ApiModelProperty(value = "上月遗留问题数")
//        private Integer lastLegacy;
//
//        @ApiModelProperty(value = "本月新增问题数")
//        private Integer nowAdd;
//
//        @ApiModelProperty(value = "本月已整改问题数")
//        private Integer nowSolve;
//
//        @ApiModelProperty(value = "本月还存留问题数")
//        private Integer nowLegacy;
//
//        @ApiModelProperty(value = "周期新增问题数（广播）")
//        private Integer cycleAddBroadcast;
//
//        @ApiModelProperty(value = "周期解决问题数（广播）")
//        private Integer cycleSolveBroadcast;
//    }
}
