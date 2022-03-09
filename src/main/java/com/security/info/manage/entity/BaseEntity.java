package com.security.info.manage.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseEntity {

    @JsonIgnore
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Timestamp createDate;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @TableField(value = "update_date", fill = FieldFill.INSERT, update = "NOW()")
    private Timestamp updateDate;

    @TableField(value = "update_by", fill = FieldFill.INSERT)
    private String updateBy;

    @JsonIgnore
    @TableLogic
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    private Integer isDelete;
}
