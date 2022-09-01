package com.mxpioframework.excel.importer.policy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mxpioframework.excel.importer.model.Cell;
import com.mxpioframework.excel.importer.model.ImporterSolution;
import com.mxpioframework.excel.importer.model.MappingRule;
import com.mxpioframework.excel.importer.model.Record;
import com.mxpioframework.jpa.JpaUtil;

import net.sf.cglib.beans.BeanMap;

public class Context {
	private int startRow = 1;

	private InputStream inpuStream;
	
	private String FileName;
	
	private long fileSize;
	
	// private String importerSolutionId;
	
	private String importerSolutionCode;
	
    private Cell currentCell;
    
    private Record currentRecord;
    
    private List<Record> records = new ArrayList<>(30);
    
    private ImporterSolution importerSolution;
    
    private MappingRule currentMappingRule;
    
    private Object currentEntity;
    
    private List<MappingRule> mappingRules;
    
    private Class<?> entityClass;
    
    private Object value;
    
    private Map<String, Object> params;

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public InputStream getInpuStream() {
		return inpuStream;
	}

	public void setInpuStream(InputStream inpuStream) {
		this.inpuStream = inpuStream;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getImporterSolutionCode() {
		return importerSolutionCode;
	}

	public void setImporterSolutionCode(String importerSolutionCode) {
		this.importerSolutionCode = importerSolutionCode;
	}
	
	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}
	
	public void addCell(Cell cell) {
		if (currentRecord == null || !currentRecord.addCellIfNeed(cell)) {
			currentRecord = new Record();
			currentRecord.addCellIfNeed(cell);
			records.add(currentRecord);
		}
	}

	public Cell getCurrentCell() {
		return currentCell;
	}

	public void setCurrentCell(Cell currentCell) {
		this.currentCell = currentCell;
		addCell(currentCell);
	}

	public Record getCurrentRecord() {
		return currentRecord;
	}

	public void setCurrentRecord(Record currentRecord) {
		this.currentRecord = currentRecord;
	}

	public ImporterSolution getImporterSolution() {
		return importerSolution;
	}

	public void setImporterSolution(ImporterSolution importerSolution) {
		this.importerSolution = importerSolution;
	}

	public MappingRule getCurrentMappingRule() {
		return currentMappingRule;
	}

	public void setCurrentMappingRule(MappingRule currentMappingRule) {
		this.currentMappingRule = currentMappingRule;
	}

	public Object getCurrentEntity() {
		return currentEntity;
	}

	public void setCurrentEntity(Object currentEntity) {
		this.currentEntity = currentEntity;
	}

	public List<MappingRule> getMappingRules() {
		return mappingRules;
	}

	public void setMappingRules(List<MappingRule> mappingRules) {
		this.mappingRules = mappingRules;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getCurrentEntityId() {
		String idProperty = JpaUtil.getIdName(entityClass);
		BeanMap beanMap = BeanMap.create(currentEntity);
		return (T) beanMap.get(idProperty);
		
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
}
