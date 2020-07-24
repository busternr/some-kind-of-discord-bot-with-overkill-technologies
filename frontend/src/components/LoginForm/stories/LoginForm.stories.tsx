import React from 'react';
import LoginForm from '../LoginForm';
import docs from './LoginForm.mdx';

export default {
	component: LoginForm,
	title: 'Login Form',
	parameters: { docs: { page: docs } }
};

export const Default = () => <LoginForm />;
