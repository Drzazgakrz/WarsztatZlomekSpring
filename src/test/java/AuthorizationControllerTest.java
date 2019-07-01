import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import pl.warsztat.zlomek.controllers.rest.AuthorizationController;
import pl.warsztat.zlomek.controllers.web.UsersController;
import pl.warsztat.zlomek.data.ClientRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {pl.warsztat.zlomek.Main.class})
@NoArgsConstructor
public class AuthorizationControllerTest {
    @Autowired
    public ClientRepository clientRepository;
    @Autowired
    public AuthorizationController authorizationController;

    @Autowired
    private UsersController usersController;

    @Test
    public void positiveRegistrationTest() throws Exception{
        MockMvc mockMvc = standaloneSetup(authorizationController).build();
        mockMvc.perform(post("/rest/authorization").contentType(MediaType.APPLICATION_JSON).content(
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

    MockMvc mockMvc;
    @Before
    public void setup(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/META-INF/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = standaloneSetup(this.usersController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void optimalizationTest() throws Exception{
        long begin = System.currentTimeMillis();
        mockMvc.perform(get("/users")).andExpect(view().name("users"));
        System.out.println(System.currentTimeMillis()-begin);
    }
}
