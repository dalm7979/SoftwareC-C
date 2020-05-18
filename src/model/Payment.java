package model;

import java.time.LocalDate;

public class Payment {
    private LocalDate date;
    private String reason;
    private double payment;
    private static int localId;
    private int id;
    public Payment(String reason, double payment) {
        id = localId;
        localId++;
        this.date = LocalDate.now();
        this.reason = reason;
        this.payment = payment;
    }


    public double getPayment() {
        return payment;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public int getId() {
        return id;
    }
}
