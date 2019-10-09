package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;


import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * 人员档案
 * 
 * @author wjh
 * @email w13617480@163.com
 * @date 2018-10-14
 */
@TableName("da_user")
public class DaUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer id;
	
	/**
	 * 部门ID
	 */
	@NotNull(message="部门不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Long deptId;
	
	/**
	 * 姓名
	 */
	@NotBlank(message="姓名不能为空")
	private String name;
	
	/**
	 * 性别(0：男，1：女)
	 */
	@NotBlank(message="性别不能为空")
	private String sex;
	
	 
	//警号
	private String worknumber;
	//身份证号
	private String idnumber;
	//民警/辅警
	private int policetype;
	
	
	/**
	 * 联系方式
	 */
	private String mobile;
	
	/**
	 * 出生日期
	 */
	private String birthday;
	
	/**
	 * 密码
	 */
	private String password;
	 
	 
	/**
	 * 状态(0：在职，1：离职)
	 */
	@NotBlank(message="是否在职不能为空")
	private String status;
	
	
	
	/**
	 * 创建时间
	 */
	private Date cretime;
	/**
	 * 创建人
	 */
	private Long creator;
	 
	
	
	//删除标记
	private int delflag;
	//删除日期
	private Date deltime;
	
	private Long points;
	private Integer level;
	
	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	
	
	/**
	 * 设置：姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：性别(0：男，1：女)
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * 获取：性别(0：男，1：女)
	 */
	public String getSex() {
		return sex;
	}
	 
	public String getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	
	public String getWorknumber() {
		return worknumber;
	}
	public void setWorknumber(String worknumber) {
		this.worknumber = worknumber;
	}
	
	
	
	
	public int getPolicetype() {
		return policetype;
	}
	public void setPolicetype(int policetype) {
		this.policetype = policetype;
	}
	/**
	 * 设置：联系方式
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：联系方式
	 */
	public String getMobile() {
		return mobile;
	}
	
	/**
	 * 设置：出生日期
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	/**
	 * 获取：出生日期
	 */
	public String getBirthday() {
		return birthday;
	}
	 
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 设置：状态(0：在职，1：离职)
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：状态(0：在职，1：离职)
	 */
	public String getStatus() {
		return status;
	}
	
	 
	/**
	 * 设置：创建时间
	 */
	public void setCretime(Date cretime) {
		this.cretime = cretime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCretime() {
		return cretime;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreator() {
		return creator;
	}
	
	public int getDelflag() {
		return delflag;
	}
	public void setDelflag(int delflag) {
		this.delflag = delflag;
	}
	 
	public Date getDeltime() {
		return deltime;
	}
	public void setDeltime(Date deltime) {
		this.deltime = deltime;
	}
	
	public Long getPoints() {
		return points;
	}
	
	public void setPoints(Long points) {
		this.points = points;
	}
	
	public Integer getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	
}
