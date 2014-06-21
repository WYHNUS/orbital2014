/*
 * SoundEngine.h
 *
 *  Created on: 9 Jun, 2014
 *      Author: dingxiangfei
 */

#ifndef SOUNDENGINE_H_
#define SOUNDENGINE_H_
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <string>
#include <map>
#include <queue>
const SLEnvironmentalReverbSettings envReverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;
class SoundEngine {
	struct AssetAudioControl {
		int fd;
		off_t start, length;
		SLObjectItf assetPlayer;
		SLPlayItf assetPlayerPlay;
		SLSeekItf assetPlayerSeek;
		SLMuteSoloItf assetPlayerMuteSolo;
		SLVolumeItf assetPlayerVolume;
	};
	struct BufferItem {
		size_t length;
		short *buffer;
	};
	short *masterBuffer;
	SLEngineItf engineEngine;
	SLObjectItf engineObject, outputMixObject, bufferQueuePlayer;
	SLEnvironmentalReverbItf outputMixEnvironmentalReverb;
	SLPlayItf bufferQueuePlayerPlay, assetPlayerPlay;
	SLSeekItf assetPlayerSeek;
	SLMuteSoloItf assetPlayerMuteSolo;
	SLAndroidSimpleBufferQueueItf bufferQueuePlayerBufferQueue;
	SLEffectSendItf bufferQueuePlayerEffectSend;
	SLVolumeItf bufferQueuePlayerVolume, assetPlayerVolume;
	// settings
	SLuint32 samplingRate; // enum SL_SAMPLINGRATE_*
	SLuint32 channels;
	SLuint32 speakers; // enum SL_SPEAKER_*
	// play queue
	static void bufferQueuePlayerCallBack (const SLObjectItf, const void *, SLuint32, SLresult, SLuint32, void*);
	void bufferQueuePop() const;

	std::map<std::string, AssetAudioControl> assetControls;
	std::queue<BufferItem> bqManagedQueue;

	bool bufferQueueEnabled;
	mutable int bqHead, bqTail;

	mutable int test;

	SoundEngine();
	~SoundEngine();
public:
	static const int SAMPLING_RATE = 8000;
	static const int BUFFER_SIZE = 80000;
	static const int BUFFER_COUNT = 10;
	static SoundEngine* Create();
	static void Destroy(const SoundEngine*);
	// buffer
	void prepareBufferQueuePlayer();
	// asset
	void prepareAssetPlayer(const std::string&, int fd, off_t start, off_t length);
	void setAssetPlayerLoop(const std::string&, bool);
	void setAssetPlayerPlayState(const std::string&, bool);
	void setAssetPlayerSeek(const std::string&, SLmillisecond);
	void setAssetPlayerStop(const std::string&);
	void setAssetPlayerVolume(int);
};

#endif /* SOUNDENGINE_H_ */
