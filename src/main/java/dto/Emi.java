package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@AllArgsConstructor
@SuperBuilder
public class Emi {

    private int emiNumber;

    private Double emiAmount;

    private Double payoutAmount;

    private Date dueDate;

    private Date payoutDate;

    private int totalEmiRemaining;
}
