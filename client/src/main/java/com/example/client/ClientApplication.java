package com.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class ClientApplication {
	public static void main(String[] args) {
		Executor executor = Executors.newFixedThreadPool(5);
		double readQuota = 0.75, writeQuota = 0.25;
		int[] readIdList = new int[]{1, 2, 3, 4, 7, 9, 11, 12, 13, 15, 17, 20, 21, 22}, writeIdList = new int[]{1, 3, 6, 7, 10, 11, 12, 15, 19, 20};
		executor.execute(new Thread(() -> {
			while (true) {
				// вероятность вызова метода getBalance
				double readProbability = readQuota / (readQuota + writeQuota);
				if (ThreadLocalRandom.current().nextDouble() < readProbability) {
					URL url;
					try {
						url = new URL("http://localhost:8086/balance?id=" + readIdList[ThreadLocalRandom.current().nextInt(readIdList.length)]);
					} catch (MalformedURLException e) {
						throw new RuntimeException(e);
					}
					try {
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						printResponse(connection, "GET");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				} else {
					URL url;
					try {
						url = new URL("http://localhost:8086/transfer?id=" + writeIdList[ThreadLocalRandom.current().nextInt(writeIdList.length)] + "&amount=" + 1L);
					} catch (MalformedURLException e) {
						throw new RuntimeException(e);
					}
					try {
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("PUT");
						printResponse(connection, "PUT");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}));
	}

	private static void printResponse(HttpURLConnection connection, String method) throws IOException {
		BufferedReader in = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder content = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		System.out.println(LocalDateTime.now() + " | " + method + " | " + content);
		in.close();
		connection.disconnect();
	}
}