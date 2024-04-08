package ru.otus.spring.commentservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private RestOperations rest = new RestTemplate();
    private String token = "";

    @Autowired
    private BookServiceProxy proxy;

    @Value("${commentsrv.userName:texuser}")
    private String userName;

    @Value("${commentsrv.password:123456}")
    private String password;

    private BookDto defaultBookFallback(Long id) {
        return new BookDto(id, "None", "None", "None");
    }

    @Override
    @HystrixCommand(fallbackMethod = "defaultBookFallback", commandProperties = {

            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
    })
    public BookDto getById(Long id) throws LibraryError {

        //вдруг токен устарел, 2 попытки.
        int autorized = 2;

        while (autorized > 0) {
            //получить токен
            if (token.isEmpty())
                getToken();

            try {
                BookDto bookDto = proxy.getBookById(token, id);

                logger.info("Запрос информации о книге {}", bookDto);
                return bookDto;

            } catch (HttpClientErrorException exception) {
                logger.error("Не удалось получить информацию , код статуса = {}", exception.getStatusCode());

                // токен уже невалиден? запроим еще раз
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
        logger.info("Запрос токена по адресу {}    /api/login", userDto);
        token = proxy.getToken(userDto);
    }

}
