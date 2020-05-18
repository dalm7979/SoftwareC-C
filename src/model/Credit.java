package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Credit {
    private LocalDate start;
    private LocalDate end;
    private long totalCredit;
    private ArrayList<Payment> payments = new ArrayList<>();
    private final double IVA = 0.19;
    private int creditTerm;
    private long diffDates;

    public Credit(LocalDate start, LocalDate end, long totalCredit) {
        this.start = start;
        this.end = end;
        diffDates = ChronoUnit.DAYS.between(start, end);
        this.totalCredit = (long) ((totalCredit*IVA)+(totalCredit*interest())+totalCredit);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public boolean getCreditExpiration() {
        return diffDates<=5 && diffDates>0;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    boolean getMora() {
        long daysInMora = (ChronoUnit.DAYS.between(LocalDate.now(), end));

        if(daysInMora<0 && getTotalDebt()>0) {
            return true;
        } else {
            return false;
        }
    }


    public long getDebtPayment() {
        long totalPayments = 0;
        for (int i = 0; i<payments.size(); i++) {
            Payment payment = payments.get(i);
            totalPayments += payment.getPayment();
        }
        return totalPayments;
    }

    public long getTotalDebt() {
        long totalDebt = 0;
        for (int i = 0; i<payments.size(); i++) {
            Payment payment = payments.get(i);
            totalDebt += payment.getPayment();
        }
        return this.totalCredit - totalDebt;
    }

    public double interest() {

      if (diffDates >= 0 && diffDates <= 30) {
          creditTerm = 1;
            return 0.01;
        } else if (diffDates >= 31 && diffDates <= 60) {
          creditTerm = 2;
            return 0.05;
        } else if (diffDates >= 61 && diffDates <= 90) {
          creditTerm = 3;
            return 0.10;
        } else {
          creditTerm = 4;
            return 0.25;
        }

    }

    public int getCreditTerm() {
        return creditTerm;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public long getTotalCredit() {
        return totalCredit;
    }


}
