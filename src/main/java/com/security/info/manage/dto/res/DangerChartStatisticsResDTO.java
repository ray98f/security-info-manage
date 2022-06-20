package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
@ApiModel
public class DangerChartStatisticsResDTO {

    @ApiModelProperty(value = "新增折线详情")
    private List<ChartStatistics> newAddStatistics;

    @ApiModelProperty(value = "留存折线详情")
    private List<ChartStatistics> legacyStatistics;

    @Data
    public static class ChartStatistics {
        @ApiModelProperty(value = "月份")
        private String month;

        @ApiModelProperty(value = "问题数量")
        private Integer num;
    }
}
