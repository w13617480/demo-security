package io.renren.modules.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.common.annotation.DataFilter;
import io.renren.common.utils.Constant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.DaUserDao;
import io.renren.modules.sys.entity.DaUserEntity;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.DaUserService;


@Service("daUserService")
public class DaUserServiceImpl extends ServiceImpl<DaUserDao, DaUserEntity> implements DaUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	
    	String name = (String)params.get("name");
    	String status = (String)params.get("status");
    	String policetype = (String)params.get("policetype");
    	String deptId = (String)params.get("deptid");
    	
    	
    	if ( status != null && status.equals("-1") ) {
    		status = "";
    	}
    	
    	if ( policetype != null && policetype.equals("-1") ) {
    		policetype = "";
    	}

    	Page<DaUserEntity> page = this.selectPage(
                new Query<DaUserEntity>(params).getPage(),
                new EntityWrapper<DaUserEntity>()
                .eq("delflag", "0")
                .eq(StringUtils.isNotBlank(status), "status", status)
                .eq(StringUtils.isNotBlank(policetype), "policetype", policetype)
                .eq(StringUtils.isNotBlank(deptId), "dept_id", deptId)
                .andNew(StringUtils.isNotBlank(name), "name like '%" + name + "%' or worknumber like '%" + name + "%'", null)
                
                .orderBy("name", false)
        );

        return new PageUtils(page);
    }
    
    

	@Override
	public DaUserEntity getDaUserEntitys(String id, String worknumber, String usernumber) {
		// TODO Auto-generated method stub
		return baseMapper.getDaUserEntity(id, worknumber, usernumber);
	}

	@Override
	public List<DaUserEntity> getallUser() {
		// TODO Auto-generated method stub
		return baseMapper.getallUser();
	}

	@Override
	public void deletebyids(Integer[] ids) {
		// TODO Auto-generated method stub
		baseMapper.deletebyids(ids);

	}

	@Override
	public List<DaUserEntity> getUderGh(String worknumber) {
		// TODO Auto-generated method stub
		return baseMapper.getUderGh(worknumber);
	}



	@Override
	public DaUserEntity getDaUserEntitysByWorknumber(String id, String worknumber) {
		// TODO Auto-generated method stub
		return baseMapper.getDaUserEntityByWorknumber(id, worknumber);
	}


 
	@Override
	public DaUserEntity login(String username, String password) {
		// TODO Auto-generated method stub
		return baseMapper.login(username, password);		
	}
	
	@Override
	public void updPassword(String userid, String password) {
		baseMapper.updPassword(userid, password);		
	}



	@Override
	public DaUserEntity getDaUserEntityByIdAndPassword(String userid, String password) {
		return baseMapper.getDaUserEntityByIdAndPassword(userid, password);
	}
}
