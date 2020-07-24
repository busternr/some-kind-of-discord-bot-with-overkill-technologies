import React from 'react';
import classNames from 'classnames';

import styles from './Input.module.scss';
import { Field } from 'react-final-form';

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {
	name: string;
	label: string;
	placeHolder?: string;
	className?: string;
}

// eslint-disable-next-line react/prop-types
const Input: React.FC<Props> = ({ name, label, placeHolder, className, ...rest }) => (
	<div>
		<label>{label}:</label>
		<Field
			name={name}
			component="input"
			placeholder={placeHolder}
			className={classNames(className, styles.input)}
			{...rest}
		/>
	</div>
);

export default Input;
