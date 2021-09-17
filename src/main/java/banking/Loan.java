package banking;

import dto.Borrower;
import dto.Emi;
import util.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Loan {

    private Borrower borrower;

    private Date sanctionDate;

    private Double initialAmount;

    private int tenure;

    private Double interestRate;

    private Double totalAmount;

    private int emiRemaining;

    private int firstRemainingEmi;

    private Double emiCost;

    private List<Emi> emiList = new ArrayList<>();

    public Loan(Borrower borrower, Double initialAmount, int tenure, Double interestRate) {

        this.borrower = borrower;
        this.sanctionDate = new Date();
        this.initialAmount = initialAmount;
        this.tenure = tenure;
        this.interestRate = interestRate;
        sanctionLoan();
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public int getEmiRemaining() {
        return emiRemaining;
    }

    private void sanctionLoan() {

        totalAmount = (initialAmount * interestRate * tenure) / 100 + initialAmount;
        emiRemaining = tenure * 12;
        emiCost = Math.ceil(totalAmount / emiRemaining);
        firstRemainingEmi = 1;
        Double emiSum = 0.0;

        for (int i = 0; i < emiRemaining; i++) {
            Emi emi = Emi.builder().emiNumber(i + 1).emiAmount(Math.min(emiCost, totalAmount - emiSum))
                    .dueDate(Utility.DateAfterDays(sanctionDate, 30 * (i + 1)))
                    .payoutAmount(Math.min(emiCost, totalAmount - emiSum))
                    .totalEmiRemaining(emiRemaining - i - 1).build();
            emiSum += emiCost;
            emiList.add(emi);
        }
    }

    public Double getTotalPaymentMade(int emiNumber) {

        double res = 0;
        for (int i = 0; i < emiNumber; i++) {
            res += emiList.get(i).getPayoutAmount();
        }
        return res;
    }

    public Emi getEmi(int emiNumber) {

        return emiNumber == 0 ? emiList.get(0) : emiList.get(emiNumber - 1);
    }

    public void makeOneTimePayment(int emiNumber, Double amount) {

        double emiSum = 0;
        for (int i = firstRemainingEmi - 1; i < emiNumber; i++) {
            Emi emi = emiList.get(i);
            emiSum += emi.getEmiAmount();
            emi.setPayoutDate(new Date());
            if (i == emiNumber - 1) {
                emi.setPayoutAmount(emi.getEmiAmount() + amount);
            } else {
                emi.setPayoutAmount(emi.getEmiAmount());
            }
            firstRemainingEmi++;
        }
        double totalAmountRemaining = totalAmount - (emiSum + amount);
        emiRemaining = (int) Math.ceil(totalAmountRemaining / this.emiCost);
        emiList.get(firstRemainingEmi - 2).setTotalEmiRemaining(emiRemaining);
        List<Integer> removeEmiIndexList = new ArrayList<>();
        int temp = emiRemaining;

        for (int i = firstRemainingEmi - 1; i < emiList.size(); i++) {
            double emiAmount = Math.min(emiCost, totalAmountRemaining - emiCost);
            if (emiAmount == 0.0) {
                removeEmiIndexList.add(i);
            } else {
                Emi emi = emiList.get(i);
                if (emi.getEmiAmount() != emiAmount) {
                    emi.setEmiAmount(emiAmount);
                }
                emi.setTotalEmiRemaining(temp - 1);
                totalAmountRemaining -= emiAmount;
                temp--;
            }
        }
        for (Integer i : removeEmiIndexList) {
            emiList.remove(i);
        }
    }
}
