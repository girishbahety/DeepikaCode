package com.github.step_definition;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pages.LoginAndSearchPage;
import com.github.user.pojo.Githubuserrepo;
import com.github.util.GeneralMethod;
import com.github.util.JsonCalls;
import com.github.util.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class GitHubUserInfo extends TestBase {

	LoginAndSearchPage githubObj;
	JsonCalls jsonCalls = new JsonCalls();
	GeneralMethod gnMethod = new GeneralMethod();
	static String userName = "";
	static String noOfStarsFromUI = "";
	static String  firstRepoName = "";
	
	static HashMap<String,Integer> repoStarMap = new HashMap<String,Integer>();
	

	@When("I give the username from the console")
	public void i_give_the_username_from_the_console()
	{
		//Open command prompt which will except the github username
		
		Scanner input = new Scanner(System.in);
		System.out.println("Enter GitHub UserName:");
		userName = input.next();
	}
	
	@When("I call userinfo api to get the given user information and print it on console")
	public void i_call_userinfo_api_to_get_the_given_user_information_and_print_it_on_console() throws ParseException, ClientProtocolException, IOException
	{
		String userInfoUrl = "https://api.github.com/users/"+userName;
		
		//GET Call
		String output = jsonCalls.HttpGetCall(userInfoUrl);
		if(!output.isEmpty())
		{
			JSONObject jsonObject = new JSONObject(output);

			System.out.println("UserName: "+jsonObject.get("login"));
			System.out.println("Name: "+jsonObject.get("name"));
			String srcDate = jsonObject.getString("created_at");

			String createdDate = gnMethod.ConvertDateFormat("yyyy-MM-dd'T'HH:mm:ssX", srcDate, "yyyy-MM-dd");

			System.out.println("createdDate: "+createdDate);
		}
		else
		{
			System.out.println("From API:Entered User: "+userName +" does not exist in GitHub(API)");
			Assert.fail("From API:Entered User: "+userName +" does not exist in GitHub(API)");
		}
		
	}
	
	@Then("I call repository api which is owned by the given user to get the repo information and print it on console")
	public void i_call_repository_api_which_is_owned_by_the_given_user_to_get_the_repo_information_and_print_it_on_console() throws ClientProtocolException, IOException
	{
		//to get the repository
		String repoUrl = "https://api.github.com/users/" + userName + "/repos";
		
		String output = jsonCalls.HttpGetCall(repoUrl);
		
		if(!output.replace("[", "").replace("]", "").isEmpty())
		{	
			List<Githubuserrepo> userRepoObj = Arrays.asList(new ObjectMapper().readValue(output,Githubuserrepo[].class));
			for(int i=0;i<userRepoObj.size();i++)
			{
				int repono = i+1;
				System.out.println("Repository"+repono+ "=" +userRepoObj.get(i).getFullName());
				
				System.out.println("\tStars="+ userRepoObj.get(i).getStargazersCount());
				
				//Store repo name and star in one map
				repoStarMap.put(userRepoObj.get(i).getFullName(), userRepoObj.get(i).getStargazersCount());
				
				//for no of release in repor
				String fullName = userRepoObj.get(i).getFullName();
				String releaseUrl = "https://api.github.com/repos/" + fullName+"/releases";
						
				String release = jsonCalls.HttpGetCall(releaseUrl);
				
				JSONArray jsonArray = new JSONArray(release);
				System.out.println("\tReleases: "+jsonArray.length());
			}
		}
		else
			System.out.println("From API:No Repository is exist for user: "+userName);
	}

	@Given("Chrome Driver is opened")
    public void chrome_driver_is_opened()
    {
		TestBase.initialization();
		githubObj = new LoginAndSearchPage();
    }
    
    @When("I opened github page and search the username")
    public void i_opened_github_page_and_search_the_username() throws InterruptedException
    {
    	driver.get("https://github.com/");
		String textToEnter = "user:"+userName+ " fork:true";

		githubObj.EnterSearchStringInTextBox(textToEnter);
		githubObj.PressEnterButton();
    }	
    
    @When("I Click on first repository and get the number of stars")
    public void i_click_on_first_repository_and_get_the_number_of_star() throws InterruptedException
    {
    	if(!githubObj.repoNotExist())
    	{
	    	firstRepoName = githubObj.getTextOfFirstSearchedRepo();
		
			//click on repository
			githubObj.ClickOnRepo();
	
			noOfStarsFromUI = githubObj.getNoOfStars();
    	}
    	else
		{
			System.out.println("From UI:No repository exist for User: "+userName);
			Assert.fail("From UI:No repository exist for User: "+userName);
		}
    }
    
    @Then("I compare stars value of UI and API")
    public void i_compare_stars_value_of_ui_and_api()
    {
    	int noOfStarsFromApi = 0;

    	for(int i=0;i<repoStarMap.size();i++)
    	{
    		if(repoStarMap.keySet().toArray()[i].equals(firstRepoName))
    		{
    			noOfStarsFromApi = repoStarMap.get(repoStarMap.keySet().toArray()[i]);
    			
    			System.out.println("Repo Name from UI: "+firstRepoName);
        		System.out.println("Repo Name from API: "+repoStarMap.keySet().toArray()[i]);
        		System.out.println("Number of Stars from UI: "+noOfStarsFromUI);
        		
        		System.out.println("Number of Stars from API: "+noOfStarsFromApi);
        		String apiStars =String.valueOf(noOfStarsFromApi);

        		//count the no of digit
        		if(noOfStarsFromApi >0)
        		{
        			int count=0;
        			count = gnMethod.DigitCount(noOfStarsFromApi);
	        		
	        		if(count>3)
	        		{
	        			apiStars = gnMethod.convertValue(noOfStarsFromApi,count);
	        			System.out.println("Number of Stars from API after conversion: "+apiStars);
	        		}
        		}
        		Assert.assertEquals("stars not matching for UI and API, UI: "+noOfStarsFromUI+" and API: "+apiStars,noOfStarsFromUI,apiStars);
        		break;
    		}
    	}
    }
}

	