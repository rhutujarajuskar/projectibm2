package restAssured;

import org.json.simple.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class project2 {
	String BASE_URI = "https://petstore.swagger.io/v2/";
	String PATH = "user/";
	static String usrnam = "Rak";
	
	public RequestSpecification connect(String base){
		RestAssured.baseURI = base;
		return RestAssured.given();
	}
	private void validateLog(int sts, Response resp){
		resp.then().statusCode(sts).log().all();
	}
	
	@SuppressWarnings("unchecked")
	private String genusrData(String usr, String f, String l, String e, String pass, String p, int s){
		JSONObject jone = useJson();
		jone.put("username", usr);
		jone.put("firstName", f);
		jone.put("lastName", l);
		jone.put("email", e);
		jone.put("password", pass);
		jone.put("phone", p);
		jone.put("Username", s);
		
		return jone.toString();
	}
	
	private JSONObject useJson(){
		return new JSONObject();
	}
	
	@Test(dataProvider="loginData", dependsOnMethods={"firstPOST"})
	public void login(String usrnam, String pass){
		System.out.println("LOG IN");
		
		validateLog(200,connect(BASE_URI).queryParam("username", usrnam).queryParam("password", pass).get(PATH+"login"));			
		
	}
	
	@Test(dependsOnMethods={"firstPOST"}, dataProvider="putData")
	public void firstPUT(String usrnam, String f, String l, String e, String pass, String p, int s){
		System.out.println("PUT");
		
		validateLog(200,connect(BASE_URI).contentType(ContentType.JSON).body(genusrData(usrnam, f, l, e, pass, p, s)).when().put(PATH+usrnam));
	
	}
	
	@Test(dependsOnMethods={"firstPUT"},dataProvider="deleteData")
	public void firstDELETE(String id){
		System.out.println("DELETE");
		
		validateLog(200, connect(BASE_URI).delete(PATH+id));
	
	}
	
	@Test(dataProvider="postData")
	public void firstPOST(String usrnam, String f, String l, String e, String pass, String p, int s){
		System.out.println("CREATE");

		validateLog(200, connect(BASE_URI).contentType(ContentType.JSON).body(genusrData(usrnam, f, l, e, pass, p, s)).when().post(PATH));
	
	}
	
	@DataProvider(name="postData")
	public Object[][] providerPOST(){
		Object[][] postData = {
			{usrnam,"003PXN","IN", "abc@xyz.com", "pass123","9876543210", 0}
		};		
		return postData;
	}
	
	@DataProvider(name="putData")
	public Object[][] providerPUT(){
		Object[][] putData = {
			{usrnam,"003PXN","744", "abc@xyz.com", "pass123","9876543210", 0}
		};
		return putData;
	}
	
	@DataProvider(name="deleteData")
	public Object[][] providerDELETE(){
		Object[][] deleteData = {{usrnam}};
		return deleteData;
	}
	
	@DataProvider(name="loginData")
	public Object[][] providerLogin(){
		Object[][] loginData = {{usrnam, "pass123"}};
		return loginData;
	}

}
