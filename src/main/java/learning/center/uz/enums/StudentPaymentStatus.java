package learning.center.uz.enums;

public enum StudentPaymentStatus {
    PAYED,  // to'langan
    DEBTOR, // qarzdor (student to'lashi kerak bo'lgan oy uchun hali to'lamaganini bildiradi.)
    DEFAULT, // to'lanmagan. (student hali bu oyni o'qishni boshlamadi. Detaul holatda StudentPaymentStatus uchun ishlatiladi.)
}
