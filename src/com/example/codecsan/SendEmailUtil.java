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
	
	private static String sendAddress = "jinhui_dev@163.com";//���ͷ�
	private static String recvAddress = "ke.wang@zifisense.co.uk";//���շ�
	/**
     * �����ʼ��ķ���
     * file Ҫ���͵��ļ�
     * @return
     */
     @SuppressWarnings("static-access")
	public static boolean sendEmail(File file){  

    	    
        Properties props = new Properties();  
        props.setProperty("mail.debug", "true");

        props.put("mail.smtp.protocol", "smtp");  
        props.put("mail.smtp.auth", "true");//����Ҫ��֤  
        props.put("mail.smtp.host", "smtp.163.com");//����host  
        props.put("mail.smtp.port", "25");  //���ö˿�  
        
        
        PassAuthenticator pass = new PassAuthenticator();   //��ȡ�ʺ�����  
        pass.getPasswordAuthentication();
        Session session = Session.getInstance(props, pass); //��ȡ��֤�Ự  
        try  
        {  
            //���÷��ͼ���������  
            InternetAddress fromAddress, toAddress;  
            /**
             * ����ط���Ҫ�ĳ��Լ�������
             */
            fromAddress = new InternetAddress(sendAddress, "���пƼ�");  
            toAddress   = new InternetAddress(recvAddress, "jinnh");
            /**
             * ���������ǣ������ʼ�ʱ��Ӹ���
             */
            MimeBodyPart attachPart = new MimeBodyPart();  
            FileDataSource fds = new FileDataSource(file); //��Ҫ���͵��ļ�  
            
            attachPart.setDataHandler(new DataHandler(fds)); 
            attachPart.setFileName(fds.getName()); 
            MimeMultipart allMultipart = new MimeMultipart("mixed"); //����  
            allMultipart.addBodyPart(attachPart);//���  
            //���÷�����Ϣ  
            MimeMessage message = new MimeMessage(session);  
            //message.setContent("test", "text/plain"); 
            message.setContent(allMultipart); //���ʼ�ʱ��Ӹ���
            message.setSubject("��APP��MAC��Ϣ");  
            message.setFrom(fromAddress);  
            message.addRecipient(Message.RecipientType.TO, toAddress);  
            message.saveChanges();  
            //�������䲢����  
            Transport transport = session.getTransport("smtp");  
            /**
             * ����ط���Ҫ�ĳ��Լ����˺ź�����
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
