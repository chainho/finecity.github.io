import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class Get {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean debug = true;
		String u = "";
		String savePath = System.getProperty("java.io.tmpdir");
		int page = 0;
		if (args.length >= 2) {
			u = args[0];
			page = Integer.valueOf(args[1]);
			savePath = args[2];
			System.out.println("savePath is " + savePath);
			if(args.length >=4) {
				String d = args[3];
				if(d.equalsIgnoreCase("true")) {
					debug = true;
				}else {
					debug = false;
				}
			}else {
				debug = false;
			}
		} else {
			String usage = "Usage: url pageNumber.  \n Example:  http://www.cnhr001.com/question/answer.aspx?sortid=%B5%B3%D4%B1%CF%E0%B9%D8&bg=bg5 5 d:/abc/";
			System.out.println(usage);
		}

		Get.extractContent(savePath, u, page, debug);
		System.out.println("getting successful!");
	}

	public static void extractContent(String savePath, String u, int page,
			boolean debug) {
		String fileName = "QA.txt";
		PrintWriter writer = null;
		
		try {
			if (u.indexOf("&page=") < 0) {
				u = u.concat("&page=");
			}
			File f = new File(savePath);
			if(f.isDirectory()) {
				if(!f.exists()) f.mkdirs();
				writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(f,fileName)),"UTF-8"));	
			}else {
				if(!f.getParentFile().exists()) f.getParentFile().mkdirs();
				writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
			}
			 
			String url = u;
			for (int i = 1; i < page; i++) {
				org.jsoup.nodes.Document doc = Jsoup.connect(url + i).get();
				Element table = doc.select("table[id=DataGrid1]").first();
				Iterator<Element> ite = table.select("td[width=95%]")
						.iterator();
				while (ite.hasNext()) {
					String Q = "Q : " + ite.next().text();
					String A = "A : " + ite.next().text();
					writer.write(Q);
					writer.write(A);
					writer.flush();
					if (debug) {
						System.out.println(Q);
						System.out.println(A);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writer.flush();
			writer.close();
		}
	}
}
