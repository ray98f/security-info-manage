package com.security.info.manage.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author frp
 */
@Data
public class VxSendMiniProgramMsgReqDTO {

    @ApiModelProperty(value = "成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）")
    private String touser;

    @ApiModelProperty(value = "部门ID列表，多个接收者用‘|’分隔，最多支持100个。")
    private String toparty;

    @ApiModelProperty(value = "标签ID列表，多个接收者用‘|’分隔，最多支持100个。")
    private String totag;

    @ApiModelProperty(value = "消息类型，此时固定为：miniprogram_notice")
    private String msgtype;

    @ApiModelProperty(value = "小程序消息本体")
    private MiniProgramNotice miniprogram_notice;

    @ApiModelProperty(value = "表示是否开启id转译，0表示否，1表示是，默认0。仅第三方应用需要用到，企业自建应用可以忽略。")
    private Integer enable_id_trans;

    @ApiModelProperty(value = "表示是否开启重复消息检查，0表示否，1表示是，默认0")
    private Integer enable_duplicate_check;

    @ApiModelProperty(value = "表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时")
    private Integer duplicate_check_interval;

    @Data
    public static class MiniProgramNotice {
        @ApiModelProperty(value = "小程序appid，必须是与当前应用关联的小程序")
        private String appid;

        @ApiModelProperty(value = "点击消息卡片后的小程序页面，仅限本小程序内的页面。该字段不填则消息点击后不跳转。")
        private String page;

        @ApiModelProperty(value = "消息标题，长度限制4-12个汉字（支持id转译）")
        private String title;

        @ApiModelProperty(value = "消息描述，长度限制4-12个汉字（支持id转译）")
        private String description;

        @ApiModelProperty(value = "是否放大第一个content_item")
        private Boolean emphasis_first_item;

        @ApiModelProperty(value = "消息内容键值对，最多允许10个item")
        private List<ContentItem> content_item;

        @Data
        public static class ContentItem {

            @ApiModelProperty(value = "长度10个汉字以内")
            private String key;

            @ApiModelProperty(value = "长度30个汉字以内（支持id转译）")
            private String value;
        }

    }
}
