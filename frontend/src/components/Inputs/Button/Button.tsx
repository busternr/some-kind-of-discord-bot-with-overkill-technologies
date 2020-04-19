import React, { ReactNode } from 'react';
import classNames from 'classnames';

import styles from './Button.module.scss';

interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
	children: ReactNode;
	className?: string;
	size: ButtonSize;
}

export enum ButtonSize {
	Small = 'small',
	Normal = 'normal',
	Big = 'big'
}

const Button: React.FC<Props> = ({ className, children, size, ...rest }) => (
	<button className={classNames(className, styles[size as string], styles.button)} {...rest}>
		{children}
	</button>
);

export default Button;
