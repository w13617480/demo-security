package io.renren.modules.sys.dao;

import io.renren.modules.sys.entity.DaUserEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 人员档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-07-19 08:51:45
 */
public interface DaUserDao extends BaseMapper<DaUserEntity> {
	DaUserEntity getDaUserEntity(@Param("id") String id, @Param("worknumber") String worknumber, @Param("usernumber") String usernumber);
	DaUserEntity getDaUserEntityByWorknumber(@Param("id") String id, @Param("worknumber") String worknumber);
	DaUserEntity getDaUserEntityByIdAndPassword(@Param("userid") String userid, @Param("password") String password);
	             
	DaUserEntity login(@Param("username")String username, @Param("password")String password);
	void updPassword(@Param("userid")String userid, @Param("password")String password);
	
	List<DaUserEntity> getallUser();
	void  deletebyids(Integer[] ids);
	List<DaUserEntity> getUderGh(@Param("worknumber") String worknumber);
}
