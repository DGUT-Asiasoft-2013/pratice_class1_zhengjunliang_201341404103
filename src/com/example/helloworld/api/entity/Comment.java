package com.example.helloworld.api.entity;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{
	Integer id;
	Date createDate;
	Date editDate;
	User author;
	String text;
	Article article;
	
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}


}
