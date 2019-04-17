package com.google.codeu.data;

public class User {

	private String email;
	private String aboutMe;
	private String name;
	private String profilePic;

	public User(String email, String aboutMe, String name) {
		this.email = email;
		this.aboutMe = aboutMe;
		this.name = name;
		this.profilePic = "";
	}
	public void setProfilePic(String imageUrl) {
		this.profilePic = imageUrl;
		return;
	}

	public String getEmail(){
		return email;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public String getName() {
		return name;
	}

	public String getprofilePic() {
		return profilePic;
	}

}