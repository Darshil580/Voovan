package org.voovan.http.monitor;

import org.voovan.http.server.HttpFilter;
import org.voovan.http.server.HttpRequest;
import org.voovan.http.server.HttpResponse;
import org.voovan.http.server.context.HttpFilterConfig;
import org.voovan.tools.TEnv;

import java.util.HashMap;
import java.util.Map;

/**
 * 监控用过滤器
 */
public class HttpMonitorFilter implements HttpFilter {
	private static Map<String,RequestAnalysis> requestInfos= new HashMap<String,RequestAnalysis>();

	/**
	 * 获取请求分析对象
	 * @return 返回的请求信息
     */
	public static Map<String, RequestAnalysis> getRequestInfos() {
		return requestInfos;
	}

	@Override
	public Object onRequest(HttpFilterConfig filterConfig, HttpRequest request, HttpResponse response, Object prevFilterResult ) {
		request.getSession().setAttribute("VOOVAN_REQSTART",System.currentTimeMillis());
		TEnv.sleep(20);
		return prevFilterResult;
	}

	@Override
	public Object onResponse(HttpFilterConfig filterConfig, HttpRequest request, HttpResponse response, Object prevFilterResult ) {
		Long startTime = (Long)request.getSession().getAttributes("VOOVAN_REQSTART");
		if(startTime!=null) {
			long dealTime = (System.currentTimeMillis() - startTime);
			String path = request.protocol().getPath();
			if (requestInfos.containsKey(path)) {
				requestInfos.get(path).add(dealTime);
			} else {
				RequestAnalysis requestAnalysis = new RequestAnalysis(path);
				requestAnalysis.add(dealTime);
				requestInfos.put(path, requestAnalysis);
			}
		}
		return prevFilterResult;
	}
}
