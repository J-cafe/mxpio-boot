package com.mxpioframework.excel.importer.processor.impl;

import com.mxpioframework.excel.importer.parser.CellPreParser;
import com.mxpioframework.excel.importer.policy.Context;
import com.mxpioframework.security.annotation.Dict;
import com.mxpioframework.security.entity.BaseEntity;
import com.mxpioframework.security.entity.DictItem;
import com.mxpioframework.system.service.PojoDictParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("importer.DictTextToValueCellPreParser")
public class DictTextToValueCellPreParser implements CellPreParser {

    @Autowired
    private PojoDictParseService pojoDictParseService;

    @Override
    public String getName() {
        return "数据字典转换解析器";
    }

    @Override
    public void parse(Context context) {
        Dict dict = pojoDictParseService.getDictByClazzAndField(context.getEntityClass(),context.getCurrentMappingRule().getPropertyName());
        if(dict==null){
            context.setValue(context.getCurrentCell().getValue());
            return;
        }
        String dicCode = dict.dicCode();
        Class<? extends BaseEntity> entityClazz = dict.dicEntity();

        String tmpText = null;
        if (DictItem.class.equals(entityClazz)) {
            tmpText = pojoDictParseService.getValueByText(dicCode, context.getCurrentCell().getValue());
        } else {
            String dicText = dict.dicText();
            tmpText = pojoDictParseService.getEntityValueByText(dicCode, entityClazz, dicText, context.getCurrentCell().getValue());
        }
        context.setValue(tmpText);

    }
}
