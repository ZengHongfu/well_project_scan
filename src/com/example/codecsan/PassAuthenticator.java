package com.example.codecsan;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class PassAuthenticator extends Authenticator {
	
	public PasswordAuthentication getPasswordAuthentication()  {  
        /**
         * 这个地方需要添加上自己的邮箱的账号和密码
         */
        String username = "jinhui_dev@163.com";  
        String pwd = "hjh4dsb.";  
        return new PasswordAuthentication(username, pwd);  
    }  
}
