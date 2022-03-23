package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class UserArchivesResDTO {

    @ApiModelProperty(value = "用户信息")
    private UserResDTO userInfo;

    @ApiModelProperty(value = "最新体检")
    private PhysicalUserResDTO latestUserPhysical;

    @ApiModelProperty(value = "用户体检记录")
    private List<PhysicalUserResDTO> userPhysicalList;

}
