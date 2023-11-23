package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountServices;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferServices;

import java.math.BigDecimal;
import java.util.Scanner;

public class App {

    private final Scanner scan = new Scanner(System.in);

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AccountServices accountServices;

    private AuthenticatedUser currentUser;
    private User user;

    private TransferServices transferServices;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        this.accountServices = new AccountServices();
        this.transferServices = new TransferServices();
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        BigDecimal userBalance = accountServices.getUserBalance(currentUser.getToken());
        System.out.println("Your current balance is: " + userBalance);
    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        System.out.println("\n========================================");
        System.out.println("               TRANSFER LIST             ");
        System.out.println("========================================");
        int userId = currentUser.getUser().getId();
        transferServices.printTransferListForCurrentUser(currentUser.getToken(), userId);
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub
        int userId = currentUser.getUser().getId();

        transferServices.getPendingTransactions(currentUser.getToken(), userId);

    }

    private void sendBucks() {
        Transfers transfers = new Transfers();
        Scanner scan = new Scanner(System.in);
        boolean isAnActualCustomer = false;

        System.out.println("Please select user to send money to via user-id (ex. 1001): ");
        User[] allUsers = accountServices.usersList();
        for (User currUser : allUsers) {
            System.out.println("(" + (currUser.getId() + ")" + " - " + currUser.getUsername()));
        }
        System.out.print("Enter ID: ");
        int moneyToUserId = scan.nextInt();

        if (moneyToUserId == currentUser.getUser().getId()) {
            System.out.println("you are not allowed to send money to yourself!");
            return;

        }

        for (User currUser : allUsers) {
            if (currUser.getId() == moneyToUserId) {
                isAnActualCustomer = true;
                break;
            }
        }
        if (!isAnActualCustomer) {
            System.out.println("User ID does not exist.. Please try again!");
        } else {
            System.out.print("Please enter amount of money to transfer: ");
            BigDecimal amountToSend = scan.nextBigDecimal();

            if (accountServices.sendTransfer(currentUser.getToken(), amountToSend, accountServices.getCurrentUserAccountId(currentUser.getUser().getId()), accountServices.getAccountByUserId(moneyToUserId))) {
                System.out.println("Your transaction was successfully sent!");
            }


            System.out.println("Transaction could not be processed, Please try again.");
        }

    }


    private void requestBucks() {
        // TODO Auto-generated method stub
        Scanner scan = new Scanner(System.in);
        System.out.println("Please select user to send money to via user-id  (ex. 1001): ");
        User[] allUsers = accountServices.usersList();
        for (User user : allUsers) {
            System.out.println("(" + (user.getId() + ")" + " - " + user.getUsername()));
        }
    }
}
