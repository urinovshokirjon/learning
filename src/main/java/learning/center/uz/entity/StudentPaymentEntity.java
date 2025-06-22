package learning.center.uz.entity;

import java.time.LocalDateTime;

public class StudentPaymentEntity {
    private String id;
    private String monthlyPaymentId;

    private LocalDateTime paymentDate; // pul to'langan sana
    private StudentPaymentType paymentType; // to'lov turi (naqd, karta orqali)
    private Long paymentAmount; // To'langan pul miqdori tiyin-da hisoblaymiz.

    // Student pul ni bir nechtaga bo'lib to'lashi mumkun.
    // deylik 800k ni bugun qolganini ertaga.
    // yoki 500k nazr qolganini plastik orqali.

    // Bu class faqat pul to'lagan payt yaratiladi.
}
