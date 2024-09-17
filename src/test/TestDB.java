package test;

import database.DatabaseConnection;

public class TestDB {
	public static void main(String[] args) {
	    if (DatabaseConnection.testConnection()) {
	        System.out.println("Kết nối tới cơ sở dữ liệu thành công!");
	    } else {
	        System.out.println("Không thể kết nối tới cơ sở dữ liệu.");
	    }
	}
}
