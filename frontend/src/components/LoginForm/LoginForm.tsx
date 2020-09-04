import React from 'react';
import { Form } from 'react-final-form';
import Button, { ButtonSize } from '../Inputs/Button/Button';
import Input from '../Inputs/Input/Input';
import { connect } from '../../api/websocket';

interface FormValues {
	username: string;
	password: string;
}

const client = connect();

const sendMessage = (body: string) => client.publish({ destination: '/queue/user/messages', body });

const sendAuthMessage = () =>
	sendMessage(`{
   "headers":{
      "type":"auth.register"
   },
   "body":{
      "username":"niko3",
      "password":"niko3"
   }
}`);

const sendValidationAuthMessage = () =>
	sendMessage(`{
   "headers":{
      "type":"auth.register"
   },
   "body":{
      "usernamed":"niko3",
      "password":"niko3"
   }
}`);

const sendEchoMessage = () =>
	sendMessage(`{
   "headers":{
      "type":"echo"
   },
   "body":{
      "message":"test"
   }
}`);

const LoginForm: React.FC = () => {
	// tslint:disable-next-line:no-empty
	const onSubmit = ({ username, password }: FormValues) => {};

	return (
		<Form
			onSubmit={onSubmit}
			render={({ handleSubmit }) => (
				<form onSubmit={handleSubmit}>
					<h3>Login form</h3>
					<Input name="username" label="Username" placeHolder="username" />
					<Input name="password" label="Password" placeHolder="password" type="password" />
					{/*<Button type="submit" size={ButtonSize.Normal}>*/}
					{/*	Submit*/}
					{/*</Button>*/}
					<div>
						<br />
						<Button onClick={sendEchoMessage} size={ButtonSize.Big}>
							Send Echo Message
						</Button>
						<Button onClick={sendAuthMessage} size={ButtonSize.Big}>
							Send Auth Message
						</Button>
						<Button onClick={sendValidationAuthMessage} size={ButtonSize.Big}>
							Send VALIDATION Auth Message
						</Button>
					</div>
				</form>
			)}
		/>
	);
};

export default LoginForm;
