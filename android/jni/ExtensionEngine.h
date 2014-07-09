#ifndef DOTAGRID_EXTENSIONENGINE_H_
#define DOTAGRID_EXTENSIONENGINE_H_
#include <v8.h>
#include <string>
#include <type_traits>
#include <functional>
#include <chrono>
#include <thread>
#include <map>

class ExtensionEngine {
	class ExtensionInterface {
	public:
		ExtensionEngine* engine;
		v8::Persistent<v8::Value> gameDelegate;
		v8::Persistent<v8::Value> autoDelegate;
		ExtensionInterface(ExtensionEngine* engine) : engine(engine) {}
	};

	class TimeoutHandler {
		v8::Isolate *iso;
		v8::Persistent<v8::Function> callback;
		std::thread trd;
		unsigned long timeout;
		volatile bool running, interrupted;
	public:
		// default
		TimeoutHandler(uint64_t, v8::Isolate*, const v8::Handle<v8::Function>&);
		// copy
		TimeoutHandler(const TimeoutHandler&) = delete;
		// move
		TimeoutHandler(TimeoutHandler&&) = delete;
		// destruct
		~TimeoutHandler();
		TimeoutHandler& operator=(TimeoutHandler&&) = delete;
		TimeoutHandler& operator=(const TimeoutHandler&) = delete;
	};

	class IntervalHandler {
		v8::Isolate *iso;
		v8::Persistent<v8::Function> callback;
		std::thread trd;
		unsigned long interval;
		volatile bool running;
	public:
		IntervalHandler(uint64_t, v8::Isolate*, const v8::Handle<v8::Function>&);
		IntervalHandler(const IntervalHandler&) = delete;
		IntervalHandler(IntervalHandler&&) = delete;
		~IntervalHandler();
		IntervalHandler& operator=(IntervalHandler&&) = delete;
		IntervalHandler& operator=(const IntervalHandler&) = delete;
	};

	std::map<ExtensionEngine::TimeoutHandler*, v8::Persistent<v8::Object>> timeoutHandlers;
	std::map<ExtensionEngine::IntervalHandler*, v8::Persistent<v8::Object>> intervalHandlers;

	v8::Isolate *iso;
	v8::Persistent<v8::Context> context;
	v8::Persistent<v8::FunctionTemplate> interfaceTemplate;
	v8::Persistent<v8::Object> jsonUtil;

	std::string src;

	ExtensionEngine();
	~ExtensionEngine();

	static void itf_ctor(ExtensionEngine&, const v8::FunctionCallbackInfo<v8::Value>&);
	v8::Handle<v8::Object> wrapInterface();

	ExtensionInterface* currentInterface;

	v8::Handle<v8::Value> parseJSON(const std::string&);
	std::string&& stringifyJSON(v8::Handle<v8::Value>);

	void notifyUpdate(const char *);
	void setCharacterProperty(const char *, const char *, const char *);
	void setCharacterPosition(const char *, const char *);
	const std::string getCharacterPosition(const char *);
	const std::string getCharacterProperty(const char *, const char *);
	const std::string getSelectedGrid();
	friend TimeoutHandler::TimeoutHandler(uint64_t, v8::Isolate*, const v8::Handle<v8::Function>&);
	friend IntervalHandler::IntervalHandler(uint64_t, v8::Isolate*, const v8::Handle<v8::Function>&);
public:

	std::function<void(void)> turnNextRoundCallback;
	std::function<void(const std::string&)> notifyUpdateCallback;
	std::function<void(const std::string&, const std::string&)> setCharacterPositionCallback;
	std::function<const std::string(const std::string&)> getCharacterPositionCallback;
	std::function<const std::string(void)> getSelectedGridCallback, getCharacterListCallback;
	std::function<const std::string(const std::string&, const std::string&)> getCharacterPropertyCallback;
	std::function<void(const std::string&, const std::string&, const std::string&)> setCharacterPropertyCallback;

	static ExtensionEngine* Create();
	static void Destroy(const ExtensionEngine*);
	void loadScript (const std::string&);
	void execute();
	void applyRule(const std::string&, const std::string&, const std::string&);
	void automate(const std::string&);
};
#endif /* DOTAGRID_EXTENSIONENGINE_H_ */
