#ifndef DOTAGRID_EXTENSIONENGINE_H_
#define DOTAGRID_EXTENSIONENGINE_H_
#include <v8.h>
#include <string>
#include <map>
#include <functional>

class ExtensionEngine {
private:
	class ExtensionInterface {
	public:
		ExtensionEngine* engine;
		v8::Persistent<v8::Value> gameDelegate;
		ExtensionInterface(ExtensionEngine* engine) : engine(engine) {}
		void getGameDelegate (v8::Local<v8::String>, const v8::PropertyCallbackInfo<v8::Value>&);
	};

	std::function<void(const std::string&)> callback;

	v8::Isolate *iso;
	v8::Persistent<v8::Context> context;
	v8::Persistent<v8::FunctionTemplate> interfaceTemplate;
	v8::Persistent<v8::Value> gameDelegate;
	v8::Persistent<v8::Object> jsonUtil;

	std::string src;

	ExtensionEngine();
	~ExtensionEngine();

	v8::Handle<v8::Object> wrapInterface();

	ExtensionInterface* currentInterface;

public:
	static ExtensionEngine* Create();
	static void Destroy(const ExtensionEngine*);
	void loadScript (const std::string&);
	void execute();
	void applyRule(const std::string&, const std::string&, const std::function<void(const std::string&)>&);
	void notifyUpdate(const char *);
};
extern
std::map<v8::Isolate*, ExtensionEngine*> DOTAGRID_EXTENSIONENGINE_ISOLATE_MAP;
#endif /* DOTAGRID_EXTENSIONENGINE_H_ */
