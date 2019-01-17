package cn.geeklemon.entity;

public class ServerParam {
	private boolean balance;
	private String msg;
	private String name;
	private String password;

	public boolean isBalance() {
		return balance;
	}

	public void setBalance(boolean balance) {
		this.balance = balance;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
