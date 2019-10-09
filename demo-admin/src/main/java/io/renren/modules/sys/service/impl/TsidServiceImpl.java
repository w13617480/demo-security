package io.renren.modules.sys.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import io.renren.modules.sys.dao.TsidDao;
import io.renren.modules.sys.entity.TsidEntity;
import io.renren.modules.sys.service.TsidService;
@Service("tsidService")
public class TsidServiceImpl extends ServiceImpl<TsidDao, TsidEntity> implements TsidService {

	@Override
	public TsidEntity insterTsid() {
		// TODO Auto-generated method stub
		return baseMapper.insterTsid();
	}

	@Override
	public void deletctTsid(int parentId) {
		// TODO Auto-generated method stub
		baseMapper.deletctTsid(parentId);
	}

}
