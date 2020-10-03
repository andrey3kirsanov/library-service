#Веб-сервис “Книжная полка”

##Краткое описание сервиса
Пользователи могут загружать электронные книги, тем самым составлять из них книжные полки. Могу читать книги напрямую с сервиса с сохранением прогресса чтения. Так же пользователи могут просматривать содержимое книжных полок других пользователей и при желании могут запросить доступ для чтения понравившейся книги. Пользователь может предоставить временный или постоянный доступ к книге, но скачивать книги запрещено.

1. Регистрация пользователей. При регистрации необходима капча. После регистрации на указанную почту приходит письмо для подтверждения аккаунта. Ссылка в письме активна в течение суток.

2. Загрузка и группировка книг по полкам. Каждая полка имеет свое название. Ввод краткой информации о книге.

3. После загрузки книги, она преобразуется в удобный формат для последующего постраничного вывода на экран пользователю (для упрощения можно использовать HTML). Конвертацию реализовать в виде отложных задач или выполнения в отдельном потоке, чтобы пользователь не ждал пока книга преобразуется. Доступ к книге появляется после преобразования. Можно использовать либо библиотеки либо сторонние сервисы, на вкус и цвет.

4. Ведение полок. Создание, удаление и перемещение книг между полками.

5. Делиться книгами с другими пользователями. Для этого пользователь должен отправить запрос владельцу книги, возможно наличие какого-то текстового комментария. Владелец получает это уведомление и разрешает на временной или постоянной основе или не разрешает (мотивированно). В случае успеха, пользователю, который делал запрос, книга становится доступной. В случае если разрешение было временным по истечении срока доступ закрывается.

6. Чтение выбранной книги и сохранение прогресса, чтобы при последующих загрузках пользователю открывалась страница на которой он остановился.

7. Опционально. Сделать закладки. Перемещение по закладкам.