package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.DaUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 人员档案
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-07-19 08:51:45
 */
public interface DaUserService extends IService<DaUserEntity> {

    PageUtils queryPage(Map<String, Object> params);
    DaUserEntity  getDaUserEntitys(String id, String worknumber, String usernumber);
	List<DaUserEntity> getallUser();
	void deletebyids(Integer[] ids);
	List<DaUserEntity> getUderGh(String name);
	DaUserEntity getDaUserEntitysByWorknumber(String id, String worknumber);
	 
	public DaUserEntity login(String username, String password);
	void updPassword(String userid, String password);
	DaUserEntity getDaUserEntityByIdAndPassword(String userid, String oldpass);
}

