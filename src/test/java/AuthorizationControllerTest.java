import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.warsztat.zlomek.configuration.SpringConfiguration;
import pl.warsztat.zlomek.controllers.rest.AuthorizationController;
import pl.warsztat.zlomek.data.ClientRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
@NoArgsConstructor
public class AuthorizationControllerTest {
    @Autowired
    public ClientRepository clientRepository;
    @Autowired
    public AuthorizationController authorizationController;

    @Test
    public void positiveRegistrationTest() throws Exception{
        MockMvc mockMvc = standaloneSetup(authorizationController).build();
        mockMvc.perform(post("/authorization").contentType(MediaType.APPLICATION_JSON).content(
                "{\n" +
                        "\t\"firstName\":\"Krzysztof\",\n" +
                        "    \"lastName\":\"Drzazga\",\n" +
                        "    \"email\":\"kylu031011996@o2.pl\",\n" +
                        "    \"phoneNumber\":\"1234567890\",\n" +
                        "    \"cityName\":\"Lublin\",\n" +
                        "    \"streetName\":\"Porzeczkowa\",\n" +
                        "    \"buildNum\":\"23\",\n" +
                        "    \"aptNum\":\"\",\n" +
                        "    \"zipCode\":\"20-141\",\n" +
                        "    \"password\":\"abc123\",\n" +
                        "    \"confirmPassword\":\"abc123\"\n" +
                        "}"
        )).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void negativeRegistrationTest(){

    }
}
