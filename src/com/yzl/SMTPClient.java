package com.yzl;

  
/** 
 * @author 	응택림 20178040
 * 
 */  
import java.io.BufferedReader;  
import java.io.BufferedWriter;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;  
import java.net.Socket;  
import java.net.SocketException;  
import java.net.UnknownHostException;  
import java.util.StringTokenizer;  
  
public class SMTPClient {  
  
    private boolean debug = true;  
    BASE64Encoder encode = new BASE64Encoder();// BASE64Encoder tool class used to encrypt strings
  
    public static void main(String[] args) throws UnknownHostException,  
            IOException {  
        MailMessage message = new MailMessage();  
        message.setFrom("zoin123456@163.com");// Sender
        message.setTo("376712116@qq.com");// Multiple recipient addresses are separated by commas
        String server = "smtp.163.com";//SMTP mail server
        message.setSubject("jiayou yzl ni shi zui bang d ");// Mail theme
        message.setContent("yzl yao bang bang de ");// Mail content  
        message.setDataFrom("zoin123456@163.com");// Sender in the sender's column of the message
        message.setDataTo("376712116@qq.com");//Recipients, displayed in the recipient section of the message
        message.setUser("zoin123456@163.com");// Login account
        message.setPassword("******");// login password
  
        SMTPClient smtp = new SMTPClient(server, 25);  
        boolean flag;  
        flag = smtp.sendMail(message, server);  
        if (flag) {  
            System.out.println("mail send success!");  
        } else {  
            System.out.println("mail send fail");  
        }  
    }  
  
    private Socket socket;  
  
    public SMTPClient(String server, int port) throws UnknownHostException,  
            IOException {  
        try {  
            socket = new Socket(server, 25);  
        } catch (SocketException e) {  
            System.out.println(e.getMessage());  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            System.out.println("Connection established!");  
        }  
  
    }  
  
    // Register to mail server
    public void helo(String server, BufferedReader in, BufferedWriter out)  
            throws IOException {  
        int result;  
        result = getResult(in);  
        // Connect mail service, the server gives 220 response
        if (result != 220) {  
            throw new IOException("Connection server failed");  
        }  
        result = sendServer("HELO " + server, in, out);  
        // HELO command success returns 250
        if (result != 250) {  
            throw new IOException("Failed to register mail server!");  
        }  
    }  
  
    private int sendServer(String str, BufferedReader in, BufferedWriter out)  
            throws IOException {  
        out.write(str);  
        out.newLine();  
        out.flush();  
        if (debug) {  
            System.out.println("Command sent:" + str);  
        }  
        return getResult(in);  
    }  
  
    public int getResult(BufferedReader in) {  
        String line = "";  
        try {  
            line = in.readLine();  
            if (debug) {  
                System.out.println("Server return status:" + line);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        // Read the status code from the server return message and convert it into an integer return
        StringTokenizer st = new StringTokenizer(line, " ");  
        return Integer.parseInt(st.nextToken());  
    }  
  
    public void authLogin(MailMessage message, BufferedReader in,  
            BufferedWriter out) throws IOException {  
        int result;  
        result = sendServer("AUTH LOGIN", in, out);  
        if (result != 334) {  
            throw new IOException("User authentication failed!");  
        }  
  
        result = sendServer(encode.encode(message.getUser().getBytes()), in,  
                out);  
        if (result != 334) {  
            throw new IOException("error!");  
        }  
        result = sendServer(encode.encode(message.getPassword().getBytes()),  
                in, out);  
  
        if (result != 235) {  
            throw new IOException("Validation failed!");  
        }  
    }  
  
    // Start sending message, mail source address
    public void mailFrom(String source, BufferedReader in, BufferedWriter out)  
            throws IOException {  
        int result;  
        result = sendServer("MAIL FROM:<" + source + ">", in, out);  
        if (result != 250) {  
            throw new IOException("Specify source address error");  
        }  
    }  
  
    // Set mail recipient. Send multiple messages with "," separate
    public void rcpt(String touchman, BufferedReader in, BufferedWriter out)  
            throws IOException {  
  
        String[] mailList = touchman.split(",");  
        if (mailList.length > 1) {  
            for (String touch : mailList) {  
                connectionServer(touch,in,out);  
            }  
        }else  
            connectionServer(touchman,in,out);  
  
    }  
  
    private void connectionServer(String touch, BufferedReader in, BufferedWriter out)  
            throws IOException {  
        int result;  
        result = sendServer("RCPT TO:<" + touch + ">", in, out);  
        if (result != 250) {  
            throw new IOException("Destination address error!");  
        }  
    }  
  
    // mail body
    public void data(String from, String to, String subject, String content,  
            BufferedReader in, BufferedWriter out) throws IOException {  
        int result;  
        result = sendServer("DATA", in, out);  
        // Enter DATA enter, after receiving the 354 response, continue to enter the message content
        if (result != 354) {  
            throw new IOException("Unable to send data");  
        }  
        out.write("From: " + from);  
        out.newLine();  
        out.write("To: " + to);  
        out.newLine();  
        out.write("Subject: " + subject);  
        out.newLine();  
        out.newLine();  
        out.write(content);  
        out.newLine();  
        // End and enter the end of the message content input
        result = sendServer(".", in, out);  
        System.out.println(result);  
        if (result != 250) {  
            throw new IOException("Error sending data");  
        }  
    }  
  
    // quit  
    public void quit(BufferedReader in, BufferedWriter out) throws IOException {  
        int result;  
        result = sendServer("QUIT", in, out);  
        if (result != 221) {  
            throw new IOException("Failed to exit correctly");  
        }  
    }  
  
    // Send mail main program
    public boolean sendMail(MailMessage message, String server) {  
        try {  
            BufferedReader in = new BufferedReader(new InputStreamReader(socket  
                    .getInputStream()));  
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(  
                    socket.getOutputStream()));  
            helo(server, in, out);// HELO command
            authLogin(message, in, out);// AUTH LOGIN command  
            mailFrom(message.getFrom(), in, out);// MAIL FROM  
            rcpt(message.getTo(), in, out);// RCPT  
            data(message.getDataFrom(), message.getDataTo(), message  
                    .getSubject(), message.getContent(), in, out);// DATA  
            quit(in, out);// QUIT  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
  
        }  
        return true;  
    }  
  
}  