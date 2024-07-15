package org.example;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("CallToPrintStackTrace")
public class Main {
    public static void main(String[] args) {
        // Создаем экземпляр CrptApi с ограничением 1 запрос в секунду
        CrptApi api = new CrptApi(TimeUnit.SECONDS, 1);

        try {
            // Создаем документ для отправки
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

            // Вызываем метод создания документа
            api.createDocument(document, "signature");

            System.out.println("Документ успешно создан.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Завершаем работу CrptApi
            api.shutdown();
        }
    }
}










