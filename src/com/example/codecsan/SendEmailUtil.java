package com.example.codecsan;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendEmailUtil {
	
	private static String sendAddress = "jinhui_dev@163.com";//发送方
	private static String recvAddress = "ke.wang@zifisense.co.uk";//接收方
	/**
     * 发送邮件的方法
     * file 要发送的文件
     * @return
     */
     @SuppressWarnings("static-access")
	public static boolean sendEmail(File file){  

    	    
        Properties props = new Properties();  
        props.setProperty("mail.debug", "true");

        props.put("mail.smtp.protocol", "smtp");  
        props.put("mail.smtp.auth", "true");//设置要验证  
        props.put("mail.smtp.host", "smtp.163.com");//设置host  
        props.put("mail.smtp.port", "25");  //设置端口  
        
        
        PassAuthenticator pass = new PassAuthenticator();   //获取帐号密码  
        pass.getPasswordAuthentication();
        Session session = Session.getInstance(props, pass); //获取验证会话  
        try  
        {  
            //配置发送及接收邮箱  
            InternetAddress fromAddress, toAddress;  
            /**
             * 这个地方需要改成自己的邮箱
             */
            fromAddress = new InternetAddress(sendAddress, "纵行科技");  
            toAddress   = new InternetAddress(recvAddress, "jinnh");
            /**
             * 以下内容是：发送邮件时添加附件
             */
            MimeBodyPart attachPart = new MimeBodyPart();  
            FileDataSource fds = new FileDataSource(file); //打开要发送的文件  
            
            attachPart.setDataHandler(new DataHandler(fds)); 
            attachPart.setFileName(fds.getName()); 
            MimeMultipart allMultipart = new MimeMultipart("mixed"); //附件  
            allMultipart.addBodyPart(attachPart);//添加  
            //配置发送信息  
            MimeMessage message = new MimeMessage(session);  
            //message.setContent("test", "text/plain"); 
            message.setContent(allMultipart); //发邮件时添加附件
            message.setSubject("【APP】MAC信息");  
            message.setFrom(fromAddress);  
            message.addRecipient(Message.RecipientType.TO, toAddress);  
            message.saveChanges();  
            //连接邮箱并发送  
            Transport transport = session.getTransport("smtp");  
            /**
             * 这个地方需要改称自己的账号和密码
             */
            transport.connect("smtp.163.com", "jinhui_dev@163.com", "hjh4dsb.");  
            
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();  
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");  
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");  
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");  
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");  
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");  
            CommandMap.setDefaultCommandMap(mc);  
            
            transport.send(message);  
            transport.close();  
            return true;
        } catch(AuthenticationFailedException e){        
            e.printStackTrace();   
            System.out.println(e);
            return false;
        } catch (MessagingException e) {  
            e.printStackTrace();  
            Exception ex = null;  
            if ((ex = e.getNextException()) != null) {  
                System.out.println(ex.toString());  
                ex.printStackTrace();  
            }   
            return false;
        } catch (Exception e) {
        	return false;
        }
          
    } 
}
