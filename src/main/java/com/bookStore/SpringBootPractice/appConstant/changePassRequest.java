package com.bookStore.SpringBootPractice.appConstant;

public class changePassRequest {
	private Integer otp;
	private String newPassword;
	public Integer getOtp() {
		return otp;
	}
	public void setOtp(Integer otp) {
		this.otp = otp;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
