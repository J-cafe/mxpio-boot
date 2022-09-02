package com.mxpioframework.filestorage.service;

import com.mxpioframework.filestorage.entity.MxpioBlob;

public interface MxpioBlobService {

	MxpioBlob put(byte[] data);

	MxpioBlob get(String id);
}
