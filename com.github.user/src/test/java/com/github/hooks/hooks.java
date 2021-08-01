package com.github.hooks;

import com.github.util.TestBase;

import io.cucumber.java.After;

public class hooks extends TestBase {
	
	@After("@GitHubUserInfoUI")
	public void tearDown()
	{
		driver.quit();
	}

}
