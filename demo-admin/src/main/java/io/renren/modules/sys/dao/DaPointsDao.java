package io.renren.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.renren.modules.sys.entity.DaPointsEntity;


public interface DaPointsDao extends BaseMapper<DaPointsEntity> {

	List<DaPointsEntity> queryStudentsBySql(Map<String,Object> data);
	int getCount();
}
