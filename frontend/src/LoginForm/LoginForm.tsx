import React from 'react';
import {Form} from "react-final-form";
import Button, {ButtonSize} from "../Inputs/Button/Button";
import Input from "../Inputs/Input/Input";

interface FormValues {
    username: string;
    password: string;
}

const LoginForm: React.FC = () => {
    const onSubmit = ({username, password}: FormValues) => {
        console.log('Submit ', username, password)
    };

    return (
        <Form
            onSubmit={onSubmit}
            render={({ handleSubmit }) => (
                <form onSubmit={handleSubmit}>
                    <h3>Login form</h3>
                    <Input name="username" label="Username" placeHolder="username"/>
                    <Input name="password" label="Password" placeHolder="password" type="password"/>
                    <Button type="submit" size={ButtonSize.Normal}>Submit</Button>
                </form>
            )}
        />
    )
};

export default LoginForm;