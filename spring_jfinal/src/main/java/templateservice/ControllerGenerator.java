package templateservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class ControllerGenerator {
	private String modelPackageName;
	private String servicePackageName;
	private String kitPackageName;
	private String controllerPackageName;
	private String controllerOutPutDir;

	public ControllerGenerator(String kitPackageName, String modelPackageName, String servicePackageName, String controllerPackageName, String controllerOutPutDir){
		this.modelPackageName = modelPackageName;
		this.servicePackageName = servicePackageName;
		this.kitPackageName = kitPackageName;
		this.controllerPackageName = controllerPackageName;
		this.controllerOutPutDir = controllerOutPutDir;
	}
	
	public String getServicePackageName() {
		return servicePackageName;
	}

	public void setServicePackageName(String servicePackageName) {
		this.servicePackageName = servicePackageName;
	}
	
	public String getModelPackageName() {
		return modelPackageName;
	}

	public void setModelPackageName(String modelPackageName) {
		this.modelPackageName = modelPackageName;
	}

	public void generate(List<TableMeta> tableMetas){
		System.out.println("Generate service ...");
		System.out.println("serviceOutputDir Output Dir: " + controllerOutPutDir);
		for (TableMeta tableMeta : tableMetas) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("servicePackageName", servicePackageName);
			map.put("modelPackageName", modelPackageName);
			map.put("kitPackageName", kitPackageName);
			map.put("modelName", tableMeta.modelName);
			map.put("modelObj", StrKit.firstCharToLowerCase(tableMeta.modelName));
			map.put("serviceName", tableMeta.modelName+"Service");
			map.put("controllerPackageName", controllerPackageName);
			String baseControllerPath = controllerOutPutDir + File.separator + "BaseController.java";
			try {
				if (!new File(baseControllerPath).exists()){
					FileKit.createFile(baseControllerPath);
					writeToFile(map, "BaseController.ftl", baseControllerPath);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			String filePath = controllerOutPutDir + File.separator + tableMeta.modelName + "Controller.java";
			if (!new File(filePath).exists()){
				FileKit.createFile(filePath);
				try {
					writeToFile(map, "Controller.ftl", filePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}

	public static void writeToFile(Map<String, Object> root, String ftl, String outPutPath) throws Exception {
		@SuppressWarnings("deprecation")
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(PathKit.getRootClassPath() + "/templateservice"));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		Template temp = cfg.getTemplate(ftl);
		FileOutputStream fos = new FileOutputStream(new File(outPutPath));
		Writer out = new OutputStreamWriter(fos);
		temp.process(root, out);
	}

}
