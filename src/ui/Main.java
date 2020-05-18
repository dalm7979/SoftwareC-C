package ui;
import model.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private Company company;
    private boolean exit;

    private Main() {
        company = new Company("Ratamax software S.A ", "123456789,", 0);
        init();
    }

    public static void main(final String[] args) {
      new Main();
    }

    private void init() {
        System.out.println("\n\nBIENVENIDO AL MEJOR PROGRAMA PARA LLEVAR SUS CUENTAS POR COBRAR \n");
        System.out.println("********************************************************************");
        System.out.println("********************* Ratamax software S.A ************************");
        System.out.println("********************************************************************\n\n");
        menu();
        if (!exit) {
            init();
        }
    }

    private void menu() {
        System.out.println("INGRESE UNA OPCION: \n\n " +
                "(1) Registrar cliente.\n " +
                "(2) Buscar cliente por documento de identidad.\n " +
                "(3) Buscar factura por intervalo de fecha.\n " +
                "(4) Consultar factura por rango de dias\n " +
                "(5) Registrar pago o abonos del cliente.\n " +
                "(6) Consultar total de cobros pendientes.\n " +
                "(7) Consultar total de pagos recibidos.\n " +
                "(8) Clientes en mora\n " +
                "(9) Clientes proximos a cobrar.\n " +
                "(10) Salir.");
        int optionSelected = scanner.nextInt();
        scanner.nextLine();
        switch (optionSelected) {
            case 1:
                addClient();
                break;
            case 2:
                searchClientById();
                break;

            case 3:
                searchBillByDate();
                break;

            case 4:
                requestBillsByDaysRange();
                break;
            case 5:
                registerPayments();

                break;
            case 6:
                requestChargesPending();
                break;
            case 7:
                requestReceivedPayments();
                break;
            case 8:
                moraClients();
                break;
            case 9:
                creditsCloseToExpire();
                break;
            case 10:
                exit = true;
                System.out.println("-----------------------gracias por utilizar nuestros servicios-------------------------------");
                break;
            default:
                System.out.println("\nLA OPCION " + optionSelected + " NO EXISTE EN EL MENU");
                menu();
        }
    }


    private void addClient() {
        System.out.println("PRIMERO COMPRUEBE SI EL CLIENTE EXISTE");
        Client clientById = searchClientById();
        if (clientById == null) {
        System.out.println("INGRESE LOS DATOS DEL CLIENTE");
        String fullName = stringScanner("Nombre completo o razon social:");
        String id = stringScanner("Numero de identificacion:");
        String address = stringScanner("Direccion:");
        String phoneNumber = stringScanner("Numero de telefono:");
        LocalDate birthDate = date("Fecha de nacimiento");
        int borrowingCapacity = intScanner("Capacidad de endeudamiento");
        Client client = new Client(fullName, id, address, phoneNumber, birthDate, borrowingCapacity);
        addCredit(client, false);
        } else {
            System.out.println("Este cliente ya existe y su cupo es de: " + clientById.getBorrowingTotalCapacity());
            if(clientById.getBorrowingTotalCapacity()==0) {
                System.out.println("Este cliente no puede recibir mas creditos");
            } else {
                addCredit(clientById, true);
            }
        }
    }

    private void addCredit(Client client, boolean clientExist) {
        System.out.println("\nINGRESE LOS DATOS DEL CREDITO \n");
        LocalDate startCredit = LocalDate.now();
        LocalDate endCredit = date("Fecha de fin del credito");
        int requestedValue = intScanner("Valor del credito");
        Credit credit = new Credit(startCredit, endCredit, requestedValue);
        if(credit.getTotalDebt() > client.getBorrowingTotalCapacity()) {
            System.out.println("El valor del credito solicitado excede el limite de este cliente");
            addCredit(client, clientExist);
       } else {
            client.addCredit(credit);
            generateCreditBill(client, credit, clientExist);
        }

    }

    private void generateCreditBill(Client client, Credit credit, boolean clientExist) {
        String reason = stringScanner("Razon o titulo de la factura");
        Bill bill = new Bill(client, credit, reason);
        client.addBill(bill);
        if(!clientExist) {
            addDataToCompany(client);
        }
        System.out.println("\nCliente registrado y factura generada");
    }

    private void addDataToCompany(Client client) {
        company.addClient(client);
    }

    private LocalDate date(String message) {
        try {
            String dateUnformatted = stringScanner(message + " con el formato YYYY-MM-DD");
            return LocalDate.parse(dateUnformatted);
        } catch (Exception e) {
            System.out.println("Formato de fecha incorrecto");
            date(message);
            return null;
        }
    }


    private Client searchClientById() {
        System.out.println("\nBUSCAR CLIENTE POR IDENTIFICACION\n");
        String id = stringScanner("Ingrese el numero de identificacion");

        for (int i = 0; i < company.getClients().size(); i++) {
            Client client = company.getClients().get(i);
            if (client.getId().equals(id)) {
                printClient(client);
                return client;
            }
        }

        System.out.println("Este cliente no existe");
        return null;
    }

    private void searchBillByDate() {
        System.out.println("\nBUSCAR FACTURA POR FECHA\n");
        LocalDate startDate = date("Fecha inicial");
        LocalDate endDate = date("Fecha final");
        for (int i = 0; i < company.getClients().size(); i++) {
            Client client = company.getClients().get(i);
            for (int j = 0; j < client.getBills().size(); j++) {
                Bill bill = client.getBills().get(j);
                Credit credit = bill.getCredit();
                LocalDate date = credit.getStart();
                if ((startDate.isBefore(date) || startDate.isEqual(date)) && (endDate.isAfter(date) || endDate.isBefore(date))) {
                    printClient(client);
                    printBill(bill);
                } else {
                    System.out.println("Esta factura no esta dentro del rango");
                }
            }
        }
    }


    private void printClient(Client client) {
        System.out.println("\n");
        System.out.println("Nombre completo o razon social: " + client.getFullName());
        System.out.println("Numero de identificacion: " + client.getId());
        System.out.println("Direccion: " + client.getAddress());
        System.out.println("Numero de telefono: " + client.getId());
        System.out.println("Fecha de nacimiento: " + client.getBirthDate());
        System.out.println("Capacidad de endeudamiento: " + client.getBorrowingTotalCapacity());
    }

    private void printBill(Bill bill) {
        System.out.println("\n");
        System.out.println("Numero de factura: " + bill.getCode());
        System.out.println("Producto: " + bill.getProduct());
        System.out.println("Fecha limite de pago: " + bill.getCredit().getEnd());
        System.out.println("Producto: " + bill.getProduct());
        System.out.println("Monto de la deuda: " + bill.getCredit().getTotalCredit());
        System.out.println("Abonos o pagos a la deuda: " + bill.getCredit().getDebtPayment());
        System.out.println("Deuda total: " + bill.getCredit().getTotalDebt());
    }

    private void creditsCloseToExpire() {
        boolean existCreditsToExpire = false;
        for (int i = 0; i < company.getClients().size(); i++) {
            Client client = company.getClients().get(i);
            for (int j = 0; j < client.getBills().size(); j++) {
                Bill bill = client.getBills().get(j);
                if (bill.getCredit().getCreditExpiration()) {
                    existCreditsToExpire = true;
                    printClient(client);
                    printBill(bill);
                }
            }
        }
        if(!existCreditsToExpire) {
            System.out.println("No existen creditos para expirar");
        }
    }

    private void registerPayments() {
        Client client = searchClientById();
        System.out.println("\nREGISTRAR PAGOS O ABONOS DE" + client.getFullName().toUpperCase() + "\n");

        for (int i = 0; i < client.getBills().size(); i++) {
            Bill bill = client.getBills().get(i);
            System.out.println("(" + i + ") " + bill.getProduct() + ".");
            System.out.println("Deuda inicial: " + bill.getCredit().getTotalCredit() + ".");
            System.out.println("Deuda total con abonos: " + bill.getCredit().getTotalDebt() + ".");
        }

        int numberOfBill = intScanner("DE LA LISTA ANTERIOR, SELECCIONE EL NUMERO DE LA FACTURA");
        String reason = stringScanner("Razon del pago");
        int value = intScanner("Valor de pago");
        Credit credit = client.getBills().get(numberOfBill).getCredit();
        Payment payment = new Payment(reason, value);
        System.out.println("Pago realizado con exito");
        credit.addPayment(payment);
        System.out.println("La deuda inicial era: " + credit.getTotalCredit());
        System.out.println("La deuda actual de esta factura queda en: " + credit.getTotalDebt());
        if (credit.getTotalDebt() < 0) {
            System.out.println("Tiene un saldo a favor de: " + credit.getTotalDebt() * -1);
        }
    }

    private void requestBillsByDaysRange() {
        int option = intScanner("Seleccione un numero para filtrar por rango \n" +
                "(1) De 0 a 30 dias\n" +
                "(2) De 31 a 60 dias\n" +
                "(3) De 61 a 90 dias \n" +
                "(4) Mas de 90 dias");
        for (int i = 0; i < company.getClients().size(); i++) {
            Client client = company.getClients().get(i);
            for (int j = 0; j < client.getBills().size(); j++) {
                Bill bill = client.getBills().get(j);
                if (bill.getCredit().getCreditTerm() == option) {
                    printBill(bill);
                }
            }
        }
    }


    private void moraClients() {
        System.out.println("\nCLIENTES MOROSOS\n");
        boolean existClientsMorosos = false;
        for (int i = 0; i < company.getClients().size(); i++) {
            Client client = company.getClients().get(i);
            if (client.isAmIMoroso()) {
                existClientsMorosos = true;
                printClient(client);
            }
        }

        if(!existClientsMorosos) {
            System.out.println("No existen clientes morosos");
        }
    }

    private void requestChargesPending(){
        System.out.println("\nLISTA DE COBROS PENDIENTES\n");
        double clientsTotalDebt = 0;
        for (int i = 0; i < company.getClients().size(); i++) {
            Client client = company.getClients().get(i);
            if(client.getBills().size()>0) {
                printClient(client);
                for (int j = 0; j < client.getBills().size(); j++) {
                    Bill bill = client.getBills().get(j);
                    printBill(bill);
                }
                clientsTotalDebt += client.getTotalCreditDebts();
            }
        }
        System.out.println("\nEl total de cobros pendientes es: " + clientsTotalDebt);
    }



    private void requestReceivedPayments() {
        System.out.println("\nLISTA DE PAGOS RECIBIDOS\n");
        double clientsTotalPayments = 0;
        for (int i = 0; i < company.getClients().size(); i++) {
            Client client = company.getClients().get(i);
            for (int j =0; j<client.getCredits().size(); j++) {
                Credit credit = client.getCredits().get(j);
                for(int k = 0; k<credit.getPayments().size(); k++) {
                    System.out.println("\nLISTA DE PAGOS RECIBIDOS\n");
                    Payment payment = credit.getPayments().get(k);
                    System.out.println("Numero de pago: "+ payment.getId());
                    System.out.println("Nombre del cliente: "+ client.getFullName());
                    System.out.println("Descripcion del pago: "+payment.getReason());
                    System.out.println("Fecha del pago: "+payment.getDate());
                    System.out.println("Valor del pago: "+payment.getPayment());
                    clientsTotalPayments += payment.getPayment();
                }
            }

        }
        System.out.println("El total de pagos recibidos es: " + clientsTotalPayments);
    }

    private String stringScanner(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    private int intScanner(String message) {
        System.out.println(message);
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

}