package ru.otus.spring.commentservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import ru.otus.spring.commentservice.dto.BookDto;
import ru.otus.spring.commentservice.dto.UserDto;
import ru.otus.spring.commentservice.error.LibraryError;

@Service
public class BookServiceImpl implements BookService {

    private RestOperations rest = new RestTemplate();
    private String token = "";

    @Value("${commentsrv.userName}")
    private String userName;

    @Value("${commentsrv.password}")
    private String password;

    @Value("${commentsrv.host}")
    private String host;

    @Override
    public BookDto getById(Long id) throws LibraryError {

        int autorized = 2;

        while (autorized > 0) {
            if (token.isEmpty())
                getToken();

            RequestEntity<Void> requestEntity = RequestEntity.get(host + "/api/books/" + id)
                    .header("contextType", "application/json")
                    .header("Authorization", token)
                    .build();
            try {

                ResponseEntity<BookDto> responseEntity = rest.exchange(requestEntity,
                        BookDto.class);

                if (responseEntity.getStatusCode() == HttpStatus.OK)
                    return responseEntity.getBody();

            } catch (HttpClientErrorException exception) {
                if (exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                    token = "";
                    autorized--;
                } else
                    throw new LibraryError("BOOK_NOT_FOUND", "id =" + String.valueOf(id));
            }
        }
        return null;
    }

    private void getToken() {
        UserDto userDto = new UserDto(userName, password);

        token = rest.postForObject(host+"/api/login", userDto, String.class);
    }

}
