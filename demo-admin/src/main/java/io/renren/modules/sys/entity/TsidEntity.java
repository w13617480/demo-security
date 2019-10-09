package io.renren.modules.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_sid")
public class TsidEntity {
	
	/**
	 * 测试主键
	 */
	@TableId
	private int id;
	/**
	 * 测试用例
	 */
	private String idname;
	/**
	 * 测试人员
	 */
	private String ids;
	/**
	 * 测试人员
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdname() {
		return idname;
	}
	public void setIdname(String idname) {
		this.idname = idname;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	
}
