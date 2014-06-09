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
	SLEngineItf engineEngine;
	SLObjectItf engineObject, outputMixObject, bufferQueuePlayer, assetPlayer;
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
	static void bufferQueuePlayerCallBack (const SLObjectItf, void const *, SLuint32, SLresult, SLuint32, void*);
	std::map<std::string, AssetAudioControl> assetControls;
	SoundEngine();
	~SoundEngine();
public:
	static SoundEngine* Create();
	static void Destroy(const SoundEngine*);
	// buffer
	void prepareBufferQueuePlayer();
	// asset
	void prepareAssetPlayer(const std::string&, int fd, off_t start, off_t length);
	void setAssetPlayerLoop(const std::string&, bool loop);
	void setAssetPlayerPlayState(const std::string&, bool play);
	void setAssetPlayerVolume(int);
};

#endif /* SOUNDENGINE_H_ */
