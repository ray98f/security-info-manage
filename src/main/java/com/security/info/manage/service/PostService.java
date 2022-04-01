package com.security.info.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.PageReqDTO;
import com.security.info.manage.dto.req.PostReqDTO;
import com.security.info.manage.dto.req.PostUserReqDTO;
import com.security.info.manage.dto.res.ApplianceWarnResDTO;
import com.security.info.manage.dto.res.PostChangeListResDTO;
import com.security.info.manage.dto.res.PostResDTO;
import com.security.info.manage.dto.res.PostWarnResDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author frp
 */
public interface PostService {

    Page<PostResDTO> listPost(String name, Integer status, PageReqDTO pageReqDTO);

    List<PostResDTO> listAllPost();

    void modifyPost(PostReqDTO postReqDTO);

    void addPost(PostReqDTO postReqDTO);

    void deletePost(PostReqDTO postReqDTO);

    void bindingPost(PostUserReqDTO postUserReqDTO);

    Page<PostWarnResDTO> listPostWarn(PageReqDTO pageReqDTO);

    void handlePostWarn(String id);

    List<PostChangeListResDTO> userArchives(String id);

}
