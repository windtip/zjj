package com.tresin.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fv.service.SubmissionWebSocket;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.ContextLoader;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 */
@ServerEndpoint("/webSocket")
@Log
public class PlanOperationWebSocket {
	private static AtomicInteger onlineCount = new AtomicInteger(0);

	private static ConcurrentHashMap<Integer, PlanOperationWebSocket> webSocketList = new ConcurrentHashMap<Integer, PlanOperationWebSocket>();

	private Session session;

	//这里使用的loginService实在spring配置文件中创建了bean的id
	private SubmissionWebSocket submissionWebSocket=(SubmissionWebSocket) ContextLoader.getCurrentWebApplicationContext().getBean("SubmissionWebSocket");
	@OnOpen
	public void onOpen(Session session){
		this.session = session;
		webSocketList.put(session.hashCode(), this);     
		addOnlineCount();
		log.info("Connection count: " + getOnlineCount());
	}

	@OnClose
	public void onClose(){
		webSocketList.remove(this.hashCode());  
		subOnlineCount();
		log.info("Connection count:" + getOnlineCount());
	}
    //type 1-计划编制提交 2-计划更改提交 3-计划编制发布  4-计划更改发布
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("Get Message:" + message);
		Iterator<Map.Entry<Integer, PlanOperationWebSocket>> webSocketIterator = webSocketList.entrySet().iterator();
		while(webSocketIterator.hasNext()){
			Map.Entry<Integer, PlanOperationWebSocket> item = webSocketIterator.next();
			try {
				if(item.getKey().intValue() == this.session.hashCode()) {
					String type=message.split(":")[0];
					String id=message.split(":")[1];
					String tit=message.split(":")[2];
					JSONObject messageJs=new JSONObject();
					messageJs.put("type",type);
					messageJs.put("id",id);
					messageJs.put("tit",tit);
					if (type.equals("1") || type.equals("2") ) {
						JSONObject web=submissionWebSocket.webSocketMq(messageJs.toJSONString());
						String resultMessage=JSON.toJSONString(web);
						item.getValue().sendMessage(resultMessage);
					}
//					else {
//						JSONObject web=submissionWebSocket.webSocketDeployMq(messageJs.toJSONString());
//						String resultMessage=JSON.toJSONString(web);
//						item.getValue().sendMessage(resultMessage);
//					}
				}  else {
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	@OnError
	public void onError(Session session, Throwable error){
		log.info("Error on web socket, error machine: " + session.hashCode());
		error.printStackTrace();
	}

	public void sendMessage(String message) throws IOException{
		this.session.getBasicRemote().sendText(message);
		log.info("Sent message to client " + session.hashCode() + "\n [Message]:" + message);
	}

	public int getOnlineCount() {
		return onlineCount.get();
	}

	public void addOnlineCount() {
		onlineCount.incrementAndGet();
		log.info("User '" + this.session.hashCode() + "' connected to WEB Socket.");
	}

	public void subOnlineCount() {
		onlineCount.decrementAndGet();
		log.info("User '" + this.session.hashCode() + "' disconnect from WEB Socket.");
	}
}
