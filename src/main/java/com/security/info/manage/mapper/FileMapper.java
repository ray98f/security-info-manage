package com.security.info.manage.mapper;

import com.security.info.manage.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface FileMapper {

    void insertFile(String id, String url, String bizCode, String name, String doName);

    List<File> selectFileInfo(List<String> list);
}
