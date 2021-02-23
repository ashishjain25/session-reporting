package com.bt.assignment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import com.bt.assignment.entity.Session;


public class SessionReportingServiceTests {
	
	private SessionReportingService service;
	
	@Before
	public void setup() {
		service = new SessionReportingService();
	}
	
	@Test
	public void testSessionMapGenerated() throws IOException {
		Map<String,List<Session>> userSessionMap = service.processLogFile("application.log");
		assertEquals(2, userSessionMap.size());
		assertEquals(Boolean.TRUE,userSessionMap.containsKey("CHARLIE"));
		assertEquals(Boolean.TRUE,userSessionMap.containsKey("ALICE99"));
		assertEquals(4,userSessionMap.get("ALICE99").size());
		assertEquals(3,userSessionMap.get("CHARLIE").size());
	}
	
	@Test
	public void testNoSessionMapGeneratedWhenNoSessionDataPresent() throws IOException {
		Map<String,List<Session>> userSessionMap = service.processLogFile("application_nosessiondata.log");
		assertEquals(0, userSessionMap.size());
	}

}
