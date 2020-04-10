package com.taest.tourismdatavisualization.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description:
 * @Author: Taest
 * @CreateDate: 2020/4/2$ 15:13$
 */
@Mapper
public interface UserMapper {

    @Select("SELECT count(1) FROM `user` WHERE username=#{username} and password=#{password}")
    Integer checkUser(String username,String password);

    @Update("update user set password =#{password} where username =#{username}")
    int updatePwd(String username,String password);
}
