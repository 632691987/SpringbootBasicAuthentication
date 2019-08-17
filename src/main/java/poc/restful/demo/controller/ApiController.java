package poc.restful.demo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import poc.restful.demo.dto.AdminDTO;
import poc.restful.demo.dto.CustomerDTO;
import poc.restful.demo.dto.OperatorDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiController {

    @Secured("ROLE_OPERATOR")
    @GetMapping("operatorapi")
    public OperatorDTO echoOperator() {
        OperatorDTO dto = new OperatorDTO();
        dto.setAttribute1("attribute1");
        dto.setAttribute2("attribute2");
        dto.setAttribute3("attribute3");
        return dto;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("adminapi")
    public AdminDTO echoAdmin() {
        AdminDTO dto = new AdminDTO();
        dto.setAttribute4("attribute4");
        dto.setAttribute5("attribute5");
        dto.setAttribute6("attribute6");
        return dto;
    }

    @Secured("ROLE_CUSTOMER")
    @GetMapping("customerapi")
    public CustomerDTO echoCustomer() {
        CustomerDTO dto = new CustomerDTO();
        dto.setAttribute7("attribute7");
        dto.setAttribute8("attribute8");
        dto.setAttribute9("attribute9");
        return dto;
    }

}
