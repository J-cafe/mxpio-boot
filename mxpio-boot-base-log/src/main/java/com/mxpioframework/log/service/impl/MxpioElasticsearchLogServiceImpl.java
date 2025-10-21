package com.mxpioframework.log.service.impl;

import com.mxpioframework.log.entity.ESMxpioLog;
import com.mxpioframework.log.service.MxpioLogService;
import com.mxpioframework.log.vo.LogParam;
import com.mxpioframework.log.vo.LogVO;
import com.mzt.logapi.beans.CodeVariableType;
import com.mzt.logapi.beans.LogRecord;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("mxpio.log.es.logService")
@ConditionalOnClass(name = {"org.elasticsearch.client.RestHighLevelClient"})
public class MxpioElasticsearchLogServiceImpl implements MxpioLogService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public void saveLog(LogRecord logRecord) {
        ESMxpioLog mxpioLog = new ESMxpioLog();
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
        elasticsearchTemplate.save(mxpioLog);
    }

    @Override
    public Page<LogVO> listPage(Pageable page, LogParam param) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if(StringUtils.isNotBlank(param.getType())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("type.keyword",param.getType()));
        }
        if(StringUtils.isNotBlank(param.getSubType())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("subType.keyword",param.getSubType()));
        }
        if(StringUtils.isNotBlank(param.getBizNo())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("bizNo.keyword",param.getBizNo()));
        }
        if(StringUtils.isNotBlank(param.getOperator())){
            boolQueryBuilder.filter(QueryBuilders.matchQuery("operator",param.getOperator()));
        }
        if(StringUtils.isNotBlank(param.getSuccess())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("success.keyword",param.getSuccess()));
        }
        if(param.getCreateTimeStart()!=null){
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("createTime").gte(param.getCreateTimeStart()));
        }
        if (param.getCreateTimeEnd()!=null){
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("createTime").lte(param.getCreateTimeEnd()));
        }
        //排序
       FieldSortBuilder createTimeSort = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        NativeSearchQuery build = nativeSearchQueryBuilder.withQuery(boolQueryBuilder)
                .withPageable(page)
                .withSort(createTimeSort)
                .build();
        SearchHits<ESMxpioLog> searchHits = elasticsearchTemplate.search(build,ESMxpioLog.class);
        SearchPage<ESMxpioLog> searchPage = SearchHitSupport.searchPageFor(searchHits, page);
        List<LogVO> voList = new ArrayList<>();
        for(SearchHit<ESMxpioLog> hits : searchPage.getContent()){
            LogVO vo = new LogVO();
            vo.setId(hits.getContent().getId());
            vo.setType(hits.getContent().getType());
            vo.setSubType(hits.getContent().getSubType());
            vo.setBizNo(hits.getContent().getBizNo());
            vo.setOperator(hits.getContent().getOperator());
            vo.setAction(hits.getContent().getAction());
            vo.setExtra(hits.getContent().getExtra());
            vo.setCreateTime(hits.getContent().getCreateTime());
            vo.setSuccess(hits.getContent().getSuccess());
            vo.setClazzName(hits.getContent().getClazzName());
            vo.setMethodName(hits.getContent().getMethodName());
            voList.add(vo);
        }
        return new PageImpl<>(voList,page,searchPage.getTotalElements());
    }


}
