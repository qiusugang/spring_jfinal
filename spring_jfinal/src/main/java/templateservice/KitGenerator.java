package templateservice;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class KitGenerator {
	private String kitPackageName;
	private String kitOutPutdir;

	public KitGenerator(String kitPackageName, String kitOutPutdir) {
		this.kitPackageName = kitPackageName;
		this.kitOutPutdir = kitOutPutdir;
	}

	public void generate() {
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("kitPackageName", kitPackageName);
		try {
			String genericsKitPath = kitOutPutdir + "/GenericsKit.java";
			if (!new File(genericsKitPath).exists()) {
				FileKit.createFile(genericsKitPath);
				ServiceGenerator.writeToFile(root, "GenericsKit.ftl", kitOutPutdir + "/GenericsKit.java");
			}
			String sqlKitPath = kitOutPutdir + "/SqlKit.java";
			if (!new File(sqlKitPath).exists()) {
				FileKit.createFile(sqlKitPath);
				ServiceGenerator.writeToFile(root, "SqlKit.ftl", kitOutPutdir + "/SqlKit.java");
			}
			String strKitPath = kitOutPutdir + "/StringKit.java";
			if (!new File(strKitPath).exists()) {
				FileKit.createFile(strKitPath);
				ServiceGenerator.writeToFile(root, "StringKit.ftl", kitOutPutdir + "/StringKit.java");
			}
			String dateKitPath = kitOutPutdir + "/DateKit.java";
			if (!new File(dateKitPath).exists()) {
				FileKit.createFile(dateKitPath);
				ServiceGenerator.writeToFile(root, "DateKit.ftl", kitOutPutdir + "/DateKit.java");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
