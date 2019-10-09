package io.renren.modules.sys.entity;
/**
 * 
 * @author liming
 *	合同实体
 */
public class HeTong {
	private String jf;//甲方
	
	private String jfs;//甲方身份证号
	
	private String yf;//乙方
	
	private String yfs;//乙方身份证号
	
	private String fs;//方式
	
	public HeTong(){
		
	}
	
	public HeTong(String jf, String jfs, String yf, String yfs, String fs) {
		super();
		this.jf = jf;
		this.jfs = jfs;
		this.yf = yf;
		this.yfs = yfs;
		this.fs = fs;
	}
	public String getJf() {
		return jf;
	}
	public void setJf(String jf) {
		this.jf = jf;
	}
	public String getJfs() {
		return jfs;
	}
	public void setJfs(String jfs) {
		this.jfs = jfs;
	}
	public String getYf() {
		return yf;
	}
	public void setYf(String yf) {
		this.yf = yf;
	}
	public String getYfs() {
		return yfs;
	}
	public void setYfs(String yfs) {
		this.yfs = yfs;
	}
	public String getFs() {
		return fs;
	}
	public void setFs(String fs) {
		this.fs = fs;
	}
}
