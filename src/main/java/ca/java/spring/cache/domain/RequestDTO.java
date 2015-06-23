/**
 * 
 */
package ca.java.spring.cache.domain;

import java.util.List;

/**
 * The request DTO for all the AJAX call using JSON.
 * @author Srinivas Rao
 *
 */
public class RequestDTO {

	private String id;
	private String method;
	private List<Object> params;
	private String jsonrpc;
	
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
	
}
