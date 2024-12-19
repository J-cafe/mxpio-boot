package com.mxpioframework.system.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mxpioframework.system.entity.SerialNumber;
import com.mxpioframework.system.service.SnRuleService;

@Service("mxpio.system.snRuleService")
public class SnRuleServiceImpl extends BaseServiceImpl<SerialNumber> implements SnRuleService {

	@Override
	@Transactional(readOnly = false)
	public Object execute(String snExpression, JSONObject formData) {
		// 判断是否有日期表达式，有则先替换
		if (snExpression.contains("${YY}") || snExpression.contains("${YYYY}") || snExpression.contains("${MM}")
				|| snExpression.contains("${DD}")) {
			Calendar data = Calendar.getInstance();
			snExpression = snExpression.replace("${YYYY}", data.get(Calendar.YEAR) + "")
					.replace("${MM}", StringUtils.leftPad(String.valueOf(data.get(Calendar.MONTH) + 1), 2, "0"))
					.replace("${DD}", StringUtils.leftPad(String.valueOf(data.get(Calendar.DAY_OF_MONTH)), 2, "0"))
					//.replace("${DD}", data.get(Calendar.DAY_OF_MONTH) + "")
					.replace("${YY}", (data.get(Calendar.YEAR) + "").substring(2, 4));
		}

		// 替换变量
		List<String> values = getContentInfo(snExpression);
		for (String s : values) {
			Object value = formData.get(s);
			if (value == null) {
				value = "";
			}
			snExpression = snExpression.replace("${" + s + "}", value.toString());
		}
		SerialNumber sn = getById(SerialNumber.class, snExpression);
		if (sn == null) {
			sn = new SerialNumber();
			sn.setSnExpression(snExpression);
			save(sn);
		}
		String currentRecord = sn.getCurrentRecord();
		if (currentRecord == null) {
			currentRecord = snExpression.replace("#", "0");
		}
		if (currentRecord.length() != snExpression.length()) {
			currentRecord = snExpression.replace("#", "0");
		}
		// 计数长度
		int index = snExpression.length() - snExpression.replace("#", "").length();
		// 当前记录计数值
		String no = currentRecord.substring(snExpression.indexOf('#'), snExpression.indexOf('#') + index);

		StringBuilder sbPlaceHolder = new StringBuilder();
		for (int i = 0; i < index; i++) {
			sbPlaceHolder.append("#");
		}

		// 判断是否日期一致，不一致计数从0开始
		if (!RegExUtils.replacePattern(currentRecord, no + "$", sbPlaceHolder.toString()).equals(snExpression)) {
			no = "0";
		}

		StringBuilder newNo = new StringBuilder((Long.parseLong(no) + 1) + "");
		if (newNo.length() > index) {
			return null;
		} else {
			while (index != newNo.length()) {
				newNo.insert(0, "0");
			}
			String result = snExpression.replace(sbPlaceHolder.toString(), newNo.toString());
			sn.setCurrentRecord(result);
			update(sn);
			return result;
		}
	}

	/**
	 * 获取表达式中${}中的值
	 * 
	 * @param content
	 * @return
	 */
	public static List<String> getContentInfo(String content) {
		Pattern regex = Pattern.compile("\\$\\{([^}]*)\\}");
		Matcher matcher = regex.matcher(content);
		List<String> strs = new ArrayList<>();
		while (matcher.find()) {
			strs.add(matcher.group(1));
		}
		return strs;
	}

}
