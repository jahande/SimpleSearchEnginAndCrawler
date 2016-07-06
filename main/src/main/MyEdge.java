package main;

import models.WebPage;

public class MyEdge {
	private  WebPage from;
	private  WebPage to;
	public WebPage getFrom() {
		return from;
	}
	public void setFrom(WebPage from) {
		this.from = from;
	}
	public WebPage getTo() {
		return to;
	}
	public void setTo(WebPage to) {
		this.to = to;
	}
	public MyEdge(WebPage from, WebPage to) {
		super();
		this.from = from;
		this.to = to;
	}
	
}
