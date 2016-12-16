#include "${my_program}.h"

Client::Client(SOCKET s, Command** p)
{
	socket = s;
	pCommands = p;
	name = 0;

	isLogged = false;
	isDisconnected = false;
	commandFound = false;

	refCount = 0;

	parser = new CommandParser();

	ioctlsocket(socket, FIONBIO, &unblockSocket);
}

Client::~Client()
{
	delete name;
}

size_t Client::GetName(char* outBuff, size_t buffLen)
{
	size_t len = name != 0 ? strlen(name) : 0;

	if (len > 0 && outBuff != 0)
		strncpy(outBuff, name, len > buffLen ? buffLen : len);

	return len;
}

void Client::SetName(char* n)
{
	delete name;

	name = new char[strlen(n) + 1];

	strcpy(name, n);
}

SOCKET Client::GetClientId()
{
	return socket;
}

void Client::Dispose()
{
	InterlockedDecrement(&refCount);

	if (refCount <= 0)
	{
		delete this;
	}

}

void Client::AddRef()
{
	InterlockedIncrement(&refCount);
}

void Client::Send(char * message)
{
	send(socket, message, (int)strlen(message), 0);
}

void Client::Receive()
{
	char buffer[BUFFER_SIZE];
	memset(buffer, 0, BUFFER_SIZE);

	int amountRecvBytes = recv(socket, buffer, BUFFER_SIZE, 0);

	if (amountRecvBytes == 0)
	{
		isDisconnected = true;
	}
	if (amountRecvBytes > 0)
	{
		parser->Feed(buffer);
	}

}

void Client::Processing()
{
	Receive();

	CommandData* data = parser->GetNextCommandData();

	while (data != 0)
	{
		// If haven't command, this is message for all.
		// Add SENDALL_COMMAND command to data object.
		if (data->GetCommand() == 0)
		{
			data->SetCommand(SENDALL_COMMAND);
		}

		// Checking for certain command.
		for (int i = 0; pCommands[i] != 0; i++)
		{

			if (strcmp(pCommands[i]->GetName(), data->GetCommand()) == 0)
			{
				pCommands[i]->Execute(data->GetArguments(), this);
				commandFound = true;

				break;
			}

			commandFound = false;
		}

		if (!commandFound)
		{
			Send("Have no such command!!\r\n");
		}

		// Delete data object.
		parser->DeleteData(data);

		// Pop data from data queue.
		data = parser->GetNextCommandData();
	}

}



