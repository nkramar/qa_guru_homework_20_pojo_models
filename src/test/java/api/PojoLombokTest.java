package api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;

public class PojoLombokTest {

  private final static String baseURL = "https://reqres.in/";

  @Test

  public void checkAvatarAndIdTest() {

    Specifications.installSpecification(Specifications.requestSpec(baseURL), Specifications.responseSpec200());

    List<UserData> users = given()
            .when()
            .get("api/users?page=2")
            .then().log().all()
            .extract().body().jsonPath().getList("data", UserData.class);

    users.forEach(x -> Assertions.assertTrue(x.getAvatar().contains(x.getId().toString())));
    Assertions.assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

    List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
    List<String> ids = users.stream().map(x -> x.getId().toString()).collect(Collectors.toList());
  }

  @Test
  public void successfulRegistrationTest() {
    Specifications.installSpecification(Specifications.requestSpec(baseURL), Specifications.responseSpec200());

    Integer id = 4;
    String token = "QpwL5tke4Pnpja7X4";

    RegisterData user = new RegisterData("eve.holt@reqres.in", "pistol");
    SuccessfulRegData successfulReg = given()
            .body(user)
            .when()
            .post("api/register")
            .then().log().all()
            .extract().as(SuccessfulRegData.class);
    Assertions.assertNotNull(successfulReg.getId());
    Assertions.assertNotNull(successfulReg.getToken());
    Assertions.assertEquals(id, successfulReg.getId());
    Assertions.assertEquals(token, successfulReg.getToken());
  }

  @Test
  public void unSuccessfulRegistrationTest() {
    Specifications.installSpecification(Specifications.requestSpec(baseURL), Specifications.responseSpecError400());

    RegisterData user = new RegisterData("sydney@fife", "");
    UnSuccessfulRegData unSuccessfulReg = given()
            .body(user)
            .post("api/register")
            .then().log().all()
            .extract().as(UnSuccessfulRegData.class);
    Assertions.assertEquals("Missing password", unSuccessfulReg.getError());
  }
  @Test
  public void sortedYearsTest() {
    Specifications.installSpecification(Specifications.requestSpec(baseURL), Specifications.responseSpec200());
    List<ColorsData> colors = given()
            .when()
            .get("api/unknown")
            .then().log().all()
            .extract().body().jsonPath().getList("data", ColorsData.class);
    List<Integer> years = colors.stream().map(ColorsData::getYear).collect(Collectors.toList());
    List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
    Assertions.assertEquals(sortedYears, years);
    System.out.println(years);
    System.out.println(sortedYears);
  }

  @Test
  public void deleteUserTest() {
    Specifications.installSpecification(Specifications.requestSpec(baseURL), Specifications.responseSpecUnique(204));
    given()
            .when()
            .delete("api/users/2")
            .then().log().all();
  }

}

