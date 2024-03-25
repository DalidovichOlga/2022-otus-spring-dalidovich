Описание

Запуск осуществляется docker-compose up. kafka также поднимается в контейнере. 

messagesource  , используя Spring integration, каждую минуту отправляет сообщение в топик кафки из настройки.
Также имеет АПИ,  которое также отправляет сообщение 
POST http://localhost:8080/api/message
{
    "messageText" : "ЗДЕСЬ ТЕКСТ СООБЩЕНИЯ"
}

kafkareceiver слушает топик из настройки (НЕ использует Spring integration) и сохраняет полученные сообщения. 
Просмотреть полученные сообщения можно вызовом апи

http://localhost:8090/api/messages

