package com.mxpioframework.filestorage.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.filestorage.entity.MxpioFileInfo;
import com.mxpioframework.filestorage.service.FileStoragePolicy;
import com.mxpioframework.filestorage.service.FileStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "FileController", description = "文件管理")
@RestController("mxpio.filestorage.fileController")
@RequestMapping("/file/")
public class FileController {

	private Map<String, FileStoragePolicy> fileStoragePolicyMap = new HashMap<String, FileStoragePolicy>();

	@Resource(name = FileStorageService.BEAN_ID)
	private FileStorageService fileService;

	@Autowired
	public void setFileStoragePolicyMap(Collection<FileStoragePolicy> fileStoragePolicys) {
		if (fileStoragePolicys == null) {
			return;
		}

		for (FileStoragePolicy fileStoragePolicy : fileStoragePolicys) {
			this.fileStoragePolicyMap.put(fileStoragePolicy.getName(), fileStoragePolicy);
		}
	}

	@GetMapping("download/{fileNo}")
	@Operation(summary = "下载文件", description = "根据fileNo下载文件", method = "GET")
	public void download(@PathVariable("fileNo") String fileNo, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		MxpioFileInfo mxpioFileInfo = fileService.get(fileNo);

		if (mxpioFileInfo == null) {
			findNotFound(response, fileNo);
			return;
		}

		HttpSession session = request.getSession();

		String filename = mxpioFileInfo.getFileName();
		response.setContentType(session.getServletContext().getMimeType(filename));

		String encodFilename = URLEncoder.encode(filename, "utf-8");
		response.setHeader("Cache-Control", "max-age=31556926");
		response.setHeader("Content-Disposition",
				String.format("attachment; filename=\"%1$s\"; filename*=utf-8''%1$s", encodFilename));

		InputStream inputStream = fileService.getInputStream(mxpioFileInfo);
		if (inputStream == null) {
			findNotFound(response, fileNo);
		}

		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			IOUtils.copy(inputStream, outputStream);
		} catch (FileNotFoundException fne) {
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}

	@PostMapping("upload")
	@Operation(summary = "上传文件", description = "上传文件", method = "POST")
	public Result<Map<String, Object>> handleFileUpload(HttpServletRequest request) throws IOException {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		Map<String, Object> result = new LinkedHashMap<String, Object>();

		Collection<Map<String, Object>> otherFiles = new ArrayList<Map<String, Object>>();
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			String fileStorageType = null;
			try {
				List<FileItem> items = upload.parseRequest(request);
				Iterator<FileItem> iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField() && "FileStorageType".equals(item.getFieldName())) {
						fileStorageType = item.getString();
					}
				}

				iterator = items.iterator();
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();

					if (!item.isFormField()) {
						String fileName = item.getName();
						MxpioFileInfo mxpioFileInfo = fileService.put(fileStorageType, item.getInputStream(), fileName);

						if ("file".equals(item.getFieldName())) {
							putFileInfo(mxpioFileInfo, result);
						} else {
							Map<String, Object> otherFile = new LinkedHashMap<String, Object>();
							putFileInfo(mxpioFileInfo, otherFile);
							otherFiles.add(otherFile);
						}
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}

		if (!otherFiles.isEmpty()) {
			result.put("files", otherFiles);
		}
		return Result.OK(result);
	}

	// @FileResolver
	/*
	 * public Map<String, Object> handleFileUploadByDoradoAction(UploadFile
	 * file, Map<String, Object> parameter) throws IOException {
	 * 
	 * MultipartFile multipartFile = file.getMultipartFile(); String
	 * fileStorageType = (String) parameter.get("fileStorageType");
	 * 
	 * Map<String, Object> result = new LinkedHashMap<String, Object>();
	 * MxpioFileInfo mxpioFileInfo = fileService.put(fileStorageType,
	 * multipartFile.getInputStream(), multipartFile.getOriginalFilename());
	 * putFileInfo(mxpioFileInfo, result); result.put("parameter", parameter);
	 * 
	 * String fileStoragePolicyName = (String)
	 * parameter.get("fileStoragePolicyName"); if (fileStoragePolicyMap.size()
	 * != 0) { if (fileStoragePolicyName != null) { FileStoragePolicy
	 * fileStoragePolicy = fileStoragePolicyMap.get(fileStoragePolicyName);
	 * Assert.notNull(fileStoragePolicy, "不存在" + fileStoragePolicyName +
	 * "的处理器"); fileStoragePolicy.apply(result); } for (FileStoragePolicy
	 * publicPolicy : fileStoragePolicyMap.values()) { if
	 * (publicPolicy.support(parameter)) { publicPolicy.apply(parameter); } } }
	 * 
	 * return result; }
	 */

	void putFileInfo(MxpioFileInfo mxpioFileInfo, Map<String, Object> result) {
		result.put("fileNo", mxpioFileInfo.getFileNo());
		result.put("fileName", mxpioFileInfo.getFileName());
	}

	void findNotFound(HttpServletResponse response, String fileNo) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

}
