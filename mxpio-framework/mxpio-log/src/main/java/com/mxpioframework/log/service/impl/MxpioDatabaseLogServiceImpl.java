package com.mxpioframework.log.service.impl;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.jpa.query.Order;
import com.mxpioframework.log.entity.DBMxpioLog;
import com.mxpioframework.log.service.MxpioLogService;
import com.mxpioframework.log.vo.LogParam;
import com.mxpioframework.log.vo.LogVO;
import com.mzt.logapi.beans.CodeVariableType;
import com.mzt.logapi.beans.LogRecord;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("mxpio.log.db.logService")
public class MxpioDatabaseLogServiceImpl implements MxpioLogService {

    @Override
    @Transactional
    public void saveLog(LogRecord logRecord) {
        DBMxpioLog mxpioLog = new DBMxpioLog();
        mxpioLog.setType(logRecord.getType());
        mxpioLog.setSubType(logRecord.getSubType());
        mxpioLog.setBizNo(logRecord.getBizNo());
        mxpioLog.setOperator(logRecord.getOperator());
        mxpioLog.setAction(logRecord.getAction());
        mxpioLog.setExtra(logRecord.getExtra());
        mxpioLog.setCreateTime(new Date());
        mxpioLog.setSuccess(logRecord.isFail()?"0":"1");
        mxpioLog.setClazzName(MapUtils.isEmpty(logRecord.getCodeVariable())?"":logRecord.getCodeVariable().get(CodeVariableType.ClassName).toString());
        mxpioLog.setMethodName(MapUtils.isEmpty(logRecord.getCodeVariable())?"":logRecord.getCodeVariable().get(CodeVariableType.MethodName).toString());
        JpaUtil.save(mxpioLog);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LogVO> listPage(Pageable page , LogParam param){
        Page<DBMxpioLog> pageList = JpaUtil.linq(DBMxpioLog.class).where(getCriteria(param)).paging(page);
        List<LogVO> voList = new ArrayList<>();
        for(DBMxpioLog mxpioLog : pageList.getContent()){
            LogVO vo = new LogVO();
            vo.setId(String.valueOf(mxpioLog.getId()));
            vo.setType(mxpioLog.getType());
            vo.setSubType(mxpioLog.getSubType());
            vo.setBizNo(mxpioLog.getBizNo());
            vo.setOperator(mxpioLog.getOperator());
            vo.setAction(mxpioLog.getAction());
            vo.setExtra(mxpioLog.getExtra());
            vo.setCreateTime(mxpioLog.getCreateTime());
            vo.setSuccess(mxpioLog.getSuccess());
            vo.setClazzName(mxpioLog.getClazzName());
            vo.setMethodName(mxpioLog.getMethodName());
            voList.add(vo);
        }
        return new PageImpl<>(voList,page,pageList.getTotalElements());
    }

    private Criteria getCriteria(LogParam param){
        Criteria criteria = Criteria.create();
        if(StringUtils.isNotBlank(param.getType())){
            criteria.addCriterion("type", Operator.EQ, param.getType());
        }
        if(StringUtils.isNotBlank(param.getSubType())){
            criteria.addCriterion("subType", Operator.EQ, param.getSubType());
        }
        if(StringUtils.isNotBlank(param.getBizNo())){
            criteria.addCriterion("bizNo", Operator.EQ, param.getBizNo());
        }
        if(StringUtils.isNotBlank(param.getOperator())){
            criteria.addCriterion("operator", Operator.EQ, param.getOperator());
        }
        if(StringUtils.isNotBlank(param.getSuccess())){
            criteria.addCriterion("success", Operator.EQ, param.getSuccess());
        }
        if(param.getCreateTimeStart()!=null){
            criteria.addCriterion("createTime", Operator.GE, param.getCreateTimeStart());
        }
        if (param.getCreateTimeEnd()!=null){
            criteria.addCriterion("createTime", Operator.LE, param.getCreateTimeEnd());
        }
        criteria.addOrder(new Order("id",true));
        return criteria;
    }

}
