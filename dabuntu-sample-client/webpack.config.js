module.exports = {
    entry: './javascript/tutorial.js',
    output: {
        path: './public',
        filename: 'bundle.js',
    },
    module: {
        loaders: [
            { test: /\.js$/, exclude: /node_modules/, loader: "babel-loader" }
        ]
    },
    devServer: {
        contentBase: 'public'
    }
}