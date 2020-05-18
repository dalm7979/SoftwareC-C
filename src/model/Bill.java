package model;

public class Bill {
    private int code;
    private String product;
    private static int totalBills;
    private Credit credit;
    private Client client;

    public Bill(Client client, Credit credit, String motivo) {
        this.code  = totalBills;
        totalBills++;
        this.credit = credit;
        this.client = client;
        this.product = motivo;
    }

    public String getProduct() {
        return product;
    }

    public int getCode() {
        return code;
    }

    public Credit getCredit() {
        return credit;
    }
}
