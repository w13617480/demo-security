package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;

import io.renren.modules.sys.entity.TsidEntity;

public interface TsidService extends IService<TsidEntity>{
	TsidEntity insterTsid();
	 void deletctTsid(int parentId);
}
