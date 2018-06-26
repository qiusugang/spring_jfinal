package ${controllerPackageName};


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jfinal.plugin.activerecord.Record;
import ${kitPackageName}.AjaxJson;
import ${kitPackageName}.SqlKit.PageInfo;
import ${modelPackageName}.${modelName};
import ${servicePackageName}.${modelName}Service;

@RestController
@RequestMapping("/${modelObj}")
public class ${modelName}Controller extends BaseController{
	@Autowired
	${modelName}Service ${modelObj}Service;
	
	@RequestMapping("/page")
	public Record getPage(PageInfo pageInfo) {
		${modelName} ${modelObj}=simpleModel(${modelName}.class);
		return easyuiPageResult(${modelObj}Service.paginateDynamicEqual(pageInfo.getPage(), pageInfo.getPageSize(), ${modelObj}));
	}

	
	@RequestMapping("/save")
	public AjaxJson save() {
		${modelName} ${modelObj}=simpleModel(${modelName}.class);
		Boolean result=${modelObj}Service.save(${modelObj});
		return result?AjaxJson.success().setData(${modelObj}):AjaxJson.failure();
	}
	
	@RequestMapping("/update")
	public AjaxJson update() {
		${modelName} ${modelObj}=simpleModel(${modelName}.class);
		${modelObj}.remove("${modelObj}name", "password");
		Boolean result=${modelObj}Service.update(${modelObj});
		return result?AjaxJson.success().setData(${modelObj}):AjaxJson.failure();
	}
  	
	@RequestMapping("/delete")
	public AjaxJson delete() {
		${modelName} ${modelObj}=simpleModel(${modelName}.class);
		Boolean result=${modelObj}Service.delete(${modelObj});
		return result?AjaxJson.success().setData(${modelObj}):AjaxJson.failure();
	}

}
