package cn.hl.kit.ox.mem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;

public class SessionMem {
	@SuppressWarnings("rawtypes")
	public static void calculate(HttpServletRequest request) {
		File file;
		try {
			file = new File("sessionFiles");
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject("session:");
			HttpSession session = request.getSession(false);
			Enumeration names = session.getAttributeNames();
			while(names.hasMoreElements()){
				oos.writeObject(session.getAttribute((String) names.nextElement()));
			}
			oos.flush();
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
