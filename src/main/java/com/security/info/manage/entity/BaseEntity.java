package com.security.info.manage.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

/**
 * description:
 *
 * @author chentong
 * @version 1.0
 * @date 2020/12/15 8:54
 */
@Data
public class BaseEntity {

    @JsonIgnore
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private Timestamp createdTime;

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(value = "updated_time", fill = FieldFill.INSERT, update = "NOW()")
    private Timestamp updatedTime;

    @TableField(value = "updated_by", fill = FieldFill.INSERT)
    private String updatedBy;

    @JsonIgnore
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Integer isDeleted;
}
