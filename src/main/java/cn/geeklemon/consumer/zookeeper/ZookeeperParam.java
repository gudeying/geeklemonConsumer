package cn.geeklemon.consumer.zookeeper;

public class ZookeeperParam {
	private String connectString;
	private int timeOut;
	/**
	 * 	/geeklemoncloud/provider/{serviceName}
	 */
	private String serviceRootPath;

	public String getConnectString() {
		return connectString;
	}

	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public String getServiceRootPath() {
		return serviceRootPath;
	}

	public void setServiceRootPath(String serviceRootPath) {
		this.serviceRootPath = serviceRootPath;
	}

}
