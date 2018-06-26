package templateservice;

import java.util.List;

import javax.sql.DataSource;
import com.jfinal.plugin.activerecord.generator.BaseModelGenerator;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.activerecord.generator.ModelGenerator;
import com.jfinal.plugin.activerecord.generator.TableMeta;

public class MyGenerator extends Generator {
	
	
	ServiceGenerator serviceGenerator;
	KitGenerator kitGenerator;
	ControllerGenerator controllerGenerator;
	/**
	 * 构造 Generator，生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
	 * @param dataSource 数据源
	 * @param baseModelPackageName base model 包名
	 * @param baseModelOutputDir base mode 输出目录
	 * @param modelPackageName model 包名
	 * @param modelOutputDir model 输出目录
	 */
	public MyGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir, String servicePackageName, String serviceOutputDir, String kitPackageName,
	String kitOutPutdir) {
		super(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		serviceGenerator = new ServiceGenerator(kitPackageName, modelPackageName, servicePackageName, serviceOutputDir);
		kitGenerator = new KitGenerator(kitPackageName, kitOutPutdir);
		
	}
	
	public MyGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir, String servicePackageName, String serviceOutputDir, String kitPackageName,
			String kitOutPutdir, String controllerPackageName,
			String controllerOutPutDir) {
				super(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
				serviceGenerator = new ServiceGenerator(kitPackageName, modelPackageName, servicePackageName, serviceOutputDir);
				kitGenerator = new KitGenerator(kitPackageName, kitOutPutdir);
				controllerGenerator = new ControllerGenerator(kitPackageName, modelPackageName, servicePackageName, controllerPackageName, controllerOutPutDir);
	}
	
/*	*//**
	 * 构造 Generator，生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
	 * @param dataSource 数据源
	 * @param baseModelPackageName base model 包名
	 * @param baseModelOutputDir base mode 输出目录
	 * @param modelPackageName model 包名
	 * @param modelOutputDir model 输出目录
	 *//*
	public MyGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir, String servicePackageName, String serviceOutputDir) {
		super(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		serviceGenerator = new ServiceGenerator(modelPackageName, servicePackageName, serviceOutputDir);
	}*/
	
	/**
	 * 构造 Generator，生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
	 * @param dataSource 数据源
	 * @param baseModelPackageName base model 包名
	 * @param baseModelOutputDir base mode 输出目录
	 * @param modelPackageName model 包名
	 * @param modelOutputDir model 输出目录
	 */
	public MyGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir) {
		super(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
	}
	
	/**
	 * 构造 Generator，只生成 baseModel
	 * @param dataSource 数据源
	 * @param baseModelPackageName base model 包名
	 * @param baseModelOutputDir base mode 输出目录
	 */
	public MyGenerator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir) {
		super(dataSource, baseModelPackageName, baseModelOutputDir);
	}
	
	public MyGenerator(DataSource dataSource, BaseModelGenerator baseModelGenerator) {
		super(dataSource, baseModelGenerator);
	}
	
	/**
	 * 使用指定 BaseModelGenerator、ModelGenerator 构造 Generator 
	 * 生成 BaseModel、Model、MappingKit 三类文件，其中 MappingKit 输出目录与包名与 Model相同
	 */
	public MyGenerator(DataSource dataSource, BaseModelGenerator baseModelGenerator, ModelGenerator modelGenerator) {
		super(dataSource, baseModelGenerator, modelGenerator);
	}

	@Override
	public void generate() {
		long start = System.currentTimeMillis();
		List<TableMeta> tableMetas = metaBuilder.build();
		if (tableMetas.size() == 0) {
			System.out.println("TableMeta 数量为 0，不生成任何文件");
			return ;
		}
		
		baseModelGenerator.generate(tableMetas);
		
		if (modelGenerator != null) {
			modelGenerator.generate(tableMetas);
		}
		
		if (mappingKitGenerator != null) {
			mappingKitGenerator.generate(tableMetas);
		}
		
		if (dataDictionaryGenerator != null && generateDataDictionary) {
			dataDictionaryGenerator.generate(tableMetas);
		}
		if (null != serviceGenerator){
			serviceGenerator.generate(tableMetas);
		}
		if (null != kitGenerator){
			kitGenerator.generate();
		}
		if (null != controllerGenerator) {
			controllerGenerator.generate(tableMetas);
		}
		long usedTime = (System.currentTimeMillis() - start) / 1000;
		System.out.println("Generate complete in " + usedTime + " seconds.");
	}
	
	
}
