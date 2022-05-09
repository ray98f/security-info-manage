package com.security.info.manage.dto.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author frp
 */
@Data
@ApiModel
public class Ztt4GPlaybackResDTO {

    @ApiModelProperty(value = "设备id")
    private String PUID;

    @ApiModelProperty(value = "当前设备的第几个视频,默认为第一个视频,值为0")
    private String ResIdx;

    @ApiModelProperty(value = "录像文件名,不包括路径")
    private String Name;

    @ApiModelProperty(value = "录像文件所在的路径,包括最后的分隔符,比如\"/\"")
    private String Path;

    @ApiModelProperty(value = "录像文件的字节数")
    private String Size;

    @ApiModelProperty(value = "录像文件的开始时间,UTC时间,单位秒")
    private String Begin;

    @ApiModelProperty(value = "录像文件的结束时间,UTC时间,单位秒")
    private String End;

    @ApiModelProperty(value = "存储事务ID")
    private String ID;

    private String Reason;

    private String Url;
}
