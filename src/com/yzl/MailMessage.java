/** 
 * mail body POJO class
 */  
package com.yzl;  
  
/** 
 * @author 응택림 20178040
 * 
 */  
public class MailMessage {  
    private String from;  
    private String to;  
    private String subject;  
    private String content;  
    private String dataFrom;  
    private String dataTo;  
    private String user;  
    private String password;  
    /** 
     *  
     */  
    public MailMessage() {  
        super();  
        // TODO Auto-generated constructor stub  
    }  
    /** 
     * @param from 
     * @param to 
     * @param subject 
     * @param content 
     * @param dataFrom 
     * @param dataTo 
     * @param user 
     * @param password 
     */  
    public MailMessage(String from, String to, String subject, String content,  
            String dataFrom, String dataTo, String user, String password) {  
        super();  
        this.from = from;  
        this.to = to;  
        this.subject = subject;  
        this.content = content;  
        this.dataFrom = dataFrom;  
        this.dataTo = dataTo;  
        this.user = user;  
        this.password = password;  
    }  
    public String getFrom() {  
        return from;  
    }  
    public void setFrom(String from) {  
        this.from = from;  
    }  
    public String getTo() {  
        return to;  
    }  
    public void setTo(String to) {  
        this.to = to;  
    }  
    public String getSubject() {  
        return subject;  
    }  
    public void setSubject(String subject) {  
        this.subject = subject;  
    }  
    public String getContent() {  
        return content;  
    }  
    public void setContent(String content) {  
        this.content = content;  
    }  
    public String getDataFrom() {  
        return dataFrom;  
    }  
    public void setDataFrom(String dataFrom) {  
        this.dataFrom = dataFrom;  
    }  
    public String getDataTo() {  
        return dataTo;  
    }  
    public void setDataTo(String dataTo) {  
        this.dataTo = dataTo;  
    }  
    public String getUser() {  
        return user;  
    }  
    public void setUser(String user) {  
        this.user = user;  
    }  
    public String getPassword() {  
        return password;  
    }  
    public void setPassword(String password) {  
        this.password = password;  
    }  
      
  
}  