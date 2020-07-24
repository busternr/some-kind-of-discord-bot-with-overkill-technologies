module.exports = {
	resolve: {
		extensions: [".ts", ".tsx"]
	},
	module: {
		rules: [
			{
				test: /\.tsx?$/,
				exclude: /node_modules/,
				use: [
					{
						loader: require.resolve("babel-loader"),
						options: {
							presets: [
								require("@babel/preset-typescript").default,
								require("@babel/preset-react").default,
								require("@babel/preset-env").default
							]
						}
					},
					require.resolve("react-docgen-typescript-loader")
				]
			},
			{
				test: /\.s[ac]ss$/i,
				use: [
					// Creates `style` nodes from JS strings
					'style-loader',
					// Translates CSS into CommonJS
					'css-loader',
					// Compiles Sass to CSS
					'sass-loader',
				],
			}
		],
	}
}