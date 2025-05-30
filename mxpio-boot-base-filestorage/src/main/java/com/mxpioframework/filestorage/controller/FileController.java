package com.mxpioframework.filestorage.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

	private final Map<String, FileStoragePolicy> fileStoragePolicyMap = new HashMap<>();

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
	
	@DeleteMapping("remove/{fileNo}")
	@Operation(summary = "删除文件", description = "根据fileNo删除文件", method = "DELETE")
	public Result<Object> removeFile(@PathVariable("fileNo") String fileNo) throws FileNotFoundException{
		if(fileService.remove(fileNo) >0 ){
			return Result.OK("删除成功！");
		}else{
			return Result.OK("删除失败！");
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
		response.setHeader("Content-Length", "" + inputStream.available());//返回文件长度（下载进度）

        OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			IOUtils.copy(inputStream, outputStream);
		} catch (FileNotFoundException ignored) {
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
	}

	@PostMapping("upload")
	@Operation(summary = "上传文件", description = "上传文件", method = "POST")
	public Result<MxpioFileInfo> handleFileUpload(MultipartFile file,String fileStorageType,HttpServletRequest request) throws IOException {
		
		if(file != null){
			String fileName = file.getOriginalFilename();
			MxpioFileInfo mxpioFileInfo = fileService.put(fileStorageType, file.getInputStream(), fileName,file.getSize(),file.getContentType());
			return Result.OK(mxpioFileInfo);
		}
		return Result.error("上传失败");
	}

	void findNotFound(HttpServletResponse response, String fileNo) throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	@GetMapping("getFileInfo/{fileNo}")
	@Operation(summary = "获取文件信息", description = "根据fileNo获取文件信息", method = "GET")
	public Result<MxpioFileInfo> getFileInfoByNo(@PathVariable("fileNo") String fileNo, HttpServletRequest request,
						 HttpServletResponse response) throws IOException {
		MxpioFileInfo mxpioFileInfo = fileService.get(fileNo);
		if (mxpioFileInfo == null) {
			return Result.error("未获取到文件");
		}else{
			return Result.OK(mxpioFileInfo);
		}
	}

}
