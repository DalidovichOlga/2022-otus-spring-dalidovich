
С помощью docer compose запускается приложение из базы данных и двух микросервисов
бибилиотека и комментарии к книге, сборка осуществляется в контейнере.
                                                                      
---описание из предыдущего ДЗ
Комментарии к книге вынесены в отдельный микросервис. 
Для проверки существования при создании комментария и при выводе книги с комментариями через рест вызывается АПИ получения книги по идентификатору
сервис с комментариями использует порт 8090, и не использует авторизацию.


!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! сервис с комментариями:

добавить комментарий к книге (идентификатор книги, автор комментария, текст)
post /api/book/{bookId}/comments
пример:
http://localhost:8090/api/book/3/comments

тело запроса
{
"nick" : "постоянный читатель" ,
"commentText" : "очень интересная книга. но сильно много нудных диалогов. "
}

вывести список комментарий по идентификатору книги
get api/book/{bookId}/comments
пример:
http://localhost:8090/api/book/3/comments

удалить комментарий по идентификатору.
delete api/book/{bookId}/comments/{commentId}
http://localhost:8090/api/book/3/comments/3

изменить комментарий к книге
patch /api/book/{bookId}/comments/{commentId}
параметры commentText , nick
http://localhost:8090/api/book/3/comments/3?commentText=текст отзыва



!!!!!!!!!!!! сервис с книгами
в приложении 3 ПОЛЬЗОВАТЕЛЯ, хранящихся в базе данных - user , vasya и petya  у всех пароль 123456
petya имеет роль ROLE_ANONYMOUS и ему доступ запрещен, два других с ROLE_USER могут работать в системе
получить токен

http://localhost:8080/api/login

{
    "username":"vasya",
    "password":"123456"
}

далее АПИ можно вызывать, скопировав полученный токен в заголовок Authorization .

Описание команд:

Cоздать книгу
post api/books/
пример
http://localhost:8080/api/books/
тело запроса
{
"title": "Анна Каренина",
"author": "Толстой Л.Н.",
"genre": "Роман"
}
здесь и далее автор задается строкой фио полностью или, если автор уже есть в бибилиотеке, то можно передавать инициалами: Булгаков M.A.

изменить книгу
patch api/books/{bookId} 
параметры title, author, genre
http://localhost:8080/api/books/7?title=Поддельная Каренина&author=Достоевский Василий Петрович

список книг в библиотеке
http://localhost:8080/api/books

удалить книгу
http://localhost:8080/api/books/{bookId}

список книг по автору
get  api/books/author/{ФИО}
http://localhost:8080/api/books/author/Толстой Л.Н.


просмотреть список авторов:
get api/authors
http://localhost:8080/api/authors

Изменить имя автора
patch   /api/authors/{authorId}?
параметры firstName,  lastName , middleName
пример переименует Пушкина в Петра:
http://localhost:8080/api/authors/1?firstName=Петр

просмотреть спиок жанров:
get /api/genres
http://localhost:8080/api/genres


Переименовать жанр
patch  /api/genres/{ID}?genreName=новое название
http://localhost:8080/api/genres/1?genreName=Неопределен  


 



