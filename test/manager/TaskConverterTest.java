package manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskConverterTest {

    @DisplayName("Check that invalid task type can not be parsed from string")
    @Test
    void shouldThrowIllegalArgumentException() {
        String invalidTask = "1,INVAL,Task,NEW,Task description";
        assertThrows(IllegalArgumentException.class, () -> TaskConverter.fromString(invalidTask));
    }
}
