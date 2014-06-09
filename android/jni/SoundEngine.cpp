/*
 * SoundEngine.cpp
 *
 *  Created on: 9 Jun, 2014
 *      Author: dingxiangfei
 */
#include <stdlib.h>
#include "SoundEngine.h"
#include <android/log.h>
#include <jni.h>
#include <sys/types.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
SoundEngine::SoundEngine() {
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
		result = (*outputMixEnvironmentalReverb)->SetEnvironmentalReverbProperties(outputMixEnvironmentalReverb, &envReverbSettings);
	}
	// initialisation complete
	channels = 1;
	samplingRate = SL_SAMPLINGRATE_8;
	speakers = SL_SPEAKER_FRONT_CENTER;
}

void SoundEngine::prepareBufferQueuePlayer() {
	SLDataLocator_AndroidSimpleBufferQueue bufferQueue = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2};
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
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "Engine Interface %p: Request New Audio Player, Result: %d", engineEngine, result);
	result = (*bufferQueuePlayer)->Realize(bufferQueuePlayer, SL_BOOLEAN_FALSE);
	__android_log_print(ANDROID_LOG_DEBUG, "SE", "BufferQueuePlayer %p: Realised, Result: %d", bufferQueuePlayer, result);
	result = (*bufferQueuePlayer)->GetInterface(bufferQueuePlayer, SL_IID_PLAY, &bufferQueuePlayerPlay);
	result = (*bufferQueuePlayer)->GetInterface(bufferQueuePlayer, SL_IID_BUFFERQUEUE, &bufferQueuePlayerBufferQueue);
	result = (*bufferQueuePlayer)->RegisterCallback(bufferQueuePlayer, &bufferQueuePlayerCallBack, this);
	result = (*bufferQueuePlayer)->GetInterface(bufferQueuePlayer, SL_IID_EFFECTSEND, &bufferQueuePlayerEffectSend);
	result = (*bufferQueuePlayer)->GetInterface(bufferQueuePlayer, SL_IID_VOLUME, &bufferQueuePlayerVolume);
	result = (*bufferQueuePlayerPlay)->SetPlayState(bufferQueuePlayerPlay, SL_PLAYSTATE_PLAYING);
}

void SoundEngine::bufferQueuePlayerCallBack (const SLObjectItf bufferQueuePlayer, void const *context, SLuint32, SLresult, SLuint32, void*) {
	SoundEngine const * se = static_cast<SoundEngine const *>(context);
}

void SoundEngine::prepareAssetPlayer(const std::string& key, int fd, off_t start, off_t length) {
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
	result = (*control.assetPlayer)->Realize(assetPlayer, SL_BOOLEAN_FALSE);
	result = (*control.assetPlayer)->GetInterface(assetPlayer, SL_IID_PLAY, &control.assetPlayerPlay);
	result = (*control.assetPlayer)->GetInterface(assetPlayer, SL_IID_SEEK, &control.assetPlayerSeek);
	result = (*control.assetPlayer)->GetInterface(assetPlayer, SL_IID_MUTESOLO, &control.assetPlayerMuteSolo);
	result = (*control.assetPlayer)->GetInterface(assetPlayer, SL_IID_VOLUME, &control.assetPlayerVolume);
	result = (*control.assetPlayerSeek)->SetLoop(assetPlayerSeek, SL_BOOLEAN_TRUE, 0, SL_TIME_UNKNOWN);
	assetControls[key] = control;
}

SoundEngine::~SoundEngine() {
}

SoundEngine* SoundEngine::Create() {
	return new SoundEngine();
}

void SoundEngine::Destroy(const SoundEngine* se) {
	delete se;
}
