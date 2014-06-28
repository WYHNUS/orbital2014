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
	};

	v8::Isolate *iso;
	v8::Persistent<v8::Context> context;
	v8::Persistent<v8::FunctionTemplate> interfaceTemplate;
	v8::Persistent<v8::Object> jsonUtil;

	std::string src;

	ExtensionEngine();
	~ExtensionEngine();

	v8::Handle<v8::Object> wrapInterface();

	ExtensionInterface* currentInterface;

public:

	std::function<void(void)> turnNextRoundCallback;
	std::function<void(const std::string&)> notifyUpdateCallback;
	std::function<void(const std::string&, const std::string&)> setCharacterPropertyCallback, setCharacterPositionCallback;
	std::function<const std::string(const std::string&)> getCharacterPropertyCallback, getCharacterPositionCallback, getCharacterListCallback;
	std::function<const std::string(void)> getSelectedGridCallback;

	static ExtensionEngine* Create();
	static void Destroy(const ExtensionEngine*);
	void loadScript (const std::string&);
	void execute();
	void applyRule(const std::string&, const std::string&, const std::string&);
	void notifyUpdate(const char *);
	void setCharacterProperty(const char *, const char *);
	void setCharacterPosition(const char *, const char *);
	const std::string getCharacterPositions(const char *);
	const std::string getSelectedGrid();
};
#endif /* DOTAGRID_EXTENSIONENGINE_H_ */
