package ru.otus.spring.commentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private RestOperations rest = new RestTemplate();
    private String token = "";

    @Value("${commentsrv.userName:texuser}")
    private String userName;

    @Value("${commentsrv.password:123456}")
    private String password;

    @Value("${commentsrv.host:http://localhost}")
    private String host;

    @Value(value = "${commentsrv.port:8080}")
    private String port;

    @Override
    public BookDto getById(Long id) throws LibraryError {

        //вдруг токен устарел, 2 попытки.
        int autorized = 2;

        ResponseEntity<BookDto> responseEntity ;
        RequestEntity<Void> requestEntity ;

        while (autorized > 0) {
            //получить токен
            if (token.isEmpty())
                getToken();

            //формирование запроса с токеном
            requestEntity = RequestEntity.get(host + ":" + port + "/api/books/" + id)
                    .header("contextType", "application/json")
                    .header("Authorization", token)
                    .build();


            logger.info("Запрос информации о книге по адресу {}:{}/api/books/{}", host, port, id);
            try {
                // вызов АПИ через rest из предыдущего урока
                responseEntity = rest.exchange(requestEntity,BookDto.class);

                if (responseEntity.getStatusCode() == HttpStatus.OK)
                    return responseEntity.getBody();

            } catch (HttpClientErrorException exception) {
                logger.error("Не удалось получить информацию , код статуса = {}", exception.getStatusCode());

                // токен уже невалиден
                if (exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                    token = "";
                    autorized--;
                } else
                    throw new LibraryError("book_not_found", "id =" + String.valueOf(id));
            } catch (Throwable throwable) {
                logger.error(String.valueOf(throwable));
            }
        }
        throw new LibraryError("book_not_found", "id =" + String.valueOf(id));
    }

    private void getToken() {
        UserDto userDto = new UserDto(userName, password);
        logger.info("Запрос токена по адресу {}:{}/api/login", host, port);
        token = rest.postForObject(host + ":" + port + "/api/login", userDto, String.class);
    }

}
