package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.*;

/**
 * CrptApiTest - класс для тестирования CrptApi.
 */
public class CrptApiTest {
    private CrptApi api;

    /**
     * Подготавливает объект CrptApi перед каждым тестом.
     */
    @BeforeEach
    public void setUp() {
        api = new CrptApi(TimeUnit.SECONDS, 1); // 1 запрос в секунду для тестирования
    }

    /**
     * Завершает работу объекта CrptApi после каждого теста.
     */
    @AfterEach
    public void tearDown() {
        api.shutdown();
    }

    /**
     * Тестирует создание документа с корректными данными.
     *
     * @throws IOException          если возникает ошибка ввода-вывода при отправке или получении данных
     * @throws InterruptedException если операция прерывается
     */
    @Test
    public void testCreateDocument() throws IOException, InterruptedException {
        CrptApi.Document document = new CrptApi.Document();
        document.doc_id = "test_doc";
        document.doc_status = "test_status";
        document.doc_type = "LP_INTRODUCE_GOODS";
        document.importRequest = true;
        document.owner_inn = "1234567890";
        document.participant_inn = "1234567890";
        document.producer_inn = "1234567890";
        document.production_date = "2020-01-23";
        document.production_type = "type";
        document.products = new CrptApi.Document.Product[1];
        document.products[0] = new CrptApi.Document.Product();
        document.products[0].certificate_document = "doc";
        document.products[0].certificate_document_date = "2020-01-23";
        document.products[0].certificate_document_number = "number";
        document.products[0].owner_inn = "1234567890";
        document.products[0].producer_inn = "1234567890";
        document.products[0].production_date = "2020-01-23";
        document.products[0].tnved_code = "code";
        document.products[0].uit_code = "uit";
        document.products[0].uitu_code = "uitu";

        String signature = "signature";

        api.createDocument(document, signature);
    }

    /**
     * Тестирует ограничение количества запросов.
     *
     * @throws IOException          если возникает ошибка ввода-вывода при отправке или получении данных
     * @throws InterruptedException если операция прерывается
     */
    @Test
    public void testRateLimit() throws IOException, InterruptedException {
        CrptApi.Document document = new CrptApi.Document();
        String signature = "signature";

        // Первый запрос должен быть успешным
        api.createDocument(document, signature);

        // Второй запрос должен блокироваться и не выбрасывать исключение, но в конечном итоге быть успешным
        assertThrows(TimeoutException.class, () -> {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(() -> {
                try {
                    api.createDocument(document, signature);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            future.get(500, TimeUnit.MILLISECONDS); // Таймаут для тестирования
        });
    }
}
