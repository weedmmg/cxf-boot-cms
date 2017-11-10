package com.cxf.mapper.xrjf;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cxf.entity.UserEntity;
import com.cxf.enums.UserSexEnum;

/**
 * 
 * @Date: 2017年10月23日 下午1:30:02
 * @author chenxf
 */
public interface UserMapper {

    @Select("<script>SELECT * FROM users WHERE 1=1  <if test=\"_parameter !=null \"> and userName like CONCAT(#{userName},'%')</if> order by id desc</script>")
    @Results({ @Result(property = "usersex", column = "user_sex", javaType = UserSexEnum.class), @Result(property = "nickname", column = "nick_name") })
    List<UserEntity> getAll(String userName);

    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results({ @Result(property = "usersex", column = "user_sex", javaType = UserSexEnum.class), @Result(property = "nickname", column = "nick_name") })
    UserEntity getOne(Long id);

    @Select("SELECT * FROM users WHERE username = #{userName}")
    @Results({ @Result(property = "usersex", column = "user_sex", javaType = UserSexEnum.class), @Result(property = "nickname", column = "nick_name") })
    UserEntity findByUsername(String userName);

    @Insert("INSERT INTO users(username,password,user_sex) VALUES(#{username}, #{password}, #{usersex})")
    void insert(UserEntity user);

    @Update("UPDATE users SET username=#{username},password=#{password} WHERE id =#{id}")
    void update(UserEntity user);

    @Delete("DELETE FROM users WHERE id =#{id}")
    void delete(Long id);
}
