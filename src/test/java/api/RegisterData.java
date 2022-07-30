package api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
public class RegisterData {

  public RegisterData(String email, String password) {
    this.email = email;
    this.password = password;
  }

  private String email;
  private String password;


}
