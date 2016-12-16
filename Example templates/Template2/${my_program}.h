#include <string.h>

class Client
{
private:
	SOCKET socket;
	char* name;

	Command** pCommands;
	volatile long refCount;

	unsigned long unblockSocket = 1;

	bool commandFound;

	~Client();

public:
	Client(SOCKET socket, Command** p);
	
	size_t GetName(char* outBuff, size_t buffLen);
	void SetName(char* name);

	void Dispose();
	void AddRef();

	void Send(char* message);
	void Receive();

	void Processing();

	SOCKET GetClientId();

	bool isLogged;
	volatile bool isDisconnected;

	CommandParser* parser;
};