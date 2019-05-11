package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.yychat.model.Message;

public class ServerReceiverThread extends Thread{
	Socket s;
	HashMap hmSocket;
	Message mess;
	String sender;
	ObjectOutputStream oos;
	
	public ServerReceiverThread(Socket s,HashMap hmSocket){
		this.s=s;
		this.hmSocket=hmSocket;
	}
	public void run(){
			ObjectInputStream ois;
		
			while(true){
				try {
				ois=new ObjectInputStream(s.getInputStream());
			mess=(Message)ois.readObject();
			System.out.println(mess.getSender()+"��"+mess.getReceiver()+"˵��"+mess.getContent());
			sender=mess.getSender();
		
			if(mess.getMessageType().equals(Message.message_Common)){
				Socket s1=(Socket)hmSocket.get(mess.getReceiver());
			    sendMessage(s1,mess);
					System.out.println("������ת������Ϣ" + mess.getSender() + "��"+ mess.getReceiver() + "˵��" + mess.getContent());
			
			}
              //��2�����������ߺ�����Ϣ���ͻ���
			if(mess.getMessageType().equals(Message.message_RequestOnlineFriend)){
				//����Ҫ�õ����ߺ�����Ϣ
				Set friendSet=StartServer.hmSocket.keySet();
				Iterator it=friendSet.iterator();//��������Ŀ���Ƕ�friendSet�����е�Ԫ�ؽ��б���
				String friendName;
				String friendString=" ";
				while(it.hasNext()){
					friendName=(String)it.next();//�ж��Ƿ�����һ������
					if(!friendName.equals(mess.getSender()))//�ų��Լ�
					friendString=friendString+friendName+" ";
				}
				System.out.println("ȫ�����ѵ����֣�"+friendString);
				
				//��mess��ֵ
			    mess.setContent(friendString);
			    mess.setReceiver(sender);
			    mess.setSender("Server");
			    mess.setMessageType(Message.message_OnlineFriend);
			    
			    //����mess
			    Socket s1=(Socket)hmSocket.get(sender);
			    sendMessage(s1,mess);
				
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
			}
		
	}
	private void sendMessage(Socket s,Message mess) throws IOException {
		oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(mess);// ת��Massage
	}
}

