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

    @Value(value = "${commentsrv.port}")
    private String port;

    @Override
    public BookDto getById(Long id) throws LibraryError {

        int autorized = 2;

        while (autorized > 0) {
            if (token.isEmpty())
                getToken();
            System.out.println("Token =="+token);
            System.out.println("host =="+host+":"+port + "/api/books/" + id);


            RequestEntity<Void> requestEntity = RequestEntity.get(host+":"+port + "/api/books/" + id)
                    .header("contextType", "application/json")
                    .header("Authorization", token)
                    .build();
            try {

                ResponseEntity<BookDto> responseEntity = rest.exchange(requestEntity,
                        BookDto.class);

                System.out.println(responseEntity.getStatusCode());
                if (responseEntity.getStatusCode() == HttpStatus.OK)
                    return responseEntity.getBody();

            } catch (HttpClientErrorException exception) {

                System.out.println("Exception = " + exception.getStatusCode());

                if (exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                    token = "";
                    autorized--;
                } else
                    throw new LibraryError("BOOK_NOT_FOUND", "id =" + String.valueOf(id));
            }
            catch (Throwable throwable){
                System.out.println(throwable);
            }
        }
        return null;
    }

    private void getToken() {
        UserDto userDto = new UserDto(userName, password);
        System.out.println("Before token ==");
        System.out.println("host =="+host+":"+port+"/api/login");

        token = rest.postForObject(host+":"+port+"/api/login", userDto, String.class);
    }

}
