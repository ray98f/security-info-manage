package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class Ztt4GEquipmentResDTO {

    @ApiModelProperty(value = "设备id")
    private String puid;

    @ApiModelProperty(value = "年度")
    private String AllowFlag;

    private String Longitude;

    private String Latitude;

    private String Remark;

    @ApiModelProperty(value = "设备的名称")
    private String Name;

    private String Description;

    private String Enable;

    @ApiModelProperty(value = "设备是否在线,0代表下线1代表上线")
    private String OnlineFlag;

    private String Model;

    private String Type;

    private String ProducerID;

    private String HardType;

    private String HardwareVersion;

    private String SoftwareVersion;

    private String DeviceID;
}
