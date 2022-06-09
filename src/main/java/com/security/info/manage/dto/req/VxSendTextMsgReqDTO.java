package com.security.info.manage.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author frp
 */
@Data
public class VxSendTextMsgReqDTO {

    @ApiModelProperty(value = "指定接收消息的成员，成员ID列表（多个接收者用‘|’分隔，最多支持1000个）。 特殊情况：指定为\"@all\"，则向该企业应用的全部成员发送")
    private String touser;

    @ApiModelProperty(value = "指定接收消息的部门，部门ID列表，多个接收者用‘|’分隔，最多支持100个。 当touser为\"@all\"时忽略本参数")
    private String toparty;

    @ApiModelProperty(value = "指定接收消息的标签，标签ID列表，多个接收者用‘|’分隔，最多支持100个。 当touser为\"@all\"时忽略本参数")
    private String totag;

    @ApiModelProperty(value = "消息类型，此时固定为：text")
    private String msgtype;

    @ApiModelProperty(value = "企业应用的id，整型")
    private Integer agentid;

    @ApiModelProperty(value = "消息内容")
    private Content text;

    @ApiModelProperty(value = "表示是否是保密消息，0表示可对外分享，1表示不能分享且内容显示水印，默认为0")
    private Integer safe;

    @ApiModelProperty(value = "表示是否开启id转译，0表示否，1表示是，默认0。仅第三方应用需要用到，企业自建应用可以忽略。")
    private Integer enable_id_trans;

    @ApiModelProperty(value = "表示是否开启重复消息检查，0表示否，1表示是，默认0")
    private Integer enable_duplicate_check;

    @ApiModelProperty(value = "表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时")
    private Integer duplicate_check_interval;

    @Data
    public static class Content {

        @ApiModelProperty(value = "消息内容，最长不超过2048个字节，超过将截断（支持id转译）")
        private String content;

        public Content(String content) {
            this.content = content;
        }
    }
}
