package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.PostReqDTO;
import com.security.info.manage.dto.req.PostUserReqDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.VxUserResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface PostMapper {

    Page<PostResDTO> listPost(Page<PostResDTO> page, String name, Integer status);

    Integer modifyPost(PostReqDTO postReqDTO);

    Integer addPost(PostReqDTO postReqDTO);

    Integer deletePost(PostReqDTO postReqDTO);

    Integer bindingPost(PostUserReqDTO postUserReqDTO);

    void insertPost(List<VxUserResDTO> list, String doName);

    void insertUserPost(List<VxUserResDTO> list);

}
