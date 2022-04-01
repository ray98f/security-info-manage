package com.security.info.manage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author frp
 */
@Mapper
@Repository
public interface FileMapper {

    void insertFile(String id, String url, String bizCode, String name, String doName);
}
