package learning.center.uz.entity;

import learning.center.uz.enums.StudentPaymentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudentMonthlyPaymentEntity {
    private String id;
    private String studentId; // entity also
    private String groupId;
    private Integer paymentMonthNumber; // to'lov kursni nechanchi oyi uchun ekanligi. Masalan 1chi oy uchun to'lov,
    private LocalDate paymentRequiredDate; // qachon pul to'lashi kerak edi.

    private Boolean paymentRequired; // Student shu oy uchun pul to'laydimi yo'qmi bildiradi. default holatda true.

    private String profileId; // Payment-ni to'landi degan odamni ID-si (ishchini id-si)

    private Long debtorAmount; // Qarzdorlik miqdori. To'lash kerak bo'lgan pul miqdori tiyin-da hisoblaymiz.
    private StudentPaymentStatus spStatus; // DEFAULT - default holatda. PAYED - bo'ladi agar pulni to'liq to'lasa to'lasa.

    private String description; // qo'shimcha ma'lumot.
    private String companyId; // firmani nomi.

    // student gruppaga qo'shilgan payt StudentMonthlyPaymentEntity to'liq yaratiladi.
    // Ya'ni Deylik gruppa 4 oylik bo'lsa,  shu student va shu gruppa uchun 4ta (agar u 1chi oydan qo'shilgan bo'lsa)
    //  StudentMonthlyPaymentEntity oldindan yaratiladi.
    // Unda  pul to'lashi kerak bo'lgan sanalari bilan yoziladi, va courseMonth 1,2,3,4 bo'ladi.
    // spStatus birinchi oy uchun DEBTOR (qarzdor) bo'ladi. Hali boshlanmagan oy uchun DEFAULT bo'ladi.
    // Agar student deylik 8-darsdan qo'shilsa o'zi o'qigan kunlar uchun pul to'laydi

    // Demak Student Gruppaga qo'shilganda u nechanchi darsdan qo'shilganini hisoblash kerak ekan va
    //  U shu oy uchun nechi pul to'lashligini hisoblash kerak.
    // bu summani finance-chi odam o'zgartira olishi mumkun bo'lishi kerak.

    // Bazan Student gruppaga 1chi oydan qo'shilgan, ammo u 3chi oydan pul to'lashni boshlaydi.
    // Bunda kelishilgan tarzda bo'ladi. Buni ham finance-chi o'zgartira olishi mumkun.
    // paymentRequired = false qilinadi. Bu degani shu student shu oy uchun pul to'lamaydi degani.

    // Student pul ni bir nechtaga bo'lib to'lashi mumkun.
    // deylik 800k ni bugun qolganini ertaga.
    // yoki 500k naqt qolganini plastik orqali.

    // Kurs tugamasdan student gruppadan chiqsa ortiqcha yaratilgan StudentMonthlyPaymentEntity-lar o'chiriladi.

}
