import java.util.*;

public class p2 {
    public static void screenOne() {
        Scanner sc = new Scanner(System.in);
        boolean screen = true;
        while (screen) {
            System.out.println("==Welcome to the Self Services Banking System! - Main Menu==");
            System.out.println("1. New Customer");
            System.out.println("2. Customer Login");
            System.out.println("3. Exit");
            String num = sc.nextLine();
            switch (num) {
                case "1":
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter your gender: ");
                    String gender = sc.nextLine();
                    System.out.print("Enter your age: ");
                    String age = sc.nextLine();
                    System.out.print("Enter your pin: ");
                    String pin = sc.nextLine();
                    BankingSystem.newCustomer(name, gender, age, pin);
                    break;
                case "2":
                    System.out.print("Enter your ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter your pin: ");
                    String pass = sc.nextLine();
                    if(id == 0 && pass.equals("0")) {
                        screenFour();
                    } 
                    else {
                        BankingSystem.login(id, pass);
                    }
                    break;
                case "3":
                    screen = false;
                    break;
                default:
                    System.out.println("Please only choose option 1, 2 or 3");
            }
        }
    }

    public static void screenThree(String cusID) {
        Scanner sc = new Scanner(System.in);
        boolean screen = true;
        while (screen) {
            System.out.println("==Customer " + cusID + " Main Menu==");
            System.out.println("1. Open Account");
            System.out.println("2. Close Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. Account Summary");
            System.out.println("7. Exit");
            String num = sc.nextLine();
            String n, amount;
            switch (num) {
                case "1":
                    System.out.print("Enter customer ID: ");
                    String id = sc.nextLine();
                    System.out.print("Enter account type (C/S): ");
                    String type = sc.nextLine();
                    System.out.print("Enter balance (initial deposit): $");
                    amount = sc.nextLine();
                    BankingSystem.openAccount(id, type, amount);
                    break;
                case "2":
                    System.out.print("Enter account number: ");
                    n = sc.nextLine();
                    BankingSystem.closeAccount(n);
                    break;
                case "3":
                    System.out.print("Enter account number: ");
                    n = sc.nextLine();
                    System.out.print("Enter deposit amount: $");
                    amount = sc.nextLine();
                    BankingSystem.deposit(n, amount);
                    break;
                case "4":
                    System.out.print("Enter account number: ");
                    n = sc.nextLine();
                    System.out.print("Enter withdraw amount: $");
                    amount = sc.nextLine();
                    BankingSystem.withdraw(n, amount);
                    break;
                case "5":
                    System.out.print("Enter source account number: ");
                    String srcNum = sc.nextLine();
                    System.out.print("Enter destination account number: ");
                    String destNum = sc.nextLine();
                    System.out.print("Enter amount: $");
                    amount = sc.nextLine();
                    BankingSystem.transfer(srcNum, destNum, amount);
                    break;
                case "6":
                    BankingSystem.accountSummary(cusID);
                    break;
                case "7":
                    screen = false;
                    break;
                default:
                    System.out.println("Please only choose option 1-7");
            }
        }
    }

    public static void screenFour() {
        Scanner sc = new Scanner(System.in);
        boolean screen = true;
        while (screen) {
            System.out.println("==Administrator Main Menu==");
            System.out.println("1. Account Summary for a Customer");
            System.out.println("2. Report A :: Customer Information with Total Balance in Decreasing Order");
            System.out.println("3. Report B :: Find the Average Total Balance Between Age Groups");
            System.out.println("4. Assign saving rate and checking rate to all the account");
            System.out.println("5. Exit");
            String num = sc.nextLine();
            switch (num) {
                case "1":
                    System.out.print("Enter an ID: ");
                    String id = sc.nextLine();
                    BankingSystem.accountSummary(id);
                    break;
                case "2":
                    BankingSystem.reportA();
                    break;
                case "3":
                    System.out.print("Enter max age: ");
                    String max = sc.nextLine();
                    System.out.print("Enter min age: ");
                    String min = sc.nextLine();
                    BankingSystem.reportB(min, max);
                    break;
                case "4":
                    System.out.print("Enter saving rate: ");
                    String save = sc.nextLine();
                    System.out.print("Enter checking rate: ");
                    String check = sc.nextLine();
                    BankingSystem.rate(save, check);
                    break;
                case "5":
                    screen = false;
                    break;
                default:
                    System.out.println("Please only choose option 1, 2, 3, 4 or 5");
            }
        }
    }
}