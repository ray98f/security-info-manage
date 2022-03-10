package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.PostReqDTO;
import com.security.info.manage.dto.req.PostUserReqDTO;
import com.security.info.manage.dto.res.PostResDTO;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author frp
 */
public interface PostService {

    Page<PostResDTO> listPost(String name, Integer status, PageReqDTO pageReqDTO);

    void modifyPost(PostReqDTO postReqDTO);

    void addPost(PostReqDTO postReqDTO);

    void deletePost(PostReqDTO postReqDTO);

    void bindingPost(PostUserReqDTO postUserReqDTO);

}