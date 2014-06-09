#ifndef EXTENSIONENGINE_H_
#define EXTENSIONENGINE_H_
#include <v8.h>
#include <string>
class ExtensionEngine {
	v8::Isolate *iso;
	std::string src;
	ExtensionEngine();
	~ExtensionEngine();
public:
	static ExtensionEngine* Create();
	static void Destroy(const ExtensionEngine*);
	void loadScript (const std::string&);
	void execute();
};
#endif /* EXTENSIONENGINE_H_ */
