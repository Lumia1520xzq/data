package com.wf.uic.controller.request.callback;

import java.io.Serializable;

public class OkoooLoginBean implements Serializable{
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String user_name;
    private String avatar_url;
    private String open_id;
    
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

   
}
