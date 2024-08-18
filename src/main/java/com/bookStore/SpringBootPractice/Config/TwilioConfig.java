package com.bookStore.SpringBootPractice.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {
	
	private String accountSid = "ACa75848921763e7a37a51c053a978f1e6";
	private String authToken = "53ff82451f72d278d731d4913483b4c2";
	private String trialNumber;
	public String getAccountSid() {
		return accountSid;
	}
	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getTrialNumber() {
		return trialNumber;
	}
	public void setTrialNumber(String trialNumber) {
		this.trialNumber = trialNumber;
	}
	

}
