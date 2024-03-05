package ru.otus.spring.booklib.service;

import org.springframework.beans.factory.annotation.Value;

import java.net.ConnectException;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Service
public class CommentServiceImpl implements CommentService {

    private RestOperations rest = new RestTemplate();

    @Value("${commentServer.host}")
    String commentServerAddress;

    @Override
    public void ClearComment(long bookId) {
        try {
            rest.delete(commentServerAddress + "/api/book/comment/" + bookId);

        } catch (Exception exception) {
            //ну недоступен и ладно..
            System.out.println(exception.getMessage());

        }


    }
}
