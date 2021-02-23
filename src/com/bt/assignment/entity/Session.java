package com.bt.assignment.entity;

import java.time.LocalTime;

public class Session {
	
	LocalTime inTime;
	LocalTime outTime;
	
	public LocalTime getInTime() {
		return inTime;
	}
	public void setInTime(LocalTime inTime) {
		this.inTime = inTime;
	}
	public LocalTime getOutTime() {
		return outTime;
	}
	public void setOutTime(LocalTime outTime) {
		this.outTime = outTime;
	}
	

	

}
