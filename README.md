# honest_sign_api_java_17

- [О проекте](#begining)
  - [Стек](#1)
  - [Сделано](#2)
  - [Объяснение тестов](#3)
- [Удовлетворение условиям](#4)
- [Скрипты](#5)
  - [Запуск билда](#6)
  - [Запуск тестов](#7)

<a name="begining"><h1>О проекте</h1></a>

> [!WARNING]  
> Это лишь пример обращения к API - на данный момент не работает, т.к. нет точной сигнатуры, например, Access-token'а.

<a name="1"><h2>Стек</h2></a>

- Java 17.
- Библиотека HTTP-клиента для выполнения запросов `java.net.http.HttpClient`.
- Библиотека для сериализации JSON `com.fasterxml.jackson.databind.ObjectMapper`.
- JavaDoc для документации кода.

<a name="2"><h2>Сделано</h2></a>

- Реализован на Java 17 thread-safe класс `CrptApi.java` для работы с API.
- Реализован метод `createDocument` – Создание документа для ввода в оборот товара, произведенного в РФ.
- Написан тест `CrptApiTest`:

<a name="3"><h2>Объяснение тестов</h2></a>

1. **setUp и tearDown**: Подготавливаем и завершаем работу объекта `CrptApi` перед каждым тестом.
2. **testCreateDocument**: Проверка создания документа с корректными данными.
3. **testRateLimit**: Проверка ограничения запросов, второй запрос должен блокироваться.

Документ и подпись передаются в метод в виде Java-объекта и строки соответственно:

```java
    /**
     * Создает документ для ввода в оборот товара, произведенного в Российской Федерации.
     *
     * @param document объект документа, который необходимо отправить
     * @param signature строка с подписью
     * @throws IOException если возникает ошибка ввода-вывода при отправке или получении данных
     * @throws InterruptedException если операция прерывается
     */
    public void createDocument(Document document, String signature) throws IOException, InterruptedException {
       // ...
    }
```

<a name="4"><h1>Удовлетворение условиям</h1></a>
Данный проект удовлетворяет следующим условиям:

1. **Блокирование при превышении лимита запросов**: Используется `Semaphore` для ограничения количества одновременных вызовов к API. Метод `acquire()` вызывается перед отправкой запроса, что блокирует выполнение, если лимит запросов достигнут.

2. **Продолжение выполнения без выбрасывания исключения**: В случае, если вызов `acquire()` блокируется из-за превышения лимита запросов, метод `createDocument()` не выбрасывает исключение, а просто блокирует выполнение до тех пор, пока не будет доступно разрешение (`permit`) в семафоре.

3. **Не превышать лимит на количество запросов**: Метод `createDocument()` гарантирует, что никогда не будет отправлено больше запросов, чем разрешенный лимит, указанный при создании экземпляра `CrptApi`.

Таким образом, решение эффективно управляет лимитом запросов к API, предотвращает его превышение и обеспечивает корректное поведение в случае блокировки вызова из-за ограничения.

<a name="5"><h1>Скрипты</h1></a>

<a name="6"><h2>Запуск билда</h2></a>

`./gradlew build`

<a name="7"><h2>Запуск тестов</h2></a>

`./gradlew test`
