package netology.ru.web;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Data

public class ClientData {
    private final String city;
    private final String fullName;
    private final String phoneNumber;
}

