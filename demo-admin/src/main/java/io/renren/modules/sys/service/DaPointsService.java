package io.renren.modules.sys.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.DaPointsEntity;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-11-13 14:29:17
 */
public interface DaPointsService extends IService<DaPointsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<DaPointsEntity> queryStudentsBySql(Map<String, Object> data);

	int getCount();
}


