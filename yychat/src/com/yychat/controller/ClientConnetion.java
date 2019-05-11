package com.yychat.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import com.yychat.model.Message;
import com.yychat.model.User;

public class ClientConnetion {
    
	//public static Socket s;//静态成员变量
	public Socket s;
	public static HashMap hmSocket=new HashMap<String,Socket>();
	public ClientConnetion(){
		try {//异常处理结构
			s=new Socket("127.0.0.1",3456);//本机地址，回测地址
            System.out.println("客户端Socket"+s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
    public Message loginValidate(User user){
    	ObjectOutputStream oos;
    	Message mess=null;
	 //对象的输入输出流
    	try {
			oos=new ObjectOutputStream(s.getOutputStream());//把字节输出流对象包装对象输出流对象
			oos.writeObject(user);
			//System.out.println("发送User：用户名为"+user.getUserName()+"密码为"+user.getPassWord());
			
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			mess=(Message)ois.readObject();//接收
			
			//登陆成功保存Socket对象到hmSocket中
			if(mess.getMessageType().equals(Message.message_LoginSuccess)){
				hmSocket.put(user.getUserName(),s);
				new ClientReceiverThread(s).start();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
    	return mess;
}

	}