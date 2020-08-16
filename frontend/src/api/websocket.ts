import { Client } from '@stomp/stompjs';

export const connect = () => {
	const client = new Client();
	client.configure({
		brokerURL: 'ws://localhost:8080/websocket',
		onConnect: () => {
			client.subscribe('/queue/user', (message) => {
				console.log('Echo response: ', message);
			});
			client.subscribe('/queue/auth', (message) => {
				console.log('Auth response: ', message);
			});
		},
		// Helps during debugging, remove in production
		debug: (str) => {
			console.info(str);
		}
	});
	client.activate();
	return client;
};
