package cn.hl.ox.exec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.UUID;

public class NativeRuntime {
	private static HashMap<String, Boolean> status = new HashMap<String, Boolean>();

	/**
	 * 运行系统CMD命令
	 * @param cmd -- 命令
	 * @param out -- 命令输出
	 * @return
	 * @throws Exception
	 */
	public static int execSystemCmd(String cmd, final StringBuffer out) throws Exception {
		// 调用CMD命令
		if (out != null) out.append("[NativeRuntime]> ").append(cmd).append("\r\n");
		ProcessBuilder builder = new ProcessBuilder("CMD", "/C", cmd);
		builder.redirectErrorStream(true);
		Process process = builder.start();
		// 获取信息流
		final InputStream iStream = process.getInputStream();
		new Thread() {
			public void run() {
				try {
					BufferedReader iBR = new BufferedReader(new InputStreamReader(iStream, "GBK"));
					String line = null;
					while ((line = iBR.readLine()) != null) {
						if (out != null) out.append(line).append("\r\n");
						System.out.println("[RUNTIME:INPUT] " + line);
					}
					iStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
		return process.waitFor();
	}

	/**
	 * 运行用户CMD命令
	 * @param cmd -- 命令
	 * @param maxWait -- 超时时间(单位: 毫秒)
	 * @throws Exception
	 */
	public static void execUserCmd(String cmd, long maxWait) throws Exception {
		final String jobID = UUID.randomUUID().toString().replaceAll("-", "");
		cmd = "START \"" + jobID + "\" /MIN " + cmd;
		try {
			// 调用CMD命令
			ProcessBuilder builder = new ProcessBuilder("CMD", "/C", cmd);
			long start = System.currentTimeMillis();
			// builder.redirectErrorStream(true);
			Process process = builder.start();
			status.put(jobID, true);
			// 获取错误流
			final InputStream eStream = process.getErrorStream();
			new Thread() {
				public void run() {
					try {
						BufferedReader eBR = new BufferedReader(new InputStreamReader(eStream, "GBK"));
						String line = null;
						while ((line = eBR.readLine()) != null) {
							System.out.println("[RUNTIME:ERROR] " + line);
						}
						eStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						status.put(jobID, false);
					}
				}
			}.start();
			// 获取信息流
			final InputStream iStream = process.getInputStream();
			new Thread() {
				public void run() {
					try {
						BufferedReader iBR = new BufferedReader(new InputStreamReader(iStream, "GBK"));
						String line = null;
						while ((line = iBR.readLine()) != null) {
							System.out.println("[RUNTIME:INPUT] " + line);
						}
						iStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						status.put(jobID, false);
					}
				}
			}.start();
			process.waitFor();
			// 处理命令执行结果
			while (status.get(jobID)) {
				if ((System.currentTimeMillis() - start) < (maxWait + 105)) {
					Thread.sleep(100);
				} else {
					String killCmd = "TASKKILL /F /T /FI \"WINDOWTITLE eq " + jobID + "\"";
					new ProcessBuilder("CMD", "/C", killCmd).start();
					throw new Exception("CAPTURE_TIMEOUT");
				}
			}
			System.out.println("[NativeRuntime]> " + cmd + " >>>DONE!");
		} catch (Exception e) {
			throw e;
		} finally {
			status.remove(jobID);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		try {
			//StringBuffer sb = new StringBuffer();
			//execSystemCmd("help", sb);
			//System.out.println(sb);
			execUserCmd("ifconfig", 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
