package com.security.info.manage.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.info.manage.dto.req.PasswordReqDTO;
import com.security.info.manage.dto.req.UserReqDTO;
import com.security.info.manage.dto.res.VxUserResDTO;
import com.security.info.manage.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author frp
 */
@Mapper
@Repository
public interface UserMapper {

    /**
     * 获取用户信息
     *
     * @param userName
     * @return
     */
    UserReqDTO selectUserInfo(Long id, String userName);

    /**
     * 获取用户权限id
     *
     * @param userId
     * @return
     */
    List<Long> selectUserRoles(Long userId);

    /**
     * 新增用户
     *
     * @param list
     * @param doName
     * @return
     */
    void insertUser(List<VxUserResDTO> list, String doName);

    void insertPost(List<VxUserResDTO> list, String doName);

    void insertUserPost(List<VxUserResDTO> list);

    /**
     * 新增用户权限
     *
     * @param userId
     * @param roleIds
     * @param doName
     * @return
     */
    int insertUserRole(Long userId, List<Long> roleIds, String doName);

    /**
     * 修改密码
     *
     * @param passwordReqDTO
     * @param updateBy
     * @return
     */
    int changePwd(PasswordReqDTO passwordReqDTO, String updateBy);

    /**
     * 修改用户
     *
     * @param userReqDTO
     * @param updateBy
     * @return
     */
    int editUser(UserReqDTO userReqDTO, String updateBy);

    /**
     * 删除用户的所有角色
     * @param userId
     */
    void deleteUserRole(Long userId);

    /**
     * 获取所有用户列表
     *
     * @return
     */
    List<User> listAllUser();

    /**
     * 查询用户列表
     * @param page
     * @param status
     * @param userRealName
     * @return
     */
    Page<User> listUser(Page<User> page, Integer status,String userRealName);

    /**
     * 根据用户名获取用户id
     * @param userName
     * @return
     */
    Long selectUserId(String userName);
}
