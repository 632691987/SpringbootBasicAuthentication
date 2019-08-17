package poc.restful.demo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ErrorDto {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
