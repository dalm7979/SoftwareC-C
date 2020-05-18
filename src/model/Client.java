package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Client{
    private static int uniqueId;
    private int localId;
    private String fullName;
    private String id;
    private String address;
    private String phoneNumber;
    private LocalDate birthDate;
    private boolean isChargeable;
    private long borrowingCapacity;
    private ArrayList <Bill> bills = new ArrayList<>();
    private ArrayList <Credit> credits = new ArrayList<>();
    private boolean amIMoroso;

    public Client(String fullName, String id, String address, String phoneNumber, LocalDate birthDate, long borrowingCapacity) {
        this.localId = uniqueId;
        uniqueId++;
        this.fullName = fullName;
        this.id = id;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.borrowingCapacity = borrowingCapacity;
    }



    public int getLocalId() {
        return localId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public boolean isChargeable() {
        return isChargeable;
    }


    public ArrayList<Credit> getCredits() {
        return credits;
    }

    public long getBorrowingCapacity() {
        return borrowingCapacity;
    }

    public void addCredit(Credit credit){
        credits.add(credit);
    }

    public void addBill(Bill bill){
        bills.add(bill);
    }


    public void setChargeable(boolean chargeable) {
        isChargeable = chargeable;
    }


    public void setAmIMoroso(){
        for (int i = 0; i < credits.size(); i++) {
            Credit credit = credits.get(i);
            if(credit.getMora()) {
                amIMoroso = true;
                return;
            } else {
                amIMoroso = false;
            }
        }
    }

    public boolean isAmIMoroso() {
        setAmIMoroso();
        return amIMoroso;
    }

    public double getTotalCreditDebts() {
        double totalCreditDebts = 0;
        for (int i = 0; i<credits.size(); i++) {
            Credit credit = credits.get(i);
            totalCreditDebts += credit.getTotalDebt();
        }
        return totalCreditDebts;
    }

    public double getTotalCreditPayments() {
        double totalCreditPayment = 0;
        for (int i = 0; i<bills.size(); i++) {
            Credit credit = bills.get(i).getCredit();
            totalCreditPayment += credit.getTotalDebt();
        }
        return totalCreditPayment;
    }

    public double getTotalCreditAssigned() {
        return borrowingCapacity;
    }

    public boolean canHasCredit(double requestedValue){
        return requestedValue < (borrowingCapacity - getTotalCreditDebts());
    }



    public double getBorrowingTotalCapacity() {
        return borrowingCapacity - getTotalCreditDebts();
    }

    public ArrayList<Bill> getBills() {
        return bills;
    }
}