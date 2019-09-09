package cn.hl.ox.clone.bean;

/**
 * Bean Parent
 */
public class BP {
	private String pId;

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	@Override
	public String toString() {
		return "P {" + "pId='" + pId + '\'' + '}';
	}
}
