package com.yychat.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import com.yychat.model.Message;
import com.yychat.model.User;

public class ClientConnetion {
    
	//public static Socket s;//��̬��Ա����
	public Socket s;
	public static HashMap hmSocket=new HashMap<String,Socket>();
	public ClientConnetion(){
		try {//�쳣����ṹ
			s=new Socket("127.0.0.1",3456);//������ַ���ز��ַ
            System.out.println("�ͻ���Socket"+s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
    public Message loginValidate(User user){
    	ObjectOutputStream oos;
    	Message mess=null;
	 //��������������
    	try {
			oos=new ObjectOutputStream(s.getOutputStream());//���ֽ�����������װ�������������
			oos.writeObject(user);
			//System.out.println("����User���û���Ϊ"+user.getUserName()+"����Ϊ"+user.getPassWord());
			
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			mess=(Message)ois.readObject();//����
			
			//��½�ɹ�����Socket����hmSocket��
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