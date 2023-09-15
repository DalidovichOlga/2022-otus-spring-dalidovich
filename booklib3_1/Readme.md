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


добавить комментарий к книге (идентификатор книги, автор комментария, текст)
post /api/books/{bookId}/comments
пример:
/api/books/3/comments

тело запроса
{
"nick" : "постоянный читатель" ,
"commentText" : "очень интересная книга. но сильно много нудных диалогов. "
}

вывести список комментарий по идентификатору книги
get api/books/{bookId}/comments
пример:
http://localhost:8080/api/books/3/comments

удалить комментарий по номеру.
delete api/books/{bookId}/comments/{number}
http://localhost:8080/api/books/3/comments/3

изменить комментарий к книге
patch /api/books/{bookId}/comments/{номер}
параметры commentText , nick
http://localhost:8080/api/books/3/comments/3?commentText=sdfkl asjdf asdlkjf alsdfjk


 



