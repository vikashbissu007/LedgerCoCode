import banking.Loan;
import dto.Bank;
import dto.Borrower;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Geektrust {

    private static final String LOAN = "LOAN";

    private static final String PAYMENT = "PAYMENT";

    private static final String BALANCE = "BALANCE";

    private static Map<String, Bank> bankMap = new HashMap<>();
    private static Map<String, Borrower> borrowerMap = new HashMap<>();

    private static final Logger logger = LogManager.getLogger(Geektrust.class);

    private static Geektrust instance;

    public static Geektrust getInstance() {
        if (instance == null) {
            //synchronized block to remove overhead
            synchronized (Geektrust.class) {
                if(instance==null) {
                    // if instance is null, initialize
                    instance = new Geektrust();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {

        String fileName = args[0];
        try {
            processInput(fileName);
        } catch (Exception e) {
            logger.error("input file processing failed", e);
            e.printStackTrace();
        }
    }

    static void processInput(String fileName) throws FileNotFoundException {
        try {
            File file = new File(fileName);
            Scanner input = new Scanner(file);

            Geektrust geektrust = Geektrust.getInstance();
            while (input.hasNextLine()) {
                String data = input.nextLine();
                String[] args = data.split(" ");
                switch (args[0]) {
                    case LOAN:
                        geektrust.processLoan(args);
                        break;
                    case PAYMENT:
                        geektrust.processPayout(args);
                        break;
                    case BALANCE:
                        geektrust.processBalance(args);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("error in processing loan or payout or balance", e);
            e.printStackTrace();
        }
    }

    void processLoan(String[] args) {

        String bankName = args[1];
        String borrowerName = args[2];

        Double principalAmount = Double.parseDouble(args[3]);

        int tenure = Integer.parseInt(args[4]);
        Double rateOfInterest = Double.parseDouble(args[5]);
        logger.info("Value for bankName : {}, borrowerName : {}, principalAmount : {}, tenure : {}, rateOfInterest : {} in processLoan",
                bankName, borrowerName, principalAmount, tenure, rateOfInterest);

        Bank bank = getBank(bankName);
        Borrower borrower = getBorrower(borrowerName);
        Loan loan = new Loan(borrower, principalAmount, tenure, rateOfInterest);
        bank.bankLoanMapping.put(borrowerName, loan);
    }

    void processBalance(String[] args) {

        String bankName = args[1];
        String borrowerName = args[2];

        int emiNumber = Integer.parseInt(args[3]);
        logger.info("Value for bankName : {}, borrowerName : {}, emiNumber : {} in processBalance",
                bankName, borrowerName, emiNumber);

        Bank bank = getBank(bankName);
        Loan loan = bank.bankLoanMapping.getOrDefault(borrowerName, null);
        if (loan != null) {
            Double totalPayoutMade = loan.getTotalPaymentMade(emiNumber);
            int totalRemainingEmis = loan.getEmi(emiNumber).getTotalEmiRemaining();
            if (emiNumber == 0) totalRemainingEmis++;

            System.out.println(bank.getName() + " " + loan.getBorrower().getName() + " " + totalPayoutMade.intValue() + " " + totalRemainingEmis);
        } else {
            logger.error("No loan exist for given borrower:{} with the bank:{}", borrowerName, bankName);
        }
    }

    void processPayout(String[] args) {

        String bankName = args[1];
        String borrowerName = args[2];

        Double amount = Double.parseDouble(args[3]);
        int emiNumber = Integer.parseInt(args[4]);
        logger.info("Value for bankName : {}, borrowerName : {}, amount : {}, emiNumber : {} in processPayout",
                bankName, borrowerName, amount, emiNumber);

        Bank bank = getBank(bankName);
        Loan loan = bank.bankLoanMapping.getOrDefault(borrowerName, null);
        if (loan != null) {
            loan.makeOneTimePayment(emiNumber, amount);
        } else {
            logger.error("No loan exist for given borrower:{} with the bank:{}", borrowerName, bankName);
        }
    }

    Bank getBank(String bankName) {
        Bank bank = null;
        if (bankMap.containsKey(bankName)) {
            bank = bankMap.get(bankName);
        } else {
            synchronized (Geektrust.class) {
                if (!bankMap.containsKey(bankName)) {
                    bank = Bank.builder().Name(bankName).build();
                    bank.bankLoanMapping = new HashMap<>();
                    bankMap.put(bankName, bank);
                }
            }
        }
        return bank;
    }

    Borrower getBorrower(String borrowerName) {

        Borrower borrower = null;
        if (borrowerMap.containsKey(borrowerMap)) {
            borrower = borrowerMap.get(borrowerName);
        } else {
            synchronized (Geektrust.class) {
                if (!borrowerMap.containsKey(borrowerMap)) {
                    borrower = Borrower.builder().name(borrowerName).build();
                    borrowerMap.put(borrowerName, borrower);
                }
            }
        }
        return borrower;
    }
}
