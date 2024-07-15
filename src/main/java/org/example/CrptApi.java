package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CrptApi - это потокобезопасный класс для работы с API Честного Знака.
 */
public class CrptApi {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Semaphore semaphore;
    private final ScheduledExecutorService scheduler;
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Создает новый экземпляр CrptApi.
     *
     * @param timeUnit единица измерения времени для ограничения запросов
     * @param requestLimit максимальное количество запросов, допустимое в заданном интервале
     */
    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.semaphore = new Semaphore(requestLimit);
        this.scheduler = Executors.newScheduledThreadPool(1);
        long interval = timeUnit.toMillis(1);
        scheduler.scheduleAtFixedRate(semaphore::release, interval, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * Создает документ для ввода в оборот товара, произведенного в Российской Федерации.
     *
     * @param document объект документа, который необходимо отправить
     * @param signature строка с подписью
     * @throws IOException если возникает ошибка ввода-вывода при отправке или получении данных
     * @throws InterruptedException если операция прерывается
     */
    public void createDocument(Document document, String signature) throws IOException, InterruptedException {
        semaphore.acquire();

        String json = objectMapper.writeValueAsString(document);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Не удалось создать документ: " + response.body());
        }
    }

    /**
     * Вспомогательный класс для представления документа.
     */
    public static class Document {
        public String description;
        public String doc_id;
        public String doc_status;
        public String doc_type;
        public boolean importRequest;
        public String owner_inn;
        public String participant_inn;
        public String producer_inn;
        public String production_date;
        public String production_type;
        public Product[] products;
        public String reg_date;
        public String reg_number;

        /**
         * Вспомогательный класс для представления продукта.
         */
        public static class Product {
            public String certificate_document;
            public String certificate_document_date;
            public String certificate_document_number;
            public String owner_inn;
            public String producer_inn;
            public String production_date;
            public String tnved_code;
            public String uit_code;
            public String uitu_code;
        }
    }

    /**
     * Завершает работу планировщика.
     */
    public void shutdown() {
        scheduler.shutdown();
    }
}