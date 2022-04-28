package com.security.info.manage.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * @author frp
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "law-file")
public class File {

    @Id
    @ApiModelProperty(value = "文件id")
    private String id;

    @ApiModelProperty(value = "文件名")
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String fileName;

    @ApiModelProperty(value = "标识字符")
    @Field(type = FieldType.Keyword, index = false)
    private String bizCode;

    @ApiModelProperty(value = "链接")
    @Field(type = FieldType.Keyword, index = false)
    private String fileUrl;

    @ApiModelProperty(value = "内容")
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String content;

    @ApiModelProperty(value = "创建时间")
    @Field(type = FieldType.Keyword, index = false)
    private Long createTime;
}
