/*
 * SoundEngine.cpp
 *
 *  Created on: 9 Jun, 2014
 *      Author: dingxiangfei
 */
#include "SoundEngine.h"
#include <android/log.h>
#include <jni.h>
#include <sys/types.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
SoundEngine::SoundEngine() : bufferQueuePlayer(0), masterBuffer(0), bufferQueueEnabled(false) {
	SLresult result;
	result = slCreateEngine(&engineObject, 0, NULL, 0, NULL, NULL);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Engine Object Address: %p, Result: %d", engineObject, result);
	result = (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Engine Object %p: Realised, Result: %d", engineObject, result);
	result = (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE, &engineEngine);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Engine Object %p: Interface %p, Result: %d", engineObject, engineEngine, result);
	// output mix
	const SLInterfaceID ids[] = {SL_IID_ENVIRONMENTALREVERB, SL_IID_VOLUME};
	const SLboolean reqs[] = {SL_BOOLEAN_FALSE, SL_BOOLEAN_TRUE};
	result = (*engineEngine)->CreateOutputMix(engineEngine, &outputMixObject, 1, ids, reqs);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Engine Interface %p: Acquired Output Mix %p Result: %d", engineEngine, outputMixObject, result);
	result = (*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Output Mix %p: Realised, Result: %d", outputMixObject, result);
	result = (*outputMixObject)->GetInterface(outputMixObject, SL_IID_ENVIRONMENTALREVERB, &outputMixEnvironmentalReverb);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Output Mix %p: Request Environmental Reverb, Result: %d", outputMixObject, result);
	if (result == SL_RESULT_SUCCESS) {
		__android_log_print(ANDROID_LOG_DEBUG, "SE", "Output Mix %p: Obtain Environmental Reverb", outputMixObject);
		result = (*outputMixEnvironmentalReverb)->SetEnvironmentalReverbProperties(outputMixEnvironmentalReverb, &envReverbSettings);
	}
	// initialisation complete
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Initialisation Complete");
	channels = 1;
	samplingRate = SL_SAMPLINGRATE_8;
	speakers = SL_SPEAKER_FRONT_CENTER;
	test = 0;
}

void SoundEngine::prepareBufferQueuePlayer() {
	if (bufferQueueEnabled)
		return;
	bufferQueueEnabled = true;
	masterBuffer = new short[SoundEngine::BUFFER_SIZE * SoundEngine::BUFFER_COUNT];
	bqHead = bqTail = 0;
	SLDataLocator_AndroidSimpleBufferQueue bufferQueue = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, SoundEngine::BUFFER_COUNT};
	SLDataFormat_PCM formatPCM = {
			SL_DATAFORMAT_PCM,
			channels,
			samplingRate,
			SL_PCMSAMPLEFORMAT_FIXED_16,
			SL_PCMSAMPLEFORMAT_FIXED_16,
			speakers,
			SL_BYTEORDER_LITTLEENDIAN
	};
	SLDataSource audioSrc = {&bufferQueue, &formatPCM};
	// audio sink
	SLDataLocator_OutputMix outputMix = {SL_DATALOCATOR_OUTPUTMIX, outputMixObject};
	SLDataSink audioSink = {&outputMix, NULL};
	// player setup
	SLresult result;
	const SLInterfaceID ids[] = {SL_IID_BUFFERQUEUE, SL_IID_EFFECTSEND, SL_IID_VOLUME};
	const SLboolean reqs[] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE};
	result = (*engineEngine)->CreateAudioPlayer(engineEngine, &bufferQueuePlayer, &audioSrc, &audioSink, 3, ids, reqs);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Engine Interface %p: Request New BufferQueue Player, Result: %d", engineEngine, result);
	result = (*bufferQueuePlayer)->Realize(bufferQueuePlayer, SL_BOOLEAN_FALSE);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "BufferQueuePlayer %p: Realised, Result: %d", bufferQueuePlayer, result);
	result = (*bufferQueuePlayer)->GetInterface(bufferQueuePlayer, SL_IID_PLAY, &bufferQueuePlayerPlay);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "BufferQueuePlayer %p: Play, Result: %d", bufferQueuePlayer, result);
	result = (*bufferQueuePlayer)->GetInterface(bufferQueuePlayer, SL_IID_BUFFERQUEUE, &bufferQueuePlayerBufferQueue);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "BufferQueuePlayer %p: BufferQueue, Result: %d", bufferQueuePlayer, result);
	result = (*bufferQueuePlayer)->RegisterCallback(bufferQueuePlayer, &bufferQueuePlayerCallBack, this);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "BufferQueuePlayer %p: BufferQueueCallback, Result: %d", bufferQueuePlayer, result);
	result = (*bufferQueuePlayer)->GetInterface(bufferQueuePlayer, SL_IID_EFFECTSEND, &bufferQueuePlayerEffectSend);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "BufferQueuePlayer %p: EffectSend, Result: %d", bufferQueuePlayer, result);
	result = (*bufferQueuePlayer)->GetInterface(bufferQueuePlayer, SL_IID_VOLUME, &bufferQueuePlayerVolume);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "BufferQueuePlayer %p: Volume, Result: %d", bufferQueuePlayer, result);
	result = (*bufferQueuePlayerPlay)->SetPlayState(bufferQueuePlayerPlay, SL_PLAYSTATE_PLAYING);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "BufferQueuePlayer %p: Set to playing, Result: %d", bufferQueuePlayer, result);
}

void SoundEngine::bufferQueuePop() const {
	if (bufferQueueEnabled) {
		bqHead++;
		if (bqHead == SoundEngine::BUFFER_COUNT)
			bqHead = 0;
	}
}

void SoundEngine::bufferQueuePlayerCallBack (const SLObjectItf bufferQueuePlayer, const void *context, SLuint32, SLresult, SLuint32, void*) {
	const SoundEngine *se = static_cast<const SoundEngine*>(context);
	se->test++;
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Test=%d", se->test);
	if (se->bqHead != se->bqTail) {
		(*se->bufferQueuePlayerBufferQueue)->Enqueue(se->bufferQueuePlayerBufferQueue, se->masterBuffer + se->bqHead * SoundEngine::BUFFER_SIZE, SoundEngine::BUFFER_SIZE);
		se->bufferQueuePop();
	}
}

void SoundEngine::prepareAssetPlayer(const std::string& key, int fd, off_t start, off_t length) {
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "FD=%d Start=%ld Length=%ld", fd, start, length);
	SLDataLocator_AndroidFD fileDescriptor = {SL_DATALOCATOR_ANDROIDFD, fd, start, length};
	SLDataFormat_MIME formatMime = {SL_DATAFORMAT_MIME, NULL, SL_CONTAINERTYPE_UNSPECIFIED};
	SLDataSource audioSrc = {&fileDescriptor, &formatMime};
	SLDataLocator_OutputMix outputMix = {SL_DATALOCATOR_OUTPUTMIX, outputMixObject};
	SLDataSink audioSink = {&outputMix, NULL};

	SLresult result;
	const SLInterfaceID ids[] = {SL_IID_SEEK, SL_IID_MUTESOLO, SL_IID_VOLUME};
	const SLboolean reqs[] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE};
	AssetAudioControl control;
	result = (*engineEngine)->CreateAudioPlayer(engineEngine, &control.assetPlayer, &audioSrc, &audioSink, 3, ids, reqs);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Engine Interface %p: Request New Asset Player, Result: %d", engineEngine, result);
	result = (*control.assetPlayer)->Realize(control.assetPlayer, SL_BOOLEAN_FALSE);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Asset Player %p: Realised, Result: %d", control.assetPlayer, result);
	result = (*control.assetPlayer)->GetInterface(control.assetPlayer, SL_IID_PLAY, &control.assetPlayerPlay);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Asset Player %p: Play, Result: %d", control.assetPlayer, result);
	result = (*control.assetPlayer)->GetInterface(control.assetPlayer, SL_IID_SEEK, &control.assetPlayerSeek);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Asset Player %p: Seek, Result: %d", control.assetPlayer, result);
	result = (*control.assetPlayer)->GetInterface(control.assetPlayer, SL_IID_MUTESOLO, &control.assetPlayerMuteSolo);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Asset Player %p: MuteSolo, Result: %d", control.assetPlayer, result);
	result = (*control.assetPlayer)->GetInterface(control.assetPlayer, SL_IID_VOLUME, &control.assetPlayerVolume);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Asset Player %p: Volume, Result: %d", control.assetPlayer, result);
	result = (*control.assetPlayerSeek)->SetLoop(control.assetPlayerSeek, SL_BOOLEAN_FALSE, 0, SL_TIME_UNKNOWN);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Asset Player %p: Set to loop, Result: %d", control.assetPlayer, result);
	assetControls[key] = control;
}

SoundEngine::~SoundEngine() {
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Release Start");
	if (bufferQueuePlayer)
		(*bufferQueuePlayer)->Destroy(bufferQueuePlayer);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Release BufferQueuePlayer");
	for (auto& control : assetControls)
		if (control.second.assetPlayer)
			(*control.second.assetPlayer)->Destroy(control.second.assetPlayer);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Release AssetPlayers");
	if (outputMixObject)
		(*outputMixObject)->Destroy(outputMixObject);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Release OutputMixObject");
	if (engineObject)
		(*engineObject)->Destroy(engineObject);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Release EngineObject");
	if (masterBuffer)
		delete[] masterBuffer;
}

void SoundEngine::setAssetPlayerLoop(const std::string& name, bool loop) {
	if (assetControls.find(name) != assetControls.end()) {
		SLresult result = (*assetControls[name].assetPlayerSeek)
				->SetLoop(assetControls[name].assetPlayerSeek, loop ? SL_BOOLEAN_TRUE : SL_BOOLEAN_FALSE, 0, SL_TIME_UNKNOWN);
		__android_log_print(ANDROID_LOG_DEBUG, "SE", "Asset Player %p: Set loop, Result: %d", assetControls[name].assetPlayer, result);
	}
}

void SoundEngine::setAssetPlayerSeek(const std::string& name, SLmillisecond position) {
	if (assetControls.find(name) != assetControls.end()) {
		SLresult result = (*assetControls[name].assetPlayerSeek)
				->SetPosition(assetControls[name].assetPlayerSeek, position, SL_SEEKMODE_FAST);
		__android_log_print(ANDROID_LOG_DEBUG, "SE", "Asset Player %p: Set position, Result: %d", assetControls[name].assetPlayer, result);
	}
}

void SoundEngine::setAssetPlayerPlayState(const std::string& name, bool play) {
	if (assetControls.find(name) != assetControls.end()) {
		__android_log_print(ANDROID_LOG_DEBUG, "SE", "Set '%s' play state", name.c_str());
		SLresult result = (*assetControls[name].assetPlayerPlay)
				->SetPlayState(assetControls[name].assetPlayerPlay, play ? SL_PLAYSTATE_PLAYING : SL_PLAYSTATE_PAUSED);
		__android_log_print(ANDROID_LOG_DEBUG, "SE", "Asset Player %p: Set play state, Result: %d", assetControls[name].assetPlayer, result);
	}
}

void SoundEngine::setAssetPlayerStop(const std::string& name) {
	if (assetControls.find(name) != assetControls.end()) {
		__android_log_print(ANDROID_LOG_DEBUG, "SE", "Stop '%s'", name.c_str());
		SLresult result = (*assetControls[name].assetPlayerPlay)
				->SetPlayState(assetControls[name].assetPlayerPlay, SL_PLAYSTATE_STOPPED);
		__android_log_print(ANDROID_LOG_DEBUG, "SE", "Asset Player %p: Stop, Result: %d", assetControls[name].assetPlayer, result);
	}
}

SoundEngine* SoundEngine::Create() {
	return new SoundEngine();
}

void SoundEngine::Destroy(const SoundEngine* se) {
	delete se;
}
