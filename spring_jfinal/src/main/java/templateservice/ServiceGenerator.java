package templateservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.TableMeta;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class ServiceGenerator {
	private String modelPackageName;
	private String servicePackageName;
	private String serviceOutputDir;
	private String kitPackageName;

	public ServiceGenerator(String kitPackageName, String modelPackageName, String servicePackageName, String serviceOutputDir){
		this.modelPackageName = modelPackageName;
		this.servicePackageName = servicePackageName;
		this.serviceOutputDir = serviceOutputDir;
		this.kitPackageName = kitPackageName;
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

	public String getServiceOutputDir() {
		return serviceOutputDir;
	}

	public void setServiceOutputDir(String serviceOutputDir) {
		this.serviceOutputDir = serviceOutputDir;
	}

	public void generate(List<TableMeta> tableMetas){
		System.out.println("Generate service ...");
		System.out.println("serviceOutputDir Output Dir: " + serviceOutputDir);
		for (TableMeta tableMeta : tableMetas) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("servicePackageName", servicePackageName);
			map.put("modelPackageName", modelPackageName);
			map.put("kitPackageName", kitPackageName);
			map.put("modelName", tableMeta.modelName);
			map.put("serviceName", tableMeta.modelName+"Service");
			String baseServicePath = serviceOutputDir + File.separator + "BaseService.java";
			String baseServiceImplPath = serviceOutputDir + File.separator + "impl" + File.separator + "BaseServiceImpl.java";
			try {
				if (!new File(baseServicePath).exists()){
					FileKit.createFile(baseServicePath);
					writeToFile(map, "BaseService.ftl", baseServicePath);
				}
				if (!new File(baseServiceImplPath).exists()){
					FileKit.createFile(baseServiceImplPath);
					writeToFile(map, "BaseServiceImpl.ftl", baseServiceImplPath);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			String filePath = serviceOutputDir + File.separator + tableMeta.modelName + "Service.java";
			String filePath1 = serviceOutputDir + File.separator + "impl" + File.separator + tableMeta.modelName + "ServiceImpl.java";
			if (!new File(filePath).exists()){
				FileKit.createFile(filePath);
				try {
					writeToFile(map, "ServiceInterface.ftl", filePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (!new File(filePath1).exists()){
				FileKit.createFile(filePath1);
				try {
					writeToFile(map, "ServiceImpl.ftl", filePath1);
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
