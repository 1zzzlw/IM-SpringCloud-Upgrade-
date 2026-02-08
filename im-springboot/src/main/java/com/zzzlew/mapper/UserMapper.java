package com.zzzlew.mapper;

import com.zzzlew.pojo.entity.UserAuth;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: zzzlew
 * @Date: 2025/11/6 - 11 - 06 - 23:09
 * @Description: com.zzzlew.zzzimserver.mapper
 * @version: 1.0
 */
public interface UserMapper {
    /**
     * 根据账号查询用户
     * 
     * @param account 账号
     * @return 用户实体类
     */
    @Select("select * from user_auth where account = #{account}")
    UserAuth getByAccount(String account);

    /**
     * 插入用户
     * 
     * @param
     */
    void insertUserAuth(UserAuth userAuth);

    /**
     * 根据用户ID列表查询用户
     * 
     * @param targetUserIdList 用户ID列表
     * @return 用户实体类列表
     */
    List<UserAuth> selectUserAuthListByUserIdList(List<Long> targetUserIdList);


     /**
      * 根据用户ID查询用户信息
      *
      * @param userId 用户ID
      * @return 用户信息VO
      */
     UserAuth selectUserInfoById(Long userId);

}
