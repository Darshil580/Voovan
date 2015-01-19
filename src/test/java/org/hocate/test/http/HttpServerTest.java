package org.hocate.test.http;

import java.io.IOException;
import java.util.Date;

import org.hocate.http.server.HttpServer;
import org.hocate.log.Logger;
import org.hocate.tools.TFile;

public class HttpServerTest {
	public static void main(String[] args) {
		try {
			// HttpServer httpServer = new
			// HttpServer("0.0.0.0",2080,100,"/Users/helyho/Downloads");
			HttpServer httpServer = HttpServer.newInstance();
			httpServer.get("/:name", (req, resp) -> {
				if (req.getSession() != null && req.getSession().getAttributes("Time") != null) {
					Logger.simple("Session saved time" + req.getSession().getAttributes("Time"));
				}
				Logger.simple(req.getRemoteAddres() + " " + req.getRemotePort());
				Logger.simple("QueryString:"+req.getQueryString());
				req.getSession().setAttribute("Time", new Date().toString());

				resp.write(TFile.loadResource("org/hocate/test/http/test.htm"));
				resp.write(req.getParameter("name"));
			});
			
			httpServer.get("/", (req, resp) -> {
				if (req.getSession() != null && req.getSession().getAttributes("Time") != null) {
					Logger.simple("Session saved time" + req.getSession().getAttributes("Time"));
				}
				Logger.simple(req.getRemoteAddres() + " " + req.getRemotePort());
				Logger.simple("QueryString:"+req.getQueryString());
				req.getSession().setAttribute("Time", new Date().toString());

				resp.write(TFile.loadResource("org/hocate/test/http/test.htm"));
			});
			
			// 重定向
			httpServer.get("/redirect", (req, resp) -> {
				resp.redirct("http://www.baidu.com");
			});

			httpServer.post("/", (req, resp) -> {
				if (req.getSession() != null && req.getSession().getAttributes("Time") != null) {
					Logger.simple("Session saved time" + req.getSession().getAttributes("Time"));
				}
				Logger.simple(req.getRemoteAddres() + " " + req.getRemotePort());
				Logger.simple(req.getQueryString());
				req.getSession().setAttribute("Time", new Date().toString());

				resp.write(TFile.loadResource("org/hocate/test/http/test.htm"));

			});
			httpServer.Serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
