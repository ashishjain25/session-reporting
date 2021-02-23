package com.bt.assignment.controller;

import java.io.IOException;

import com.bt.assignment.service.SessionReportingService;

public class SessionReporting {

	public static void main(String[] args) {
		
		if(args.length == 0) {
			System.out.println("Please provide the log file!");
		} else if (args.length > 1) {
			System.out.println("Please provide only the log file!");
		}
		
		SessionReportingService service = new SessionReportingService();
		try {
			service.processLogFile(args[0]);
			service.generateReport();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
