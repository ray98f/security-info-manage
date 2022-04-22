package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class VxRegionResDTO {

    @ApiModelProperty(value = "类型")
    private RegionTypeResDTO type;

    @ApiModelProperty(value = "区域")
    private List<RegionResDTO> regions;
}
