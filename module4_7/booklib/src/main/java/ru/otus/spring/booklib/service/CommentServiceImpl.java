package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Service
public class CommentServiceImpl implements CommentService {

    private RestOperations rest = new RestTemplate();

    @Value("${commentServer.host}")
    private String commentServerAddress;
    @Value(value = "${commentServer.port}")
    private String commentServerPort;

    @Override
    public void ClearComment(long bookId) {
        try {
            rest.delete(commentServerAddress + ":"+commentServerPort+ "/api/book/comment/" + bookId);
        } catch (Exception exception) {
            //ну недоступен и ладно..
            System.out.println(exception.getMessage());
        }
    }
}
