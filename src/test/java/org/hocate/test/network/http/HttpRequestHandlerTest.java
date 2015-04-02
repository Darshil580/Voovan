package org.hocate.test.network.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.hocate.http.message.HttpParser;
import org.hocate.http.message.Request;
import org.hocate.http.message.Response;
import org.hocate.tools.log.Logger;
import org.hocate.network.IoHandler;
import org.hocate.network.IoSession;


public class HttpRequestHandlerTest implements IoHandler {

	private String hostNameString;
	
	public HttpRequestHandlerTest(String hostNameString){
		this.hostNameString = hostNameString;
	}
	@Override
	public Object onConnect(IoSession session) {
		Logger.simple("onConnect");
		Request request = new Request();
		request.header().put("Host", hostNameString);
		request.header().put("Connection", "keep-alive");
		
		return ByteBuffer.wrap(request.asBytes());
	}

	@Override
	public void onDisconnect(IoSession session) {
		Logger.simple("onDisconnect");
	}

	@Override
	public Object onReceive(IoSession session, Object obj) {
		Logger.simple("onRecive string "+obj.toString()+"["+session.remoteAddress()+":"+session.remotePort()+"]" +" "+obj.getClass().getName());
		
		try {
			Response response = HttpParser.parseResponse(new ByteArrayInputStream(((ByteBuffer)obj).array()));
			Logger.simple(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		session.close();
		return obj;
	}

	@Override
	public void onException(IoSession session, Exception e) {
		Logger.simple("Exception");
		e.printStackTrace();
	}

	@Override
	public void onSent(IoSession session, Object obj) {
		Logger.simple("onSent "+obj);
	}

}
