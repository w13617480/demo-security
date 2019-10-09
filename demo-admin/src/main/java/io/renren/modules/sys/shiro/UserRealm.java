/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.renren.modules.sys.shiro;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.renren.common.utils.Constant;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.dao.SysMenuDao;
import io.renren.modules.sys.dao.SysUserDao;
import io.renren.modules.sys.entity.SysMenuEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 认证
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 上午11:55:49
 */
@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysMenuDao sysMenuDao;
    
    /**
     * 授权(验证权限时调用)
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
		Long userId = user.getUserId();
		
		List<String> permsList;
		
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
			permsList = new ArrayList<>(menuList.size());
			permsList.add("sys:dict:showtype");
			permsList.add("sys:dateam:alllist");
			permsList.add("sys:dacheckpoint:alllist");
			permsList.add("sys:tzborrow:renquery");
			permsList.add("sys:tzborrow:shebeiquery");
			permsList.add("sys:tzborrow:shebeionlyquery");
			permsList.add("sys:tzborrow:pilis");
			permsList.add("sys:tzborrow:sbstatus");
			permsList.add("sys:tzborrow:getuserName");
			permsList.add("sys:tzborrow:getshebName");
			permsList.add("sys:tzborrow:getsbwghs");
			permsList.add("sys:tzborrow:getsbyghs");
			permsList.add("sys:tzborrow:getsbwgh");
			permsList.add("sys:tzborrow:shebeiguiquery");
			permsList.add("sys:tzborrow:updates");
			permsList.add("sys:tzborrow:code");
			permsList.add("sys:tzborrow:getoperator");
			permsList.add("sys:tzborrow:impzjqgtxxtj");
			permsList.add("sys:tzborrow:zjqshtj");
			permsList.add("sys:tzborrow:impzjqshtj");
			permsList.add("sys:tzborrow:dbwjzjqtj");
			permsList.add("sys:tzborrow:impdbwjzjqtj");
			permsList.add("sys:tzborrow:aqjczzjqtj");
			permsList.add("sys:tzborrow:impaqjczzjqtj");
			permsList.add("sys:tzborrow:getjczcount");
			permsList.add("sys:tzborrow:getjczcountId");
			permsList.add("sys:tzborrow:getcshjczname");
			permsList.add("sys:tzborrow:getjczyj");
			permsList.add("sys:tzborrow:getzjqyj");
			permsList.add("sys:tzborrow:getdbzjqkc");
			permsList.add("sys:tzborrow:impdbzjqkc");
			permsList.add("sys:tzborrow:renquerysg");
			permsList.add("sys:tzborrow:ryzw");
			permsList.add("sys:dauser:getalluser");
			permsList.add("sys:dauser:deletebyids");
			permsList.add("sys:dauser:impUser");
			permsList.add("sys:dauser:importExcel");
			permsList.add("sys:dauser:importExcelMessige");
			permsList.add("sys:daequipment:impEquipment");
			permsList.add("sys:daequipment:importExcel");
			permsList.add("sys:daequipment:importExcelMessige");
			permsList.add("sys:schedule:list");
			
			permsList.add("sys:daquery:querypoints");
			permsList.add("sys:daquery:querynews");
			permsList.add("sys:dapoints:getDaPointsCount");

			for(SysMenuEntity menu : menuList){
				permsList.add(menu.getPerms());
			}
		}else{
			permsList = sysUserDao.queryAllPerms(userId);
			permsList.add("sys:dict:showtype");
			permsList.add("sys:dateam:alllist");
			permsList.add("sys:dacheckpoint:alllist");
			permsList.add("sys:tzborrow:renquery");
			permsList.add("sys:tzborrow:shebeiquery");
			permsList.add("sys:tzborrow:shebeionlyquery");
			permsList.add("sys:tzborrow:pilis");
			permsList.add("sys:tzborrow:sbstatus");
			permsList.add("sys:tzborrow:getuserName");
			permsList.add("sys:tzborrow:getshebName");
			permsList.add("sys:tzborrow:getsbwghs");
			permsList.add("sys:tzborrow:getsbyghs");
			permsList.add("sys:tzborrow:getsbwgh");
			permsList.add("sys:tzborrow:shebeiguiquery");
			permsList.add("sys:tzborrow:updates");
			permsList.add("sys:tzborrow:code");
			permsList.add("sys:tzborrow:getoperator");
			permsList.add("sys:tzborrow:impzjqgtxxtj");
			permsList.add("sys:tzborrow:zjqshtj");
			permsList.add("sys:tzborrow:impzjqshtj");
			permsList.add("sys:tzborrow:dbwjzjqtj");
			permsList.add("sys:tzborrow:aqjczzjqtj");
			permsList.add("sys:tzborrow:impaqjczzjqtj");
			permsList.add("sys:tzborrow:impdbwjzjqtj");
			permsList.add("sys:tzborrow:getjczcount");
			permsList.add("sys:tzborrow:getjczcountId");
			permsList.add("sys:tzborrow:getcshjczname");
			permsList.add("sys:tzborrow:getjczyj");
			permsList.add("sys:tzborrow:getzjqyj");
			permsList.add("sys:tzborrow:getdbzjqkc");
			permsList.add("sys:tzborrow:impdbzjqkc");
			permsList.add("sys:tzborrow:renquerysg");
			permsList.add("sys:tzborrow:ryzw");
			permsList.add("sys:dauser:getalluser");
			permsList.add("sys:dauser:deletebyids");
			permsList.add("sys:dauser:impUser");
			permsList.add("sys:dauser:importExcel");
			permsList.add("sys:dauser:importExcelMessige");
			permsList.add("sys:daequipment:impEquipment");
			permsList.add("sys:daequipment:importExcel");
			permsList.add("sys:daequipment:importExcelMessige");
			permsList.add("sys:schedule:list");

			permsList.add("sys:daquery:querypoints");
			permsList.add("sys:daquery:querynews");
			permsList.add("sys:dapoints:getDaPointsCount");
		}

		//用户权限列表
		Set<String> permsSet = new HashSet<>();
		for(String perms : permsList){
			if(StringUtils.isBlank(perms)){
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		return info;
	}

	/**
	 * 认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken)authcToken;

		//查询用户信息
		SysUserEntity user = new SysUserEntity();
		user.setUsername(token.getUsername());
		user = sysUserDao.selectOne(user);

		//账号不存在
		if(user == null) {
			
			/*
			user = new SysUserEntity();
			user.setUsername(token.getUsername());
			user = sysUserDao.getDaUserEntityByPassword(user.getUsername());
			if ( user == null ) {
				throw new UnknownAccountException("账号或密码不正确");
			}
			user.setSalt("YzcmCZNvbXocrsz9dm8e");
			*/
			throw new UnknownAccountException("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
		return info;
	}

	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
		shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
		shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
		super.setCredentialsMatcher(shaCredentialsMatcher);
	}
}
