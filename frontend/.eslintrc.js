module.exports = {
	'env': {
		'browser': true,
		'es6': true
	},
	'extends': [
		'react-app',
		'eslint:recommended',
		'plugin:react/recommended',
		'plugin:@typescript-eslint/eslint-recommended',
		'prettier',
		'prettier/@typescript-eslint',
		'plugin:prettier/recommended'
	],
	'globals': {
		'Atomics': 'readonly',
		'SharedArrayBuffer': 'readonly'
	},
	'parser': '@typescript-eslint/parser',
	'parserOptions': {
		'ecmaFeatures': {
			'jsx': true
		},
		'ecmaVersion': 2018,
		'sourceType': 'module'
	},
	'plugins': [
		'react',
		'@typescript-eslint',
		'prettier'
	],
	'rules': {
		'linebreak-style': [
			'error',
			'unix'
		],
		'quotes': [
			'error',
			'single'
		],
		'semi': [
			'error',
			'always'
		],
		'prettier/prettier': 'error',
		'react/prop-types': 'off',
		'no-console': 'off'
	}
};