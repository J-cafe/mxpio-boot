package com.mxpioframework.filestorage.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.filestorage.entity.MxpioBlob;
import com.mxpioframework.filestorage.service.MxpioBlobService;
import com.mxpioframework.jpa.JpaUtil;

@Service
@Transactional
public class MxpioBlobServiceImpl implements MxpioBlobService {

	@Override
	public MxpioBlob put(byte[] data) {
		MxpioBlob mxpioBlob = new MxpioBlob();
	    mxpioBlob.setData(data);
	    JpaUtil.persist(mxpioBlob);
	    return mxpioBlob;
	}

	@Override
	public MxpioBlob get(String id) {
		return JpaUtil.linq(MxpioBlob.class).equal("id", id).findOne();
	}

}
