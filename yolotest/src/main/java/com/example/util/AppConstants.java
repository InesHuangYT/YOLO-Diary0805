package com.example.util;

/*
 除了request和reponse payloads之外，
 所有其他controllers和services也將使用一些utility(實用程序)類。
*/
public interface AppConstants {
	String DEFAULT_PAGE_NUMBER = "0";
	String DEFAULT_PAGE_SIZE = "30";

	int MAX_PAGE_SIZE = 50;

}
