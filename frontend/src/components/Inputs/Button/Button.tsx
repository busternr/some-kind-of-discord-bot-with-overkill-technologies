// eslint-disable-next-line no-unused-vars
import React, { ReactNode } from 'react';
import classNames from 'classnames';

import styles from './Button.module.scss';

interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
	children: ReactNode;
	className?: string;
	size: ButtonSize;
}

export enum ButtonSize {
	// eslint-disable-next-line no-unused-vars
	Small = 'small',
	// eslint-disable-next-line no-unused-vars
	Normal = 'normal',
	// eslint-disable-next-line no-unused-vars
	Big = 'big'
}

// eslint-disable-next-line react/prop-types
const Button: React.FC<Props> = ({ className, children, size, ...rest }) => (
	<button className={classNames(className, styles[size as string], styles.button)} {...rest}>
		{children}
	</button>
);

export default Button;
