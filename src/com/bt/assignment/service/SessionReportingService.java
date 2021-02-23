package com.bt.assignment.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bt.assignment.entity.Session;

public class SessionReportingService {

	private final String SPLITTER_SPACE = " ";

	private enum SessionStatus {
		Start, End;
	}

	public Map<String, List<Session>> processLogFile(String fileName) throws IOException {

		Map<String, List<Session>> userSessionMap = new LinkedHashMap<>();

		List<String> matchingLogLineList = Files.lines(Paths.get(fileName))
				.filter(s -> s.matches("^([0-9][0-9]:[0-9][0-9]:[0-9][0-9] [a-zA-Z0-9]* [a-zA-Z0-9]*)"))
				.collect(Collectors.toList());

		if (matchingLogLineList.size() == 0) {
			return userSessionMap;
		}

		LocalTime firstLogTime = LocalTime.parse(matchingLogLineList.get(0).split(SPLITTER_SPACE)[0]);

		matchingLogLineList.forEach(logLine -> {
			System.out.println("Processing log: " + logLine);
			String[] logArray = logLine.split(" ");

			LocalTime loggedSessionTime = LocalTime.parse(logArray[0]);
			String loggedUser = logArray[1];
			String loggedSessionStatus = logArray[2];

			Session session = new Session();

			if (loggedSessionStatus.equalsIgnoreCase(SessionStatus.Start.name()))
				session.setInTime(loggedSessionTime);

			if (userSessionMap.get(loggedUser) == null) {
				// Saving session info for new user

				List<Session> userSessions = new LinkedList<Session>();

				if (loggedSessionStatus.equalsIgnoreCase(SessionStatus.End.name())) {
					session.setInTime(firstLogTime);
					session.setOutTime(loggedSessionTime);
				}
				userSessions.add(session);
				userSessionMap.put(loggedUser, userSessions);

			} else {
				// Saving session info for existing user

				if (loggedSessionStatus.equalsIgnoreCase(SessionStatus.Start.name())) {
					// Add session InTime detail for newly started session
					userSessionMap.get(loggedUser).add(session);
				}

				if (loggedSessionStatus.equalsIgnoreCase(SessionStatus.End.name())) {
					// Add session OutTime detail for running session which is ending
					Optional<Session> sessionOptional = userSessionMap.get(loggedUser).stream()
							.filter(session1 -> session1.getOutTime() == null).findFirst();
					if (sessionOptional.isPresent()) {
						sessionOptional.get().setOutTime(loggedSessionTime);
					} else {
						// Special scenario where starting session log is missing and only ending
						// session info available
						session.setInTime(firstLogTime);
						session.setOutTime(loggedSessionTime);
						userSessionMap.get(loggedUser).add(session);
					}
				}
			}
		});

		endSessionWhenLogInfoMissing(userSessionMap);
		return userSessionMap;
	}

	/**
	 * Method setting OutTime when session end log is missing in log file
	 */
	private void endSessionWhenLogInfoMissing(Map<String, List<Session>> userSessionMap) {
		userSessionMap.entrySet().stream().forEach(e -> {
			for (Session session : e.getValue()) {
				if (session.getOutTime() == null) {
					session.setOutTime(session.getInTime());
				}
			}
		});
	}

	/**
	 * Method generating report of user sessions
	 */
	public void generateReport(Map<String, List<Session>> userSessionMap) {
		System.out.println("\n\n================== Report on user sessions durations ==================");
		System.out.println("User          Session Duration (seconds)");
		userSessionMap.entrySet().stream().forEach(e -> {
			long totalSessionDuration = 0L;
			for (Session session : e.getValue()) {
				long seconds = Duration.between(session.getInTime(), session.getOutTime()).getSeconds();
				totalSessionDuration += seconds;
			}
			System.out.println(e.getKey() + "        " + totalSessionDuration);
		});
	}
}
