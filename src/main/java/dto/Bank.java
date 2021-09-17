package dto;

import banking.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@AllArgsConstructor
@SuperBuilder
public class Bank {
    public String Name;

    public Map<String, Loan> bankLoanMapping;
}
