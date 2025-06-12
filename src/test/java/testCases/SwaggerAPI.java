package testCases;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.Map;
import utilities.Dataprovider;
import utilities.LoggerLoad;
import requestDataBody.AddInformation;
import requestDataBody.UserInformation;

@Test
public class SwaggerAPI extends BaseTest {

	int UsrID;String Name;


	@Test(priority = 1, dataProvider = "GetUser", dataProviderClass = Dataprovider.class)
	public void GetAllUsersTest(Map<String, String> rowData) {

		int code = Double.valueOf(rowData.get("Code")).intValue();
		String LineMsg =rowData.get("Linemsg");
		String Scenario = rowData.get("Scenario");

		LoggerLoad.info("GET REQUEST: "+Scenario);
		if ("Valid".equals(Scenario)) {
			Response response = given()
					.auth().basic(Username, Password)
					.when().get(BaseURL + SearchAllEp);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);

			JsonPath jsonPath = response.jsonPath();
			System.out.println("Response Code: " + response.getStatusCode());
			// Assert response
		//	Assert.assertNotNull(response.getBody());
			Assert.assertTrue(response.asString().contains("200")); // Check for specific JSON key or value

		}
		
		else if("NoAuth".equals(Scenario)) {
			Response response = given()
					.auth().basic(NoUserName,NoPassword)
					.when().get(BaseURL + SearchAllEp);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
          
			// Assert response
			Assert.assertTrue(response.asString().contains("401")); // Check for specific JSON key or value

		}
		else if("EmptyUsr".equals(Scenario)) {	
			Response response = given()
					.auth().basic(NoUserName,Password)
					.when().get(BaseURL + SearchAllEp);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);

			// Assert response
			Assert.assertTrue(response.asString().contains("401")); // Check for specific JSON key or value

		}
		else if("EmptyPswd".equals(Scenario)) {
			Response response = given()
					.auth().basic(Username,NoPassword)
					.when().get(BaseURL + SearchAllEp);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);

			// Assert response
			Assert.assertTrue(response.asString().contains("401")); // Check for specific JSON key or value


		}
		else if("IncorrectEndPoint".equals(Scenario)) {
			Response response = given()
					.auth().basic(Username,Password)
					.when().get(BaseURL + SearchAllEp+"invalid");
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);

			// Assert response
			Assert.assertTrue(response.asString().contains("404")); // Check for specific JSON key or value


		}
		else if("IncorrectBaseURL".equals(Scenario)) {
			Response response = given()
					.auth().basic(Username,Password)
					.when().get(InValidBaseURL + SearchAllEp);
			response.then().statusCode(code).statusLine(LineMsg);
			//Assert response
			//Assert.assertTrue(response.asString().contains("404")); // Check for specific JSON key or value
		}
	
		else {
			LoggerLoad.info("GET REQUEST SKIPPED manually: "+Scenario);
			throw new SkipException("Skipping this test due to a specific condition.");
		}
	}
	

	
	@Test(priority = 2, dataProvider = "PostUser", dataProviderClass = Dataprovider.class)
	public void CreateUser(Map<String, String> rowData) {
		
		int code = Double.valueOf(rowData.get("Code")).intValue();
		String LineMsg =rowData.get("Linemsg");
		String Scenario = rowData.get("Scenario");
		String FName = rowData.get("FirstName");
		String LName = rowData.get("LastName");
		String Phone = rowData.get("Phno");			
		String Mail = rowData.get("Email");
		String Plot = rowData.get("Plot");
		String Street = rowData.get("Street");
		String State = rowData.get("State");
		String Country = rowData.get("Country");
		String Zip = rowData.get("Zip");
		
		 // ✅ Construct nested address JSON
	    JSONObject address = new JSONObject()
	        .put("plotNumber", Plot)
	        .put("street", Street)
	        .put("state", State)
	        .put("country", Country)
	        .put("zipCode", Zip);

	    // ✅ Construct main JSON object with address nested
	    JSONObject jsonObj = new JSONObject()
	        .put("userFirstName", FName)
	        .put("userLastName", LName)
	        .put("userContactNumber", Phone)
	        .put("userEmailId", Mail)
	        .put("userAddress", address);
		LoggerLoad.info("POST REQUEST: "+Scenario);
		
		 if("Valid".equals(Scenario)){	
			 System.out.println("INSUDE VALID");
			Response response =
			given().auth().basic(Username,Password).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
			.when().post(BaseURL+CreateEP);
			response.then()
            .statusCode(code).header("Content-Type", Json).statusLine(LineMsg);

//			System.out.println("Response Body:\n" + response.getBody().asString());
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
			
//
			JsonPath jsonPath = response.jsonPath();
			Assert.assertEquals(jsonPath.getString("userFirstName"), FName);
			//System.out.println(jsonPath.getString("userFirstName")+"    "+FName);
			Assert.assertEquals(jsonPath.getString("userLastName"), LName);
			//System.out.println(jsonPath.getString("userLastName")+"    "+LName);
			Assert.assertEquals(jsonPath.getString("userContactNumber"), Phone);
			//System.out.println(jsonPath.getString("userContactNumber")+"    "+Phone);
			Assert.assertEquals(jsonPath.getString("userEmailId"), Mail);
			//System.out.println(jsonPath.getString("userEmailId")+"    "+Mail);
			Assert.assertEquals(jsonPath.getString("userAddress.plotNumber"), Plot);
			Assert.assertEquals(jsonPath.getString("userAddress.street"), Street);
			Assert.assertEquals(jsonPath.getString("userAddress.state"), State);
			Assert.assertEquals(jsonPath.getString("userAddress.country"), Country);
			Assert.assertEquals(jsonPath.getString("userAddress.zipCode"), Zip);
		    UsrID = jsonPath.getInt("userId");
		    Name = jsonPath.getString("userFirstName");
			//System.out.println(jsonPath.getInt("userId"));		
			//System.out.println(Name);
		}
		 else if("NoAuth".equals(Scenario)) {
			    Response response =
				given().auth().basic(NoUserName,NoPassword).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
				.when().post(BaseURL+CreateEP);
				response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
				System.out.println("Response Code: " + response.getStatusCode());
				System.out.println("Status Line: " + response.getStatusLine());
				
		}
		else if("EmptyUsr".equals(Scenario)) {
			  Response response =
				given().auth().basic(NoUserName,Password).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
				.when().post(BaseURL+CreateEP);
				response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
				System.out.println("Response Code: " + response.getStatusCode());
				System.out.println("Status Line: " + response.getStatusLine());
		}
			
		else if("EmptyPswd".equals(Scenario)) {
			 Response response =
		   	given().auth().basic(Username,NoPassword).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
				.when().post(BaseURL+CreateEP);
				response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
				System.out.println("Response Code: " + response.getStatusCode());
				System.out.println("Status Line: " + response.getStatusLine());
	
		}
		else if("IncorrectEndPoint".equals(Scenario)) {
			 Response response =
			given().auth().basic(Username,Password).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
			.when().post(BaseURL+CreateEP+"@invalid");
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
	
		}
		else if("IncorrectBaseURL".equals(Scenario)) {
			Response response =
			given().auth().basic(Username,Password).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
			.when().post(BaseURL+CreateEP);
//			response.then().statusCode(code).statusLine(LineMsg);
//			System.out.println("Response Body:\n" + response.getBody().asString());
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("ExistingUser".equals(Scenario))
		{
			Response response =
					given().auth().basic(Username,Password).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
					.when().post(BaseURL+CreateEP);
					response.then().statusCode(code).statusLine(LineMsg);
//					System.out.println("Response Body:\n" + response.getBody().asString());
					System.out.println("Response Code: " + response.getStatusCode());
					System.out.println("Status Line: " + response.getStatusLine());
		}
		 
			else {
			given().auth().basic(Username,Password).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
			.when().post(BaseURL+CreateEP)
			.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
		}
	}
	
	
	@Test(priority = 3, dataProvider = "GetUser", dataProviderClass = Dataprovider.class)
	public void GetUserById(Map<String, String> rowData) {
		int code = Double.valueOf(rowData.get("Code")).intValue();
		String LineMsg =rowData.get("Linemsg");
		String Scenario = rowData.get("Scenario");
		
		LoggerLoad.info("GET REQUEST By ID: "+Scenario);
		if ("Valid".equals(Scenario)) {
			Response response =
			given().auth().basic(Username,Password).pathParam("id", UsrID).log().all()
			.when().get(BaseURL+SearchByidEP);
			response.then().assertThat().body(matchesJsonSchemaInClasspath("schema.json")).statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			
			//System.out.println("Response Body:\n" + response.getBody().asString());
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
			
		}

		else if("NoAuth".equals(Scenario)) {
			Response response = given()
					.auth().basic(NoUserName,NoPassword)
					.when().get(BaseURL + SearchAllEp);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
          
			// Assert response
			Assert.assertTrue(response.asString().contains("401")); // Check for specific JSON key or value
		}
		else if("EmptyUsr".equals(Scenario)) {	
			Response response = given()
					.auth().basic(NoUserName,Password)
					.when().get(BaseURL + SearchAllEp);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());

			// Assert response
			Assert.assertTrue(response.asString().contains("401")); // Check for specific JSON key or value
		}
		else if("EmptyPswd".equals(Scenario)) {
			Response response = given()
					.auth().basic(Username,NoPassword)
					.when().get(BaseURL + SearchAllEp);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());

			// Assert response
			Assert.assertTrue(response.asString().contains("401")); // Check for specific JSON key or value

		}
		else if("IncorrectEndPoint".equals(Scenario)) {
			Response response = given()
					.auth().basic(Username,Password)
					.when().get(BaseURL + SearchAllEp+"invalid");
						response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
						

			// Assert response
			Assert.assertTrue(response.asString().contains("404")); // Check for specific JSON key or value

		}
		else if("IncorrectBaseURL".equals(Scenario)) {
			Response response = given()
					.auth().basic(Username,Password)
					.when().get(InValidBaseURL + SearchAllEp);
						response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
						System.out.println("Status Line: " + response.getStatusLine());
				//Assert response
		//	Assert.assertTrue(response.asString().contains("404")); // Check for specific JSON key or value
		
		}
	
		else {
			LoggerLoad.info("GET REQUEST SKIPPED manually: "+Scenario);
			throw new SkipException("Skipping this test due to a specific condition.");
		}
	}
		
		
		@Test(priority = 4, dataProvider = "GetUser", dataProviderClass = Dataprovider.class)
		public void GetUserByFName(Map<String, String> rowData) {
			int code = Double.valueOf(rowData.get("Code")).intValue();
			String LineMsg =rowData.get("Linemsg");
			String Scenario = rowData.get("Scenario");
			
			LoggerLoad.info("GET REQUEST By Name: "+Scenario);

			if ("Valid".equals(Scenario)) {
				Response response =
				given().auth().basic(Username,Password).pathParam("fname", Name).log().all()
				.when().get(BaseURL+SearchByNamEP);
				response.then().assertThat().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
				System.out.println("Response Body:\n" + response.getBody().asString());
				System.out.println("Response Code: " + response.getStatusCode());
				System.out.println("Status Line: " + response.getStatusLine());
			}
			else if("NoAuth".equals(Scenario)) {
					given().auth().basic(NoUserName,NoPassword).pathParam("fname", Name).log().all()
					.when().get(BaseURL+SearchByNamEP)
					.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}
			else if("EmptyUsr".equals(Scenario)) {
					given().auth().basic(NoUserName,Password).pathParam("fname", Name).log().all()
					.when().get(BaseURL+SearchByNamEP)
					.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}
			else if("EmptyPswd".equals(Scenario)) {
					given().auth().basic(Username,NoPassword).pathParam("fname", Name).log().all()
					.when().get(BaseURL+SearchByNamEP)
					.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}
			else if("IncorrectEndPoint".equals(Scenario)) {
				given().auth().basic(Username,Password).pathParam("fname", Name).log().all()
				.when().get(BaseURL+SearchByNamEP+"$invalid")
				.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}
			else if("IncorrectBaseURL".equals(Scenario)) {
				given().auth().basic(Username,Password).pathParam("fname", Name).log().all()
				.when().get(InValidBaseURL+SearchByNamEP)
				.then().statusCode(code).statusLine(LineMsg);
			}	
			else if("NonExistingUser".equals(Scenario)) {
				given().auth().basic(Username,Password).pathParam("fname", Name+"API").log().all()
				.when().get(BaseURL+SearchByNamEP)
				.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}
		}
	
	@Test(priority = 5, dataProvider = "UpdateUser", dataProviderClass = Dataprovider.class)
	public void UpdateUser(Map<String, String> rowData) {

		int code = Double.valueOf(rowData.get("Code")).intValue();
		String LineMsg =rowData.get("Linemsg");
		String Scenario = rowData.get("Scenario");
		String FName = rowData.get("FirstName");
		String LName = rowData.get("LastName");
		String Phone = rowData.get("Phno");			
		String Mail = rowData.get("Email");
		String Plot = rowData.get("Plot");
		String Street = rowData.get("Street");
		String State = rowData.get("State");
		String Country = rowData.get("Country");
		String Zip = rowData.get("Zip");

		LoggerLoad.info("PUT REQUEST By ID: "+Scenario);

		// ✅ Construct nested address JSON
		JSONObject address = new JSONObject()
				.put("plotNumber", Plot)
				.put("street", Street)
				.put("state", State)
				.put("country", Country)
				.put("zipCode", Zip);

		// ✅ Construct main JSON object with address nested
		JSONObject jsonObj = new JSONObject()
				.put("userFirstName", FName)
				.put("userLastName", LName)
				.put("userContactNumber", Phone)
				.put("userEmailId", Mail)
				.put("userAddress", address);
		LoggerLoad.info("PUT REQUEST: "+Scenario);

		if("Valid".equals(Scenario)){	
			System.out.println("INSIDE VALID");
			Response response =
			given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
			.when().put(BaseURL+UpdateEP);
			response.then()
			   .statusCode(code).header("Content-Type", Json).statusLine(LineMsg);

			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());


			JsonPath jsonPath = response.jsonPath();
			Assert.assertEquals(jsonPath.getString("userFirstName"), FName);
			System.out.println(jsonPath.getString("userFirstName")+"    "+FName);
			Assert.assertEquals(jsonPath.getString("userLastName"), LName);
			System.out.println(jsonPath.getString("userLastName")+"    "+LName);
			Assert.assertEquals(jsonPath.getString("userContactNumber"), Phone);
			System.out.println(jsonPath.getString("userContactNumber")+"    "+Phone);
			Assert.assertEquals(jsonPath.getString("userEmailId"), Mail);
			System.out.println(jsonPath.getString("userEmailId")+"    "+Mail);
			Assert.assertEquals(jsonPath.getString("userAddress.plotNumber"), Plot);
			Assert.assertEquals(jsonPath.getString("userAddress.street"), Street);
			Assert.assertEquals(jsonPath.getString("userAddress.state"), State);
			Assert.assertEquals(jsonPath.getString("userAddress.country"), Country);
			Assert.assertEquals(jsonPath.getString("userAddress.zipCode"), Zip);
			UsrID = jsonPath.getInt("userId");
			Name = jsonPath.getString("userFirstName");
			System.out.println(jsonPath.getInt("userId"));		
			System.out.println(Name);
		} else if("AlreadyExisting".equals(Scenario)) {
			Response response =
					given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
					.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}else if("CharFirstName".equals(Scenario)) {
			Response response =
					given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
					.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}else if("EmptyFirstName".equals(Scenario)) {
			Response response =
					given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
					.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}else if("NumericLastname".equals(Scenario)) {
			Response response =
					given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
					.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}else if("CharLastName".equals(Scenario)) {
			Response response =
					given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
					.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}else if("EmptyLastName".equals(Scenario)) {
			Response response =
					given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
					.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("PhoneGrt10Digit".equals(Scenario)) {
			Response response =
					given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
					.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("PhoneLess10Digit".equals(Scenario)) {
			Response response =
					given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
					.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}
	
			else if("EmptyUsr".equals(Scenario)) {
				Response response =
			given().auth().basic(NoUserName,Password).pathParam("id", UsrID).header("Content-Type", Json).body(jsonObj.toString()).log().all()
			.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("EmptyPswd".equals(Scenario)) {
			Response response =
			given().auth().basic(Username,NoPassword).pathParam("id", UsrID).header("Content-Type", Json).body(jsonObj.toString()).log().all()
			.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("NoAuth".equals(Scenario)) {
		    Response response =
			given().auth().basic(NoUserName,NoPassword).pathParam("id", UsrID).header("Content-Type", "application/json").body(jsonObj.toString()).log().all()
			.when().put(BaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
			
	}
		else if("IncorrectEndPoint".equals(Scenario)) {
			Response response =
			given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", Json).body(jsonObj.toString()).log().all()
			.when().put(BaseURL+UpdateEP+"@invalid");
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("IncorrectBaseURL".equals(Scenario)) {
			Response response =
			given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", Json).body(jsonObj.toString()).log().all()
			.when().put(InValidBaseURL+UpdateEP);
			response.then().statusCode(code).statusLine(LineMsg);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}
		else {
			given().auth().basic(Username,Password).pathParam("id", UsrID).header("Content-Type", Json).body(jsonObj).log().all()
			.when().put(BaseURL+UpdateEP)
			.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);

		}


	}


		@Test(priority = 6, dataProvider = "DeleteUser", dataProviderClass = Dataprovider.class)
	public void DeleteUserByFNameTest(Map<String, String> rowData) {
		int code = Double.valueOf(rowData.get("Code")).intValue();
		String LineMsg =rowData.get("Linemsg");
		String Scenario = rowData.get("Scenario");
		LoggerLoad.info("DELETE REQUEST By Name: "+Scenario);
        System.out.println("DELETE REQUEST");
        System.out.println("Name"+Name);
		if ("Valid".equals(Scenario)) {
			Response response =
			given().auth().basic(Username,Password).pathParam("fname", Name).log().all()
			.when().delete(BaseURL+DeleteNameEP);
			response.then().assertThat().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("NoAuth".equals(Scenario)) {
			Response response =
				given().auth().basic(NoUserName,NoPassword).pathParam("fname", Name).log().all()
				.when().get(BaseURL+DeleteNameEP);
				response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
				System.out.println("Response Code: " + response.getStatusCode());
				System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("EmptyUsr".equals(Scenario)) {
			Response response =
				given().auth().basic(NoUserName,Password).pathParam("fname", Name).log().all()
				.when().get(BaseURL+DeleteNameEP);
				response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
				System.out.println("Response Code: " + response.getStatusCode());
				System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("EmptyPswd".equals(Scenario)) {
			Response response =
				given().auth().basic(Username,NoPassword).pathParam("fname", Name).log().all()
				.when().get(BaseURL+DeleteNameEP);
				response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
				System.out.println("Response Code: " + response.getStatusCode());
				System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("IncorrectEndPoint".equals(Scenario)) {
			Response response =
			given().auth().basic(Username,Password).pathParam("fname", Name).log().all()
			.when().get(BaseURL+DeleteNameEP+"$invalid");
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}
		else if("IncorrectBaseURL".equals(Scenario)) {
			Response response =
			given().auth().basic(Username,Password).pathParam("fname", Name).log().all()
			.when().get(InValidBaseURL+DeleteNameEP);
			response.then().statusCode(code).statusLine(LineMsg);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}	
		else if("NonExistingUser".equals(Scenario)) {
			Response response =
			given().auth().basic(Username,Password).pathParam("fname", Name+"API").log().all()
			.when().get(BaseURL+DeleteNameEP);
			response.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			System.out.println("Response Code: " + response.getStatusCode());
			System.out.println("Status Line: " + response.getStatusLine());
		}	
		}

		
		
		 @Test(priority = 7, dataProvider = "DeleteUser", dataProviderClass = Dataprovider.class)
		public void DeleteUserByIdTest(Map<String, String> rowData) {
			int code = Double.valueOf(rowData.get("Code")).intValue();
			String LineMsg =rowData.get("Linemsg");
			String Scenario = rowData.get("Scenario");
			LoggerLoad.info("DELETE REQUEST By ID: "+Scenario);
	
			if ("Valid".equals(Scenario)) {
				given().auth().basic(Username,Password).pathParam("id", UsrID).log().all()
				.when().delete(BaseURL+DeleteByIdEP)
				.then().assertThat().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}
			else if("NoAuth".equals(Scenario)) {
					given().auth().basic(NoUserName,NoPassword).pathParam("id", UsrID).log().all()
					.when().delete(BaseURL+DeleteByIdEP)
					.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}
			else if("EmptyUsr".equals(Scenario)) {
					given().auth().basic(NoUserName,Password).pathParam("id", UsrID).log().all()
					.when().delete(BaseURL+DeleteByIdEP)
					.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}
			else if("EmptyPswd".equals(Scenario)) {
					given().auth().basic(Username,NoPassword).pathParam("id", UsrID).log().all()
					.when().delete(BaseURL+DeleteByIdEP)
					.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}
			else if("IncorrectEndPoint".equals(Scenario)) {
				given().auth().basic(Username,Password).pathParam("id", UsrID).log().all()
				.when().delete(BaseURL+DeleteByIdEP+"$invalid")
				.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}
			else if("IncorrectBaseURL".equals(Scenario)) {
				given().auth().basic(Username,Password).pathParam("id", UsrID).log().all()
				.when().delete(InValidBaseURL+DeleteByIdEP)
				.then().statusCode(code).statusLine(LineMsg);
			}	
			else if("NonExistingUser".equals(Scenario)) {
				given().auth().basic(Username,Password).pathParam("id", UsrID+1000).log().all()
				.when().delete(BaseURL+DeleteByIdEP)
				.then().statusCode(code).statusLine(LineMsg).header("Content-Type", Json);
			}	
		} 
}
