package com.abdotareq.subway_e_ticketing.model.dto;

import com.google.gson.annotations.SerializedName;

public class UserPassword {

	@SerializedName("email")
	private String email;

	@SerializedName("oldPassword")
	private String oldPassword;

	@SerializedName("newPassword")
	private String newPassword;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
