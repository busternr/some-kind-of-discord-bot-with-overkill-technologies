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

const LoginForm: React.FC = () => {
	const onSubmit = ({ username, password }: FormValues) => {
		console.log(username, password);
		client.publish({
			destination: '/queue/user/echo',
			body: '{\n' + '"headers": {"type": "echo"},\n' + '"body": "test" \n' + '}'
		});
	};

	return (
		<Form
			onSubmit={onSubmit}
			render={({ handleSubmit }) => (
				<form onSubmit={handleSubmit}>
					<h3>Login form</h3>
					<Input name="username" label="Username" placeHolder="username" />
					<Input name="password" label="Password" placeHolder="password" type="password" />
					<Button type="submit" size={ButtonSize.Normal}>
						Submit
					</Button>
				</form>
			)}
		/>
	);
};

export default LoginForm;
