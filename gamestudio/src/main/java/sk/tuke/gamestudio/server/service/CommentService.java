package sk.tuke.gamestudio.server.service;

import java.util.List;

import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.CommentException;

public interface CommentService {
	
	void addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;

}
