import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

public class GeektrustTest {

    Geektrust geektrust = spy(Geektrust.class);

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void processLoanTest() {
        String[] args = new String[]{"LOAN", "IDIDI", "Dale", "10000", "5", "4"};
        geektrust.processLoan(args);
        args = new String[]{"BALANCE", "IDIDI", "Dale", "5"};
        geektrust.processBalance(args);
        assertEquals("IDIDI Dale 1000 55", outContent.toString().trim());
    }

    @Test
    public void processPayoutTest() {
        String[] args = new String[]{"LOAN", "MBI", "Harry", "10000", "3", "7"};
        geektrust.processLoan(args);
        args = new String[]{"BALANCE", "MBI", "Harry", "12"};
        geektrust.processBalance(args);
        assertEquals("MBI Harry 4044 24", outContent.toString().trim());
        args = new String[]{"PAYMENT", "MBI", "Harry", "5000", "12"};
        geektrust.processPayout(args);
        args = new String[]{"BALANCE", "MBI", "Harry", "12"};
        geektrust.processBalance(args);
        assertEquals("MBI Harry 9044 10", outContent.toString().split("\\r?\\n")[1].trim());
    }
}
